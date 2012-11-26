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

public class TestColorTest {

    private Color color;
    private int red;
    private int green;
    private int blue;

    class TestData {

        final public Color color;
        final public boolean same;

        public TestData(Color color, boolean same) {
            this.color = color;
            this.same = same;
        }
    }

    @Before
    public void setUp() {
        red = 123;
        green = 456;
        blue = 789;
        color = new Color(red, green, blue);
    }

    @Test
    public void testColorIntIntInt() {

        assertNotNull(color);
        assertEquals(red, color.red());
        assertEquals(green, color.green());
        assertEquals(blue, color.blue());
    }

    @Test
    public void testRed() {
        red = 666;
        color = new Color(red, green, blue);
        assertEquals(red, color.red());
    }

    @Test
    public void testGreen() {
        green = 666;
        color = new Color(red, green, blue);
        assertEquals(green, color.green());
    }

    @Test
    public void testBlue() {
        blue = 666;
        color = new Color(red, green, blue);
        assertEquals(blue, color.blue());
    }

    @Test
    public void testEquals() {
        TestData[] array = new TestData[]{
            new TestData(color, true),
            new TestData(new Color(red, blue, green), false),
            new TestData(new Color(blue, green, red), false),
            new TestData(new Color(blue, red, green), false),
            new TestData(new Color(red, green, blue), true),
            new TestData(new Color(0, 0, 0), false)
        };

        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i].same, color.equals(array[i].color));
            assertEquals(array[i].same, array[i].color.equals(color));
        }

        assertFalse(color.equals(null));
        assertFalse(color.equals(new Version(1, 1)));
    }
}
