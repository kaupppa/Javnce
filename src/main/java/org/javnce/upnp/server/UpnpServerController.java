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

package org.javnce.upnp.server;

/**
 * The Class UpnpServerController provides UPnP server control.
 */
public class UpnpServerController {
    
    /** The controller instance. */
    final static private UpnpServerController controller = new UpnpServerController();
    
    /** The UPnP server. */
    private UpnPServer server;

    /**
     * Instance.
     *
     * @return the UPnP server controller
     */
    public static UpnpServerController instance() {
        return controller;
    }

    /**
     * Start.
     *
     * @param name the name
     * @param port the port
     */
    public void start(String name, int port)
    {
        shutdown();
        server = new UpnPServer(name, port);
        server.start();
    }

    /**
     * Shutdown.
     */
    synchronized public void shutdown() {
        if (null != server)
        {
            server.shutdown();
        }
    }
}
