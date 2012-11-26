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

public class TestMsgUnknownTest {

    private MsgUnknown msg;

    @Test
    public void testDemarshal() {

        String[] array = new String[]{
            "R", "RFB 003.008", "RFB 003.008\n", "RFB 003.008\ntadaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        };

        for (int i = 0; i < array.length; i++) {
            msg = new MsgUnknown();
            ByteBuffer buffer = ByteBuffer.wrap(array[i].getBytes());

            assertTrue(msg.demarshal(buffer));
            assertTrue(msg.isValid());
            assertEquals(0, buffer.remaining());
        }
    }

    @Test
    public void testMarshal() {
        msg = new MsgUnknown();

        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());
    }

    @Test
    public void testGetId() {
        msg = new MsgUnknown();
        assertNotNull(msg);
        assertEquals(Id.Unknown, msg.getId());

    }
}
