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
package org.javnce.vnc.server;

import java.util.ArrayList;
import java.util.Arrays;
import org.javnce.rfb.types.Rect;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestRectContainerTest {

    @Test
    public void testClear() {

        ArrayList<Rect> list = new ArrayList<>();
        Rect[] array = new Rect[]{
            new Rect(0, 0, 10, 10),
            new Rect(0, 100, 10, 10)
        };

        for (Rect rect : array) {
            list.add(rect);
        }

        RectContainer container = new RectContainer();
        container.add(list);

        container.clear();
        Rect[] rects = container.get();

        assertEquals(0, rects.length);

    }

    @Test
    public void testGet() {
        ArrayList<Rect> list = new ArrayList<>();
        Rect[] array = new Rect[]{
            new Rect(0, 0, 10, 10),
            new Rect(0, 100, 10, 10)
        };
        list.addAll(Arrays.asList(array));

        RectContainer container = new RectContainer();
        container.add(list);

        Rect[] rects = container.get();
        assertEquals(2, rects.length);
        assertArrayEquals(array, rects);
    }

    @Test
    public void testAddOneArea() {
        ArrayList<Rect> list = new ArrayList<>();
        Rect[] array = new Rect[]{
            new Rect(100, 100, 100, 100),
            new Rect(0, 100, 100, 100),
            new Rect(100, 0, 100, 100),
            new Rect(200, 100, 100, 100),
            new Rect(100, 200, 100, 100),};
        list.addAll(Arrays.asList(array));

        RectContainer container = new RectContainer();
        container.add(list);

        Rect[] rects = container.get();
        assertEquals(1, rects.length);
        assertEquals(new Rect(0, 0, 300, 300), rects[0]);
    }

    @Test
    public void testAddSeveralAreas() {
        ArrayList<Rect> list = new ArrayList<>();
        Rect[] array = new Rect[]{
            new Rect(100, 100, 10, 10),
            new Rect(0, 0, 10, 10),
            new Rect(200, 200, 10, 10),};
        list.addAll(Arrays.asList(array));

        RectContainer container = new RectContainer();
        container.add(list);

        Rect[] rects = container.get();
        assertArrayEquals(array, rects);
    }
}
