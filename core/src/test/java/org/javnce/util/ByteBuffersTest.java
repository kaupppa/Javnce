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
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

public class ByteBuffersTest {

    @Test
    public void testAsBuffer_ByteBufferArr() {
        {
            ByteBuffer[] buffers = null;
            ByteBuffer expResult = null;
            ByteBuffer result = ByteBuffers.asBuffer(buffers);
            assertEquals(expResult, result);
        }
        {
            ByteBuffer[] buffers = new ByteBuffer[0];
            ByteBuffer expResult = null;
            ByteBuffer result = ByteBuffers.asBuffer(buffers);
            assertEquals(expResult, result);
        }
        {
            ByteBuffer[] buffers = new ByteBuffer[]{ByteBuffer.allocateDirect(100), ByteBuffer.allocateDirect(100)};
            ByteBuffer result = ByteBuffers.asBuffer(buffers);
            assertEquals(200, result.remaining());
            assertEquals(200, result.capacity());
            assertTrue(result.hasArray());
        }
        {
            ByteBuffer[] buffers = new ByteBuffer[]{ByteBuffer.allocate(100)};
            ByteBuffer result = ByteBuffers.asBuffer(buffers);
            assertEquals(buffers[0], result);
        }
    }

    @Test
    public void testAsBuffer_List() {
        {
            List<ByteBuffer> buffers = null;
            ByteBuffer expResult = null;
            ByteBuffer result = ByteBuffers.asBuffer(buffers);
            assertEquals(expResult, result);
        }
        {
            List<ByteBuffer> buffers = new ArrayList<>();
            ByteBuffer expResult = null;
            ByteBuffer result = ByteBuffers.asBuffer(buffers);
            assertEquals(expResult, result);
        }

        {
            List<ByteBuffer> buffers = new ArrayList<>();
            buffers.add(ByteBuffer.allocateDirect(100));
            buffers.add(ByteBuffer.allocateDirect(100));
            ByteBuffer result = ByteBuffers.asBuffer(buffers);
            assertEquals(200, result.remaining());
            assertEquals(200, result.capacity());
            assertTrue(result.hasArray());
        }
        {
            List<ByteBuffer> buffers = new ArrayList<>();
            buffers.add(ByteBuffer.allocate(100));
            ByteBuffer result = ByteBuffers.asBuffer(buffers);
            assertEquals(buffers.get(0), result);
        }
    }
}
