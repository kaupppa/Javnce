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

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EventLoopTest {

    final static int WaitTime = 100;
    final static int SleepTimeTime = 50;
    final static int TimerAccuracyTime = 50;
    private EventGroup root;

    class MyErrorHandler implements EventLoopErrorHandler {

        boolean called = false;

        @Override
        public void fatalError(Object object, Throwable throwable) {
            called = true;
        }
    }

    void sleep() {
        try {
            Thread.sleep(SleepTimeTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(EventLoopTest.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    @Before
    public void setUp() throws Exception {
        root = EventGroup.instance();
        //Clear interrupted
        Thread.interrupted();
    }

    @After
    public void tearDown() throws Exception {
        Thread.interrupted();
        root.shutdown();

        if (false == root.isEmpty()) {
            throw new Exception("tearDown failure, EventLoopGroup not empty");
        }
    }

    @Test
    public void testRun() throws Throwable {
        EventLoop eventLoop = new EventLoop();

        //Test that empty eventLoop does not stay running
        Thread thread = new Thread(eventLoop);
        thread.start();

        thread.join(WaitTime);
        assertFalse(thread.isAlive());
    }


    @Test
    public void testDispatchEvent() {
        EventTester tester = new EventTester(null);

        TestEvent event = new TestEvent("Event");

        tester.eventLoop.subscribe(event.Id(), tester);

        tester.thread.start();
        tester.eventLoop.dispatchEvent(event);
        
        sleep();
        assertEquals(1, tester.events.size());

    }

    @Test
    public void testDispatchEventAfterShutdown() {

        EventTester tester = new EventTester(null);

        TestEvent event = new TestEvent("Event");

        tester.eventLoop.shutdown();

        tester.eventLoop.dispatchEvent(event); // No throw
    }

    @Test
    public void testPublish() {
        EventTester tester = new EventTester(null);

        TestEvent event = new TestEvent("Event");

        tester.eventLoop.subscribe(event.Id(), tester);
        tester.thread.start();

        tester.eventLoop.publish(event);

        sleep();
        assertEquals(1, tester.events.size());

        EventLoop.shutdownAll();
        sleep();
        tester.eventLoop.publish(event); // No throw
    }

    @Test
    public void testProcessEmpty() {

        //Test that empty eventLoop does return from process
        EventLoop eventLoop = new EventLoop();
        eventLoop.process(); //Hangs in case of failure
    }

    @Test
    public void testProcessWithUncheckException() {

        MyErrorHandler h = new MyErrorHandler();
        EventLoopErrorHandler old = EventLoop.setErrorHandler(h);

        //Test that error handler is called if execption is thrown
        TestEvent event = new TestEvent("Event");
        EventLoop eventLoop = new EventLoop();

        eventLoop.subscribe(event.Id(), new EventSubscriber() {
            @Override
            public void event(Event event) {
                throw new RuntimeException("Testing");
            }
        });
        eventLoop.publish(event);
        eventLoop.process(); //Hangs or throws if not correct handling


        assertTrue(h.called);
        EventLoop.setErrorHandler(old);
    }

    @Test
    public void testProcessWithInterrupt() {

        MyErrorHandler h = new MyErrorHandler();
        EventLoopErrorHandler old = EventLoop.setErrorHandler(h);
        //Test that error handler is not called incase of interupt
        TestEvent event = new TestEvent("Event");
        EventLoop eventLoop = new EventLoop();

        eventLoop.subscribe(event.Id(), new EventSubscriber() {
            @Override
            public void event(Event event) {
                Thread.currentThread().interrupt();
            }
        });
        eventLoop.publish(event);
        eventLoop.process(); //Hangs or throws if not correct handling

        assertFalse(h.called);
        EventLoop.setErrorHandler(old);

    }

    @Test
    public void testSubscribeEventIdEventSubscriber() {
        EventTester tester = new EventTester(null);

        TestEvent event = new TestEvent("Event");

        tester.eventLoop.subscribe(event.Id(), tester);

        tester.thread.start();

        tester.eventLoop.publish(event);

        sleep();

        assertEquals(1, tester.events.size());
    }

    @Test
    public void testRemoveSubscribeEventIdEventSubscriber() {
        EventTester tester = new EventTester(null);
        TestEvent event = new TestEvent("Event");

        tester.eventLoop.subscribe(event.Id(), tester);
        tester.thread.start();

        tester.eventLoop.publish(event);

        sleep();

        assertEquals(1, tester.events.size());
        tester.events.clear();

        tester.eventLoop.removeSubscribe(event.Id(), tester);
        tester.eventLoop.publish(event);

        sleep();
        assertEquals(0, tester.events.size());
    }

    public void testRemoveSubscribeEventAfterShutdown() {
        MyErrorHandler h = new MyErrorHandler();
        EventLoopErrorHandler old = EventLoop.setErrorHandler(h);


        EventTester tester = new EventTester(null);
        TestEvent event = new TestEvent("Event");
        tester.eventLoop.subscribe(event.Id(), tester);
        tester.eventLoop.shutdown();

        //Remove after shutdown
        tester.eventLoop.removeSubscribe(event.Id(), tester);
        assertFalse(h.called);
        EventLoop.setErrorHandler(old);
    }


    @Test
    public void testSubscribeSelectableChannel() throws Exception {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            // Normal case
            EventTester tester = new EventTester(null);

            tester.eventLoop.subscribe(loopback.channel1(), tester, SelectionKey.OP_READ);

            Thread thread = new Thread(tester.eventLoop);
            thread.start();

            int length = 10;
            loopback.channel2().write(ByteBuffer.allocate(length));

            sleep();
            assertEquals(length, tester.buffer.position());
        }
    }

    @Test
    public void testSubscribeSelectableChannelIllegal() {
        MyErrorHandler h = new MyErrorHandler();
        EventLoopErrorHandler old = EventLoop.setErrorHandler(h);
        EventLoop eventLoop = new EventLoop();

        //Illegal channel
        eventLoop.subscribe(null, null, SelectionKey.OP_READ);

        assertTrue(h.called);
        EventLoop.setErrorHandler(old);
    }

    @Test
    public void testSubscribeSelectableChannelAfterShutdown() {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            // Test that adding after shutdown is not an error
            MyErrorHandler h = new MyErrorHandler();
            EventLoopErrorHandler old = EventLoop.setErrorHandler(h);

            EventTester tester = new EventTester(null);

            tester.eventLoop.shutdown();
            tester.eventLoop.subscribe(loopback.channel1(), tester, SelectionKey.OP_READ);

            assertFalse(h.called);
            EventLoop.setErrorHandler(old);
        } catch (Exception ex) {
            Logger.getLogger(EventLoopTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    @Test
    public void testRemoveSubscribeSelectableChannel() throws Exception {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            EventTester tester = new EventTester(null);

            tester.eventLoop.subscribe(loopback.channel1(), tester, SelectionKey.OP_READ);

            tester.thread.start();


            int length = 10;
            loopback.channel2().write(ByteBuffer.allocate(length));

            sleep();
            assertEquals(length, tester.buffer.position());

            tester.buffer.clear();

            //Remove
            tester.eventLoop.removeSubscribe(loopback.channel1());

            //Write again
            loopback.channel2().write(ByteBuffer.allocate(length));

            sleep();
            assertEquals(0, tester.buffer.position());
        }
    }

    @Test
    public void testRemoveSubscribeSelectableChannelIllegal() {

        MyErrorHandler h = new MyErrorHandler();
        EventLoopErrorHandler old = EventLoop.setErrorHandler(h);

        //Removing with illegal paramters
        EventTester tester = new EventTester(null);

        //Remove
        tester.eventLoop.removeSubscribe(null);

        assertFalse(h.called);
        EventLoop.setErrorHandler(old);
    }

    @Test
    public void testRemoveSubscribeSelectableChannelAfterShutdown() {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            MyErrorHandler h = new MyErrorHandler();
            EventLoopErrorHandler old = EventLoop.setErrorHandler(h);

            //Removing after shutdown
            EventTester tester = new EventTester(null);

            tester.eventLoop.subscribe(loopback.channel1(), tester, SelectionKey.OP_READ);
            tester.eventLoop.shutdown();

            //Remove
            tester.eventLoop.removeSubscribe(loopback.channel1());

            assertFalse(h.called);
            EventLoop.setErrorHandler(old);
        } catch (Exception ex) {
            Logger.getLogger(EventLoopTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }


    @Test
    public void testShutdownAllInTheGroup() throws InterruptedException {
        //Lets create n sub groups each sub group having m threads
        int subGroupCount = 5;
        int threadsInGroupt = 5;

        EventTester testers[] = new EventTester[subGroupCount];

        TestEvent event = new TestEvent("Event");


        for (int i = 0; i < testers.length; i++) {
            testers[i] = new EventTester(null);
            testers[i].eventLoop.moveToNewChildGroup();
            testers[i].createValidGroup(threadsInGroupt);
            testers[i].handleEvent(event.Id());
            testers[i].startAll();
        }

        sleep();

        //All alive ?
        for (int i = 0; i < testers.length; i++) {
            assertTrue(testers[i].thread.isAlive());

            for (EventTester child : testers[i].testers) {
                assertTrue(child.thread.isAlive());
            }
        }

        //Kill first group
        testers[0].eventLoop.shutdownGroup();
        testers[0].thread.join(WaitTime);
        assertFalse(testers[0].thread.isAlive());

        for (EventTester child : testers[0].testers) {
            child.thread.join(WaitTime);
            assertFalse(child.thread.isAlive());
        }

        //Check that others are alive
        for (int i = 1; i < testers.length; i++) {
            assertTrue(testers[i].thread.isAlive());

            for (EventTester child : testers[i].testers) {
                assertTrue(child.thread.isAlive());
            }
        }
    }

    @Test
    public void testShutdownBeforeStart() {
        EventTester tester = new EventTester(null);
        TestEvent event = new TestEvent("Event");
        tester.eventLoop.subscribe(event.Id(), tester);

        tester.eventLoop.shutdown();

        tester.thread.start();

        sleep();

        assertFalse(tester.thread.isAlive());
    }

    @Test
    public void testShutdownAfterStart() {
        EventTester tester = new EventTester(null);
        TestEvent event = new TestEvent("Event");
        tester.eventLoop.subscribe(event.Id(), tester);

        tester.thread.start();

        tester.eventLoop.shutdown();

        sleep();

        assertFalse(tester.thread.isAlive());
        
        tester.eventLoop.shutdownGroup();
    }

    @Test
    public void testFatalError() {

        MyErrorHandler h = new MyErrorHandler();
        EventLoopErrorHandler old = EventLoop.setErrorHandler(h);
        assertNotNull(old);
        assertFalse(h.called);

        EventLoop.fatalError(this, new UnsupportedOperationException("Not supported yet."));
        assertTrue(h.called);


        //Check that no throw 
        EventLoop.setErrorHandler(null);
        EventLoop.fatalError(this, new UnsupportedOperationException("Not supported yet."));

        EventLoop.setErrorHandler(old);
    }

    @Test
    public void testAddTimerAfterShutdown() {
        MyErrorHandler h = new MyErrorHandler();
        EventLoopErrorHandler old = EventLoop.setErrorHandler(h);

        final EventLoop eventLoop = new EventLoop();
        //Add timer
        long timeOut = 50;
        Timer timer = new Timer(new TimeOutCallback() {
            @Override
            public void timeout() {
                eventLoop.shutdown();
            }
        }, timeOut);

        eventLoop.shutdown();

        eventLoop.addTimer(timer);

        assertFalse(h.called);
        EventLoop.setErrorHandler(old);
    }

    @Test
    public void testAddTimerWhenOnlyTimers() {
        final EventLoop eventLoop = new EventLoop();
        //Add timer
        long timeOut = 50;
        Timer timer = new Timer(new TimeOutCallback() {
            @Override
            public void timeout() {
                eventLoop.shutdown();
            }
        }, timeOut);
        long startTime = System.currentTimeMillis();

        eventLoop.addTimer(timer);

        //We get stuck in case of failure
        eventLoop.process();

        long elapsedTime = System.currentTimeMillis() - startTime;

        //Check that time matches timeout
        assertTrue((timeOut + TimerAccuracyTime) >= elapsedTime);
        assertTrue((timeOut) <= elapsedTime);
    }

    @Test
    public void testAddTimerWithEvents() {
        final EventLoop eventLoop = new EventLoop();

        TestEvent event = new TestEvent("Event");
        //Add event
        eventLoop.subscribe(event.Id(), new EventSubscriber() {
            @Override
            public void event(Event event) {
                // Do nothing
            }
        });


        //Add timer
        long timeOut = 50;
        Timer timer = new Timer(new TimeOutCallback() {
            @Override
            public void timeout() {
                eventLoop.shutdown();
            }
        }, timeOut);
        long startTime = System.currentTimeMillis();

        eventLoop.addTimer(timer);

        //We get stuck in case of failure
        eventLoop.process();

        long elapsedTime = System.currentTimeMillis() - startTime;

        //Check that time matches timeout
        assertTrue((timeOut + TimerAccuracyTime) >= elapsedTime);
        assertTrue((timeOut) <= elapsedTime);
    }

    @Test
    public void testAddTimerWithSockets() throws Exception {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            final EventLoop eventLoop = new EventLoop();

            eventLoop.subscribe(loopback.channel1(), new ChannelSubscriber() {
                @Override
                public void channel(SelectionKey key) {
                    //Do nothing
                }
            }, SelectionKey.OP_READ);

            //Add timer
            long timeOut = 50;
            Timer timer = new Timer(new TimeOutCallback() {
                @Override
                public void timeout() {
                    eventLoop.shutdown();
                }
            }, timeOut);
            long startTime = System.currentTimeMillis();

            eventLoop.addTimer(timer);

            //We get stuck in case of failure
            eventLoop.process();

            long elapsedTime = System.currentTimeMillis() - startTime;

            //Check that time matches timeout
            assertTrue((timeOut + TimerAccuracyTime) >= elapsedTime);
            assertTrue((timeOut) <= elapsedTime);
        }
    }

    @Test
    public void testShutdownAll() {

        EventTester tester = new EventTester(null);
        TestEvent event = new TestEvent("Event");
        tester.eventLoop.subscribe(event.Id(), tester);
        tester.thread.start();

        assertTrue(tester.thread.isAlive());

        EventLoop.shutdownAll();

        sleep();

        assertFalse(tester.thread.isAlive());
    }
}
