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
import java.util.ArrayList;
import org.javnce.eventing.EventLoop;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;

/**
 * The FrameBufferBlockCompare class updates frame buffer and checks if blocks
 * where changed.
 *
 * The block change detection is done by calculating checksum for n lines per
 * block.
 *
 */
class FrameBufferBlockCompare {

    /**
     * The checksums.
     */
    final private long[] checksums;
    /**
     * The max amount of lines in ablock.
     */
    static final private int MaxLinesInBlock = 40;
    /**
     * The lines in block.
     */
    final private int linesInBlock;
    /**
     * The dev.
     */
    final private FramebufferDevice dev;
    /**
     * The first compare call.
     */
    private boolean first;
    /**
     * The block size in bytes.
     */
    final private int blockSizeInBytes;
    /**
     * The width.
     */
    final private int width;
    /**
     * The block count.
     */
    final private int blockCount;
    /**
     * The size.
     */
    final private Size size;

    /**
     * Instantiates a new frame buffer block compare.
     */
    FrameBufferBlockCompare(FramebufferDevice dev) {
        this.dev = dev;
        first = true;

        PixelFormat format = dev.format();
        size = dev.size();
        linesInBlock = calcLinesInBlock(size);
        blockSizeInBytes = size.width() * linesInBlock * format.bytesPerPixel();
        width = size.width();
        blockCount = size.height() / linesInBlock;
        checksums = new long[size.height() / linesInBlock];
    }

    /**
     * Calculate number of lines in a block.
     *
     * @param size the frame buffer size
     * @return the whole number of lines in block
     */
    static private int calcLinesInBlock(Size size) {
        int count = 1;
        final int height = size.height();
        for (int i = MaxLinesInBlock; i > 1; i--) {
            if ((height % i) == 0) {
                count = i;
                break;
            }
        }
        return count;
    }

    /**
     * Compare.
     *
     * First time called it returns list with one area == whole frame buffer.
     *
     * @return the array of change since previous call
     */
    public ArrayList<Rect> compare() {
        ArrayList<Rect> list;

        if (first) {
            first = false;
            list = new ArrayList<>();
            list.add(new Rect(new Point(0, 0), size));
            calcChecksums();
        } else {
            list = calcChecksums();
        }
        return list;
    }

    /**
     * Calculates checksums for each block.
     *
     * @return the list of changed areas in frame buffer.
     */
    private ArrayList<Rect> calcChecksums() {
        ArrayList<Rect> list = new ArrayList<>();

        ByteBuffer buffers[] = dev.buffer(0, 0, size.width(), size.height());
        if (1 != buffers.length) {
            EventLoop.fatalError(this, new RuntimeException("Cannot support buffer arrays"));
            return list;
        }
        ByteBuffer buffer = buffers[0];
        Rect rect = null;
        int lastIndex = -2;
        for (int i = 0; i < blockCount; i++) {
            if (updateChecksum(i, buffer, i * blockSizeInBytes)) {
                if (null == rect) {
                    //first changed
                    rect = new Rect(0, i * linesInBlock, width, linesInBlock);
                } else if ((lastIndex + 1) == i) {
                    //previous was also changed
                    rect = new Rect(0, rect.y(), width, rect.height() + linesInBlock);
                } else {
                    //previous was not changed
                    list.add(rect);
                    rect = new Rect(0, i * linesInBlock, width, linesInBlock);
                }
            }
        }
        if (null != rect) {
            list.add(rect);
        }
        return list;
    }

    /**
     * Update checksum for a block.
     *
     * @param index the block index
     * @param buffer the frame buffer in a ByteBuffer
     * @param offset the block offset in frame buffer
     * @param stride the stride is block size in bytes
     * @return true, if block was changed
     */
    private boolean updateChecksum(int index, ByteBuffer buffer, int offset) {
        //long value = Checksum.crc32(buffer, offset, stride);
        long value = Checksum.adler32(buffer, offset, blockSizeInBytes);
        boolean changed = (checksums[index] != value);
        checksums[index] = value;
        return changed;
    }
}
