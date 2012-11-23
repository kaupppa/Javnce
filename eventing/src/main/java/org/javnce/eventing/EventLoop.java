/*
 * Copyright (C) 2012  Pauli Kauppinen
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package org.javnce.eventing;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The class for processing events and non-blocking sockets.
 */
public class EventLoop implements Runnable {

    /**
     * The error handler.
     */
    static private EventLoopErrorHandler errorHandler = new DefaultErrorHandler();
    /**
     * The event queue.
     */
    final private LinkedBlockingQueue<Event> queue;
    /**
     * The event subscribers.
     */
    final private EventSubscriberList eventSubscribers;
    /**
     * The channel subscribers.
     */
    final private ChannelSubscriberList channelSubscribers;
    /**
     * The allow processing.
     */
    private volatile boolean allowRunning;
    /**
     * The event group.
     */
    private EventLoopGroup group;
    /**
     * The timer handler.
     */
    final private TimerContainer timers;
    /**
     * An event object used for wakeup.
     */
    static final private Event wakeupEvent = new Event() {
        @Override
        public EventId Id() {
            return new EventId("What evere as object instance is checked");
        }
    };

    /**
     * Instantiates a new event loop into root group.
     */
    public EventLoop() {
        this(null);
    }

    /**
     * Instantiates a new event loop into same group where otherLoop is in.
     *
     * @param otherLoop the other event loop
     */
    public EventLoop(EventLoop otherLoop) {
        queue = new LinkedBlockingQueue<>();
        eventSubscribers = new EventSubscriberList();
        channelSubscribers = new ChannelSubscriberList();
        timers = new TimerContainer();
        allowRunning = true;

        initGroup(otherLoop);
    }

    /**
     * Adds event loop into event group.
     *
     * @param otherLoop the otherLoop. If null the root group is used.
     */
    private void initGroup(EventLoop otherLoop) {
        if (null == otherLoop) {
            this.group = EventLoopGroup.instance();
        } else {
            this.group = otherLoop.getGroup();
        }
        this.group.add(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        process();
    }

    /**
     * Create new sub event group and move event loop into it.
     */
    public void moveToNewChildGroup() {
        group = group.moveToNewChild(this);
    }

    /**
     * Checks if is runnable.
     *
     * @return true, if is runnable
     */
    private boolean isRunnable() {
        return allowRunning;
    }

    /**
     * Wakeup the event loop.
     */
    private void wakeup() {
        channelSubscribers.wakeup();
        queue.add(wakeupEvent);
    }

    /**
     * Adds the event to be processed in this event loop.
     *
     * @param event the event
     */
    void addEvent(Event event) {
        if (isRunnable()) {
            queue.add(event);
            wakeup();
        }
    }

    public void addTimer(Timer timer) {
        timers.add(timer);
    }

    /**
     * Publish an event.
     *
     * @param event the event
     */
    public void publish(Event event) {
        if (null != group) {
            group.publish(event);
        } else {
            EventLoopGroup.instance().publish(event);
        }
    }

    /**
     * Process an event.
     *
     * @param thread the current thread
     * @param event the event
     */
    private void processEvent(Thread thread, Event event) {
        if (null != event && wakeupEvent != event && canProcess(thread)) {
            eventSubscribers.process(event);
        }
    }

    /**
     * Process all pending event (none-blocking).
     *
     * @param thread the current thread
     */
    private void processPendingEvents(Thread thread) {

        while (canProcess(thread)) {
            Event event = queue.poll();
            processEvent(thread, event);

            if (null == event) {
                break;
            }
        }
    }

    /**
     * Waits for next event (blocking).
     *
     * @param thread the thread
     * @throws InterruptedException the interrupted exception
     */
    private void waitEvent(Thread thread) throws InterruptedException {

        long timeOut = 0;
        if (canProcess(thread)) {
            timeOut = timers.process();
        }
        if (canProcess(thread)) {
            Event event = null;
            if (0 == timeOut) {
                event = queue.take();
            } else {
                event = queue.poll(timeOut, timers.getUnit());
            }
            if (null != event) {
                processEvent(thread, event);
            }
        }
    }

    /**
     * Checks if processing can be done.
     *
     * @param thread the thread
     * @return true, if successful
     */
    private boolean canProcess(Thread thread) {
        boolean canProcess = false;

        try {
            if (isRunnable() && !thread.isInterrupted()) {
                canProcess = true;
            }

            if (channelSubscribers.isEmpty() && eventSubscribers.isEmpty() && timers.isEmpty()) {
                canProcess = false;
            }
        } catch (Throwable e) {
            canProcess = false;
        }

        return canProcess;
    }

    private void processChannels(Thread thread) throws IOException {

        if (canProcess(thread)) {
            long timeOut = timers.process();
            channelSubscribers.process(timeOut);
        }
    }

    /**
     * The event loop that processes event and sockets. Returns when current
     * thread is interrupted or {@link #shutdown() } is called.
     */
    public void process() {
        Thread currentThread = Thread.currentThread();

        while (canProcess(currentThread)) {
            try {
                timers.process();
                processPendingEvents(currentThread);

                processChannels(currentThread);

                if (channelSubscribers.isEmpty()) {
                    waitEvent(currentThread);
                }
            } catch (InterruptedException e) {
                break;
            } catch (Throwable e) {
                fatalError(this, e);
            }
        }

        clear();
    }

    /**
     * Event subscribing of given event id.
     *
     * @param id the id
     * @param object the callback object
     */
    public void subscribe(EventId id, EventSubscriber object) {
        if (isRunnable()) {
            eventSubscribers.add(id, object);
        }
    }

    /**
     * Removes the event subscribe.
     *
     * @param id the id
     * @param object the callback object
     */
    public void removeSubscribe(EventId id, EventSubscriber object) {
        eventSubscribers.remove(id, object);
    }

    /**
     * Checks if is event supported in this event loop.
     *
     * @param id the id
     * @return true, if is event supported
     */
    public boolean isEventSupported(EventId id) {
        return (isRunnable() && eventSubscribers.contains(id));
    }

    /**
     * Channel subscribing of given non-blocking channel. Overrides previous
     * subscriber as channel can have only one subscriber.
     *
     * @param channel the non-blocking channel
     * @param object the callback object
     * @param ops See {@link java.nio.channels.SelectableChannel#validOps() }
     */
    public void subscribe(SelectableChannel channel, ChannelSubscriber object, int ops) {

        if (isRunnable()) {
            try {
                channelSubscribers.add(channel, object, ops);
            } catch (Throwable t) {
                fatalError(this, t);
            }
        }
    }

    /**
     * Removes the channel subscriber.
     *
     * @param channel the channel
     */
    public void removeSubscribe(SelectableChannel channel) {
        channelSubscribers.remove(channel);
    }

    /**
     * Gets the event group where event loop belongs.
     *
     * @return the group
     */
    EventLoopGroup getGroup() {
        return group;
    }

    /**
     * Fatal error.
     *
     * @param object the calling object
     * @param throwable the throwable
     */
    synchronized static public void fatalError(Object object, Throwable throwable) {

        if (null != errorHandler) {
            errorHandler.fatalError(object, throwable);
        }
    }

    /**
     * Shutdown all event loops.
     */
    static public void shutdownAll() {
        EventLoopGroup.shutdown(EventLoopGroup.instance());
    }

    /**
     * Shutdown all event loops in event group.
     */
    public void shutdownAllInTheGroup() {
        EventLoopGroup.shutdown(group);
    }

    /**
     * Shutdown this event loop.
     */
    public void shutdown() {
        allowRunning = false;
        wakeup();
        clear();
    }

    /**
     * Clear event loop.
     */
    private void clear() {
        allowRunning = false;
        try {
            eventSubscribers.clear();
        } catch (Throwable e) {
        }
        try {
            channelSubscribers.close();
        } catch (Throwable e) {
        }

        if (null != group) {
            EventLoopGroup temp = group;
            group = null;
            temp.remove(this);
        }
    }

    /**
     * Set error handler.
     *
     * @param handler the new error handler
     * @return previous handler
     */
    synchronized public static EventLoopErrorHandler setErrorHandler(EventLoopErrorHandler handler) {
        EventLoopErrorHandler old = errorHandler;
        errorHandler = handler;
        return old;
    }
}
