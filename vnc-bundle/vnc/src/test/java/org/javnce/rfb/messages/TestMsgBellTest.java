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
package org.javnce.rfb.messages;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestMsgBellTest {

    private MsgBell msg;

    @Test
    public void testDemarshal() {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte) 2);
        buffer.clear();

        msg = new MsgBell();
        assertTrue(msg.demarshal(buffer));
        assertTrue(msg.isValid());
    }

    @Test
    public void testMarshal() {
        msg = new MsgBell();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(1, list.size());

        assertEquals(1, MyByteBufferHelper.arrayListToBuffer(list).capacity());
        assertEquals(2, MyByteBufferHelper.arrayListToBuffer(list).get());
    }

    @Test
    public void testMsgBell() {
        msg = new MsgBell();
        assertNotNull(msg);
        assertTrue(msg.isValid());
    }

    @Test
    public void testGetId() {
        msg = new MsgBell();
        assertNotNull(msg);
        assertEquals(Id.Bell, msg.getId());
    }
}
