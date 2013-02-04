/*
 * Copyright (C) 2013 Pauli Kauppinen
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
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.javnce.vnc.server.platform;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Pauli Kauppinen
 */
public class FrameBufferCompareTest {

    static class MyDev extends FramebufferDevice {

        final Size size;
        final Rect rect;
        final PixelFormat format;
        final byte[] buffer;

        MyDev() {
            size = new Size(1440, 900);
            rect = new Rect(new Point(0, 0), size);
            format = PixelFormat.createARGB888();
            buffer = new byte[size.width() * size.height() * format.bytesPerPixel()];

            byte fillByte = (byte) 0xa5;
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = fillByte;
            }
        }

        void change(Rect area) {
            IntBuffer temp = ByteBuffer.wrap(buffer).asIntBuffer();
            final int xLimit = area.x() + area.width();
            final int yLimit = area.y() + area.height();

            for (int y = area.y(); y < yLimit && y < rect.height(); y++) {
                int offset = y * rect.width();
                for (int x = area.x(); x < xLimit && x < rect.width(); x++) {
                    int org = temp.get(offset + x);
                    temp.put(offset + x, ~org);
                }
            }
        }

        @Override
        public Size size() {
            return size;
        }

        @Override
        public PixelFormat format() {
            return format;
        }

        @Override
        public ByteBuffer[] buffer(int x, int y, int width, int height) {
            return new ByteBuffer[]{ByteBuffer.wrap(buffer)};
        }

        @Override
        public void grabScreen() {
        }
    }

    @Test
    public void testCompareFirst() {
        FrameBufferCompare compare = new FrameBufferCompare();
        MyDev dev = new MyDev();
        ArrayList<Rect> list = compare.compare(dev);

        assertEquals(1, list.size());
        assertEquals(dev.rect, list.get(0));
    }

    @Test
    public void testCompareOnePixelChange() {
        FrameBufferCompare compare = new FrameBufferCompare();
        MyDev dev = new MyDev();
        //Get first
        ArrayList<Rect> list = compare.compare(dev);


        Rect[] areas = new Rect[]{new Rect(0, 0, 1, 1),
            new Rect(dev.size.width() - 1, 0, 1, 1),
            new Rect(0, dev.size.height() - 1, 1, 1),
            new Rect(dev.size.height() - 1, dev.size.height() - 1, 1, 1),
            new Rect(200, 200, 1, 1)
        };

        for (int i = 0; i < areas.length; i++) {
            dev.change(areas[i]);
            list = compare.compare(dev);
            assertEquals(1, list.size());
            assertTrue(list.get(0).contains(areas[i]));

        }
    }

    @Test
    public void testCompareSeveralChange() {
        FrameBufferCompare compare = new FrameBufferCompare();
        MyDev dev = new MyDev();
        //Get first
        ArrayList<Rect> list = compare.compare(dev);


        Rect[] areas = new Rect[]{new Rect(0, 0, 10, 10),
            new Rect(dev.size.width() - 11, 0, 10, 10),
            new Rect(0, dev.size.height() - 11, 10, 10),
            new Rect(dev.size.height() - 11, dev.size.height() - 11, 10, 10),
            new Rect(200, 200, 10, 10)
        };

        //Modify several areas
        for (int i = 0; i < areas.length; i++) {
            dev.change(areas[i]);
        }
        list = compare.compare(dev);

        //Check that all areas are in list
        for (int i = 0; i < areas.length; i++) {
            boolean contains = false;
            for (Rect rect : list) {
                if (rect.contains(areas[i])) {
                    contains = true;
                    break;
                }
            }
            assertTrue(contains);

        }
    }
}
