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
package org.javnce.vnc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import org.javnce.eventing.ChannelSubscriber;
import org.javnce.eventing.EventLoop;

class Listener extends Thread implements ChannelSubscriber {

    final private EventLoop eventLoop;
    final private VncServerController controller;

    Listener(VncServerController controller) {
        eventLoop = new EventLoop();
        this.controller = controller;
    }

    void shutdown() {
        eventLoop.shutdown();
    }

    private boolean bind(ServerSocketChannel channel, int port) {
        boolean binded = false;
        try {
            channel.socket().bind(new InetSocketAddress(port));
            binded = true;
        } catch (IOException ex) {
            if (0 != port) {
                binded = bind(channel, 0);
            } else {
                EventLoop.fatalError(this, ex);
            }
        }

        return binded;
    }

    @Override
    public void run() {
        try (ServerSocketChannel channel = ServerSocketChannel.open()) {
            channel.configureBlocking(false);
            int port = 5900;
            if (!bind(channel, port)) {
                controller.setPort(0);
                return;
            }
            eventLoop.subscribe(channel, this, channel.validOps());
            controller.setPort(channel.socket().getLocalPort());
            eventLoop.process();
        } catch (Throwable ex) {
            controller.setPort(0);
            EventLoop.fatalError(this, ex);
        }
    }

    @Override
    public void channel(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                RemoteClient client = new RemoteClient(channel.accept());
                controller.addClient(client);
            }
        } catch (Throwable ex) {
            EventLoop.fatalError(this, ex);
        }
    }
}
