package org.javnce.eventing;

import java.lang.ref.WeakReference;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EventGroupTest {

    class TestDispatcher implements EventDispatcher {

        boolean dispatch, result, shutdown;

        TestDispatcher() {
            dispatch = false;
            result = true;
            shutdown = false;
        }

        @Override
        public boolean dispatchEvent(Event event) {
            dispatch = true;
            return result;
        }

        @Override
        public void shutdown(EventGroup group) {
            shutdown = true;
            if (null != group) {
                group.remove(this);
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        EventGroup.instance().shutdown();
    }

    @After
    public void tearDown() throws Exception {
        EventGroup.instance().shutdown();
    }

    @Test
    public void testInstance() {
        EventGroup root = EventGroup.instance();
        assertNotNull(root);
        assertTrue(root.isEmpty());
    }

    @Test
    public void testAdd() {
        EventGroup group = EventGroup.instance();
        assertEquals(0, group.refsList().size());

        //Add null
        group.add(null);
        assertEquals(0, group.refsList().size());

        TestDispatcher dispatcher = new TestDispatcher();

        //Add dispatcher
        group.add(dispatcher);
        assertEquals(1, group.refsList().size());

        //Add duplicate
        group.add(dispatcher);
        assertEquals(1, group.refsList().size());
    }

    @Test
    public void testRemove() {
        EventGroup group = EventGroup.instance();
        assertEquals(0, group.refsList().size());

        TestDispatcher dispatchers[] = new TestDispatcher[]{new TestDispatcher(), new TestDispatcher(), new TestDispatcher(), new TestDispatcher()};

        for (int i = 0; i < dispatchers.length; i++) {
            //Add dispatcher
            group.add(dispatchers[i]);
        }
        assertEquals(dispatchers.length, group.refsList().size());

        group.remove((TestDispatcher) null);

        for (int i = 0; i < dispatchers.length; i++) {
            //Add dispatcher
            group.remove(dispatchers[i]);
            assertEquals(dispatchers.length - 1 - i, group.refsList().size());
        }
        assertEquals(0, group.refsList().size());
    }

    @Test
    public void testMoveToNewChild() {
        EventGroup group = EventGroup.instance();
        assertEquals(0, group.refsList().size());

        TestDispatcher dispatchers[] = new TestDispatcher[]{new TestDispatcher(), new TestDispatcher(), new TestDispatcher(), new TestDispatcher()};

        //Add dispatchers
        for (int i = 0; i < dispatchers.length; i++) {

            group.add(dispatchers[i]);
        }
        assertEquals(dispatchers.length, group.refsList().size());


        assertNull(group.moveToNewChild((TestDispatcher) null));

        //Move to own groups
        for (int i = 0; i < dispatchers.length; i++) {
            EventGroup child = group.moveToNewChild(dispatchers[i]);
            assertNotNull(child);

            assertTrue(group != child);
            assertEquals(1, child.refsList().size());

            assertEquals(dispatchers.length - 1 - i, group.refsList().size());
        }
        assertEquals(0, group.refsList().size());
    }

    @Test
    public void testShutdown() {
        EventGroup group = EventGroup.instance();

        TestDispatcher dispatchersA[] = new TestDispatcher[]{new TestDispatcher(), new TestDispatcher(), new TestDispatcher(), new TestDispatcher()};
        TestDispatcher dispatchersB[] = new TestDispatcher[]{new TestDispatcher(), new TestDispatcher(), new TestDispatcher(), new TestDispatcher()};
        TestDispatcher dispatchersC[] = new TestDispatcher[]{new TestDispatcher(), new TestDispatcher(), new TestDispatcher(), new TestDispatcher()};

        //A is in root
        for (int i = 0; i < dispatchersA.length; i++) {
            group.add(dispatchersA[i]);
        }

        //B=child 1
        group.add(dispatchersB[0]);
        EventGroup child1 = group.moveToNewChild(dispatchersB[0]);
        for (int i = 1; i < dispatchersB.length; i++) {
            child1.add(dispatchersB[i]);
        }

        //C=child 2
        group.add(dispatchersC[0]);
        EventGroup child2 = group.moveToNewChild(dispatchersC[0]);
        for (int i = 1; i < dispatchersC.length; i++) {
            child2.add(dispatchersC[i]);
        }

        //shutdown child 2
        child2.shutdown();

        for (int i = 0; i < dispatchersA.length; i++) {
            assertEquals(false, dispatchersA[i].shutdown);
        }
        for (int i = 0; i < dispatchersB.length; i++) {
            assertEquals(false, dispatchersB[i].shutdown);

        }
        for (int i = 0; i < dispatchersC.length; i++) {
            assertEquals(true, dispatchersC[i].shutdown);
            dispatchersC[i].shutdown = false;
        }

        //shutdown root and all it's children
        group.shutdown();

        for (int i = 0; i < dispatchersA.length; i++) {
            assertEquals(true, dispatchersA[i].shutdown);
        }
        for (int i = 0; i < dispatchersB.length; i++) {
            assertEquals(true, dispatchersB[i].shutdown);

        }
        //Already shutdown -> not called
        for (int i = 0; i < dispatchersC.length; i++) {
            assertEquals(false, dispatchersC[i].shutdown);
        }
    }

    @Test
    public void testPublish() {
        EventGroup group = EventGroup.instance();

        TestDispatcher dispatchersA[] = new TestDispatcher[]{new TestDispatcher(), new TestDispatcher(), new TestDispatcher(), new TestDispatcher()};
        TestDispatcher dispatchersB[] = new TestDispatcher[]{new TestDispatcher(), new TestDispatcher(), new TestDispatcher(), new TestDispatcher()};
        TestDispatcher dispatchersC[] = new TestDispatcher[]{new TestDispatcher(), new TestDispatcher(), new TestDispatcher(), new TestDispatcher()};

        //Add dispatchers A=root
        for (int i = 0; i < dispatchersA.length; i++) {
            group.add(dispatchersA[i]);
            dispatchersA[i].result = false;
            dispatchersA[i].dispatch = false;
        }

        //Add dispatchers B=child 1 and has result true
        group.add(dispatchersB[0]);
        dispatchersB[0].result = true;
        dispatchersB[0].dispatch = false;
        EventGroup child1 = group.moveToNewChild(dispatchersB[0]);
        for (int i = 1; i < dispatchersB.length; i++) {
            child1.add(dispatchersB[i]);
            dispatchersB[i].result = true;
            dispatchersB[i].dispatch = false;
        }

        //Add dispatchers C=child 2
        group.add(dispatchersC[0]);
        dispatchersC[0].result = false;
        dispatchersC[0].dispatch = false;
        EventGroup child2 = group.moveToNewChild(dispatchersC[0]);
        for (int i = 1; i < dispatchersC.length; i++) {
            child2.add(dispatchersC[i]);
            dispatchersC[i].result = false;
            dispatchersC[i].dispatch = false;
        }

        //Test publish within child1 group
        child1.publish(new TestEvent("Jeps"));

        for (int i = 0; i < dispatchersA.length; i++) {
            assertEquals(false, dispatchersA[i].dispatch);
        }
        for (int i = 0; i < dispatchersB.length; i++) {
            assertEquals(true, dispatchersB[i].dispatch);
            dispatchersB[i].dispatch = false;
        }
        for (int i = 0; i < dispatchersC.length; i++) {
            assertEquals(false, dispatchersC[i].dispatch);
        }

        //Test publish to other group (note child2 has result = false)
        child2.publish(new TestEvent("Jeps"));

        for (int i = 0; i < dispatchersA.length; i++) {
            assertEquals(true, dispatchersA[i].dispatch);
            dispatchersA[i].dispatch = false;
        }
        for (int i = 0; i < dispatchersB.length; i++) {
            assertEquals(true, dispatchersB[i].dispatch);
            dispatchersB[i].dispatch = false;
        }
        for (int i = 0; i < dispatchersC.length; i++) {
            assertEquals(true, dispatchersC[i].dispatch);
            dispatchersC[i].dispatch = false;
        }
    }

    @Test
    public void testWeakReference() {
        EventGroup group = EventGroup.instance();
        assertEquals(0, group.refsList().size());

        TestDispatcher dispatcher = new TestDispatcher();

        WeakReference<TestDispatcher> ref = new WeakReference<>(dispatcher);
        assertNotNull(ref.get());

        dispatcher = null;
        System.gc();

        //Lets check that dispatcher is cleaned
        assertNull(ref.get());

        //Cause refresh
        group.shutdown();

        assertTrue(group.isEmpty());
    }
}
