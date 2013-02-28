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
package org.javnce.util;

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
     * @param buffers the array of RAW frame buffers
     * @param bytePerPixel the byte per pixel
     * @return the list of run-length encoded buffers
     */
    static public List<ByteBuffer> encode(ByteBuffer[] buffers, int bytePerPixel) {
        return encode(ByteBuffers.asBuffer(buffers), bytePerPixel);
    }

    /**
     * Run-length Encoder method.
     *
     * @param buffer the RAW frame buffer
     * @param bytePerPixel the byte per pixel
     * @return the list of run-length encoded buffers
     */
    static List<ByteBuffer> encode(ByteBuffer buffer, int bytePerPixel) {
        RlePixelBuffer rleBuffer = new RlePixelBuffer(bytePerPixel);

        if (4 == bytePerPixel) {
            encodeInt(rleBuffer, buffer, bytePerPixel);
        } else {
            encodeAnySize(rleBuffer, buffer, bytePerPixel);
        }
        return rleBuffer.getBuffers();
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

        while (bytePerPixel <= buffer.remaining()) {

            if (0 == count) {
                pixel = buffer.getInt();
                count++;
            } else {
                final int nextpixel = buffer.getInt();

                if (nextpixel == pixel) {
                    count++;
                    if ((MaxRunLength == count)) {
                        rleBuffer.putInt(pixel, count);
                        count = 0;
                    }
                } else {
                    rleBuffer.putInt(pixel, count);
                    pixel = nextpixel;
                    count = 1;
                }
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

        byte[] pixel = new byte[bytePerPixel];
        byte[] nextpixel = new byte[bytePerPixel];
        int count = 0;

        while (bytePerPixel <= buffer.remaining()) {

            if (0 == count) {
                buffer.get(pixel);
                count = 1;
            } else {
                buffer.get(nextpixel);

                if (Arrays.equals(nextpixel, pixel)) {
                    count++;
                    if ((MaxRunLength == count)) {
                        rleBuffer.put(pixel, count);
                        count = 0;
                    }
                } else {
                    rleBuffer.put(pixel, count);
                    //Swap
                    byte[] temp = pixel;
                    pixel = nextpixel;
                    nextpixel = temp;
                    count = 1;
                }
            }
        }

        if (0 != count) {
            rleBuffer.put(pixel, count);
        }
    }

    /**
     * Convert Run-length encoded data.
     *
     * @param buffers the RLE coded buffers
     * @param width the width
     * @param height the height
     * @param bytesPerPixel bytes per pixel
     * @return the byte buffer
     */
    static public ByteBuffer decode(ByteBuffer[] buffers, int width, int height, int bytesPerPixel) {
        return decode(ByteBuffers.asBuffer(buffers), width, height, bytesPerPixel);
    }

    /**
     * Convert Run-length encoded data.
     *
     * @param buffer the RLE coded buffer
     * @param width the width
     * @param height the height
     * @return the byte buffer
     */
    static ByteBuffer decode(ByteBuffer src, int width, int height, int bytesPerPixel) {

        byte[] pixel = new byte[bytesPerPixel];

        ByteBuffer dst = ByteBuffer.allocate(width * height * bytesPerPixel);

        int rleSize = bytesPerPixel + 1;

        while ((rleSize) <= src.remaining() && bytesPerPixel <= dst.remaining()) {

            int count = src.get() & 0xff;
            count += 1;

            src.get(pixel);

            for (int i = 0; i < count; i++) {
                dst.put(pixel);
            }
        }

        dst.clear();
        return dst;
    }

    private RunLengthEncoder() {
    }
}
