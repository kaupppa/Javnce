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
    final private Map<EventId, Set<WeakReference<EventSubscriber>>> map;

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
    void add(EventId id, EventSubscriber handler) {
        if (null != handler && null != id) {
            Set<WeakReference<EventSubscriber>> set = getSet(id, true);
            set.add(new WeakReference<>(handler));
        }
    }

    /**
     * Removes an event subscriber.
     *
     * @param id the event id of subscribed event
     * @param handler the handler callback object
     */
    void remove(EventId id, EventSubscriber handler) {
        Set<WeakReference<EventSubscriber>> set = getSet(id, false);

        if (null != set) {
            for (Iterator<WeakReference<EventSubscriber>> i = set.iterator(); i.hasNext();) {
                WeakReference<EventSubscriber> ref = i.next();
                if (handler == ref.get() || null == ref.get()) {
                    i.remove();
                }
            }
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
    private List<WeakReference<EventSubscriber>> get(EventId id) {
        List<WeakReference<EventSubscriber>> list = null;
        Set<WeakReference<EventSubscriber>> set = getSet(id, false);

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
    boolean contains(EventId id) {
        return map.containsKey(id);
    }

    /**
     * Gets the sets of the subscribers of certain event.
     *
     * @param id the event id
     * @param createIfNull if true then an empty set is created
     * @return the set of subscribers
     */
    private Set<WeakReference<EventSubscriber>> getSet(EventId id, boolean createIfNull) {
        Set<WeakReference<EventSubscriber>> set = map.get(id);

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
        checkRefs();
        if (null == event) {
            return;
        }
        List<WeakReference<EventSubscriber>> list = get(event.Id());
        if (null != list) {
            for (Iterator<WeakReference<EventSubscriber>> i = list.iterator(); i.hasNext();) {
                WeakReference<EventSubscriber> ref = i.next();
                EventSubscriber subscriber = ref.get();
                if (null != subscriber) {
                    subscriber.event(event);
                }
            }
        }
    }

    /**
     * Checks if subscribers exists or not.
     *
     * @return true if no subscribers
     */
    boolean isEmpty() {
        checkRefs();
        return map.isEmpty();
    }

    /**
     * Goes through callbacks and removes callbacks that are cleaned by garbage
     * collector.
     */
    private void checkRefs() {

        Set<EventId> keys = map.keySet();

        for (Iterator<EventId> keyIterator = keys.iterator(); keyIterator.hasNext();) {
            EventId id = keyIterator.next();
            Set<WeakReference<EventSubscriber>> set = map.get(id);
            if (null == set) {
                keyIterator.remove();
            } else {
                for (Iterator<WeakReference<EventSubscriber>> i = set.iterator(); i.hasNext();) {
                    WeakReference<EventSubscriber> ref = i.next();
                    if (null == ref.get()) {
                        i.remove();
                    }
                }
                if (set.isEmpty()) {
                    map.remove(id);
                }
            }
        }
    }
}
