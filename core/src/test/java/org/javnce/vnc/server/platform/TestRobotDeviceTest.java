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
import org.junit.Test;

public class TestRobotDeviceTest {

    private RobotDevice dev;

    @Test
    public void testIsSupported() {
        assertTrue(RobotDevice.isSupported());
    }

    @Test
    public void testRobotDevice() {
        dev = RobotDevice.instance();

        assertNotNull(dev);
    }

    @Test
    public void testSize() {
        dev = RobotDevice.instance();

        assertTrue(0 != dev.size().width());
        assertTrue(0 != dev.size().height());

    }

    @Test
    public void testFormat() {
        dev = RobotDevice.instance();

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
        dev = RobotDevice.instance();

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

            assertEquals(1, buffers.length);

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
        dev = RobotDevice.instance();
        dev.grabScreen();
    }

}
