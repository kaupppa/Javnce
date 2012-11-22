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

import static org.junit.Assert.*;
import org.junit.Test;

public class TestEventSubscriberListTest {
    private EventSubscriberList list;

    class EventHandler implements EventSubscriber {
        Event lastEvent;
        @Override
        public void event(Event event) {
            lastEvent = event;
        }
    }

    @Test
    public void testEventSubscriberList() {
        list = new EventSubscriberList();
        assertNotNull(list);
    }

    @Test
    public void testAddingAndRemoving() {

        list = new EventSubscriberList();
        EventHandler handler = new EventHandler();

        TestEvent event = new TestEvent("An event");
        list.add(event.Id(), handler);

        assertTrue(list.contains(event.Id()));
        list.remove(event.Id(), handler);
        assertFalse(list.contains(event.Id()));
    }


    @Test
    public void testProcess() {
        TestEvent event1 = new TestEvent("Event 1");
        TestEvent event2 = new TestEvent("Event 2");

        list = new EventSubscriberList();
        EventHandler handler1 = new EventHandler();
        EventHandler handler1a = new EventHandler();
        EventHandler handler2 = new EventHandler();

        assertNull(handler1.lastEvent);
        assertNull(handler1a.lastEvent);
        assertNull(handler2.lastEvent);


        list.add(event1.Id(), handler1);
        list.add(event1.Id(), handler1a);
        list.add(event2.Id(), handler2);

        list.process(event1);

        assertEquals(event1, handler1.lastEvent);
        assertEquals(event1, handler1a.lastEvent);
        assertNull(handler2.lastEvent);

        list.process(event2);

        assertEquals(event1, handler1.lastEvent);
        assertEquals(event1, handler1a.lastEvent);
        assertEquals(event2, handler2.lastEvent);
    }
}