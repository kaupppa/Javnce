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
package org.javnce.upnp;

import java.net.InetSocketAddress;
import java.net.URL;
import org.fourthline.cling.model.meta.RemoteDevice;

/**
 * The Class RemoteServerInfo provides information of the remote service.
 */
public class RemoteServerInfo {

    /** The unique server id. */
    final private String id;
    
    /** The friendly name. */
    final private String name;
    
    /** The server host name. */
    final private String hostname;
    
    /** The server  port. */
    final private int port;

    /**
     * Instantiates a new remote server info.
     *
     * @param id the id
     * @param name the name
     * @param hostname the host name
     * @param port the port
     */
    private RemoteServerInfo(String id, String name, String hostname, int port) {
        this.id = id;
        this.name = name;
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Creates RemoteServerInfo from given RemoteDevice object.
     *
     * @param device the device
     * @param port the port
     * @return the remote server info
     */
    static RemoteServerInfo factory(RemoteDevice device, int port) {
        URL url = device.getIdentity().getDescriptorURL();
        String id = url.toString();
        String name = device.getDetails().getFriendlyName();
        String hostname = url.getHost();

        return new RemoteServerInfo(id, name, hostname, port);
    }

    /**
     * Gets the remote server address.
     *
     * @return the address
     */
    public InetSocketAddress getAddress() {
        return new InetSocketAddress(hostname, port);
    }

    /**
     * Gets the (friendly) name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unique server id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }
}
