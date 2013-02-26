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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import org.javnce.eventing.EventLoop;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ListenerTest {

    @Before
    public void setUp() throws Exception {
        EventLoop.shutdownAll();
    }

    @After
    public void tearDown() throws Exception {
        assertFalse(EventLoop.exists());
    }

    @Test
    public void testPort() {
        Listener listener = new Listener();
        TestRemoteClientObserver tester = new TestRemoteClientObserver();
        listener.addObserver(tester);
        listener.start();
        assertEquals(5900, tester.port());
        {
            //Second
            Listener listener2 = new Listener();
            TestRemoteClientObserver tester2 = new TestRemoteClientObserver();
            listener2.addObserver(tester2);
            listener2.start();
            int port = tester2.port();
            assertNotEquals(5900, port);
            assertNotEquals(0, port);
            listener2.removeObserver(tester2);
            listener2.shutdown();
        }
        listener.shutdown();
        assertEquals(0, tester.port());
    }

    @Test
    public void testChannel() throws IOException {
        Listener listener = new Listener();
        TestRemoteClientObserver tester = new TestRemoteClientObserver();
        listener.addObserver(tester);
        listener.start();
        int port = tester.port();

        try (SocketChannel ch = SocketChannel.open(new InetSocketAddress(port))) {
            RemoteClient client = tester.client();
            assertNotNull(client);
            client.disconnect();
        }
        listener.shutdown();

    }
}
