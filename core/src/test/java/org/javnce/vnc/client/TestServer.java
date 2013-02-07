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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.javnce.eventing.EventLoop;

class TestServer {

    ServerSocketChannel serverChannel;
    SocketChannel channel;
    Thread thread;

    TestServer() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    channel = serverChannel.accept();
                    serverChannel.close();
                    serverChannel = null;
                } catch (IOException ex) {
                    EventLoop.fatalError(this, ex);
                }
            }
        });
        init();
    }

    private void init() {
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), 0));

            thread.start();
        } catch (IOException ex) {
            EventLoop.fatalError(this, ex);
        }
    }

    void accept() {
        try {
            thread.join();
        } catch (InterruptedException ex) {
            EventLoop.fatalError(this, ex);
        }
    }

    void close() {
        try {
            if (null != serverChannel) {
                serverChannel.close();
            }
            if (null != channel) {
                channel.close();
            }
        } catch (IOException ex) {
            EventLoop.fatalError(this, ex);
        }
    }

    InetSocketAddress address() {
        return (InetSocketAddress) serverChannel.socket().getLocalSocketAddress();
    }
}
