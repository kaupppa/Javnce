/* * Copyright (C) 2013  Pauli Kauppinen
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
package org.javnce.vnc.common;

import static org.junit.Assert.*;
import org.junit.Test;

public class KeyEventTest {

    private static final boolean down = true;
    private static final long key = 0x1234l;

    @Test
    public void testId() {
        assertEquals(KeyEvent.eventId(), new KeyEvent(down, key).Id());
    }

    @Test
    public void testGet() {
        KeyEvent event = new KeyEvent(down, key);
        assertEquals(down, event.down());
        assertEquals(key, event.key());
    }
}