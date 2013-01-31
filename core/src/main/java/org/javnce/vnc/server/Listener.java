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

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import org.javnce.eventing.ChannelSubscriber;
import org.javnce.eventing.EventLoop;

/**
 * The socket listener.
 */
class Listener extends Thread implements ChannelSubscriber {

    /**
     * The server socket channel.
     */
    private ServerSocketChannel channel;
    /**
     * The event loop.
     */
    final private EventLoop eventLoop;
    /**
     * The observer.
     */
    final private VncServerObserver observer;

    /**
     * Instantiates a new listener.
     *
     * @param observer the observer
     */
    Listener(VncServerObserver observer) {
        eventLoop = new EventLoop();
        this.observer = observer;
        setName("Javne-Listener");
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        try {
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            int port = 5900;
            channel.socket().bind(new InetSocketAddress(port));
            eventLoop.subscribe(channel, this, channel.validOps());

            observer.listening(port);
            eventLoop.process();
        } catch (Throwable ex) {
            EventLoop.fatalError(this, ex);
        }

        try {
            channel.close();
        } catch (Throwable ex) {
            EventLoop.fatalError(this, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.javnce.eventing.ChannelSubscriber#channel(java.nio.channels.SelectionKey)
     */
    @Override
    public void channel(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                observer.newConnection(channel.accept());
            }
        } catch (Throwable ex) {
            EventLoop.fatalError(this, ex);
        }
    }

    /**
     * Shutdown.
     */
    void shutdown() {
        eventLoop.shutdown();
    }
}
