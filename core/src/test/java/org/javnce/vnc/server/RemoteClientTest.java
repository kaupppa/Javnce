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

import java.util.concurrent.atomic.AtomicBoolean;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.LoopbackChannelPair;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Test;

public class RemoteClientTest {

    @After
    public void tearDown() throws Exception {
        assertFalse(EventLoop.exists());
    }

    @Test
    public void testAddress() throws Throwable {
        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {

            RemoteClient client = new RemoteClient(sockets.channel1());
            assertFalse(client.address().isEmpty());
        }
    }

    @Test
    public void testState() throws Throwable {
        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {

            RemoteClient client = new RemoteClient(sockets.channel1());
            assertEquals(RemoteClient.State.PendingConnection, client.state());

            client.connect();
            assertEquals(RemoteClient.State.Connected, client.state());

            client.disconnect();
            assertEquals(RemoteClient.State.Disconnected, client.state());
        }
    }

    @Test
    public void testGiveChannel() throws Throwable {
        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {

            RemoteClient client = new RemoteClient(sockets.channel1());
            assertEquals(sockets.channel1(), client.giveChannel());

            assertNull(client.giveChannel());
            client.disconnect();
        }
    }

    @Test
    public void testObserver() throws Throwable {
        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {

            RemoteClient client = new RemoteClient(sockets.channel1());
            assertEquals(RemoteClient.State.PendingConnection, client.state());

            final AtomicBoolean called = new AtomicBoolean(false);
            RemoteClientObserver observer = new RemoteClientObserver() {
                @Override
                public void vncClientChanged(RemoteClient client) {
                    called.set(true);
                }

                @Override
                public void portChanged(int port) {
                }
            };
            client.addObserver(observer);
            assertFalse(called.get());
            client.connect();
            assertTrue(called.get());

            client.removeObserver(observer);
            client.disconnect();
        }
    }
}
