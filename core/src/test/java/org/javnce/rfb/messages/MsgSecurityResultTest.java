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

public class MsgSecurityResultTest {

    private MsgSecurityResult msg;

    @Test
    public void testDemarshal() {
        MsgSecurityResult[] array = new MsgSecurityResult[]{
            new MsgSecurityResult(true),
            new MsgSecurityResult(false),
            new MsgSecurityResult("Failure")
        };

        for (int i = 0; i < array.length; i++) {
            ArrayList<ByteBuffer> list = array[i].marshal();
            assertEquals(1, list.size());

            assertTrue(array[i].isValid());

            msg = new MsgSecurityResult();
            assertTrue(msg.demarshal(MyByteBufferHelper.arrayListToBuffer(list)));
            assertTrue(msg.isValid());

            assertEquals(array[i].getText(), msg.getText());
            assertEquals(array[i].getStatus(), msg.getStatus());
        }
    }

    @Test
    public void testMarshal() {
        //Not valid
        msg = new MsgSecurityResult();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());
    }

    @Test
    public void testMsgSecurityResult() {
        msg = new MsgSecurityResult();
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgSecurityResultBoolean() {
        msg = new MsgSecurityResult(true);
        assertNotNull(msg);
        assertTrue(msg.isValid());
    }

    @Test
    public void testMsgSecurityResultString() {
        msg = new MsgSecurityResult("Failure");
        assertNotNull(msg);
        assertTrue(msg.isValid());

    }

    @Test
    public void testGetId() {
        msg = new MsgSecurityResult();
        assertEquals(Id.SecurityResult, msg.getId());

        msg = new MsgSecurityResult(true);
        assertEquals(Id.SecurityResult, msg.getId());

        msg = new MsgSecurityResult("Failure");
        assertEquals(Id.SecurityResult, msg.getId());
    }

    @Test
    public void testToString() {
        msg = new MsgSecurityResult();
        String text = msg.toString();
        assertNotNull(text);
    }
}
