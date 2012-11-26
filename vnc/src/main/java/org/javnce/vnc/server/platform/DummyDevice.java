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
package org.javnce.vnc.server.platform;

import java.nio.ByteBuffer;
import org.javnce.rfb.types.*;

/**
 * The Class DummyDevice implements FramebufferDevice with fixed data. The
 * PointerDevice and KeyBoardDevice implementations doesn't do anything.
 *
 * DummyDevice is used in view access for PointerDevice and KeyBoardDevice.
 */
class DummyDevice implements FramebufferDevice, PointerDevice, KeyBoardDevice {

    /**
     * The size.
     */
    final private Size size;
    /**
     * The format.
     */
    final private PixelFormat format;
    /**
     * The buffer.
     */
    final private byte[] buffer;

    /**
     * Instantiates a new dummy device.
     */
    DummyDevice() {
        size = new Size(1440, 900);
        format = PixelFormat.createARGB888();
        buffer = new byte[size.width() * size.height() * format.bytesPerPixel()];
        updateBuffer();
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#size()
     */
    @Override
    public Size size() {
        return size;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#format()
     */
    @Override
    public PixelFormat format() {
        return format;
    }

    /**
     * Update buffer.
     */
    private void updateBuffer() {
        byte fillByte = (byte) 0x77;

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = fillByte;
        }
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#buffer(int, int, int, int)
     */
    @Override
    public ByteBuffer[] buffer(int x, int y, int width, int height) {
        ByteBuffer[] buffers = null;
        int bpp = format.bytesPerPixel();
        int lineLength = size.width() * bpp;

        if (x == 0 && width == size.width()) {
            buffers = new ByteBuffer[]{ByteBuffer.wrap(buffer, lineLength * y, lineLength * height).slice()};
        } else {

            buffers = new ByteBuffer[height];
            int copyLength = width * bpp;

            for (int i = 0; i < height; i++) {
                buffers[i] = ByteBuffer.wrap(buffer, lineLength * (y + i) + x * bpp, copyLength).slice();
            }
        }

        return buffers;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.KeyBoardDevice#keyEvent(boolean, long)
     */
    @Override
    public void keyEvent(boolean down, long key) {
        //Do nothing
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.PointerDevice#pointerEvent(int, int, int)
     */
    @Override
    public void pointerEvent(int mask, int x, int y) {
        //Do nothing
    }
}
