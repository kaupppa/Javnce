/*
 * Copyright (C) 2013 Pauli Kauppinen
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
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.javnce.upnp;

import org.fourthline.cling.model.types.ServiceId;
import org.fourthline.cling.model.types.UDAServiceId;

/**
 * The Class Upnp provides constants for UPnP.
 */
class Upnp {

    /**
     * The UPnP service name.
     */
    public final static String serviceName = "VncServer";
    /**
     * The UPnP device name.
     */
    public final static String deviceName = "JavnceServer";
    /**
     * The UPnP device and service version.
     */
    public final static int version = 1;
    /**
     * The name of only action.
     */
    public final static String actionName = "getVncServerPort";
    /**
     * The name of out argument from only action.
     */
    public final static String argumentName = "Port";
    /**
     * The UPnP serviceId.
     */
    public final static ServiceId serviceId = new UDAServiceId(Upnp.serviceName);
}
