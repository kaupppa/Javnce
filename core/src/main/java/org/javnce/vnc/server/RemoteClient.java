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

public class RemoteClient {

    private SocketChannel channel;
    private State state;
    private VncServerController controller;
    final private Object lock;
    private RemoteClientWorker worker;
    private String address;

    public enum State {

        PendingConnection,
        Connected,
        Disconnected
    }

    public RemoteClient(SocketChannel channel) {
        this.channel = channel;
        state = State.PendingConnection;
        lock = new Object();
        try {
            address = ((InetSocketAddress) channel.getRemoteAddress())
                    .getAddress()
                    .getCanonicalHostName();
        } catch (IOException ex) {
            address = "I don't know the address";
            EventLoop.fatalError(this, ex);
        }
    }

    void setController(VncServerController controller) {
        synchronized (lock) {
            this.controller = controller;
        }
    }

    public String address() {
        String temp;
        synchronized (lock) {
            temp = address;
        }
        return temp;
    }

    VncServerController getController() {
        VncServerController temp;
        synchronized (lock) {
            temp = controller;
        }
        return temp;

    }

    public State state() {
        State temp;

        synchronized (lock) {
            temp = state;
        }
        return temp;
    }

    void setState(State state) {
        Boolean changed = false;

        synchronized (lock) {
            if (this.state != state) {
                this.state = state;
                changed = true;
            }
        }
        VncServerController cntlr = getController();
        if (changed && null != cntlr) {
            cntlr.clientChanged(this);
        }
    }

    public void connect() {

        State temp = state();
        synchronized (lock) {
            if (State.PendingConnection == temp && null != channel && null == worker) {
                worker = new RemoteClientWorker(this);
                worker.start();
                temp = State.Connected;
            } else {
                EventLoop.fatalError(this, new UnsupportedOperationException("Operation not supported"));
            }
        }
        setState(temp);
    }

    public void disconnect() {
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
        setState(State.Disconnected);
    }

    public SocketChannel giveChannel() {
        SocketChannel temp;
        synchronized (lock) {
            temp = channel;
            channel = null;
        }
        return temp;
    }
}
