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

import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.Assert.*;
import org.junit.Test;

public class EventSubscriberListTest {

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
    public void testAdd() {

        list = new EventSubscriberList();
        EventHandler handler = new EventHandler();
        EventHandler handler2 = new EventHandler();
        TestEvent event = new TestEvent("An event");
        list.add(event.Id(), handler);
        list.add(event.Id(), handler2);
        list.add(null, handler);
        list.add(event.Id(), null);
        list.add(null, null);

        assertTrue(list.contains(event.Id()));
        assertFalse(list.contains(null));

    }

    @Test
    public void testRemove() {

        list = new EventSubscriberList();
        EventHandler handler = new EventHandler();
        TestEvent event = new TestEvent("An event");
        list.add(event.Id(), handler);

        list.remove(null);
        assertTrue(list.contains(event.Id()));
        list.remove(event.Id());

        assertFalse(list.contains(event.Id()));

    }

    @Test
    public void testProcess() {
        TestEvent events[] = new TestEvent[]{new TestEvent("1"), new TestEvent("2"), new TestEvent("3")};
        EventHandler handlers[] = new EventHandler[events.length];

        list = new EventSubscriberList();

        for (int i = 0; i < events.length; i++) {
            handlers[i] = new EventHandler();
            assertNull(handlers[i].lastEvent);
            list.add(events[i].Id(), handlers[i]);
        }

        for (int i = 0; i < events.length; i++) {
            list.process(events[i]);
            assertEquals(events[i], handlers[i].lastEvent);
            handlers[i].lastEvent = null;
            for (int j = 0; j < events.length; j++) {
                assertNull(handlers[j].lastEvent);
            }
        }
        list.process(null); //No throw
        list.process(new TestEvent("Unknown")); //No throw
    }

    @Test
    public void testAnonymousCallback() {

        list = new EventSubscriberList();

        TestEvent event = new TestEvent("An event");
        final AtomicInteger integer = new AtomicInteger(0);

        list.add(event.Id(), new EventSubscriber() {
            @Override
            public void event(Event event) {
                integer.set(integer.get() + 1);

            }
        });


        //Call garbage collector to test that callback is not cleared
        System.gc();

        assertEquals(0, integer.get());
        list.process(event);
        assertEquals(1, integer.get());

    }
}
