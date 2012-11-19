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
 * The Class UpnpClientController for handling UPnP Client.
 */
public class UpnpClientController {

    /**
     * The controller instance.
     */
    final static private UpnpClientController controller = new UpnpClientController();
    /**
     * The client.
     */
    private UpnpClient client;

    /**
     * Instance.
     *
     * @return the UPnP client controller
     */
    public static UpnpClientController instance() {
        return controller;
    }

    /**
     * Instantiates a new UPnP client controller.
     */
    private UpnpClientController() {
        client = null;
    }

    /**
     * Shutdown the UPnP Client.
     */
    synchronized public void shutdown() {
        if (null != client) {
            client.shutdown();
            client = null;
        }
    }

    /**
     * Start.
     *
     * @param observer the observer
     */
    synchronized public void start(UpnpClientObserver observer) {
        shutdown();
        client = new UpnpClient(observer);
        client.start();
    }
}
