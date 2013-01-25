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
import java.util.ArrayList;
import org.javnce.eventing.LoopbackChannelPair;
import org.javnce.rfb.messages.Message;
import org.javnce.rfb.messages.MsgBell;
import org.javnce.rfb.messages.MsgClientCutText;
import org.javnce.rfb.messages.MsgClientInit;
import org.javnce.rfb.messages.MsgFramebufferUpdate;
import org.javnce.rfb.messages.MsgFramebufferUpdateRequest;
import org.javnce.rfb.messages.MsgKeyEvent;
import org.javnce.rfb.messages.MsgPointerEvent;
import org.javnce.rfb.messages.MsgProtocolVersion;
import org.javnce.rfb.messages.MsgSecurityTypeList;
import org.javnce.rfb.messages.MsgServerCutText;
import org.javnce.rfb.messages.MsgSetColourMapEntries;
import org.javnce.rfb.messages.MsgSetEncodings;
import org.javnce.rfb.messages.MsgSetPixelFormat;
import org.javnce.rfb.messages.MyByteBufferHelper;
import org.javnce.rfb.types.Color;
import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.SecurityType;
import org.javnce.rfb.types.Size;
import org.javnce.rfb.types.Version;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SocketReaderTest {

    static final private PixelFormat format = PixelFormat.createRGB565();
    private SocketReader reader;
    private Message[] toBeSent;

    @Before
    public void setUp() throws Exception {

        int bytePerPixel = format.bytesPerPixel();
        Rect rect = new Rect(new Point(0, 0), new Size(10, 10));
        ByteBuffer[] fbbuffers = new ByteBuffer[]{ByteBuffer.allocate(rect.width() * rect.height() * bytePerPixel)};

        Framebuffer[] fbs = new Framebuffer[]{new Framebuffer(rect, 0, fbbuffers), new Framebuffer(rect, 0, fbbuffers)};

        toBeSent = new Message[]{
            new MsgProtocolVersion(new Version(3, 8)),
            new MsgSecurityTypeList(new SecurityType[]{SecurityType.None}),
            new MsgClientInit(true),
            new MsgSetPixelFormat(new PixelFormat(0, 1, false, true, new Color(2, 3, 4), new Color(5, 6, 7))),
            new MsgSetEncodings(new int[]{1, 2, 3, 4, 5}),
            new MsgFramebufferUpdateRequest(true, new Rect(new Point(0xFFFF, 0xFFFF), new Size(0xFFFF, 0xFFFF))),
            new MsgKeyEvent(true, 0x12345678L),
            new MsgPointerEvent(0xFF, new Point(0xFFFF, 0xFFFF)),
            new MsgClientCutText("What ever"),
            new MsgFramebufferUpdate(format, fbs),
            new MsgSetColourMapEntries(1, new Color[]{new Color(1, 2, 3), new Color(4, 5, 6), new Color(123, 456, 789)}),
            new MsgBell(),
            new MsgServerCutText("pla pla pla pla plaaaa......"),
            new MsgSetPixelFormat(new PixelFormat(0, 1, false, true, new Color(2, 3, 4), new Color(5, 6, 7))),
            new MsgSetEncodings(new int[]{1, 2, 3, 4, 5}),
            new MsgFramebufferUpdateRequest(true, new Rect(new Point(0xFFFF, 0xFFFF), new Size(0xFFFF, 0xFFFF))),
            new MsgKeyEvent(true, 0x12345678L),
            new MsgPointerEvent(0xFF, new Point(0xFFFF, 0xFFFF)),
            new MsgClientCutText("What ever"),
            new MsgFramebufferUpdate(format, fbs),
            new MsgSetColourMapEntries(1, new Color[]{new Color(1, 2, 3), new Color(4, 5, 6), new Color(123, 456, 789)}),
            new MsgBell(),
            new MsgServerCutText("pla pla pla pla plaaaa......"),};
    }

    class ReceiveMsgFactory implements ReceiveMessageFactory {

        Message[] msgs;
        int index;

        ReceiveMsgFactory() {
            index = 0;
            msgs = new Message[]{
                new MsgProtocolVersion(),
                new MsgSecurityTypeList(),
                new MsgClientInit(),
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

        @Override
        public Message nextReceiveMessage() {
            Message msg = null;
            if (index < msgs.length) {
                msg = msgs[index++];
            }
            return msg;
        }
    }

    @Test
    public void testSocketReader() {
        reader = new SocketReader(new ReceiveMsgFactory());
        assertNotNull(reader);
    }

    @Test
    public void testReadOneByOne() throws Throwable {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            ReceiveMsgFactory f = new ReceiveMsgFactory();
            reader = new SocketReader(f);

            loopback.channel2().configureBlocking(true);
            for (int i = 0; i < f.msgs.length; i++) {
                ArrayList<ByteBuffer> list = toBeSent[i].marshal();

                ByteBuffer data = MyByteBufferHelper.arrayListToBuffer(list);
                loopback.channel1().write(data);
                //logger.info("Write " + toBeSent[i]);

                Message msg = reader.read(loopback.channel2());
                assertTrue(data.remaining() == 0);
                //logger.info("Read " + toBeRead[i]);
                assertNotNull(msg);
                assertTrue(msg.isValid());
                assertEquals(toBeSent[i].getId(), msg.getId());
            }
        }
    }

    @Test
    public void testReadAllAtOnce() throws Throwable {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            //Logger logger = Logger.getLogger(TestSocketReaderTest.class.getName());
            ReceiveMsgFactory f = new ReceiveMsgFactory();
            reader = new SocketReader(f);

            for (int i = 0; i < toBeSent.length; i++) {
                ArrayList<ByteBuffer> list = toBeSent[i].marshal();
                loopback.channel1().write(MyByteBufferHelper.arrayListToBuffer(list));
                //logger.info("Write " + toBeSent[i]);
            }

            for (int i = 0; i < f.msgs.length; i++) {
                Message msg = reader.read(loopback.channel2());
                //logger.info("Read " + toBeRead[i]);
                assertNotNull(msg);
                assertTrue(msg.isValid());
                assertEquals(toBeSent[i].getId(), msg.getId());
            }
        }
    }

    @Test
    public void testReadByteByByte() throws Throwable {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            ReceiveMsgFactory f = new ReceiveMsgFactory();
            reader = new SocketReader(f);

            loopback.channel2().configureBlocking(true);

            for (int i = 0; i < f.msgs.length; i++) {
                ArrayList<ByteBuffer> list = toBeSent[i].marshal();
                ByteBuffer data = MyByteBufferHelper.arrayListToBuffer(list);
                Message msg = null;

                while (0 != data.remaining() && null == msg) {
                    byte[] temp = new byte[1];
                    temp[0] = data.get();
                    loopback.channel1().write(ByteBuffer.wrap(temp));
                    msg = reader.read(loopback.channel2());
                }

                assertNotNull(msg);
                assertTrue(msg.isValid());
                assertEquals(toBeSent[i].getId(), msg.getId());
            }
        }
    }
}
