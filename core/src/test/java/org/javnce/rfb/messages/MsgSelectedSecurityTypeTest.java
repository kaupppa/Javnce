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
import org.javnce.rfb.types.SecurityType;
import org.javnce.util.ByteBuffers;
import static org.junit.Assert.*;
import org.junit.Test;

public class MsgSelectedSecurityTypeTest {

    private MsgSelectedSecurityType msg;

    @Test
    public void testDemarshal() {
        SecurityType[] array = new SecurityType[]{SecurityType.Invalid, SecurityType.None};

        for (int i = 0; i < array.length; i++) {
            msg = new MsgSelectedSecurityType();
            ByteBuffer buffer = ByteBuffer.allocate(1);
            buffer.put(array[i].id());
            buffer.clear();

            assertTrue(msg.demarshal(buffer));
            assertTrue(msg.isValid());

            assertEquals(array[i], msg.get());
        }
    }

    @Test
    public void testMarshal() {
        SecurityType[] array = new SecurityType[]{SecurityType.Invalid, SecurityType.None};

        for (int i = 0; i < array.length; i++) {
            msg = new MsgSelectedSecurityType(array[i]);
            ArrayList<ByteBuffer> list = msg.marshal();
            assertEquals(1, list.size());
            byte type = ByteBuffers.asBuffer(list).get();
            assertEquals(type, (byte) i);
        }

        //Not valid
        msg = new MsgSelectedSecurityType();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());

    }

    @Test
    public void testMsgSelectedSecurityType() {
        msg = new MsgSelectedSecurityType();
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgSelectedSecurityTypeByte() {
        msg = new MsgSelectedSecurityType(SecurityType.None);
        assertNotNull(msg);
        assertTrue(msg.isValid());

    }

    @Test
    public void testGetId() {
        msg = new MsgSelectedSecurityType(SecurityType.None);
        assertEquals(Id.SelectedSecurityType, msg.getId());

        msg = new MsgSelectedSecurityType();
        assertEquals(Id.SelectedSecurityType, msg.getId());
    }

    @Test
    public void testToString() {
        msg = new MsgSelectedSecurityType();
        String text = msg.toString();
        assertNotNull(text);
    }
}
