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

/**
 * The Class RemoteClient handles the state of one client.
 *
 * The RemoteClient is thread safe.
 */
public class RemoteClient {

    /**
     * The client channel.
     */
    private SocketChannel channel;
    /**
     * The state.
     */
    private State state;
    /**
     * The observer collection.
     */
    final private RemoteClientObserverCollection observers;
    /**
     * The lock.
     */
    final private Object lock;
    /**
     * The worker.
     */
    private RemoteClientWorker worker;
    /**
     * The client's address.
     */
    private String address;

    /**
     * The client State.
     */
    public enum State {

        /**
         * The client connection is waiting for {@link #connect()} or
         * {@link #disconnect()}.
         */
        PendingConnection,
        /**
         * The client is connected and protocol is up and running.
         */
        Connected,
        /**
         * The client is disconnected.
         */
        Disconnected
    }

    /**
     * Instantiates a new remote client.
     *
     * @param channel the channel
     */
    public RemoteClient(SocketChannel channel) {
        this.channel = channel;
        state = State.PendingConnection;
        lock = new Object();
        observers = new RemoteClientObserverCollection();
        try {
            address = ((InetSocketAddress) channel.getRemoteAddress())
                    .getAddress()
                    .getCanonicalHostName();
        } catch (IOException ex) {
            EventLoop.fatalError(this, ex);
        }
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
     * Client address getter.
     *
     * @return the string
     */
    public String address() {
        String temp;
        synchronized (lock) {
            temp = address;
        }
        return temp;
    }

    /**
     * The client state getter.
     *
     * @return the state
     */
    public State state() {
        State temp;

        synchronized (lock) {
            temp = state;
        }
        return temp;
    }

    /**
     * Sets the client state.
     *
     * @param state the new state
     */
    private void setState(State state) {
        Boolean changed = false;

        synchronized (lock) {
            if (this.state != state) {
                this.state = state;
                changed = true;
            }
        }
        if (changed) {
            //Outside lock as observers have it's own locks
            observers.vncClientChanged(this);
        }
    }

    /**
     * Connect the client.
     *
     * Method launches RemoteClientWorker thread.
     */
    public void connect() {
        boolean allowConnect = false;
        if (State.PendingConnection == state()) {
            synchronized (lock) {
                if (null != channel && null == worker) {
                    worker = new RemoteClientWorker(this);
                    allowConnect = true;
                } else {
                    EventLoop.fatalError(this, new UnsupportedOperationException("Operation not supported"));
                }
            }
        }
        if (allowConnect) {
            setState(State.Connected);
            worker.start();
        }

    }

    /**
     * Disconnect the client.
     */
    public void disconnect() {
        if (State.Disconnected != state()) {
            setState(State.Disconnected);
            synchronized (lock) {
                if (null != worker) {
                    worker.shutdown();
                    worker = null;
                }
                if (null != channel) {
                    try {
                        channel.close();
                    } catch (IOException ex) {
                    }
                    channel = null;
                }
            }
        }

    }

    /**
     * Client channel getter.
     *
     * @return the socket channel
     */
    public SocketChannel giveChannel() {
        SocketChannel temp;
        synchronized (lock) {
            temp = channel;
            channel = null;
        }
        return temp;
    }
}
