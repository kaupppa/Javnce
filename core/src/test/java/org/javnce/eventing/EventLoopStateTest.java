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

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EventLoopStateTest {

    @Before
    public void setUp() throws Exception {
        //Clear interrupted
        Thread.interrupted();
    }

    @After
    public void tearDown() throws Exception {
        //Clear interrupted
        Thread.interrupted();
    }

    @Test
    public void testEventLoopState() {
        EventLoopState state = new EventLoopState();
        assertNotNull(state);
        assertTrue(state.isRunnable());
        assertFalse(state.isAttached());
    }

    @Test
    public void testAttachAndDetach() throws InterruptedException {
        final EventLoopState state = new EventLoopState();

        assertFalse(state.isAttached());


        state.attachCurrentThread();
        assertTrue(state.isAttached());

        state.detachCurrentThread();
        assertFalse(state.isAttached());

    }

    @Test
    public void testIsRunnable() throws InterruptedException {
        final EventLoopState state = new EventLoopState();

        //Clear interrupted
        Thread.interrupted();

        assertTrue(state.isRunnable());

        state.attachCurrentThread();

        assertTrue(state.isRunnable());
        assertTrue(state.isAttached());

        Thread.currentThread().interrupt();

        assertTrue(state.isAttached());
        assertFalse(state.isRunnable());

    }

    @Test
    public void testShutdown() {
        final EventLoopState state = new EventLoopState();

        assertTrue(state.isRunnable());
        state.shutdown();
        assertFalse(state.isRunnable());
    }

    @Test
    public void testIsAttached() {
        final EventLoopState state = new EventLoopState();

        assertFalse(state.isAttached());

        state.attachCurrentThread();
        assertTrue(state.isAttached());

        state.detachCurrentThread();
        assertFalse(state.isAttached());
    }
}
