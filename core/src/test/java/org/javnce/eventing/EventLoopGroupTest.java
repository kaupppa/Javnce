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
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EventLoopGroupTest {

    final static int SleepTimeTime = 50;
    private EventLoopGroup root;

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
    public void testInstance() {
        assertTrue(root == EventLoopGroup.instance());
        assertTrue(root.isEmpty());
    }

    @Test
    public void testAdd() {
        assertTrue(root.isEmpty());

        //This cause add
        EventLoop eventLoop = new EventLoop();

        assertFalse(root.isEmpty());
        assertTrue(root == eventLoop.getGroup());
    }

    @Test
    public void testRemove() {
        assertTrue(root.isEmpty());

        EventLoop eventLoop = new EventLoop();

        assertFalse(root.isEmpty());
        assertTrue(root == eventLoop.getGroup());

        //This cause remove
        eventLoop.shutdown();

        assertTrue(root.isEmpty());
        assertNull(eventLoop.getGroup());
    }

    @Test
    public void testMoveToNewChild() {
        assertTrue(root.isEmpty());

        EventLoop eventLoop = new EventLoop();
        assertFalse(root.isEmpty());
        assertTrue(root == eventLoop.getGroup());

        eventLoop.moveToNewChildGroup();

        EventLoopGroup child = eventLoop.getGroup();

        assertTrue(root != child);
        assertFalse(child.isEmpty());
        assertFalse(root.isEmpty());
    }

    @Test
    public void testPublish() throws Throwable {
        //Lets create n sub groups
        //Each thread handles group specific event and a generic event

        int subGroupCount = 5;

        EventTester testers[] = new EventTester[subGroupCount];

        TestEvent allEvent = new TestEvent("ANY");

        for (int i = 0; i < testers.length; i++) {

            testers[i] = new EventTester(null);
            testers[i].eventLoop.moveToNewChildGroup();
            testers[i].handleEvent(allEvent.Id());

            TestEvent groupEvent = new TestEvent("Group" + i);
            testers[i].handleEvent(groupEvent.Id());

            testers[i].startAll();
        }


        //Publish three events to first sub group
        testers[0].eventLoop.getGroup().publish(allEvent);
        testers[0].eventLoop.getGroup().publish(new TestEvent("Group" + 0));
        testers[0].eventLoop.getGroup().publish(new TestEvent("Group" + 1)); //handled by threads[1]

        //Lets wait threads to handle the events
        Thread.sleep(SleepTimeTime);
        assertEquals(2, testers[0].events.size());
        assertEquals(1, testers[1].events.size());

        for (int i = 2; i < testers.length; i++) {
            assertEquals(0, testers[i].events.size());
        }

        //Publish generic event to root (no handler)
        root.publish(allEvent);
        //Lets wait threads to handle the events
        Thread.sleep(SleepTimeTime);
        assertEquals(3, testers[0].events.size());
        assertEquals(2, testers[1].events.size());

        for (int i = 2; i < testers.length; i++) {
            assertEquals(1, testers[i].events.size());
        }
    }

    @Test
    public void testShutdown() throws Throwable {

        EventLoopGroup.shutdown(null);

        //Lets create n sub groups each sub group having m threads
        //root is empty
        assertTrue(root.isEmpty());
        int subGroupCount = 5;
        int threadsInGroupt = 5;

        EventTester testers[] = new EventTester[subGroupCount];
        TestEvent event = new TestEvent("Event");

        for (int i = 0; i < testers.length; i++) {
            testers[i] = new EventTester(null);
            testers[i].eventLoop.moveToNewChildGroup();
            testers[i].createValidGroup(threadsInGroupt - 1);
            testers[i].handleEvent(event.Id());
            testers[i].startAll();

            //Lets add thread to root
            EventTester temp = new EventTester(null);
            temp.handleEvent(event.Id());
            temp.startAll();
        }

        //Lets exit each sub group
        for (int i = 0; i < testers.length; i++) {
            assertEquals(testers.length - i, root.getChildren().size());

            EventLoopGroup child = testers[i].eventLoop.getGroup();
            assertEquals(threadsInGroupt, child.getLoops().size());

            EventLoopGroup.shutdown(child);
            assertEquals(0, child.getLoops().size());
            assertEquals(testers.length - i - 1, root.getChildren().size());
        }

        List<EventLoopGroup> children = root.getChildren();
        assertEquals(0, children.size());
        assertEquals(testers.length, root.getLoops().size());
    }

    @Test
    public void testParent() {
        assertTrue(root.isEmpty());

        EventLoop eventLoop = new EventLoop();
        eventLoop.moveToNewChildGroup();

        EventLoopGroup child = eventLoop.getGroup();

        assertTrue(null == root.parent());
        assertTrue(root == child.parent());
    }

    @Test
    public void testWeakReference() {
        assertTrue(root.isEmpty());

        EventLoop eventLoop = new EventLoop();
        assertFalse(root.isEmpty());

        WeakReference<EventLoop> ref = new WeakReference<>(eventLoop);
        assertNotNull(ref.get());

        eventLoop = null;
        System.gc();

        //Check that eventLoop is cleaned
        assertNull(ref.get());

        //Check that root is empty
        assertTrue(root.isEmpty());
    }
}
