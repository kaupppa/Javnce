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
import org.junit.Before;
import org.junit.Test;

public class TestMsgKeyEventTest {

    private MsgKeyEvent msg;
    private boolean down;
    private long key;

    class TestData {

        final private boolean down;
        final private long key;

        public TestData(boolean down, long key) {
            this.down = down;
            this.key = key;
        }
    }

    @Before
    public void setUp() {
        down = true;
        key = 0x87654321;
    }

    @Test
    public void testDemarshal() {
        TestData[] array = new TestData[]{
            new TestData(false, 0),
            new TestData(false, 1),
            new TestData(false, 0x7FFF),
            new TestData(false, 0x7FFFFFFFL),
            new TestData(false, 0xFFFFFFFFL),
            new TestData(true, 0xFFFFFFFFL),};

        for (int i = 0; i < array.length; i++) {

            msg = new MsgKeyEvent(array[i].down, array[i].key);
            ArrayList<ByteBuffer> list = msg.marshal();
            assertEquals(1, list.size());


            msg = new MsgKeyEvent();
            assertTrue(msg.demarshal(MyByteBufferHelper.arrayListToBuffer(list)));
            assertTrue(msg.isValid());

            assertEquals(array[i].down, msg.getDown());
            assertEquals(array[i].key, msg.getKey());
        }
    }

    @Test
    public void testMarshal() {
        //Not valid
        msg = new MsgKeyEvent();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());

    }

    @Test
    public void testMsgKeyEvent() {
        msg = new MsgKeyEvent();
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgKeyEventBooleanLong() {
        msg = new MsgKeyEvent(down, key);
        assertNotNull(msg);
        assertTrue(msg.isValid());
    }

    @Test
    public void testGetId() {
        msg = new MsgKeyEvent();
        assertNotNull(msg);
        assertEquals(Id.KeyEvent, msg.getId());

        msg = new MsgKeyEvent(down, key);
        assertNotNull(msg);
        assertEquals(Id.KeyEvent, msg.getId());
    }
}
