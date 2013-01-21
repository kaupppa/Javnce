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

import org.javnce.upnp.server.UpnpServerController;
import org.javnce.vnc.server.VncServerController;

/**
 * The UpnpController and VncController getter class.
 */
public class ServerConfiguration {

    /**
     * The configuration.
     */
    final static private ServerConfiguration configuration = new ServerConfiguration();

    /**
     * Instance.
     *
     * @return the server configuration
     */
    public static synchronized ServerConfiguration instance() {
        return configuration;
    }

    /**
     * Gets the UPnP controller.
     *
     * @return the upnp controller
     */
    public UpnpServerController getUpnpController() {
        return UpnpServerController.instance();
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        UpnpServerController.instance().shutdown();
        VncServerController.instance().shutdown();
    }

    /**
     * Gets the VNC controller.
     *
     * @return the vnc controller
     */
    public VncServerController getVncController() {
        return VncServerController.instance();
    }
}
