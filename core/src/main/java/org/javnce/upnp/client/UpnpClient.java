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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ServiceId;
import org.fourthline.cling.model.types.UDAServiceId;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

/**
 * The Class UpnpClient implements the UPnP client.
 */
class UpnpClient extends Thread {

    /**
     * The UPnP service.
     */
    final private UpnpService upnpService;
    /**
     * The observer.
     */
    final private UpnpClientObserver observer;
    /**
     * The remote servers.
     */
    final private HashMap<String, RemoteServerInfo> remoteServers;

    /**
     * Instantiates a new UPnP client.
     *
     * @param observer the observer
     */
    UpnpClient(UpnpClientObserver observer) {
        this.observer = observer;
        remoteServers = new HashMap<>();
        upnpService = new UpnpServiceImpl();
        setName("Javnce-Upnp-Client");
    }

    /**
     * Shutdown.
     */
    void shutdown() {
        upnpService.shutdown();
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        try {
            upnpService.getRegistry()
                    .addListener(createRegistryListener(upnpService));

            // Broadcast a search message for all devices
            upnpService.getControlPoint().search(
                    new STAllHeader());

        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            System.exit(1);
        }
    }

    /**
     * Creates the registry listener.
     *
     * @param upnpService the UPnP service
     * @return the registry listener
     */
    private RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {
            final ServiceId serviceId = new UDAServiceId("VncService");

            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

                Service service = device.findService(serviceId);

                if (service != null) {
                    executeAction(upnpService, service);
                }
            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                String hostname = device.getIdentity().getDescriptorURL().getHost();

                if (remoteServers.containsKey(hostname)) {
                    RemoteServerInfo remoteServer = remoteServers.get(hostname);

                    observer.serverLost(remoteServer);
                    remoteServers.remove(hostname);
                }
            }
        };
    }

    /**
     * Execute action.
     *
     * @param upnpService the UPnP service
     * @param service the service
     */
    private void executeAction(UpnpService upnpService, Service service) {
        Action action = service.getAction("GetPort");
        ActionInvocation request = new ActionInvocation(action);

        // Executes asynchronous in the background
        upnpService.getControlPoint().execute(
                new ActionCallback(request) {
                    @Override
                    public void success(ActionInvocation reply) {
                        RemoteDeviceIdentity id = (RemoteDeviceIdentity) reply.getAction()
                                .getService()
                                .getDevice()
                                .getIdentity();
                        String name = reply.getAction()
                                .getService()
                                .getDevice()
                                .getDetails()
                                .getFriendlyName();
                        String hostname = id.getDescriptorURL().getHost();


                        Integer port = (Integer) reply.getOutput("vncPort").getValue();
                        try {
                            InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(hostname), port);
                            RemoteServerInfo remoteServer = new RemoteServerInfo(address, name);
                            remoteServers.put(hostname, remoteServer);

                            observer.serverFound(remoteServer);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(UpnpClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    @Override
                    public void failure(ActionInvocation reply,
                            UpnpResponse operation,
                            String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                });

    }
}
