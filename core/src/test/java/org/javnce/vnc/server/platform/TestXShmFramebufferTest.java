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
package org.javnce.vnc.server.platform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import org.junit.Test;

public class TestXShmFramebufferTest {

    final private static boolean isLinux = System.getProperty("os.name").startsWith("Linux");
    private XShmFramebuffer dev;

    @Test
    public void testIsSupported() {
        assertEquals(isLinux, XShmFramebuffer.isSupported());
    }

    @Test
    public void testSize() {
        assumeTrue(isLinux);
        dev = new XShmFramebuffer();
        assertTrue(0 != dev.size().width());
        assertTrue(0 != dev.size().height());
    }

    @Test
    public void testFormat() {
        assumeTrue(isLinux);
        dev = new XShmFramebuffer();

        assertTrue(dev.format().trueColour());
        assertTrue(!dev.format().bigEndian());
        assertTrue(dev.format().bitsPerPixel() >= dev.format().depth());
    }

    class TestData {

        int x;
        int y;
        int width;
        int height;

        TestData(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    @Test
    public void testBuffer() {
        assumeTrue(isLinux);
        dev = new XShmFramebuffer();
        PixelFormat format = dev.format();
        Size size = dev.size();

        TestData[] array = new TestData[]{
            new TestData(0, 0, size.width(), 1), //One full line
            new TestData(0, 0, size.width(), size.height()), //Whole framebuffer
            new TestData(0, 100, size.width(), 100), //Several full lines
            new TestData(0, 0, size.width() - 10, 1),
            new TestData(0, 0, size.width() - 10, size.height()),
            new TestData(10, 0, size.width() - 20, size.height()),};

        for (int i = 0; i < array.length; i++) {
            ByteBuffer[] buffers = dev.buffer(array[i].x, array[i].y, array[i].width, array[i].height);

            if (array[i].x == 0 && array[i].width == size.width()) {
                assertEquals(1, buffers.length);
            } else {
                assertEquals(array[i].height, buffers.length);
            }

            int length = 0;

            for (ByteBuffer b : buffers) {
                length += b.capacity();
                assertEquals(ByteOrder.BIG_ENDIAN, b.order());
            }

            assertEquals(array[i].width * array[i].height * format.bytesPerPixel(), length);
        }
    }

    @Test
    public void testGrabScreen() {
        assumeTrue(isLinux);
        dev = new XShmFramebuffer();
        dev.grabScreen();
    }
}
