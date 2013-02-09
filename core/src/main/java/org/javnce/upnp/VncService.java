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

import org.fourthline.cling.binding.annotations.*;

/**
 * The Class VncService is the UPnP service implemented as Cling's annotated service.
 */
@UpnpService( 
        serviceId = @UpnpServiceId(Upnp.serviceName),
        serviceType = @UpnpServiceType(value = Upnp.serviceName, version = Upnp.version))

@UpnpStateVariables(
    {
        @UpnpStateVariable( name = "A_ARG_TYPE_" + Upnp.argumentName,
                            defaultValue = "0",
                            sendEvents = false,
                            datatype = "i4")
    })
public class VncService {

    /**
     * The vnc port.
     */
    private int port;

    /**
     * Instantiates a new VNC UPnP service.
     */
    public VncService() {
        port = 0;
    }

    /**
     * The port getter action.
     *
     * @return the int
     */
    @UpnpAction(
            name = Upnp.actionName, 
            out = @UpnpOutputArgument(name = Upnp.argumentName))
    public int port() {
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
