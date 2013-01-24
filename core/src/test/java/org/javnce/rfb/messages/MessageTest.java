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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MessageTest {

    private TestMsg msg;
    private Id id;
    private byte[] array;

    class TestMsg extends Message {

        final private int length;
        private byte[] array;

        public TestMsg(Id id, int length, byte[] array) {
            super(id);
            this.length = length;
            this.array = array;
        }

        @Override
        public boolean demarshal(ByteBuffer buffer) {
            array = null;

            if (buffer.remaining() >= length) {
                array = new byte[length];
                buffer.get(array);
            }

            return (array != null && array.length == length);
        }

        @Override
        public ArrayList<ByteBuffer> marshal() {
            ArrayList<ByteBuffer> list = new ArrayList<>();
            list.add(ByteBuffer.wrap(array));
            return list;
        }
    }

    @Before
    public void setUp() throws Exception {
        array = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        id = Id.Unknown;
        msg = new TestMsg(id, array.length, array);
    }

    @Test
    public void testMessage() {
        assertNotNull(msg);
    }

    @Test
    public void testGetId() {
        assertEquals(id, msg.getId());
    }

    @Test
    public void testIsValid() {
        assertFalse(msg.isValid());
    }

    @Test
    public void testSetValid() {
        assertFalse(msg.isValid());
        msg.setValid(true);
        assertTrue(msg.isValid());
    }

    @Test
    public void testDemarshaller() {

        // Test reading when stream have too short data
        {
            for (int i = 1; i < array.length; i++) {
                msg = new TestMsg(id, array.length, null);
                ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOf(array, i - 1));

                assertFalse(msg.demarshal(buffer));
            }
        }
        // Test reading when stream have n times data
        {
            int n = 10;
            int dataLength = array.length * n;
            ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOf(array, dataLength));

            for (int i = 1; i <= n; i++) {
                msg = new TestMsg(id, array.length, null);

                assertTrue(msg.demarshal(buffer));

                int remainingLength = dataLength - (i * array.length);
                assertEquals(remainingLength, buffer.remaining());
            }
        }
    }

    @Test
    public void testMarshaller() {

        ArrayList<ByteBuffer> list = msg.marshal();

        assertEquals(1, list.size());
        assertTrue(Arrays.equals(array, MyByteBufferHelper.arrayListToBuffer(list).array()));
    }
}
