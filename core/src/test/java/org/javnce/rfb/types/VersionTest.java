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
import org.junit.Before;
import org.junit.Test;

public class VersionTest {

    private Version version;

    @Before
    public void setUp() {
        version = new Version(3, 8);
    }

    @Test
    public void testVersionIntInt() {
        assertNotNull(version);
    }

    @Test
    public void testMajor() {
        assertEquals(3, version.major());
    }

    @Test
    public void testMinor() {
        assertEquals(8, version.minor());
    }

    @Test
    public void testEqualsObject() {
        Version version1 = version;
        Version version2 = new Version(3, 8);
        Version version3 = new Version(4, 8);
        Version version4 = new Version(3, 7);

        assertTrue(version.equals(version1));
        assertTrue(version.equals(version2));

        assertFalse(version.equals(version3));
        assertFalse(version.equals(version4));

        Object object = new Object();
        assertFalse(version.equals(object));
    }

    @Test
    public void testToString() {
        version = new Version(3, 8);
        String text = version.toString();
        assertNotNull(text);
    }
}
