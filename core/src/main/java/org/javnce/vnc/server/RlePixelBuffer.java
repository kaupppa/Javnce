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
import java.util.ArrayList;
import java.util.List;

/**
 * The Run-length Encoding encoded frame buffer class.
 *
 * The size of buffer is dynamic.
 */
class RlePixelBuffer {

    /**
     * The size of one buffer.
     */
    final static private int PixelsPerBuffer = 10000;
    /**
     * The byte per pixel.
     */
    final private int bytePerPixel;
    /**
     * The list of buffers.
     */
    final private List<ByteBuffer> list;
    /**
     * The buffer.
     */
    private ByteBuffer buffer;

    /**
     * Instantiates a new run-length encoded pixel buffer.
     *
     * @param bytePerPixel the byte per pixel
     */
    RlePixelBuffer(int bytePerPixel) {
        this.bytePerPixel = bytePerPixel;
        list = new ArrayList<>();
        buffer = ByteBuffer.allocate((bytePerPixel + 1) * PixelsPerBuffer);
    }

    /**
     * Gets the run-length encoded buffers.
     *
     * @return the list of buffers
     */
    List<ByteBuffer> getBuffers() {

        refesh();

        if (0 != buffer.position()) {
            buffer.flip();
            list.add(buffer.slice());
        }

        return list;
    }

    /**
     * Checks if buffer needs to swapped.
     */
    private void refesh() {
        if (0 == buffer.remaining()) {
            list.add(buffer);
            buffer.clear();
            buffer = ByteBuffer.allocate((bytePerPixel + 1) * PixelsPerBuffer);
        }

    }

    /**
     * Put a 32-bit run-length encoded pixel.
     *
     * @param pixel the pixel
     * @param count the count
     */
    void putInt(int pixel, int count) {

        refesh();
        buffer.put((byte) ((count - 1) & 0xFF));
        buffer.putInt(pixel);
    }

    /**
     * Put a n-bit run-length encoded pixel.
     *
     * @param array the array
     * @param count the count
     */
    void put(byte array[], int count) {

        refesh();
        buffer.put((byte) ((count - 1) & 0xFF));
        buffer.put(array);
    }
}
