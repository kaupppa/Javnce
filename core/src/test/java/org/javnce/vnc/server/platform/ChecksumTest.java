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
import static org.junit.Assert.*;
import org.junit.Test;

public class ChecksumTest {

    @Test
    public void testadler32Full() {

        byte data[] = new byte[100];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        ByteBuffer buffer1 = ByteBuffer.allocateDirect(data.length);
        buffer1.put(data);

        long result1 = Checksum.adler32(buffer1, 0, buffer1.capacity());

        ByteBuffer buffer2 = ByteBuffer.allocate(data.length);
        buffer2.put(data);
        long result2 = Checksum.adler32(buffer2, 0, buffer2.capacity());
        assertEquals(result1, result2);
    }

    @Test
    public void testadler32() {

        int offset = 30;
        int length = 30;
        byte data[] = new byte[100];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        ByteBuffer buffer1 = ByteBuffer.allocateDirect(data.length);
        buffer1.put(data);

        long result1 = Checksum.adler32(buffer1, offset, length);

        ByteBuffer buffer2 = ByteBuffer.allocate(data.length);
        buffer2.put(data);
        long result2 = Checksum.adler32(buffer2, offset, length);
        assertEquals(result1, result2);
    }
}
