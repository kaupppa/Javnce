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
import java.util.zip.Adler32;
import org.javnce.eventing.EventLoop;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;

/**
 * The FrameBufferBlockCompare class calculates CRC32 for n lines in a frame
 * buffer.
 *
 */
class FrameBufferBlockCompare implements FrameBufferCompare {

    /**
     * The current checksums.
     */
    private long[] checksums;
    /**
     * The frame buffer format.
     */
    private PixelFormat format;
    /**
     * The frame buffer size.
     */
    private Size size;
    /**
     * The checksum computer.
     */
    final private Adler32 adler32;
    /**
     * The max count of lines in one CRC32 calculation.
     */
    static final private int MaxLinesInBlock = 40;
    /**
     * The count of lines in one CRC32 calculation.
     */
    private int linesInBlock;

    /**
     * Instantiates a new frame buffer compare.
     */
    public FrameBufferBlockCompare() {
        adler32 = new Adler32();
        linesInBlock = 1;
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

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FrameBufferCompare#compare(org.javnce.vnc.server.platform.FramebufferDevice)
     */
    @Override
    public ArrayList<Rect> compare(FramebufferDevice fb) {
        ArrayList<Rect> list = null;
        if (!fb.format().equals(format) || !fb.size().equals(size)) {
            checksums = null;
            format = fb.format();
            size = fb.size();
            linesInBlock = calcLinesInBlock(size);
        }
        if (null == checksums) {
            checksums = new long[size.height() / linesInBlock];
            list = new ArrayList<>();
            list.add(new Rect(new Point(0, 0), size));
            calcChecksums(fb);
        } else {
            list = calcChecksums(fb);
        }
        return list;
    }

    /**
     * Calculates checksums.
     *
     * @param fb the frame buffer device
     * @return the list of changed areas in frame buffer.
     */
    private ArrayList<Rect> calcChecksums(FramebufferDevice fb) {
        ArrayList<Rect> list = new ArrayList<>();
        ByteBuffer buffers[] = fb.buffer(0, 0, size.width(), size.height());
        if (1 != buffers.length) {
            EventLoop.fatalError(this, new RuntimeException("Cannot support buffer arrays"));
        } else if (buffers[0].hasArray()) {
            byte[] bytes = buffers[0].array();
            Rect rect = null;
            int lastIndex = -2;
            int offset = 0;
            final int stride = size.width() * linesInBlock * format.bytesPerPixel();
            final int width = size.width();
            final int blockCount = size.height() / linesInBlock;
            for (int i = 0; i < blockCount; i++, offset += stride) {
                if (updateCRC32(i, bytes, offset, stride)) {

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
        }
        return list;
    }

    /**
     * Update CRC32 for a block.
     *
     * @param index the block index
     * @param bytes the frame buffer bytes
     * @param offset the block offset in frame buffer
     * @param stride the stride is block size in bytes
     * @return true, if block was changed
     */
    private boolean updateCRC32(int index, byte[] bytes, int offset, int stride) {
        adler32.reset();
        adler32.update(bytes, offset, stride);
        long value = adler32.getValue();
        boolean changed = (checksums[index] != value);
        checksums[index] = value;
        return changed;
    }
}
