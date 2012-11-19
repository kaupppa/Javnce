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

import java.nio.channels.SocketChannel;

/**
 * An interface for observing VNC server.
 */
public interface VncServerObserver {

    /**
     * The listening is called when Server socket is opened.
     *
     * @param port the port of listener socket
     */
    public void listening(int port);

    /**
     * The connectionClosed is called when client is disconnected.
     *
     * @param userData the user data is same as given in 
     * {@link VncServerController#acceptConnection(org.javnce.vnc.server.VncServerObserver, java.nio.channels.SocketChannel, boolean, java.lang.Object) }
     */
    public void connectionClosed(Object userData);

    /**
     * The newConnection is called when client is connecting. Observer needs to
     * call {@link VncServerController#acceptConnection(org.javnce.vnc.server.VncServerObserver, java.nio.channels.SocketChannel, boolean, java.lang.Object)
     * }.
     *
     * @param channel the channel
     */
    public void newConnection(SocketChannel channel);
}
