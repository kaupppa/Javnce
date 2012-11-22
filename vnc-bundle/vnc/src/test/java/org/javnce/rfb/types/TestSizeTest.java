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

public class TestSizeTest {

    private Size size;
    private int width = 123;
    private int height = 456;

    class TestData {

        final public Size size;
        final public boolean same;

        public TestData(Size size, boolean same) {
            this.size = size;
            this.same = same;
        }
    }

    @Before
    public void setUp() {
        width = 123;
        height = 456;
        size = new Size(width, height);
    }

    @Test
    public void testSizeIntInt() {
        assertNotNull(size);
        assertEquals(width, size.width());
        assertEquals(height, size.height());
    }

    @Test
    public void testEquals() {
        TestData[] array = new TestData[]{
            new TestData(size, true),
            new TestData(new Size(height, width), false),
            new TestData(new Size(width, height), true),
            new TestData(new Size(0, 0), false)
        };

        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i].same, size.equals(array[i].size));
            assertEquals(array[i].same, array[i].size.equals(size));
        }

        assertFalse(size.equals(null));
        assertFalse(size.equals(new Version(1, 1)));
    }
}
