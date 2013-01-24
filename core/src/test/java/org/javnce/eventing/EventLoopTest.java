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
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EventLoopTest {

    private EventLoopGroup root;

    private static void sleep(int ms) {
        try {
            //Wait
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
        }
    }

    @Before
    public void setUp() throws Exception {
        root = EventLoopGroup.instance();
    }

    @After
    public void tearDown() throws Exception {
        EventLoopGroup.shutdown(root);

        if (false == root.isEmpty()) {
            throw new Exception("tearDown failure, EventLoopGroup not empty");
        }
    }

    @Test
    public void testRun() {
        EventLoop eventLoop = new EventLoop();

        //Test that empty eventLoop does not stay running
        Thread thread = new Thread(eventLoop);
        thread.start();

        sleep(20);
        assertFalse(thread.isAlive());
    }

    @Test
    public void testMoveToChildGroup() {
        EventLoop eventLoop = new EventLoop();
        EventLoop childEventLoop = new EventLoop();
        assertEquals(eventLoop.getGroup(), childEventLoop.getGroup());

        childEventLoop.moveToNewChildGroup();
        assertTrue(eventLoop.getGroup() != childEventLoop.getGroup());
    }

    @Test
    public void testAddEvent() {
        {
            //Normal case
            EventTester tester = new EventTester(null);

            TestEvent event = new TestEvent("Event");

            tester.eventLoop.subscribe(event.Id(), tester);

            tester.thread.start();

            //Wait
            sleep(10);
            tester.eventLoop.addEvent(event);

            //Wait
            sleep(10);
            assertEquals(1, tester.events.size());
        }
        {
            //Add after shutdown
            MyErrorHandler h = new MyErrorHandler();
            EventLoopErrorHandler old = EventLoop.setErrorHandler(h);

            EventTester tester = new EventTester(null);

            TestEvent event = new TestEvent("Event");

            tester.eventLoop.shutdown();

            tester.eventLoop.addEvent(event);

            assertFalse(h.called);
            EventLoop.setErrorHandler(old);
        }
    }

    @Test
    public void testPublish() {
        EventTester tester = new EventTester(null);

        TestEvent event = new TestEvent("Event");

        tester.eventLoop.subscribe(event.Id(), tester);

        tester.thread.start();

        //Wait
        sleep(40);

        tester.eventLoop.publish(event);

        //Wait
        sleep(40);

        assertEquals(1, tester.events.size());
        EventLoop.shutdownAll();
        sleep(40);
        tester.eventLoop.publish(event);
    }

    @Test
    public void testProcess() {

        {
            //Test that empty eventLoop does return from process
            EventLoop eventLoop = new EventLoop();
            eventLoop.process(); //Hangs in case of failure
        }
        {
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
        {
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
    }

    @Test
    public void testSubscribeEventIdEventSubscriber() {
        EventTester tester = new EventTester(null);

        TestEvent event = new TestEvent("Event");

        tester.eventLoop.subscribe(event.Id(), tester);

        tester.thread.start();

        //Wait
        sleep(10);

        tester.eventLoop.publish(event);

        //Wait
        sleep(10);

        assertEquals(1, tester.events.size());
    }

    @Test
    public void testRemoveSubscribeEventIdEventSubscriber() {
        {
            //Normal case
            EventTester tester = new EventTester(null);
            TestEvent event = new TestEvent("Event");

            tester.eventLoop.subscribe(event.Id(), tester);
            tester.thread.start();

            //Wait
            sleep(10);

            tester.eventLoop.publish(event);

            //Wait
            sleep(10);

            assertEquals(1, tester.events.size());
            tester.events.clear();

            tester.eventLoop.removeSubscribe(event.Id(), tester);
            tester.eventLoop.publish(event);

            //Wait
            sleep(10);
            assertEquals(0, tester.events.size());
        }
        {
            MyErrorHandler h = new MyErrorHandler();
            EventLoopErrorHandler old = EventLoop.setErrorHandler(h);
            //Remove after shutdown
            EventTester tester = new EventTester(null);
            TestEvent event = new TestEvent("Event");
            tester.eventLoop.subscribe(event.Id(), tester);
            tester.eventLoop.shutdown();

            tester.eventLoop.removeSubscribe(event.Id(), tester);
            assertFalse(h.called);
            EventLoop.setErrorHandler(old);
        }

    }

    @Test
    public void testIsEventSupported() {
        {
            //Normal case
            EventTester tester = new EventTester(null);
            TestEvent event = new TestEvent("Event");
            tester.eventLoop.subscribe(event.Id(), tester);

            assertTrue(tester.eventLoop.isEventSupported(event.Id()));

            tester.eventLoop.removeSubscribe(event.Id(), tester);
            assertFalse(tester.eventLoop.isEventSupported(event.Id()));
        }
        {
            //Test that after shutdown events are not supported
            EventTester tester = new EventTester(null);
            TestEvent event = new TestEvent("Event");
            tester.eventLoop.subscribe(event.Id(), tester);

            assertTrue(tester.eventLoop.isEventSupported(event.Id()));

            tester.eventLoop.shutdown();
            assertFalse(tester.eventLoop.isEventSupported(event.Id()));
        }
    }

    @Test
    public void testSubscribeSelectableChannelChannelSubscriberInt() throws Exception {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            {
                // Normal case
                EventTester tester = new EventTester(null);

                tester.eventLoop.subscribe(loopback.channel1(), tester, SelectionKey.OP_READ);

                Thread thread = new Thread(tester.eventLoop);
                thread.start();

                sleep(10);

                int length = 10;
                loopback.channel2().write(ByteBuffer.allocate(length));

                sleep(10);
                assertEquals(length, tester.buffer.position());
            }

            {
                // Negative case, dummy parameters
                MyErrorHandler h = new MyErrorHandler();
                EventLoopErrorHandler old = EventLoop.setErrorHandler(h);
                EventLoop eventLoop = new EventLoop();

                eventLoop.subscribe(null, null, SelectionKey.OP_READ);

                assertTrue(h.called);
                EventLoop.setErrorHandler(old);
            }
            {
                // Test that adding after shutdown is not an error
                MyErrorHandler h = new MyErrorHandler();
                EventLoopErrorHandler old = EventLoop.setErrorHandler(h);

                EventTester tester = new EventTester(null);

                tester.eventLoop.shutdown();
                tester.eventLoop.subscribe(loopback.channel1(), tester, SelectionKey.OP_READ);

                assertFalse(h.called);
                EventLoop.setErrorHandler(old);
            }
        }
    }

    @Test
    public void testRemoveSubscribeSelectableChannel() throws Exception {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            {
                //Normal case
                EventTester tester = new EventTester(null);

                tester.eventLoop.subscribe(loopback.channel1(), tester, SelectionKey.OP_READ);

                tester.thread.start();

                sleep(10);

                int length = 10;
                loopback.channel2().write(ByteBuffer.allocate(length));
                sleep(10);
                assertEquals(length, tester.buffer.position());

                tester.buffer.clear();
                //Remove
                tester.eventLoop.removeSubscribe(loopback.channel1());

                //Write again
                loopback.channel2().write(ByteBuffer.allocate(length));

                sleep(10);
                assertEquals(0, tester.buffer.position());
            }
            {
                MyErrorHandler h = new MyErrorHandler();
                EventLoopErrorHandler old = EventLoop.setErrorHandler(h);

                //Removing with illegal paramters
                EventTester tester = new EventTester(null);

                //Remove
                tester.eventLoop.removeSubscribe(null);

                assertFalse(h.called);
                EventLoop.setErrorHandler(old);
            }
            {
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
            }
        }
    }

    @Test
    public void testGetGroup() {
        EventLoop eventLoop = new EventLoop();
        assertTrue(root == eventLoop.getGroup());
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

        //Wait
        sleep(10 * threadsInGroupt);

        //All alive ?
        for (int i = 0; i < testers.length; i++) {
            assertTrue(testers[i].thread.isAlive());

            for (EventTester child : testers[i].testers) {
                assertTrue(child.thread.isAlive());
            }
        }

        //Kill first group
        testers[0].eventLoop.shutdownAllInTheGroup();
        testers[0].thread.join();
        assertFalse(testers[0].thread.isAlive());

        for (EventTester child : testers[0].testers) {
            child.thread.join();
            assertFalse(child.thread.isAlive());
        }


        //other alive ?
        for (int i = 1; i < testers.length; i++) {
            assertTrue(testers[i].thread.isAlive());

            for (EventTester child : testers[i].testers) {
                assertTrue(child.thread.isAlive());
            }
        }
    }

    @Test
    public void testShutdown() {

        {
            //Before start
            EventTester tester = new EventTester(null);
            TestEvent event = new TestEvent("Event");
            tester.eventLoop.subscribe(event.Id(), tester);
            tester.eventLoop.shutdown();
            tester.thread.start();

            //Wait
            sleep(10);

            assertFalse(tester.thread.isAlive());
        }
        {
            //After start
            EventTester tester = new EventTester(null);
            TestEvent event = new TestEvent("Event");
            tester.eventLoop.subscribe(event.Id(), tester);
            tester.thread.start();

            //Wait
            sleep(10);
            assertTrue(tester.thread.isAlive());

            tester.eventLoop.shutdown();
            //Wait
            sleep(10);

            assertFalse(tester.thread.isAlive());
        }
    }

    class MyErrorHandler implements EventLoopErrorHandler {

        boolean called = false;

        @Override
        public void fatalError(Object object, Throwable throwable) {
            called = true;
        }
    }

    @Test
    public void testFatalError() {

        MyErrorHandler h = new MyErrorHandler();
        EventLoopErrorHandler old = EventLoop.setErrorHandler(h);
        assertNotNull(old);
        assertFalse(h.called);
        EventLoop.fatalError(this, new UnsupportedOperationException("Not supported yet."));
        assertTrue(h.called);

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
        assertTrue((timeOut + 10) >= elapsedTime);
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
        assertTrue((timeOut + 10) >= elapsedTime);
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
            assertTrue((timeOut + 10) >= elapsedTime);
            assertTrue((timeOut) <= elapsedTime);
        }
    }

    @Test
    public void testShutdownAll() {

        EventTester tester = new EventTester(null);
        TestEvent event = new TestEvent("Event");
        tester.eventLoop.subscribe(event.Id(), tester);
        tester.thread.start();

        //Wait
        sleep(10);
        assertTrue(tester.thread.isAlive());

        EventLoop.shutdownAll();
        //Wait
        sleep(10);

        assertFalse(tester.thread.isAlive());
    }
}
