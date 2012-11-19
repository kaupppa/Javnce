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

import org.fourthline.cling.binding.annotations.*;

/**
 * The Class VncService is the UPnP service description.
 */
@UpnpService(
    serviceId = @UpnpServiceId("VncService"),
    serviceType = @UpnpServiceType(value = "VncService", version = 1))

class VncService {

    /**
     * The state variable port .
     */
    @UpnpStateVariable(sendEvents = false)
    private int port = 0;

    /**
     * The UPnP action.
     *
     * @return the port
     */
    @UpnpAction(out = @UpnpOutputArgument(name = "vncPort"))
    int getPort() {
        return port;
    }

    /**
     * Sets the port.
     *
     * @param port the new port
     */
    void setPort(int port) {
        this.port = port;
    }
}
