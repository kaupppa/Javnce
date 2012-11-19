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
package org.javnce.vnc.common;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import org.javnce.rfb.messages.Message;
import org.javnce.rfb.messages.MsgBell;
import org.javnce.rfb.messages.MsgClientCutText;
import org.javnce.rfb.messages.MsgFramebufferUpdate;
import org.javnce.rfb.messages.MsgFramebufferUpdateRequest;
import org.javnce.rfb.messages.MsgKeyEvent;
import org.javnce.rfb.messages.MsgPointerEvent;
import org.javnce.rfb.messages.MsgServerCutText;
import org.javnce.rfb.messages.MsgSetColourMapEntries;
import org.javnce.rfb.messages.MsgSetEncodings;
import org.javnce.rfb.messages.MsgSetPixelFormat;
import org.javnce.rfb.types.Color;
import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestSocketWriterTest {

    static final private PixelFormat format = PixelFormat.createRGB565();
    private Pipe pipe;
    private SocketWriter writer;
    private Message[] toBeRead;
    private Message[] toBeSent;

    @Before
    public void setUp() throws Exception {
        pipe = Pipe.open();
        pipe.source().configureBlocking(false);
        int bytePerPixel = format.bytesPerPixel();
        Rect rect = new Rect(new Point(0, 0), new Size(10, 10));
        int fbSize = rect.width() * rect.height() * bytePerPixel;

        toBeSent = new Message[]{
            new MsgSetPixelFormat(new PixelFormat(0, 1, false, true, new Color(2, 3, 4), new Color(5, 6, 7))),
            new MsgSetEncodings(new int[]{1, 2, 3, 4, 5}),
            new MsgFramebufferUpdateRequest(true, new Rect(new Point(0xFFFF, 0xFFFF), new Size(0xFFFF, 0xFFFF))),
            new MsgKeyEvent(true, 0x12345678L),
            new MsgPointerEvent(0xFF, new Point(0xFFFF, 0xFFFF)),
            new MsgClientCutText("What ever"),
            new MsgFramebufferUpdate(format, new Framebuffer[]{new Framebuffer(rect, 0, new ByteBuffer[]{ByteBuffer.allocate(fbSize)}), new Framebuffer(rect, 0, new ByteBuffer[]{ByteBuffer.allocate(fbSize)})}),
            new MsgSetColourMapEntries(1, new Color[]{new Color(1, 2, 3), new Color(4, 5, 6), new Color(123, 456, 789)}),
            new MsgBell(),
            new MsgServerCutText("pla pla pla pla plaaaa......"),
            new MsgSetPixelFormat(new PixelFormat(0, 1, false, true, new Color(2, 3, 4), new Color(5, 6, 7))),
            new MsgSetEncodings(new int[]{1, 2, 3, 4, 5}),
            new MsgFramebufferUpdateRequest(true, new Rect(new Point(0xFFFF, 0xFFFF), new Size(0xFFFF, 0xFFFF))),
            new MsgKeyEvent(true, 0x12345678L),
            new MsgPointerEvent(0xFF, new Point(0xFFFF, 0xFFFF)),
            new MsgClientCutText("What ever"),
            new MsgFramebufferUpdate(format, new Framebuffer[]{new Framebuffer(rect, 0, new ByteBuffer[]{ByteBuffer.allocate(fbSize)}), new Framebuffer(rect, 0, new ByteBuffer[]{ByteBuffer.allocate(fbSize)})}),
            new MsgSetColourMapEntries(1, new Color[]{new Color(1, 2, 3), new Color(4, 5, 6), new Color(123, 456, 789)}),
            new MsgBell(),
            new MsgServerCutText("pla pla pla pla plaaaa......"),};
        toBeRead = new Message[]{
            new MsgSetPixelFormat(),
            new MsgSetEncodings(),
            new MsgFramebufferUpdateRequest(),
            new MsgKeyEvent(),
            new MsgPointerEvent(),
            new MsgClientCutText(),
            new MsgFramebufferUpdate(format),
            new MsgSetColourMapEntries(),
            new MsgBell(),
            new MsgServerCutText(),
            new MsgSetPixelFormat(),
            new MsgSetEncodings(),
            new MsgFramebufferUpdateRequest(),
            new MsgKeyEvent(),
            new MsgPointerEvent(),
            new MsgClientCutText(),
            new MsgFramebufferUpdate(format),
            new MsgSetColourMapEntries(),
            new MsgBell(),
            new MsgServerCutText(),};
    }

    @Test
    public void testSocketWriter() {
        writer = new SocketWriter();
        assertNotNull(writer);
        assertTrue(writer.isEmpty());
    }

    @Test
    public void testAdd() {
        writer = new SocketWriter();
        assertNotNull(writer);

        for (int i = 0; i < toBeSent.length; i++) {
            writer.add(toBeSent[i]);
        }

        assertFalse(writer.isEmpty());
    }

    @Test
    public void testWrite() throws Throwable {
        //Logger logger = Logger.getLogger(TestSocketWriterTest.class.getName());
        writer = new SocketWriter();

        ByteBuffer data = ByteBuffer.allocate(10000);

        for (int i = 0; i < toBeSent.length; i++) {
            writer.add(toBeSent[i]);
            //logger.info("Write " + toBeSent[i]);
            writer.write(pipe.sink());
            assertTrue(writer.isEmpty());

            pipe.source().read(data);
            data.flip();

            assertTrue(toBeRead[i].demarshal(data));
            //logger.info("Read " + toBeRead[i]);

            assertTrue(data.remaining() == 0);


            assertTrue(toBeRead[i].isValid());
            assertEquals(toBeSent[i].getId(), toBeRead[i].getId());
            data.clear();
        }
    }
}
