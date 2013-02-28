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
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;
import org.javnce.util.ByteBuffers;
import static org.junit.Assert.*;
import org.junit.Test;

public class MsgServerFactoryTest {

    private MsgServerFactory msg;

    class TestData {

        byte[] data;
        Id id;

        public TestData(byte[] data) {
            this.data = data;
            this.id = Id.Unknown;
        }

        public TestData(Message msg) {
            ArrayList<ByteBuffer> list = msg.marshal();
            this.data = ByteBuffers.asBuffer(list).array();
            this.id = msg.getId();
        }
    }

    @Test
    public void testDemarshal() {
        TestData[] array = new TestData[]{
            new TestData("F".getBytes()),
            new TestData("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF10".getBytes()),
            new TestData(new MsgSetPixelFormat(new PixelFormat(0, 1, false, true, new Color(2, 3, 4), new Color(5, 6, 7)))),
            new TestData(new MsgSetEncodings(new int[]{1, 2, 3, 4, 5})),
            new TestData(new MsgFramebufferUpdateRequest(true, new Rect(new Point(0xFFFF, 0xFFFF), new Size(0xFFFF, 0xFFFF)))),
            new TestData(new MsgKeyEvent(true, 0x12345678L)),
            new TestData(new MsgPointerEvent(0xFF, new Point(0xFFFF, 0xFFFF))),
            new TestData(new MsgClientCutText("dsadasdadsasd")),};

        for (int i = 0; i < array.length; i++) {
            msg = new MsgServerFactory();
            ByteBuffer buffer = ByteBuffer.wrap(array[i].data);
            assertTrue(msg.demarshal(buffer));
            assertEquals(0, buffer.remaining());
            assertTrue(msg.isValid());

            Message factoryMsg = msg.get();
            assertNotNull(factoryMsg);
            assertEquals(0, buffer.remaining());
            assertEquals(array[i].id, factoryMsg.getId());
            assertTrue(factoryMsg.isValid());
        }
    }

    @Test
    public void testMarshal() {
        msg = new MsgServerFactory();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());
    }

    @Test
    public void testMsgFactory() {
        msg = new MsgServerFactory();
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testGetId() {
        msg = new MsgServerFactory();
        assertNotNull(msg);
        assertEquals(Id.Factory, msg.getId());
    }

    @Test
    public void testToString() {
        msg = new MsgServerFactory();
        String text = msg.toString();
        assertNotNull(text);
    }
}
