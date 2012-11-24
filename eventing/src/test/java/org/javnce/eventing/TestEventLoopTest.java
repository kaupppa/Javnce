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
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestEventLoopTest {

    private EventLoopGroup root;
    private Pipe pipe;

    @Before
    public void setUp() throws Exception {
        root = EventLoopGroup.instance();
        pipe = Pipe.open();
        pipe.source().configureBlocking(false);
    }

    @After
    public void tearDown() throws Exception {
        pipe = null;
        EventLoopGroup.shutdown(root);

        if (false == root.isEmpty()) {
            throw new Exception("tearDown failure, EventLoopGroup not empty");
        }
    }

    @Test
    public void testRun() throws InterruptedException {
        EventLoop eventLoop = new EventLoop();

        //Test that empty eventLoop does not stay running
        Thread thread = new Thread(eventLoop);
        thread.start();

        thread.join(2);
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
    public void testAddEvent() throws InterruptedException {
        EventTester tester = new EventTester(null);

        TestEvent event = new TestEvent("Event");

        tester.eventLoop.subscribe(event.Id(), tester);

        tester.thread.start();

        //Wait
        tester.thread.join(10);

        tester.eventLoop.addEvent(event);

        //Wait
        tester.thread.join(10);

        assertEquals(1, tester.events.size());
    }

    @Test
    public void testPublish() throws InterruptedException {
        EventTester tester = new EventTester(null);

        TestEvent event = new TestEvent("Event");

        tester.eventLoop.subscribe(event.Id(), tester);

        tester.thread.start();

        //Wait
        tester.thread.join(10);

        tester.eventLoop.publish(event);

        //Wait
        tester.thread.join(10);

        assertEquals(1, tester.events.size());
    }

    @Test
    public void testProcess() {
        //Test that empty eventLoop does return from process
        EventLoop eventLoop = new EventLoop();
        eventLoop.process(); //Hangs in case of failure
    }

    @Test
    public void testSubscribeEventIdEventSubscriber() throws InterruptedException {
        EventTester tester = new EventTester(null);

        TestEvent event = new TestEvent("Event");

        tester.eventLoop.subscribe(event.Id(), tester);

        tester.thread.start();

        //Wait
        tester.thread.join(10);

        tester.eventLoop.publish(event);

        //Wait
        tester.thread.join(10);

        assertEquals(1, tester.events.size());
    }

    @Test
    public void testRemoveSubscribeEventIdEventSubscriber() throws InterruptedException {
        EventTester tester = new EventTester(null);

        TestEvent event = new TestEvent("Event");

        tester.eventLoop.subscribe(event.Id(), tester);

        tester.thread.start();

        //Wait
        tester.thread.join(10);

        tester.eventLoop.publish(event);

        //Wait
        tester.thread.join(10);

        assertEquals(1, tester.events.size());
        tester.events.clear();

        tester.eventLoop.removeSubscribe(event.Id(), tester);
        tester.eventLoop.publish(event);

        //Wait
        tester.thread.join(10);

        assertEquals(0, tester.events.size());

    }

    @Test
    public void testIsEventSupported() {
        EventTester tester = new EventTester(null);
        TestEvent event = new TestEvent("Event");
        tester.eventLoop.subscribe(event.Id(), tester);

        assertTrue(tester.eventLoop.isEventSupported(event.Id()));

        tester.eventLoop.removeSubscribe(event.Id(), tester);
        assertFalse(tester.eventLoop.isEventSupported(event.Id()));
    }

    @Test
    public void testSubscribeSelectableChannelChannelSubscriberInt() throws Throwable {
        EventTester tester = new EventTester(null);

        tester.eventLoop.subscribe(pipe.source(), tester, SelectionKey.OP_READ);

        Thread thread = new Thread(tester.eventLoop);
        thread.start();

        thread.join(10);

        int length = 10;
        pipe.sink().write(ByteBuffer.allocate(length));

        thread.join(10);
        assertEquals(length, tester.buffer.position());
    }

    @Test
    public void testRemoveSubscribeSelectableChannel() throws Throwable {
        EventTester tester = new EventTester(null);

        tester.eventLoop.subscribe(pipe.source(), tester, SelectionKey.OP_READ);

        tester.thread.start();

        tester.thread.join(10);

        int length = 10;
        pipe.sink().write(ByteBuffer.allocate(length));
        tester.thread.join(10);
        assertEquals(length, tester.buffer.position());

        tester.buffer.clear();
        //Remove
        tester.eventLoop.removeSubscribe(pipe.source());

        //Write again
        pipe.sink().write(ByteBuffer.allocate(length));

        tester.thread.join(10);
        assertEquals(0, tester.buffer.position());
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
        testers[0].thread.join(10 * threadsInGroupt);

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
    public void testShutdown() throws InterruptedException {

        {
            //Before start
            EventTester tester = new EventTester(null);
            TestEvent event = new TestEvent("Event");
            tester.eventLoop.subscribe(event.Id(), tester);
            tester.eventLoop.shutdown();
            tester.thread.start();

            //Wait
            tester.thread.join(10);

            assertFalse(tester.thread.isAlive());
        }
        {
            //After start
            EventTester tester = new EventTester(null);
            TestEvent event = new TestEvent("Event");
            tester.eventLoop.subscribe(event.Id(), tester);
            tester.thread.start();

            //Wait
            tester.thread.join(10);
            assertTrue(tester.thread.isAlive());

            tester.eventLoop.shutdown();
            //Wait
            tester.thread.join(10);

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
    public void testAddTimerWithSockets() {
        final EventLoop eventLoop = new EventLoop();

        eventLoop.subscribe(pipe.source(), new ChannelSubscriber() {
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
