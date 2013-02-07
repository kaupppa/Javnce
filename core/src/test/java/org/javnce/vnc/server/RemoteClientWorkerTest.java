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

import org.javnce.eventing.EventLoop;
import org.javnce.eventing.LoopbackChannelPair;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Test;

public class RemoteClientWorkerTest {

    @After
    public void tearDown() throws Exception {
        assertFalse(EventLoop.exists());
    }

    @Test
    public void testConnect() throws Throwable {

        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {

            RemoteClient client = new RemoteClient(sockets.channel1());
            RemoteClientWorker worker = new RemoteClientWorker(client);

            worker.start();
            worker.shutdown();

            Thread.sleep(100l);
            assertEquals(RemoteClient.State.Disconnected, client.state());
        }
    }

    @Test
    public void testShutdown() throws Throwable {

        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {

            RemoteClient client = new RemoteClient(sockets.channel1());
            RemoteClientWorker worker = new RemoteClientWorker(client);

            //No start
            worker.shutdown();
            assertEquals(RemoteClient.State.Disconnected, client.state());
        }
    }

    @Test
    public void testDisconnectBeforeRun() throws Throwable {

        try (LoopbackChannelPair sockets = new LoopbackChannelPair()) {

            RemoteClient client = new RemoteClient(sockets.channel1());
            RemoteClientWorker worker = new RemoteClientWorker(client);

            client.disconnect();
            worker.start();
            worker.shutdown();

            Thread.sleep(100l);
            assertEquals(RemoteClient.State.Disconnected, client.state());
        }
    }
}
