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

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * The RAW to Run-length Encoding converting class.
 */
public class RunLengthEncoder {

    /**
     * The Max Runs.
     */
    final static public int MaxRunLength = 256;

    /**
     * Run-length Encoder method.
     *
     * @param buffer the RAW framebuffer
     * @param bytePerPixel the byte per pixel
     * @return the list of run-length encoded buffers
     */
    static public List<ByteBuffer> encode(ByteBuffer buffer, int bytePerPixel) {
        RlePixelBuffer rleBuffer = new RlePixelBuffer(bytePerPixel);

        switch (bytePerPixel) {
            case 1:
                encodeByte(rleBuffer, buffer, bytePerPixel);
                break;
            case 2:
                encodeShort(rleBuffer, buffer, bytePerPixel);
                break;
            case 4:
                encodeInt(rleBuffer, buffer, bytePerPixel);
                break;
            default:
                encodeAnySize(rleBuffer, buffer, bytePerPixel);
                break;
        }

        return rleBuffer.getBuffers();
    }

    /**
     * Run-length Encoder method for 8-bit pixels.
     *
     * @param rleBuffer the rle buffer
     * @param buffer the buffer
     * @param bytePerPixel the byte per pixel
     */
    static private void encodeByte(RlePixelBuffer rleBuffer, ByteBuffer buffer, int bytePerPixel) {
        byte pixel = 0;
        int count = 0;
        byte nextpixel;

        while (bytePerPixel <= buffer.remaining()) {

            if (0 == count) {
                pixel = buffer.get();
                count++;
                continue;
            }

            nextpixel = buffer.get();

            if (nextpixel == pixel) {
                count++;
            } else {
                rleBuffer.putByte(pixel, count);
                pixel = nextpixel;
                count = 1;
            }

            if ((MaxRunLength == count)) {
                rleBuffer.putByte(pixel, count);
                count = 0;
            }

        }

        if (0 != count) {
            rleBuffer.putByte(pixel, count);
        }
    }

    /**
     * Run-length Encoder method for 16-bit pixels.
     *
     * @param rleBuffer the rle buffer
     * @param buffer the buffer
     * @param bytePerPixel the byte per pixel
     */
    static private void encodeShort(RlePixelBuffer rleBuffer, ByteBuffer buffer, int bytePerPixel) {
        short pixel = 0;
        short nextpixel;
        int count = 0;

        while (bytePerPixel <= buffer.remaining()) {

            if (0 == count) {
                pixel = buffer.getShort();
                count++;
                continue;
            }

            nextpixel = buffer.getShort();

            if (nextpixel == pixel) {
                count++;
            } else {
                rleBuffer.putShort(pixel, count);
                pixel = nextpixel;
                count = 1;
            }

            if ((MaxRunLength == count)) {
                rleBuffer.putShort(pixel, count);
                count = 0;
            }

        }

        if (0 != count) {
            rleBuffer.putShort(pixel, count);
        }
    }

    /**
     * Run-length Encoder method for 32-bit pixels.
     *
     * @param rleBuffer the rle buffer
     * @param buffer the buffer
     * @param bytePerPixel the byte per pixel
     */
    static private void encodeInt(RlePixelBuffer rleBuffer, ByteBuffer buffer, int bytePerPixel) {

        int pixel = 0;
        int count = 0;
        int nextpixel;

        while (bytePerPixel <= buffer.remaining()) {

            if (0 == count) {
                pixel = buffer.getInt();
                count++;
                continue;
            }

            nextpixel = buffer.getInt();

            if (nextpixel == pixel) {
                count++;
            } else {
                rleBuffer.putInt(pixel, count);
                pixel = nextpixel;
                count = 1;
            }

            if ((MaxRunLength == count)) {
                rleBuffer.putInt(pixel, count);
                count = 0;
            }

        }

        if (0 != count) {
            rleBuffer.putInt(pixel, count);
        }
    }

    /**
     * Run-length Encoder method for n-bit pixels (for example 24-bit).
     *
     * @param rleBuffer the rle buffer
     * @param buffer the buffer
     * @param bytePerPixel the byte per pixel
     */
    static private void encodeAnySize(RlePixelBuffer rleBuffer, ByteBuffer buffer, int bytePerPixel) {

        ByteBuffer pixel = ByteBuffer.allocate(bytePerPixel);
        ByteBuffer nextpixel = ByteBuffer.allocate(bytePerPixel);
        int count = 0;

        while (bytePerPixel <= buffer.remaining()) {

            if (0 == count) {
                pixel.clear();

                while (0 != pixel.remaining()) {
                    pixel.put(buffer.get());
                }

                count++;
                continue;
            }

            nextpixel.clear();

            while (0 != nextpixel.remaining()) {
                nextpixel.put(buffer.get());
            }

            if (Arrays.equals(nextpixel.array(), pixel.array())) {
                count++;
            } else {
                rleBuffer.put(pixel.array(), count);
                ByteBuffer temp = pixel;
                pixel = nextpixel;
                nextpixel = temp;
                count = 1;
            }

            if ((MaxRunLength == count)) {
                rleBuffer.put(pixel.array(), count);
                count = 0;
            }
        }

        if (0 != count) {
            rleBuffer.put(pixel.array(), count);
        }
    }
}
