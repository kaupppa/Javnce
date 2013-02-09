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

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDN;
import static org.junit.Assert.*;
import org.junit.Test;

public class RemoteServerInfoTest {

    private RemoteDevice createRemoteDevice() throws MalformedURLException, ValidationException {
        UDN udn = null;
        Integer maxAgeSeconds = new Integer(3);
        URL descriptorURL = new URL("http://java.sun.com/index.html");
        byte[] interfaceMacAddress = null;
        InetAddress discoveredOnLocalAddress = null;

        RemoteDeviceIdentity id = new RemoteDeviceIdentity(udn, maxAgeSeconds, descriptorURL, interfaceMacAddress, discoveredOnLocalAddress);
        DeviceType type = null;
        DeviceDetails details = new DeviceDetails("Tadaa",
                new ManufacturerDetails("Pauli Kauppinen", "https://github.com/kaupppa/Javnce"),
                new ModelDetails("Javnce", "An easy screen sharing application", "0.0.0.1"));
        RemoteService service = null;
        RemoteDevice device = new RemoteDevice(id, type, details, service);
        return device;
    }

    @Test
    public void testFactory() throws MalformedURLException, ValidationException {
        RemoteDevice device = createRemoteDevice();

        RemoteServerInfo info = RemoteServerInfo.factory(device, 5900);
        assertNotNull(info);
        assertNotNull(info.getAddress());
        assertNotNull(info.getId());
        assertNotNull(info.getName());
    }
}
