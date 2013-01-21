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

public class TestRectTest {

    private Rect rect;
    private Size size;
    private Point point;

    class TestData {

        final public Rect rect;
        final public boolean same;

        public TestData(Rect rect, boolean same) {
            this.rect = rect;
            this.same = same;
        }
    }

    @Before
    public void setUp() {
        size = new Size(1234, 5678);
        point = new Point(4321, 8765);
        rect = new Rect(point, size);
    }

    @Test
    public void testRectPointSize() {
        assertNotNull(rect);
        assertEquals(point, rect.point());
        assertEquals(size, rect.size());

    }

    @Test
    public void testRectRect() {
        Rect rect2 = new Rect(rect);
        assertNotNull(rect2);
        assertEquals(point, rect2.point());
        assertEquals(size, rect2.size());
    }

    @Test
    public void testEqualsObject() {
        assertFalse(rect.equals(null));
        assertFalse(rect.equals(new Version(1, 1)));

        TestData[] array = new TestData[]{
            new TestData(rect, true),
            new TestData(new Rect(rect), true),
            new TestData(new Rect(point, size), true),
            new TestData(new Rect(point, new Size(2, 4)), false),
            new TestData(new Rect(new Point(1, 2), size), false),
            new TestData(new Rect(new Point(1, 2), new Size(2, 4)), false),};

        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i].same, rect.equals(array[i].rect));
            assertEquals(array[i].same, array[i].rect.equals(rect));
        }
    }

    @Test
    public void testOverlaps() {

        Rect area51 = new Rect(100, 100, 100, 100);

        TestData[] array = new TestData[]{
            new TestData(new Rect(0, 100, 100, 100), false),
            new TestData(new Rect(100, 0, 100, 100), false),
            new TestData(new Rect(200, 100, 100, 100), false),
            new TestData(new Rect(100, 200, 100, 100), false),
            new TestData(new Rect(0, 100, 101, 100), true),
            new TestData(new Rect(100, 0, 100, 101), true),
            new TestData(new Rect(199, 100, 100, 100), true),
            new TestData(new Rect(100, 199, 100, 100), true),};

        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i].same, area51.overlaps(array[i].rect));
        }
    }

    @Test
    public void testOverlappnig() {

        Rect area51 = new Rect(100, 100, 100, 100);

        Rect test = area51.overlapping(new Rect(0, 100, 101, 100));
        assertEquals(test, new Rect(100, 100, 1, 100));

        test = area51.overlapping(new Rect(100, 0, 100, 101));
        assertEquals(test, new Rect(100, 100, 100, 1));

        test = area51.overlapping(new Rect(199, 100, 100, 100));
        assertEquals(test, new Rect(199, 100, 1, 100));

        test = area51.overlapping(new Rect(100, 199, 100, 100));
        assertEquals(test, new Rect(100, 199, 100, 1));

        test = area51.overlapping(new Rect(100, 200, 100, 100));
        assertEquals(test, null);
    }

    @Test
    public void testConnecting() {

        Rect area51 = new Rect(100, 100, 100, 100);

        TestData[] array = new TestData[]{
            new TestData(new Rect(0, 100, 100, 100), true),
            new TestData(new Rect(100, 0, 100, 100), true),
            new TestData(new Rect(200, 100, 100, 100), true),
            new TestData(new Rect(100, 200, 100, 100), true),
            new TestData(new Rect(0, 100, 99, 100), false),
            new TestData(new Rect(99, 100, 1, 100), true),
            new TestData(new Rect(100, 0, 100, 99), false),
            new TestData(new Rect(100, 99, 100, 1), true),
            new TestData(new Rect(201, 100, 100, 100), false),
            new TestData(new Rect(100, 201, 100, 100), false),};

        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i].same, area51.connecting(array[i].rect));
        }
    }

    @Test
    public void testBounding() {

        Rect area51 = new Rect(100, 100, 100, 100);

        Rect test = area51.bounding(new Rect(0, 100, 100, 100));
        assertEquals(test, new Rect(0, 100, 200, 100));

        test = area51.bounding(new Rect(100, 0, 100, 100));
        assertEquals(test, new Rect(100, 0, 100, 200));

        test = area51.bounding(new Rect(200, 100, 100, 100));
        assertEquals(test, new Rect(100, 100, 200, 100));

        test = area51.bounding(new Rect(100, 200, 100, 100));
        assertEquals(test, new Rect(100, 100, 100, 200));

    }
}
