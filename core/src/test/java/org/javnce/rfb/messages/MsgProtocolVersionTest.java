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
import org.javnce.rfb.types.Version;
import org.javnce.util.ByteBuffers;
import static org.junit.Assert.*;
import org.junit.Test;

public class MsgProtocolVersionTest {

    MsgProtocolVersion msg;

    class TestData {

        final public byte[] array;
        final public boolean valid;
        final public Version version;

        public TestData(byte[] array, boolean valid, Version version) {
            this.array = array;
            this.valid = valid;
            this.version = version;
        }
    }

    @Test
    public void testMsgProtocolVersion() {
        msg = new MsgProtocolVersion();
        assertNotNull(msg);
        assertEquals(new Version(-1, -1), msg.get());
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgProtocolVersionVersion() {
        msg = new MsgProtocolVersion(new Version(3, 8));
        assertNotNull(msg);
        assertTrue(msg.get().equals(new Version(3, 8)));
        assertTrue(msg.isValid());
    }

    @Test
    public void testGetId() {
        msg = new MsgProtocolVersion();
        assertEquals(Id.ProtocolVersion, msg.getId());

        msg = new MsgProtocolVersion(new Version(3, 8));
        assertEquals(Id.ProtocolVersion, msg.getId());
    }

    @Test
    public void testDemarshaller() {

        TestData[] array = new TestData[]{
            new TestData("R".getBytes(), false, new Version(-1, -1)), new TestData("RFB 003.008".getBytes(), false, new Version(-1, -1)), new TestData("RFB 003.008\n".getBytes(), true, new Version(3, 8)), new TestData("RFB 003.008\ntadaaaaaaa".getBytes(), true, new Version(3, 8)), new TestData("RFB 012.034\n".getBytes(), true, new Version(12, 34)), new TestData("RFB 123.456\n".getBytes(), true, new Version(123, 456)), new TestData("VNC 003.008\n".getBytes(), false, new Version(-1, -1)), new TestData("RFG 003.008\n".getBytes(), false, new Version(-1, -1))
        };

        for (int i = 0; i < array.length; i++) {
            msg = new MsgProtocolVersion();
            ByteBuffer buffer = ByteBuffer.wrap(array[i].array);

            if (array[i].array.length >= 12) {
                assertTrue(msg.demarshal(buffer));
            } else {
                assertFalse(msg.demarshal(buffer));
            }

            assertEquals(array[i].valid, msg.isValid());
            assertEquals(array[i].version, msg.get());
        }
    }

    @Test
    public void testMarshaller() {
        TestData[] array = new TestData[]{
            new TestData("RFB 003.008\n".getBytes(), true, new Version(3, 8)), new TestData("RFB 012.034\n".getBytes(), true, new Version(12, 34)), new TestData("RFB 123.456\n".getBytes(), true, new Version(123, 456))
        };

        for (int i = 0; i < array.length; i++) {
            msg = new MsgProtocolVersion(array[i].version);

            ArrayList<ByteBuffer> list = msg.marshal();
            assertEquals(1, list.size());
            assertTrue(Arrays.equals(array[i].array, ByteBuffers.asBuffer(list).array()));
        }

        //Not valid
        msg = new MsgProtocolVersion();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());
    }

    @Test
    public void testToString() {
        msg = new MsgProtocolVersion();
        String text = msg.toString();
        assertNotNull(text);
    }
}
