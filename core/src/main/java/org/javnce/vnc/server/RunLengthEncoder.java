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
     * @param buffer the RAW frame buffer
     * @param bytePerPixel the byte per pixel
     * @return the list of run-length encoded buffers
     */
    static public List<ByteBuffer> encode(ByteBuffer buffer, int bytePerPixel) {
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

    private RunLengthEncoder() {
    }
}
