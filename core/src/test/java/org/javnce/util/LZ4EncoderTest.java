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
package org.javnce.util;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;
import org.javnce.vnc.server.platform.FramebufferDevice;
import static org.junit.Assert.*;
import org.junit.Test;

public class LZ4EncoderTest {

    final private FramebufferDevice dev;
    final private Size size;
    final private PixelFormat format;

    public LZ4EncoderTest() {
        dev = FramebufferDevice.factory();
        size = dev.size();
        format = dev.format();

    }

    @Test
    public void testByteArray() {
        LZ4Encoder coder = new LZ4Encoder();

        ByteBuffer[] buffers = dev.buffer(0, 0, size.width(), size.height());
        byte[] data = ByteBuffers.asBuffer(buffers).array();

        ByteBuffer decoded = coder.compress(data);
        assertNotNull(decoded);
        assertFalse(0 == decoded.capacity());

        ByteBuffer encoded = coder.decompress(decoded.array(), data.length);
        assertNotNull(encoded);
        assertFalse(0 == encoded.capacity());

        assertTrue(Arrays.equals(data, encoded.array()));
    }

    @Test
    public void testByteBuffers() {
        LZ4Encoder coder = new LZ4Encoder();

        ByteBuffer[] buffers = dev.buffer(0, 0, size.width(), size.height());

        ByteBuffer decoded = coder.compress(buffers);
        assertNotNull(decoded);

        int count = size.width() * size.height() * format.bytesPerPixel();
        ByteBuffer encoded = coder.decompress(new ByteBuffer[]{decoded}, count);
        assertNotNull(encoded);

        ByteBuffer org = ByteBuffers.asBuffer(buffers);

        assertTrue(Arrays.equals(org.array(), encoded.array()));
    }
}
