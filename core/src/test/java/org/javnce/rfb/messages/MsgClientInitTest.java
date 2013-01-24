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

public class MsgClientInitTest {

    MsgClientInit msg;

    @Test
    public void testDemarshal() {
        for (int i = 0; i < 0xff; i++) {

            msg = new MsgClientInit();
            ByteBuffer buffer = ByteBuffer.allocate(1);
            buffer.put((byte) i);
            buffer.clear();

            assertTrue(msg.demarshal(buffer));
            assertTrue(msg.isValid());

            boolean shared = msg.get();
            assertEquals((0 == i), shared);
        }
    }

    @Test
    public void testMarshal() {
        byte value;

        msg = new MsgClientInit(true);
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(1, list.size());

        value = MyByteBufferHelper.arrayListToBuffer(list).get();
        assertEquals((byte) 1, value);

        msg = new MsgClientInit(false);
        list = msg.marshal();
        assertEquals(1, list.size());

        value = MyByteBufferHelper.arrayListToBuffer(list).get();
        assertEquals((byte) 0, value);

        //Not valid
        msg = new MsgClientInit();
        list = msg.marshal();
        assertEquals(0, list.size());
    }

    @Test
    public void testMsgClientInit() {
        msg = new MsgClientInit();
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgClientInitBoolean() {
        msg = new MsgClientInit(true);
        assertNotNull(msg);
        assertTrue(msg.isValid());

    }

    @Test
    public void testGetId() {
        msg = new MsgClientInit();
        assertEquals(Id.ClientInit, msg.getId());

        msg = new MsgClientInit(true);
        assertEquals(Id.ClientInit, msg.getId());
    }

    @Test
    public void testToString() {
        msg = new MsgClientInit(true);
        String text = msg.toString();
        assertNotNull(text);
    }
}
