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

import java.util.HashMap;
import java.util.Map;

/**
 * The event subscriber collection with callback handling.
 *
 * The EventSubscriberList is thread safe.
 *
 */
class EventSubscriberList {

    /**
     * The map of event subscribers.
     */
    final private Map<EventId, EventSubscriber> map;
    /**
     * The synchronization lock.
     */
    final private Object lock;

    /**
     * Instantiates a new event subscriber list.
     */
    EventSubscriberList() {
        map = new HashMap<>();
        lock = new Object();
    }

    /**
     * Adds an event subscriber. Previous mapping will be removed, if any.
     *
     * @param id the event id of subscribed event
     * @param handler the callback object
     */
    void add(EventId id, EventSubscriber handler) {
        if (null != handler && null != id) {
            synchronized (lock) {
                map.put(id, handler);
            }
        }
    }

    /**
     * Removes an event subscriber.
     *
     * @param id the event id of subscribed event
     */
    void remove(EventId id) {
        synchronized (lock) {
            map.remove(id);
        }
    }

    /**
     * Checks if event subscriber is found.
     *
     * @param id the event id
     * @return true, if subscriber
     */
    boolean contains(EventId id) {
        boolean contains = false;
        synchronized (lock) {
            contains = map.containsKey(id);
        }
        return contains;
    }

    /**
     * Call subscribers for given event.
     *
     * @param event the event that is passed to all subscribers.
     */
    synchronized void process(Event event) {
        if (null == event) {
            return;
        }
        EventSubscriber subscriber = map.get(event.Id());
        if (null != subscriber) {
            //Note that subscriber.event must be called outside of synchronized (lock)
            subscriber.event(event);
        }
    }

    /**
     * Subscriber getter.
     *
     * @return EventSubscriber object or null if not found.
     */
    private EventSubscriber get(EventId id) {
        EventSubscriber subscriber = null;
        synchronized (lock) {
            subscriber = map.get(id);
        }
        return subscriber;
    }

    /**
     * Checks if subscribers exists or not.
     *
     * @return true if no subscribers
     */
    boolean isEmpty() {
        boolean empty = false;

        synchronized (lock) {
            empty = map.isEmpty();
        }
        return empty;
    }
}
