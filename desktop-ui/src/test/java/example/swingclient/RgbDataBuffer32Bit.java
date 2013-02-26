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
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;
import org.javnce.vnc.common.LZ4Encoder;

/**
 * The Class 32bit RgbDataBuffer.
 */
public class RgbDataBuffer32Bit implements RgbDataBuffer {

    /**
     * The data buffer.
     */
    final private DataBufferInt dataBuffer;
    /**
     * The rgb buffer.
     */
    final private IntBuffer rgbBuffer;
    /**
     * The buffer width.
     */
    final private int bufferWidth;
    /**
     * The buffer height.
     */
    final private int bufferHeight;
    /**
     * The Rle item size.
     */
    final static private int rleSize = 5;
    final private LZ4Encoder lz4Encoder;

    /**
     * Instantiates a new buffer.
     *
     * @param size the size
     * @param format the format
     */
    public RgbDataBuffer32Bit(Size size, PixelFormat format) {
        this.bufferWidth = size.width();
        this.bufferHeight = size.height();

        rgbBuffer = IntBuffer.allocate(bufferWidth * bufferHeight);
        dataBuffer = new DataBufferInt(rgbBuffer.array(), rgbBuffer.capacity());
        lz4Encoder = new LZ4Encoder();
    }

    /* (non-Javadoc)
     * @see example.swingclient.RgbDataBuffer#writeRaw(int, int, int, int, java.nio.ByteBuffer)
     */
    @Override
    public void writeRaw(int x, int y, int width, int height, ByteBuffer buffer) {
        boolean useCopy = true;
        ByteOrder savedOrder = buffer.order();
        buffer.order(rgbBuffer.order());

        if (useCopy) {
            IntBuffer temp = IntBuffer.allocate(width * height);
            buffer.order(rgbBuffer.order());
            temp.put(buffer.asIntBuffer());
            temp.clear();
            copy(x, y, width, height, temp);
        } else {
            put(x, y, width, height, buffer);
        }

        buffer.order(savedOrder);
    }

    /* (non-Javadoc)
     * @see example.swingclient.RgbDataBuffer#writeRle(int, int, int, int, java.nio.ByteBuffer)
     */
    @Override
    public void writeRle(int x, int y, int width, int height, ByteBuffer buffer) {
        ByteOrder savedOrder = buffer.order();
        buffer.order(rgbBuffer.order());

        int pixel;
        int count;
        int row = y * bufferWidth;
        int column = x;

        while ((rleSize) <= buffer.remaining()) {
            count = buffer.get() & 0xff;
            count += 1;

            pixel = buffer.getInt();

            for (int i = 0; i < count; i++) {

                rgbBuffer.put(row + column, pixel);
                column++;

                if (column >= width) {
                    row += bufferWidth;
                    column = x;
                }
            }
        }

        buffer.order(savedOrder);
    }

    /* (non-Javadoc)
     * @see example.swingclient.RgbDataBuffer#dataBuffer()
     */
    @Override
    public DataBuffer dataBuffer() {
        return dataBuffer;
    }

    /**
     * Copy.
     *
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     * @param buffer the buffer
     */
    private void copy(int x, int y, int width, int height, IntBuffer buffer) {
        int[] data = buffer.array();

        if (isFullWidth(x, width)) {
            System.arraycopy(data, 0, rgbBuffer.array(), y * bufferWidth, height * bufferWidth);
        } else {
            for (int i = 0; i < height; i++, y++) {
                System.arraycopy(data, i * width, rgbBuffer.array(), y * bufferWidth + x, width);
            }
        }
    }

    /**
     * Checks if is full width.
     *
     * @param x the x
     * @param width the width
     * @return true, if is full width
     */
    private boolean isFullWidth(int x, int width) {
        boolean fullWidth = false;

        if (x == 0 && width == bufferWidth) {
            fullWidth = true;
        }

        return fullWidth;
    }

    /**
     * Put.
     *
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     * @param buffer the buffer
     */
    private void put(int x, int y, int width, int height, ByteBuffer buffer) {

        int row = y * bufferWidth;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rgbBuffer.put(row + x + j, buffer.getInt());
            }

            row += bufferWidth;
        }
    }

    @Override
    public void writeLZ4(int x, int y, int width, int height, ByteBuffer[] buffers) {
        ByteBuffer buffer = lz4Encoder.decompress(buffers, width * height * 4);
        writeRaw(x, y, width, height, buffer);
    }
}