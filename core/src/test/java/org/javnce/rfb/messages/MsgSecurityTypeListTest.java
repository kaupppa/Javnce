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
import java.util.Arrays;
import org.javnce.rfb.types.SecurityType;
import static org.junit.Assert.*;
import org.junit.Test;

public class MsgSecurityTypeListTest {

    private MsgSecurityTypeList msg;

    @Test
    public void testDemarshal() {
        MsgSecurityTypeList[] array = new MsgSecurityTypeList[]{
            new MsgSecurityTypeList(new SecurityType[]{SecurityType.Invalid}),
            new MsgSecurityTypeList(new SecurityType[]{SecurityType.Invalid, SecurityType.None}),
            new MsgSecurityTypeList("Failure"),};

        for (int i = 0; i < array.length; i++) {

            ArrayList<ByteBuffer> list = array[i].marshal();
            assertEquals(1, list.size());
            assertTrue(array[i].isValid());

            msg = new MsgSecurityTypeList();
            assertTrue(msg.demarshal(MyByteBufferHelper.arrayListToBuffer(list)));
            assertTrue(msg.isValid());

            assertEquals(array[i].getText(), msg.getText());
            assertTrue(Arrays.equals(array[i].getTypes(), msg.getTypes()));
        }
    }

    @Test
    public void testMarshal() {
        //Not valid
        msg = new MsgSecurityTypeList();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());
    }

    @Test
    public void testMsgSecurityTypeList() {
        msg = new MsgSecurityTypeList();
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgSecurityTypeListByteArray() {
        msg = new MsgSecurityTypeList(new SecurityType[]{SecurityType.Invalid, SecurityType.None});
        assertNotNull(msg);
        assertTrue(msg.isValid());
    }

    @Test
    public void testMsgSecurityTypeListString() {
        msg = new MsgSecurityTypeList("Failure");
        assertNotNull(msg);
        assertTrue(msg.isValid());
    }

    @Test
    public void testGetId() {
        msg = new MsgSecurityTypeList();
        assertNotNull(msg);
        assertEquals(Id.SecurityTypeList, msg.getId());

        msg = new MsgSecurityTypeList(new SecurityType[]{SecurityType.Invalid, SecurityType.None});
        assertNotNull(msg);
        assertEquals(Id.SecurityTypeList, msg.getId());

        msg = new MsgSecurityTypeList("Failure");
        assertNotNull(msg);
        assertEquals(Id.SecurityTypeList, msg.getId());

    }

    @Test
    public void testToString() {
        msg = new MsgSecurityTypeList();
        String text = msg.toString();
        assertNotNull(text);
    }
}
