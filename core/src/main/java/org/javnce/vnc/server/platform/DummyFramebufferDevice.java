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

class DummyFramebufferDevice extends FramebufferDevice {

    final private Size size;
    final private PixelFormat format;
    final private byte[] buffer;

    DummyFramebufferDevice() {
        size = new Size(1440, 900);
        format = PixelFormat.createARGB888();
        buffer = new byte[size.width() * size.height() * format.bytesPerPixel()];
        updateBuffer();
    }

    @Override
    public Size size() {
        return size;
    }

    @Override
    public PixelFormat format() {
        return format;
    }

    private void updateBuffer() {
        byte fillByte = (byte) 0x77;

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = fillByte;
        }
    }

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

    @Override
    public void grabScreen() {
        //Do nothing
    }
}
