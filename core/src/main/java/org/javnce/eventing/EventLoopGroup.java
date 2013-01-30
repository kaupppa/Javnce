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

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

/**
 * The event loop group.
 */
class EventLoopGroup {

    /**
     * The event loops in this group.
     */
    final private ConcurrentSet<WeakReference<EventLoop>> loops;
    /**
     * The child groups.
     */
    final private ConcurrentSet<EventLoopGroup> children;
    /**
     * The parent.
     */
    private EventLoopGroup parent;
    /**
     * The root instance.
     */
    static final private EventLoopGroup root = new EventLoopGroup();

    /**
     * The root instance getter.
     *
     * @return the root event loop group
     */
    static synchronized EventLoopGroup instance() {
        return root;
    }

    /**
     * Instantiates a new event loop group.
     *
     * @param parent the parent
     */
    private EventLoopGroup() {
        loops = new ConcurrentSet<>();
        children = new ConcurrentSet<>();
        this.parent = null;
    }

    /**
     * Adds a EventLoopGroup as child.
     *
     * @param childGroup the child group
     */
    private void addChildGroup(EventLoopGroup childGroup) {
        children.add(childGroup);
        childGroup.parent = this;
    }

    /**
     * Adds event loop into this group.
     *
     * @param loop the event loop
     */
    void add(EventLoop loop) {
        if (null != loop) {
            loops.add(new WeakReference<>(loop));
        }
    }

    /**
     * Removes event loop from this group.
     *
     * @param loop the event loop
     */
    void remove(EventLoop loop) {
        if (null != loop) {
            List<WeakReference<EventLoop>> list = getLoops();

            for (Iterator<WeakReference<EventLoop>> i = list.iterator(); i.hasNext();) {
                WeakReference<EventLoop> ref = i.next();
                if (loop == ref.get() || null == ref.get()) {
                    loops.remove(ref);
                }
            }
        }
        refresh(this);
        refresh(parent);
    }

    /**
     * Move event loop into new child group.
     *
     * @param loop the loop
     * @return the event loop group
     */
    EventLoopGroup moveToNewChild(EventLoop loop) {
        EventLoopGroup child = null;
        if (null != loop) {
            child = new EventLoopGroup();
            child.add(loop);

            this.remove(loop);
            this.addChildGroup(child);
        }
        return child;

    }

    /**
     * Refresh given group. Removes empty children.
     *
     * @param group the group
     */
    static private void refresh(EventLoopGroup group) {
        if (null != group) {
            for (EventLoopGroup child : group.getChildren()) {
                refresh(child);

                if (child.isEmpty()) {
                    group.children.remove(child);
                }
            }
            group.refreshLoops();
        }
    }

    /**
     * Removes cleaned loops from group.
     *
     */
    private void refreshLoops() {
        List<WeakReference<EventLoop>> list = getLoops();

        for (Iterator<WeakReference<EventLoop>> i = list.iterator(); i.hasNext();) {
            WeakReference<EventLoop> ref = i.next();
            if (null == ref.get()) {
                loops.remove(ref);
            }
        }
    }

    /**
     * Checks if no event loop is in this or child groups.
     *
     * @return true, if is empty
     */
    boolean isEmpty() {
        refreshLoops();
        return (loops.isEmpty() && children.isEmpty());
    }

    /**
     * Publish an event. If event is not processed within this group or it's
     * child groups then event is broadcasted.
     *
     * @param event the event
     */
    void publish(Event event) {

        boolean handled = process(event);

        if (!handled) {
            root.broadcast(event);
        }
    }

    /**
     * Process event within this group or it's child groups.
     *
     * @param event the event
     * @return true, if event was handled in this or it's child groups.
     */
    private boolean process(Event event) {
        boolean handled = false;
        List<WeakReference<EventLoop>> list = getLoops();

        for (Iterator<WeakReference<EventLoop>> i = list.iterator(); i.hasNext();) {
            WeakReference<EventLoop> ref = i.next();
            EventLoop loop = ref.get();
            if (null != loop) {
                if (loop.isEventSupported(event.Id())) {
                    loop.addEvent(event);
                    handled = true;
                }
            }
        }
        return handled;
    }

    /**
     * Broadcast event to root instance and to all it's children.
     *
     * @param event the event
     */
    private void broadcast(Event event) {

        process(event);

        for (EventLoopGroup child : getChildren()) {
            child.broadcast(event);
        }
    }

    /**
     * Shutdown all event loop in given group and it's child groups.
     *
     * @param group the group
     */
    static void shutdown(EventLoopGroup group) {
        if (null != group) {
            for (EventLoopGroup child : group.getChildren()) {
                shutdown(child);
            }

            group.stopLoops();
            refresh(group);
        }
    }

    /**
     * Stop event loops in this group.
     */
    private void stopLoops() {
        List<WeakReference<EventLoop>> list = getLoops();

        for (Iterator<WeakReference<EventLoop>> i = list.iterator(); i.hasNext();) {
            WeakReference<EventLoop> ref = i.next();
            EventLoop loop = ref.get();
            if (null != loop) {
                loop.shutdown();
            }
        }

    }

    /**
     * Parent group getter.
     *
     * @return the event loop group
     */
    EventLoopGroup parent() {
        return parent;
    }

    /**
     * Gets the list of children.
     *
     * @return the children
     */
    List<EventLoopGroup> getChildren() {
        return children.get();
    }

    /**
     * Gets the list of event loops.
     *
     * @return the loops
     */
    List<WeakReference<EventLoop>> getLoops() {
        return loops.get();
    }
}
