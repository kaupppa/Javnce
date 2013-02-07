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

/**
 * The Class Listener is a thread that listens for incoming connections.
 */
class Listener extends Thread implements ChannelSubscriber {

    /**
     * The event loop.
     */
    final private EventLoop eventLoop;
    /**
     * The observer collection.
     */
    final private RemoteClientObserverCollection observers;

    /**
     * Instantiates a new listener.
     *
     * @param observer the observer
     */
    Listener() {
        eventLoop = new EventLoop();
        observers = new RemoteClientObserverCollection();
        setName("Javnce-Listener");
    }

    /**
     * Adds the observer.
     *
     * @param observer the observer
     */
    public void addObserver(RemoteClientObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes the observer.
     *
     * @param observer the observer
     */
    public void removeObserver(RemoteClientObserver observer) {
        observers.remove(observer);
    }

    /**
     * Shutdown the thread.
     */
    void shutdown() {
        eventLoop.shutdown();
    }

    /**
     * Bind to given port.
     *
     * @param channel the channel
     * @param port the port
     * @return true, if successful
     */
    private boolean bind(ServerSocketChannel channel, int port) {
        boolean binded = false;
        try {
            channel.socket().bind(new InetSocketAddress(port));
            binded = true;
        } catch (IOException ex) {
            if (0 != port) {
                // Try to bind to autoport
                binded = bind(channel, 0);
            } else {
                EventLoop.fatalError(this, ex);
            }
        }
        return binded;
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        try (ServerSocketChannel channel = ServerSocketChannel.open()) {
            channel.configureBlocking(false);
            int port = 5900;
            if (!bind(channel, port)) {
                observers.portChanged(0);
                return;
            }
            eventLoop.subscribe(channel, this, channel.validOps());
            observers.portChanged(channel.socket().getLocalPort());
            eventLoop.process();
        } catch (Throwable ex) {
            EventLoop.fatalError(this, ex);
        }
        observers.portChanged(0);
    }

    /* (non-Javadoc)
     * @see org.javnce.eventing.ChannelSubscriber#channel(java.nio.channels.SelectionKey)
     */
    @Override
    public void channel(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                RemoteClient client = new RemoteClient(channel.accept());
                observers.vncClientChanged(client);
            }
        } catch (Throwable ex) {
            EventLoop.fatalError(this, ex);
        }
    }
}
