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
package org.javnce.rfb.types;

import java.nio.ByteBuffer;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FramebufferTest {

    private Rect rect;
    private int encoding;
    private ByteBuffer[] buffers;
    private Framebuffer fb;

    @Before
    public void setUp() throws Exception {
        rect = new Rect(new Point(4321, 8765), new Size(1234, 5678));
        encoding = 1234;
        buffers = new ByteBuffer[]{ByteBuffer.allocate(100)};
        fb = new Framebuffer(rect, encoding, buffers);
    }

    @Test
    public void testFramebuffer() {
        fb = new Framebuffer(rect, encoding, buffers);
        assertNotNull(fb);
    }

    @Test
    public void testRect() {
        assertEquals(rect, fb.rect());
    }

    @Test
    public void testEncoding() {
        assertEquals(encoding, fb.encoding());
    }

    @Test
    public void testBuffer() {
        assertTrue(Arrays.equals(buffers, fb.buffers()));

        fb = new Framebuffer(rect, encoding, null);
        assertNull(fb.buffers());
    }
}
