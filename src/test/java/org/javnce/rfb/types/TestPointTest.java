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

public class TestPointTest {

    private Point point;
    private int x = 123;
    private int y = 456;

    class TestData {

        final public Point point;
        final public boolean same;

        public TestData(Point point, boolean same) {
            this.point = point;
            this.same = same;
        }
    }

    @Before
    public void setUp() {
        x = 123;
        y = 456;
        point = new Point(x, y);
    }

    @Test
    public void testPointIntInt() {
        assertNotNull(point);
        assertEquals(x, point.x());
        assertEquals(y, point.y());

    }

    @Test
    public void testEqualsObject() {
        assertFalse(point.equals(null));
        assertFalse(point.equals(new Version(1, 1)));

        TestData[] array = new TestData[]{
            new TestData(point, true),
            new TestData(new Point(y, x), false),
            new TestData(new Point(x, y), true),
            new TestData(new Point(0, 0), false)
        };

        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i].same, point.equals(array[i].point));
            assertEquals(array[i].same, array[i].point.equals(point));
        }

    }
}
