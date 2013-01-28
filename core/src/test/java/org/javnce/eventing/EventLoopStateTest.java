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

    @Test
    public void testInitialValues() {
        EventLoopState state = new EventLoopState();
        assertNotNull(state);
        assertTrue(state.isRunnable());
        assertFalse(state.isAttached());
    }

    @Test
    public void testStateHandling() {
        final EventLoopState state = new EventLoopState();

        assertTrue(state.isRunnable());
        state.shutdown();
        assertFalse(state.isRunnable());
    }

    @Test
    public void testAttachHandling() {
        final EventLoopState state = new EventLoopState();

        assertFalse(state.isAttached());

        state.attachCurrentThread();
        assertTrue(state.isAttached());

        state.detachCurrentThread();
        assertFalse(state.isAttached());
    }
}
