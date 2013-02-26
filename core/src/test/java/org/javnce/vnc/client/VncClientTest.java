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
package org.javnce.vnc.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.javnce.eventing.EventLoop;
import org.javnce.rfb.messages.Message;
import org.javnce.rfb.messages.MsgProtocolVersion;
import org.javnce.rfb.messages.MsgSecurityResult;
import org.javnce.rfb.messages.MsgSecurityTypeList;
import org.javnce.rfb.messages.MsgServerInit;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.SecurityType;
import org.javnce.rfb.types.Size;
import org.javnce.rfb.types.Version;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VncClientTest {

    @Before
    public void setUp() throws Exception {
        EventLoop.shutdownAll();
    }

    @After
    public void tearDown() throws Exception {
        assertFalse(EventLoop.exists());
    }

    void sleep() {
        try {
            Thread.yield();
            Thread.sleep(90l);
            Thread.yield();
        } catch (InterruptedException ex) {
            EventLoop.fatalError(this, ex);
        }
    }

    @Test
    public void testShutdown() {

        TestServer server = new TestServer();
        TestObserver tester = new TestObserver();

        VncClient client = new VncClient(server.address(), tester);
        client.start();

        server.accept();
        client.shutdown();
        sleep();
        assertTrue(tester.connectionClosed);
        server.close();
    }

    @Test
    public void testInit() {


        TestServer server = new TestServer();
        TestObserver tester = new TestObserver();

        VncClient client = new VncClient(server.address(), tester);
        client.start();

        server.accept();
        assertFalse(tester.initFramebuffer);
        Message[] messages = new Message[]{
            new MsgProtocolVersion(new Version(3, 8)),
            new MsgSecurityTypeList(new SecurityType[]{SecurityType.None}),
            new MsgSecurityResult(true),
            new MsgServerInit(PixelFormat.createRGB565(), new Size(10, 10), "Tadaa"),};

        for (int i = 0; i < messages.length; i++) {
            ArrayList<ByteBuffer> buffers = messages[i].marshal();
            try {
                server.channel.write(buffers.toArray(new ByteBuffer[buffers.size()]));
            } catch (IOException ex) {
                EventLoop.fatalError(this, ex);
            }
            sleep();
        }
        assertTrue(tester.initFramebuffer);
        assertFalse(tester.connectionClosed);

        client.shutdown();
        server.close();
    }

    @Test
    public void testWrongVersion() {


        TestServer server = new TestServer();
        TestObserver tester = new TestObserver();

        VncClient client = new VncClient(server.address(), tester);
        client.start();

        server.accept();
        assertFalse(tester.initFramebuffer);
        Message[] messages = new Message[]{
            new MsgProtocolVersion(new Version(3, 7)),
            new MsgSecurityTypeList(new SecurityType[]{SecurityType.None}),
            new MsgSecurityResult(true),
            new MsgServerInit(PixelFormat.createRGB565(), new Size(10, 10), "Tadaa"),};

        for (int i = 0; i < messages.length; i++) {
            ArrayList<ByteBuffer> buffers = messages[i].marshal();
            try {
                server.channel.write(buffers.toArray(new ByteBuffer[buffers.size()]));
            } catch (IOException e) {
                break;
            }
            sleep();
        }
        assertFalse(tester.initFramebuffer);
        assertTrue(tester.connectionClosed);

        client.shutdown();
        server.close();
    }

    @Test
    public void testWrongSecurityType() {

        TestServer server = new TestServer();
        TestObserver tester = new TestObserver();

        VncClient client = new VncClient(server.address(), tester);
        client.start();

        server.accept();
        assertFalse(tester.initFramebuffer);
        Message[] messages = new Message[]{
            new MsgProtocolVersion(new Version(3, 8)),
            new MsgSecurityTypeList(new SecurityType[]{SecurityType.Invalid}),
            new MsgSecurityResult(true),
            new MsgServerInit(PixelFormat.createRGB565(), new Size(10, 10), "Tadaa"),};

        for (int i = 0; i < messages.length; i++) {
            ArrayList<ByteBuffer> buffers = messages[i].marshal();
            try {
                server.channel.write(buffers.toArray(new ByteBuffer[buffers.size()]));
            } catch (IOException e) {
                break;
            }
            sleep();
        }
        assertFalse(tester.initFramebuffer);
        assertTrue(tester.connectionClosed);

        client.shutdown();
        server.close();
    }

    @Test
    public void testSecurityTypeFailure() {

        TestServer server = new TestServer();
        TestObserver tester = new TestObserver();

        VncClient client = new VncClient(server.address(), tester);
        client.start();

        server.accept();
        assertFalse(tester.initFramebuffer);
        Message[] messages = new Message[]{
            new MsgProtocolVersion(new Version(3, 8)),
            new MsgSecurityTypeList("Failure test"),
            new MsgSecurityResult(true),
            new MsgServerInit(PixelFormat.createRGB565(), new Size(10, 10), "Tadaa"),};

        for (int i = 0; i < messages.length; i++) {
            ArrayList<ByteBuffer> buffers = messages[i].marshal();
            try {
                server.channel.write(buffers.toArray(new ByteBuffer[buffers.size()]));
            } catch (IOException e) {
                break;
            }
            sleep();
        }
        assertFalse(tester.initFramebuffer);
        assertTrue(tester.connectionClosed);

        client.shutdown();
        server.close();
    }

    @Test
    public void testWrongSecurityResult() {

        TestServer server = new TestServer();
        TestObserver tester = new TestObserver();

        VncClient client = new VncClient(server.address(), tester);
        client.start();

        server.accept();
        assertFalse(tester.initFramebuffer);
        Message[] messages = new Message[]{
            new MsgProtocolVersion(new Version(3, 8)),
            new MsgSecurityTypeList(new SecurityType[]{SecurityType.None}),
            new MsgSecurityResult("Failure test"),
            new MsgServerInit(PixelFormat.createRGB565(), new Size(10, 10), "Tadaa"),};

        for (int i = 0; i < messages.length; i++) {
            ArrayList<ByteBuffer> buffers = messages[i].marshal();
            try {
                server.channel.write(buffers.toArray(new ByteBuffer[buffers.size()]));
            } catch (IOException e) {
                break;
            }
            sleep();
        }
        assertFalse(tester.initFramebuffer);
        assertTrue(tester.connectionClosed);

        client.shutdown();
        server.close();
    }

    @Test
    public void testEvents() {

        TestServer server = new TestServer();
        TestObserver tester = new TestObserver();

        VncClient client = new VncClient(server.address(), tester);
        client.start();

        server.accept();
        assertFalse(tester.initFramebuffer);
        Message[] messages = new Message[]{
            new MsgProtocolVersion(new Version(3, 8)),
            new MsgSecurityTypeList(new SecurityType[]{SecurityType.None}),
            new MsgSecurityResult(true),
            new MsgServerInit(PixelFormat.createRGB565(), new Size(10, 10), "Tadaa"),};

        for (int i = 0; i < messages.length; i++) {
            ArrayList<ByteBuffer> buffers = messages[i].marshal();
            try {
                server.channel.write(buffers.toArray(new ByteBuffer[buffers.size()]));
            } catch (IOException ex) {
                EventLoop.fatalError(this, ex);
            }
            sleep();
        }
        assertTrue(tester.initFramebuffer);
        assertFalse(tester.connectionClosed);

        VncClientController.pointerEvent(0, 0, 0);
        VncClientController.keyEvent(true, 0);
        VncClientController.setFormat(PixelFormat.createRGB565());
        VncClientController.requestFramebuffer(false, new Rect(0, 0, 10, 10));
        sleep();

        assertFalse(tester.connectionClosed);

        client.shutdown();
        server.close();
    }
}
