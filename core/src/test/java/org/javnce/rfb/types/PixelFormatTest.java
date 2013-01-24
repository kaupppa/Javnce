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

public class PixelFormatTest {

    private PixelFormat format;
    private int bitsPerPixel;
    private int depth;
    private boolean bigEndian;
    private boolean trueColour;
    private Color max;
    private Color shift;

    class TestData {

        final public PixelFormat format;
        final public boolean same;

        public TestData(PixelFormat format, boolean same) {
            this.format = format;
            this.same = same;
        }
    }

    @Before
    public void setUp() throws Exception {
        bitsPerPixel = 32;
        depth = 24;
        bigEndian = false;
        trueColour = true;
        max = new Color(255, 255, 255);
        shift = new Color(16, 8, 0);
        format = new PixelFormat(bitsPerPixel, depth, bigEndian, trueColour, max, shift);
    }

    @Test
    public void testPixelFormatIntIntBooleanBooleanColorColor() {
        format = new PixelFormat(bitsPerPixel, depth, bigEndian, trueColour, max, shift);
        assertNotNull(format);
    }

    @Test
    public void testBitsPerPixel() {
        format = new PixelFormat(bitsPerPixel, depth, bigEndian, trueColour, max, shift);
        assertEquals(bitsPerPixel, format.bitsPerPixel());
    }

    @Test
    public void testDepth() {
        format = new PixelFormat(bitsPerPixel, depth, bigEndian, trueColour, max, shift);
        assertEquals(depth, format.depth());
    }

    @Test
    public void testBigEndian() {
        format = new PixelFormat(bitsPerPixel, depth, true, trueColour, max, shift);
        assertTrue(format.bigEndian());
    }

    @Test
    public void testTrueColour() {
        format = new PixelFormat(bitsPerPixel, depth, bigEndian, false, max, shift);
        assertFalse(format.trueColour());
    }

    @Test
    public void testMax() {
        format = new PixelFormat(bitsPerPixel, depth, bigEndian, trueColour, max, shift);
        assertEquals(max, format.max());
    }

    @Test
    public void testShift() {
        format = new PixelFormat(bitsPerPixel, depth, bigEndian, trueColour, max, shift);
        assertEquals(shift, format.shift());
    }

    @Test
    public void testHasAlpha() {
        assertTrue(PixelFormat.createARGB888().hasAlpha());
        assertFalse(PixelFormat.createRGB565().hasAlpha());
    }

    @Test
    public void testRedMask() {
        assertEquals(0xFF0000, PixelFormat.createARGB888().redMask());
        assertEquals(0xF800, PixelFormat.createRGB565().redMask());
    }

    @Test
    public void testGreenMask() {
        assertEquals(0xFF00, PixelFormat.createARGB888().greenMask());
        assertEquals(0x7E0, PixelFormat.createRGB565().greenMask());
    }

    @Test
    public void testBlueMask() {
        assertEquals(0xFF, PixelFormat.createARGB888().blueMask());
        assertEquals(0x1F, PixelFormat.createRGB565().blueMask());
    }

    @Test
    public void testAlphaMask() {
        assertEquals(0xFF000000, PixelFormat.createARGB888().alphaMask());
        assertEquals(0, PixelFormat.createRGB565().alphaMask());
        PixelFormat temp = new PixelFormat(16, 15, false, true, new Color(31, 31, 31), new Color(10, 5, 0));
        assertEquals(0x8000, temp.alphaMask());
    }

    @Test
    public void testEqualsObject() {
        TestData[] array = new TestData[]{
            new TestData(format, true),
            new TestData(new PixelFormat(bitsPerPixel, depth, bigEndian, trueColour, shift, max), false),
            new TestData(new PixelFormat(bitsPerPixel, depth, !bigEndian, trueColour, max, shift), false),
            new TestData(new PixelFormat(bitsPerPixel, depth, bigEndian, !trueColour, max, shift), false),
            new TestData(new PixelFormat(bitsPerPixel, depth - 1, bigEndian, !trueColour, max, shift), false),
            new TestData(new PixelFormat(bitsPerPixel + 1, depth, bigEndian, !trueColour, max, shift), false),
            new TestData(new PixelFormat(bitsPerPixel, depth, bigEndian, trueColour, max, shift), true),
            new TestData(new PixelFormat(0, 0, false, false, shift, shift), false)
        };

        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i].same, format.equals(array[i].format));
            assertEquals(array[i].same, array[i].format.equals(format));
        }

        assertFalse(format.equals(null));
        assertFalse(format.equals(new Version(1, 1)));
    }

    @Test
    public void testCreateRGB565() {
        bitsPerPixel = 16;
        depth = 16;
        bigEndian = false;
        trueColour = true;
        max = new Color(31, 63, 31);
        shift = new Color(11, 5, 0);

        format = PixelFormat.createRGB565();

        assertNotNull(format);
        assertEquals(bitsPerPixel, format.bitsPerPixel());
        assertEquals(depth, format.depth());
        assertEquals(bigEndian, format.bigEndian());
        assertEquals(trueColour, format.trueColour());
        assertTrue(max.equals(format.max()));
        assertTrue(shift.equals(format.shift()));
    }

    @Test
    public void testCreateARGB888() {
        bitsPerPixel = 32;
        depth = 24;
        bigEndian = false;
        trueColour = true;
        max = new Color(255, 255, 255);
        shift = new Color(16, 8, 0);

        format = PixelFormat.createARGB888();

        assertNotNull(format);
        assertEquals(bitsPerPixel, format.bitsPerPixel());
        assertEquals(depth, format.depth());
        assertEquals(bigEndian, format.bigEndian());
        assertEquals(trueColour, format.trueColour());
        assertTrue(max.equals(format.max()));
        assertTrue(shift.equals(format.shift()));

    }

    @Test
    public void testToString() {
        format = PixelFormat.createARGB888();
        String text = format.toString();
        assertNotNull(text);
    }
}
