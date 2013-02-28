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
import junit.framework.TestCase;
import org.javnce.rfb.types.Color;
import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;
import org.javnce.util.ByteBuffers;
import org.junit.Test;

public class MsgClientFactoryTest extends TestCase {

    static final private PixelFormat format = PixelFormat.createRGB565();
    private MsgClientFactory msg;

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
        int bytePerPixel = format.bytesPerPixel();
        Rect rect = new Rect(new Point(0, 0), new Size(10, 10));
        ByteBuffer[] fbbuffers = new ByteBuffer[]{ByteBuffer.allocate(rect.width() * rect.height() * bytePerPixel)};
        Framebuffer[] fbs = new Framebuffer[]{new Framebuffer(rect, 0, fbbuffers), new Framebuffer(rect, 0, fbbuffers)};


        TestData[] array = new TestData[]{
            new TestData("F".getBytes()),
            new TestData("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF10".getBytes()),
            new TestData(new MsgFramebufferUpdate(format, fbs)),
            new TestData(new MsgSetColourMapEntries(1, new Color[]{new Color(1, 2, 3), new Color(4, 5, 6), new Color(123, 456, 789)})),
            new TestData(new MsgBell()),
            new TestData(new MsgServerCutText("fdfjdfjfipoewmlf'fkfaslffaf")),};

        for (int i = 0; i < array.length; i++) {
            msg = new MsgClientFactory(format);
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
        msg = new MsgClientFactory(format);
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());
    }

    @Test
    public void testMsgFactory() {
        msg = new MsgClientFactory(format);
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testGetId() {
        msg = new MsgClientFactory(format);
        assertNotNull(msg);
        assertEquals(Id.Factory, msg.getId());

    }

    @Test
    public void testToString() {
        msg = new MsgClientFactory(format);
        String text = msg.toString();
        assertNotNull(text);
    }
}
