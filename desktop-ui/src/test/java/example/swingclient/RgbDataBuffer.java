/*
 * Copyright (C) 2012 Pauli Kauppinen
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
package example.swingclient;

import java.awt.image.DataBuffer;
import java.nio.ByteBuffer;

/**
 * The interface for 8/16/24/32bit RgbDataBuffers.
 */
public interface RgbDataBuffer {

    /**
     * Data buffer getter.
     *
     * @return the data buffer
     */
    DataBuffer dataBuffer();

    /**
     * Write raw RGB data.
     *
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     * @param buffer the buffer
     */
    void writeRaw(int x, int y, int width, int height, ByteBuffer buffer);

    /**
     * Write Rle encoded RGB data.
     *
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     * @param buffer the buffer
     */
    void writeRle(int x, int y, int width, int height, ByteBuffer buffer);

    /**
     * Write LZ4 encoded RGB data.
     *
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     * @param buffers the buffers
     */
    void writeLZ4(int x, int y, int width, int height, ByteBuffer[] buffers);
}