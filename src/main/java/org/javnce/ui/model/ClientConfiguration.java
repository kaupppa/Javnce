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
package org.javnce.ui.model;

import org.javnce.upnp.client.UpnpClientController;
import org.javnce.vnc.client.VncClientController;

/**
 * The UpnpController and VncController getter class.
 */
public class ClientConfiguration {

    /**
     * The configuration.
     */
    final static private ClientConfiguration configuration = new ClientConfiguration();

    /**
     * Instance.
     *
     * @return the client configuration
     */
    public static ClientConfiguration instance() {
        return configuration;
    }

    /**
     * Gets the UPnP controller.
     *
     * @return the upnp controller
     */
    public UpnpClientController getUpnpController() {
        return UpnpClientController.instance();
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        UpnpClientController.instance().shutdown();
        VncClientController.instance().shutdown();
    }

    /**
     * Gets the VNC controller.
     *
     * @return the vnc controller
     */
    public VncClientController getVncController() {
        return VncClientController.instance();
    }
}
