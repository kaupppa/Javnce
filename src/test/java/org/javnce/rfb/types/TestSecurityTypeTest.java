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
package org.javnce.rfb.types;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestSecurityTypeTest {

    @Test
    public void testId() {

        assertEquals(-1, SecurityType.UnSupported.id());
        assertEquals(0, SecurityType.Invalid.id());
        assertEquals(1, SecurityType.None.id());
    }

    @Test
    public void testCreate() {
        assertEquals(SecurityType.Invalid, SecurityType.create((byte) 0));
        assertEquals(SecurityType.None, SecurityType.create((byte) 1));

        for (byte i = 2; i < 30; i++) {
            assertEquals(SecurityType.UnSupported, SecurityType.create(i));
        }
    }
}
