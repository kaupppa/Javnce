/*
 * Copyright (C) 2013 Pauli Kauppinen
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
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.javnce.vnc.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javnce.eventing.Event;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.EventSubscriber;
import org.javnce.eventing.LoopbackChannelPair;
import org.javnce.rfb.messages.Message;
import org.javnce.rfb.messages.MsgClientInit;
import org.javnce.rfb.messages.MsgFramebufferUpdateRequest;
import org.javnce.rfb.messages.MsgKeyEvent;
import org.javnce.rfb.messages.MsgPointerEvent;
import org.javnce.rfb.messages.MsgProtocolVersion;
import org.javnce.rfb.messages.MsgSelectedSecurityType;
import org.javnce.rfb.messages.MsgSetEncodings;
import org.javnce.rfb.messages.MsgSetPixelFormat;
import org.javnce.rfb.types.Color;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.SecurityType;
import org.javnce.rfb.types.Size;
import org.javnce.rfb.types.Version;
import org.javnce.vnc.common.FbEncodingsEvent;
import org.javnce.vnc.common.FbFormatEvent;
import org.javnce.vnc.common.FbRequestEvent;
import org.javnce.vnc.common.KeyEvent;
import org.javnce.vnc.common.PointerEvent;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProtocolHandlerTest {

    class Tester implements EventSubscriber {

        EventLoop eventLoop;
        ArrayList<Event> events;
        Thread thread;

        Tester() {
            eventLoop = new EventLoop();
            eventLoop.moveToNewChildGroup();
            events = new ArrayList<>();
            thread = new Thread(eventLoop);
        }

        @Override
        public void event(Event event) {
            events.add(event);
        }
    }

    void sleep() {
        try {
            Thread.yield();
            Thread.sleep(20l);
            Thread.yield();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProtocolHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Before
    public void setUp() throws Exception {
        EventLoop.shutdownAll();
    }

    @After
    public void tearDown() throws Exception {
        assertFalse(EventLoop.exists());
    }

    @Test
    public void testInit() throws Exception {
        Tester tester = new Tester();
        ProtocolHandler handler = new ProtocolHandler(tester.eventLoop);

        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {
            handler.init(sockets.channel1());
            tester.thread.start();

            Message[] messages = new Message[]{
                new MsgProtocolVersion(new Version(3, 8)),
                new MsgSelectedSecurityType(SecurityType.None),
                new MsgClientInit(true)};

            for (int i = 0; i < messages.length; i++) {
                ArrayList<ByteBuffer> buffers = messages[i].marshal();
                sockets.channel2().write(buffers.toArray(new ByteBuffer[buffers.size()]));
                sleep();
            }
            assertTrue(sockets.channel1().isOpen());
        }
        tester.eventLoop.shutdown();
    }

    @Test
    public void testWrongNumber() throws Exception {
        Tester tester = new Tester();
        ProtocolHandler handler = new ProtocolHandler(tester.eventLoop);

        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {

            handler.init(sockets.channel1());
            tester.thread.start();

            Message[] messages = new Message[]{
                new MsgProtocolVersion(new Version(3, 7)),
                new MsgSelectedSecurityType(SecurityType.None),
                new MsgClientInit(true)};

            try {
                for (int i = 0; i < messages.length; i++) {
                    ArrayList<ByteBuffer> buffers = messages[i].marshal();
                    sockets.channel2().write(buffers.toArray(new ByteBuffer[buffers.size()]));
                    sleep();
                }
            } catch (Exception e) {
                //Should get broken pipe
            }
            //Should stop
            tester.thread.join(100);
            assertFalse(sockets.channel1().isOpen());
        }
        tester.eventLoop.shutdown();
    }

    @Test
    public void testWrongSecType() throws Exception {
        Tester tester = new Tester();
        ProtocolHandler handler = new ProtocolHandler(tester.eventLoop);

        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {

            handler.init(sockets.channel1());
            tester.thread.start();

            Message[] messages = new Message[]{
                new MsgProtocolVersion(new Version(3, 8)),
                new MsgSelectedSecurityType(SecurityType.Invalid),
                new MsgClientInit(true)};

            for (int i = 0; i < messages.length; i++) {
                try {
                    ArrayList<ByteBuffer> buffers = messages[i].marshal();
                    sockets.channel2().write(buffers.toArray(new ByteBuffer[buffers.size()]));
                    sleep();
                } catch (Exception e) {
                    //Should get broken pipe
                    break;
                }
            }
            //Should stop
            tester.thread.join(100);

            assertFalse(sockets.channel1().isOpen());
        }
        tester.eventLoop.shutdown();
    }

    @Test
    public void testEvent() throws Exception {
        Tester tester = new Tester();
        tester.eventLoop.subscribe(KeyEvent.eventId(), tester);
        tester.eventLoop.subscribe(PointerEvent.eventId(), tester);
        tester.eventLoop.subscribe(FbFormatEvent.eventId(), tester);
        tester.eventLoop.subscribe(FbEncodingsEvent.eventId(), tester);
        tester.eventLoop.subscribe(FbRequestEvent.eventId(), tester);

        ProtocolHandler handler = new ProtocolHandler(tester.eventLoop);

        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {
            handler.init(sockets.channel1());
            tester.thread.start();

            Message[] messages = new Message[]{
                new MsgProtocolVersion(new Version(3, 8)),
                new MsgSelectedSecurityType(SecurityType.None),
                new MsgClientInit(true),
                new MsgKeyEvent(true, 0x12345678L),
                new MsgPointerEvent(0xFF, new Point(0xFFFF, 0xFFFF)),
                new MsgSetPixelFormat(new PixelFormat(0, 1, false, true, new Color(2, 3, 4), new Color(5, 6, 7))),
                new MsgSetEncodings(new int[]{1, 2, 3, 4, 5}),
                new MsgFramebufferUpdateRequest(true, new Rect(new Point(0xFFFF, 0xFFFF), new Size(0xFFFF, 0xFFFF)))
            };

            for (int i = 0; i < messages.length; i++) {
                ArrayList<ByteBuffer> buffers = messages[i].marshal();
                sockets.channel2().write(buffers.toArray(new ByteBuffer[buffers.size()]));
                sleep();
            }

            assertTrue(5 == tester.events.size());
            assertTrue(tester.events.get(0) instanceof KeyEvent);
            assertTrue(tester.events.get(1) instanceof PointerEvent);
            assertTrue(tester.events.get(2) instanceof FbFormatEvent);
            assertTrue(tester.events.get(3) instanceof FbEncodingsEvent);
            assertTrue(tester.events.get(4) instanceof FbRequestEvent);

        }
        tester.eventLoop.shutdown();
    }
}