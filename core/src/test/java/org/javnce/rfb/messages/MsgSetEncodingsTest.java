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

public class MsgSetEncodingsTest {

    private MsgSetEncodings msg;

    @Test
    public void testDemarshal() {
        int[][] array = new int[][]{
            new int[]{1, 2, 3, 4, 5},
            new int[]{},
            new int[]{0},
            new int[]{-123, -34534, -34534, -34534, -9999999}
        };

        for (int i = 0; i < array.length; i++) {

            msg = new MsgSetEncodings(array[i]);
            ArrayList<ByteBuffer> list = msg.marshal();
            assertEquals(1, list.size());
            assertTrue(msg.isValid());

            ByteBuffer temp = MyByteBufferHelper.arrayListToBuffer(list);
            msg = new MsgSetEncodings();
            assertTrue(msg.demarshal(temp));
            assertTrue(msg.isValid());
            assertEquals(0, temp.remaining());


            org.junit.Assert.assertArrayEquals(array[i], msg.get());
        }

    }

    @Test
    public void testMarshal() {
        //Not valid
        msg = new MsgSetEncodings();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());

    }

    @Test
    public void testMsgSetEncodings() {
        msg = new MsgSetEncodings();
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgSetEncodingsIntArray() {
        msg = new MsgSetEncodings(new int[]{1, 2, 3, 4, 5});
        assertNotNull(msg);
        assertTrue(msg.isValid());

    }

    @Test
    public void testGet() {
        msg = new MsgSetEncodings();
        assertNull(msg.get());

        msg = new MsgSetEncodings(new int[]{1, 2, 3, 4, 5});
        assertNotNull(msg.get());
    }

    @Test
    public void testGetId() {
        msg = new MsgSetEncodings();
        assertNotNull(msg);
        assertEquals(Id.SetEncodings, msg.getId());

        msg = new MsgSetEncodings(new int[]{1, 2, 3, 4, 5});
        assertNotNull(msg);
        assertEquals(Id.SetEncodings, msg.getId());
    }

    @Test
    public void testToString() {
        msg = new MsgSetEncodings(new int[]{-123, -34534, -34534, -34534, -9999999});
        String text = msg.toString();
        assertNotNull(text);

        msg = new MsgSetEncodings();
        text = msg.toString();
        assertNotNull(text);
    }
}
