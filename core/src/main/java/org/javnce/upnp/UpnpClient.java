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

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;
import org.javnce.eventing.EventLoop;

/**
 * The Class UpnpClient is the UPnP client implementation.
 */
public class UpnpClient implements Runnable {

    /**
     * The UPnP service.
     */
    private UpnpService upnpService;
    /**
     * The observer.
     */
    private UpnpClientObserver observer;
    /**
     * The killer.
     */
    private Thread killer;

    /**
     * Instantiates a new UPnP client.
     */
    public UpnpClient() {
    }

    /**
     * Sets the one and only observer. Previous is removed.
     *
     * @param observer the new observer
     */
    synchronized public void setObserver(UpnpClientObserver observer) {
        this.observer = observer;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    synchronized public void run() {

        killer = new Thread() {
            @Override
            public void run() {
                killMe();
            }
        };
        Runtime.getRuntime().addShutdownHook(killer);

        try {
            upnpService = new UpnpServiceImpl();
            upnpService.getRegistry().addListener(createListener());

            // SSDP m-search all
            upnpService.getControlPoint().search(new STAllHeader());

        } catch (Throwable ex) {
            EventLoop.fatalError(this, ex);
        }
    }

    /**
     * Creates the listener.
     *
     * @return the registry listener
     */
    private RegistryListener createListener() {
        return new DefaultRegistryListener() {
            @Override
            public void remoteDeviceAdded(Registry registry, final RemoteDevice device) {
                DeviceType type = device.getType();

                if (Upnp.deviceName.equals(type.getType())) {
                    RemoteService service = device.findService(Upnp.serviceId);
                    if (null == service) {
                        return;
                    }
                    Action<RemoteService> action = service.getAction(Upnp.actionName);
                    ActionInvocation<RemoteService> request = new ActionInvocation<>(action);

                    // Executes asynchronous in the background
                    upnpService.getControlPoint().execute(
                            new ActionCallback(request) {
                                @SuppressWarnings("rawtypes")
                                @Override
                                public void success(ActionInvocation reply) {
                                    Integer port = (Integer) reply.getOutput(Upnp.argumentName).getValue();
                                    RemoteServerInfo info = RemoteServerInfo.factory(device, port.intValue());
                                    if (null != observer) {
                                        observer.serverFound(info);
                                    }
                                }

                                @SuppressWarnings("rawtypes")
                                @Override
                                public void failure(ActionInvocation reply, UpnpResponse operation, String defaultMsg) {
                                    System.err.println(defaultMsg);
                                }
                            });
                }
            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                DeviceType type = device.getType();
                if (Upnp.deviceName.equals(type.getType())) {
                    if (null != observer) {
                        observer.serverLost(RemoteServerInfo.factory(device, 0));
                    }
                }
            }
        };
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
