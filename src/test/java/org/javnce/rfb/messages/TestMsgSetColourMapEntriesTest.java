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
import org.javnce.rfb.types.Color;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestMsgSetColourMapEntriesTest {

    MsgSetColourMapEntries msg;
    private int firstColour;
    private Color[] array;

    @Before
    public void setUp() throws Exception {
        firstColour = 10;
        array = new Color[]{new Color(1, 2, 3), new Color(4, 5, 6), new Color(123, 456, 789)};
    }

    @Test
    public void testDemarshal() {
        msg = new MsgSetColourMapEntries(firstColour, array);
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(1, list.size());
        assertTrue(msg.isValid());

        msg = new MsgSetColourMapEntries();
        assertTrue(msg.demarshal(MyByteBufferHelper.arrayListToBuffer(list)));
        assertTrue(msg.isValid());

        assertEquals(firstColour, msg.getFirstColour());

        Color[] result = msg.getColours();
        assertEquals(array.length, result.length);

        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], result[i]);
        }

    }

    @Test
    public void testMarshal() {
        //Not valid
        msg = new MsgSetColourMapEntries();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());
    }

    @Test
    public void testMsgSetColourMapEntries() {
        msg = new MsgSetColourMapEntries();
        assertNotNull(msg);
        assertFalse(msg.isValid());

    }

    @Test
    public void testMsgSetColourMapEntriesIntColorArray() {
        msg = new MsgSetColourMapEntries(firstColour, array);
        assertNotNull(msg);
        assertTrue(msg.isValid());

    }

    @Test
    public void testGetFirstColour() {
        msg = new MsgSetColourMapEntries();
        assertTrue(0 == msg.getFirstColour());

        msg = new MsgSetColourMapEntries(firstColour, array);
        assertTrue(firstColour == msg.getFirstColour());
    }

    @Test
    public void testGetColours() {
        msg = new MsgSetColourMapEntries();
        assertNull(msg.getColours());

        msg = new MsgSetColourMapEntries(firstColour, array);
        assertTrue(array == msg.getColours());
    }

    @Test
    public void testGetId() {
        msg = new MsgSetColourMapEntries();
        assertNotNull(msg);
        assertEquals(Id.SetColourMapEntries, msg.getId());

        msg = new MsgSetColourMapEntries(firstColour, array);
        assertNotNull(msg);
        assertEquals(Id.SetColourMapEntries, msg.getId());
    }
}
