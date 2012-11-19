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

import java.net.InetSocketAddress;

/**
 * The Class RemoteServerInfo provides information of remote Javnce.
 */
public class RemoteServerInfo {

    /**
     * The address of server.
     */
    final InetSocketAddress address;
    /**
     * The name of server.
     */
    final String name;

    /**
     * Instantiates a new remote server info.
     *
     * @param address the address
     * @param name the name
     */
    public RemoteServerInfo(InetSocketAddress address, String name) {
        this.address = address;
        this.name = name;
    }

    /**
     * Gets the server address.
     *
     * @return the address
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     * Gets the server name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
