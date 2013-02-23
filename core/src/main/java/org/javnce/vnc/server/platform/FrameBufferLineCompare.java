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
 * The FrameBufferLineCompare class counts CRC32 for each line in frame buffer.
 */
class FrameBufferLineCompare implements FrameBufferCompare {

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
     * The count of empty lines needed to find changed region.
     */
    static final private int MaxEmptyLines = 50;

    /**
     * Instantiates a new frame buffer compare.
     */
    public FrameBufferLineCompare() {
        adler32 = new Adler32();
    }

    @Override
    public ArrayList<Rect> compare(FramebufferDevice fb) {

        ArrayList<Rect> list = null;

        if (!fb.format().equals(format) || !fb.size().equals(size)) {
            checksums = null;
            format = fb.format();
            size = fb.size();
        }

        if (null == checksums) {
            checksums = new long[size.height()];
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
        int min = size.height() + 1;
        int max = -1;


        ByteBuffer buffers[] = fb.buffer(0, 0, size.width(), size.height());
        if (1 != buffers.length) {
            EventLoop.fatalError(this, new RuntimeException("Cannot support buffer arrays"));
            return list;
        }

        byte[] bytes = buffers[0].array();

        int lineLength = size.width() * format.bytesPerPixel();
        int height = size.height();

        int offset = 0;

        for (int i = 0; i < height; i++) {
            adler32.reset();
            adler32.update(bytes, offset, lineLength);
            long value = adler32.getValue();

            if (checksums[i] != value) {
                checksums[i] = value;
                min = Math.min(min, i);
                max = Math.max(max, i);
            } else if (-1 != max && (max + MaxEmptyLines) < i) {
                Rect rect = new Rect(0, min, size.width(), max - min + 1);
                list.add(rect);
                min = size.height() + 1;
                max = -1;
            }
            offset += lineLength;
        }

        if (-1 != max) {
            Rect rect = new Rect(0, min, size.width(), max - min + 1);
            list.add(rect);
        }

        return list;
    }
}
