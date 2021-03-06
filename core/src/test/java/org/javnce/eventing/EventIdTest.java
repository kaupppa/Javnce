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

public class EventIdTest {

    @Test
    public void testHashCode() {
        String text = this.getClass().getName();
        EventId id = new EventId(text);
        assertEquals(text.hashCode(), id.hashCode());
    }

    @Test
    public void testEqualsObject() {
        String text = this.getClass().getName();
        EventId id = new EventId(text);

        assertFalse(id.equals(null));
        assertFalse(id.equals(text));
        assertTrue(id.equals(new EventId(text)));
        assertFalse(id.equals(new EventId("Some other event id")));
        assertTrue(id.equals(id));
    }
}
