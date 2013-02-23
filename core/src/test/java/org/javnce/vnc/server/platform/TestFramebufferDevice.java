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
import java.nio.IntBuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;

public class TestFramebufferDevice extends FramebufferDevice {

    final Size size;
    final Rect rect;
    final PixelFormat format;
    final byte[] buffer;

    TestFramebufferDevice() {
        size = new Size(1440, 900);
        rect = new Rect(new Point(0, 0), size);
        format = PixelFormat.createARGB888();
        buffer = new byte[size.width() * size.height() * format.bytesPerPixel()];

        byte fillByte = (byte) 0xa5;
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = fillByte;
        }
    }

    void change(Rect area) {
        IntBuffer temp = ByteBuffer.wrap(buffer).asIntBuffer();
        final int xLimit = area.x() + area.width();
        final int yLimit = area.y() + area.height();

        for (int y = area.y(); y < yLimit && y < rect.height(); y++) {
            int offset = y * rect.width();
            for (int x = area.x(); x < xLimit && x < rect.width(); x++) {
                int org = temp.get(offset + x);
                temp.put(offset + x, ~org);
            }
        }
    }

    @Override
    public Size size() {
        return size;
    }

    @Override
    public PixelFormat format() {
        return format;
    }

    @Override
    public ByteBuffer[] buffer(int x, int y, int width, int height) {
        return new ByteBuffer[]{ByteBuffer.wrap(buffer)};
    }

    @Override
    public void grabScreen() {
    }
}