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

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.*;
import org.fourthline.cling.binding.annotations.*;
import org.fourthline.cling.model.*;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.*;
import org.javnce.eventing.EventLoop;

/**
 * The Class UpnPServer provides the UPnP device and service.
 */
public class UpnPServer implements Runnable {

    /**
     * The friendly name.
     */
    private final String name;
    /**
     * The VNC port.
     */
    private final int port;
    /**
     * The UPnP service.
     */
    private UpnpService upnpService;
    /**
     * The upnp killer.
     */
    private Thread killer;

    /**
     * Instantiates a new UPnP server.
     *
     * @param name the name
     * @param port the port
     */
    public UpnPServer(String name, int port) {
        this.name = name;
        this.port = port;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        killer = new Thread() {
            @Override
            public void run() {
                killMe();
            }
        };

        try {
            Runtime.getRuntime().addShutdownHook(killer);
            upnpService = new UpnpServiceImpl();
            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(createDevice());
        } catch (Throwable ex) {
            EventLoop.fatalError(this, ex);
        }
    }

    /**
     * Provides salt.
     *
     * @return the string
     */
    private static String salt() {
        String salt = "Javnce";
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());

            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            salt += sb.toString();
        } catch (Throwable ex) {
        }
        return salt;
    }

    /**
     * Creates the UPnP device.
     *
     * @return the local device
     * @throws ValidationException the validation exception
     * @throws LocalServiceBindingException the local service binding exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private LocalDevice createDevice() throws ValidationException, LocalServiceBindingException, IOException {


        DeviceIdentity identity = new DeviceIdentity(UDN.uniqueSystemIdentifier(salt()));

        DeviceType type = new UDADeviceType(Upnp.deviceName, Upnp.version);

        DeviceDetails details = new DeviceDetails(name,
                new ManufacturerDetails("Pauli Kauppinen", "https://github.com/kaupppa/Javnce"),
                new ModelDetails("Javnce", "An easy screen sharing application", "0.0.0.1"));
        Icon icon = null;

        @SuppressWarnings("unchecked")
        LocalService<VncService> service = new AnnotationLocalServiceBinder().read(VncService.class);
        service.setManager(new DefaultServiceManager<>(service, VncService.class));
        service.getManager().getImplementation().setPort(port);
        return new LocalDevice(identity, type, details, icon, service);
    }

    /**
     * Kill me.
     */
    synchronized private void killMe() {
        if (null != upnpService) {
            upnpService.shutdown();
            upnpService = null;
        }
        if (null != killer) {
            killer = null;
        }
    }

    /**
     * Shutdown.
     */
    synchronized public void shutdown() {
        if (null != killer) {
            Runtime.getRuntime().removeShutdownHook(killer);
        }
        killMe();
    }
}
