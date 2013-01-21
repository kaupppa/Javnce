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

import java.util.List;

/**
 * The event loop group.
 */
class EventLoopGroup {

    /**
     * The event loops in this group.
     */
    final private ConcurrentSet<EventLoop> loops;
    /**
     * The child groups.
     */
    final private ConcurrentSet<EventLoopGroup> children;
    /**
     * The parent.
     */
    final private EventLoopGroup parent;
    /**
     * The root instance.
     */
    static final private EventLoopGroup root = new EventLoopGroup(null);

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
    private EventLoopGroup(EventLoopGroup parent) {
        loops = new ConcurrentSet<>();
        children = new ConcurrentSet<>();
        this.parent = parent;

        if (null != parent) {
            parent.children.add(this);
        }
    }

    /**
     * Adds event loop into this group.
     *
     * @param loop the event loop
     */
    void add(EventLoop loop) {
        loops.add(loop);
    }

    /**
     * Removes event loop from this group.
     *
     * @param loop the event loop
     */
    void remove(EventLoop loop) {
        loops.remove(loop);
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

        EventLoopGroup child = new EventLoopGroup(this);
        child.add(loop);
        remove(loop);
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
        }
    }

    /**
     * Checks if no event loop is in this or child groups.
     *
     * @return true, if is empty
     */
    boolean isEmpty() {
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
            if (null != root) {
                root.broadcast(event);
            }
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


        for (EventLoop loop : getLoops()) {
            if (loop.isEventSupported(event.Id())) {
                loop.addEvent(event);
                handled = true;
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

        for (EventLoop loop : getLoops()) {
            loop.shutdown();
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
    List<EventLoop> getLoops() {
        return loops.get();
    }
}
