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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The event subscriber collection with callback handling.
 *
 */
class EventSubscriberList {

    /**
     * The map of event subscribers.
     */
    final private Map<EventId, Set<EventSubscriber>> map;

    /**
     * Instantiates a new event subscriber list.
     */
    EventSubscriberList() {
        this.map = new HashMap<>();
    }

    /**
     * Adds an event subscriber.
     *
     * @param id the event id of subscribed event
     * @param handler the callback object
     */
    synchronized void add(EventId id, EventSubscriber handler) {
        if (null != handler) {
            Set<EventSubscriber> set = getSet(id, true);
            set.add(handler);
        }
    }

    /**
     * Removes an event subscriber.
     *
     * @param id the event id of subscribed event
     * @param handler the handler callback object
     */
    synchronized void remove(EventId id, EventSubscriber handler) {
        Set<EventSubscriber> set = getSet(id, false);

        if (null != set) {
            set.remove(handler);

            if (set.isEmpty()) {
                map.remove(id);
            }
        }
    }

    /**
     * Gets the list of subscriber for given event.
     *
     * @param id the event id
     * @return the list
     */
    private synchronized List<EventSubscriber> get(EventId id) {
        List<EventSubscriber> list = null;
        Set<EventSubscriber> set = getSet(id, false);

        if (null != set) {
            list = new ArrayList<>(set);
        }

        return list;
    }

    /**
     * Checks if event subscriber is found.
     *
     * @param id the event id
     * @return true, if subscriber
     */
    synchronized boolean contains(EventId id) {
        return map.containsKey(id);
    }

    /**
     * Gets the sets of the subscribers of certain event.
     *
     * @param id the event id
     * @param createIfNull if true then an empty set is created
     * @return the set of subscribers
     */
    private Set<EventSubscriber> getSet(EventId id, boolean createIfNull) {
        Set<EventSubscriber> set = map.get(id);

        if (null == set && createIfNull) {
            set = new HashSet<>();
            map.put(id, set);
        }

        return set;
    }

    /**
     * Call subscribers for given event.
     *
     * @param event the event that is passed to all subscribers.
     */
    void process(Event event) {

        if (null != event) {
            List<EventSubscriber> list = get(event.Id());
            Iterator<EventSubscriber> iterator = list.iterator();

            while (iterator.hasNext()) {
                EventSubscriber subscriber = iterator.next();
                subscriber.event(event);
            }
        }
    }

    /**
     * Checks if subscribers exists or not.
     *
     * @return true if no subscribers
     */
    synchronized boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Removes all subscribers.
     */
    synchronized void clear() {
        map.clear();
    }
}
