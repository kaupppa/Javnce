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
package org.javnce.vnc.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javnce.vnc.server.platform.FullAccessPlatformFactory;
import org.javnce.vnc.server.platform.PlatformController;
import org.javnce.vnc.server.platform.PlatformFactory;
import org.javnce.vnc.server.platform.ViewAccessPlatformFactory;

/**
 * The Class VncServerController takes care of listener, platform and worker
 * threads.
 */
public class VncServerController {

    /**
     * The controller instance.
     */
    final static private VncServerController controller = new VncServerController();
    /**
     * The manager.
     */
    final private PlatformController manager;
    /**
     * The listener.
     */
    private Listener listener;

    /**
     * Instantiates a new vnc server controller.
     */
    public VncServerController() {
        manager = PlatformController.instance();
    }

    /**
     * Instance.
     *
     * @return the vnc server controller
     */
    public static VncServerController instance() {
        return controller;
    }

    /**
     * Start listener and platform.
     *
     * @param fullAccessMode the access mode of platform
     * @param observer the observer
     */
    public void start(boolean fullAccessMode, VncServerObserver observer) {
        shutdown();
        listener = new Listener(observer);
        listener.start();
        PlatformFactory factory;
        if (fullAccessMode) {
            factory = new FullAccessPlatformFactory();
        } else {
            factory = new ViewAccessPlatformFactory();
        }
        manager.start(factory);
    }

    /**
     * Shutdown.
     */
    synchronized public void shutdown() {
        manager.shutdown();
        if (null != listener) {
            listener.shutdown();
        }
        listener = null;

    }

    /**
     * Accept connection. If accept is false then socket is closed else new
     * worker is started.
     *
     * @param observer the observer
     * @param channel the channel
     * @param accept the accept
     * @param userData the user data that is will be returned untouched 
     * in {@link VncServerObserver#connectionClosed(java.lang.Object) }
     */
    synchronized public void acceptConnection(VncServerObserver observer, SocketChannel channel, boolean accept, Object userData) {
        if (!accept) {
            try {
                channel.close();
            } catch (IOException ex) {
                Logger.getLogger(VncServerController.class.getName()).log(Level.INFO, null, ex);
            }
        } else {
            Worker worker = new Worker(observer, channel, userData);
            worker.start();
        }
    }
}
