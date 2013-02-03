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
package org.javnce.upnp.client;

/**
 * An interface for receiving server information from UPnP client.
 */
public interface UpnpClientObserver {

    /**
     * Called when server found.
     *
     * @param server the server
     */
    void serverFound(RemoteServerInfo server);

    /**
     * Called when server is lost.
     *
     * @param server the server
     */
    void serverLost(RemoteServerInfo server);
}
