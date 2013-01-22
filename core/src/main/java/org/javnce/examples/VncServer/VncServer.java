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
package org.javnce.examples.VncServer;

import java.nio.channels.SocketChannel;
import org.javnce.eventing.EventLoop;
import org.javnce.vnc.server.VncServerController;
import org.javnce.vnc.server.VncServerObserver;

/**
 * The Class VncServer is simple vnc server.
 * The VncServer is needed for functional and benchmark testing.
 */
public class VncServer implements VncServerObserver {

    /** The controller. */
    final private VncServerController controller;
    
    /** The event loop. */
    final private EventLoop eventLoop;

    /**
     * Instantiates a new vnc server.
     */
    public VncServer() {
        controller = VncServerController.instance();
        eventLoop = new EventLoop();
    }

    /**
     * Launch server in new thread.
     */
    public void launch() {
        controller.start(true, this);
        new Thread(eventLoop).start();
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.VncServerObserver#listening(int)
     */
    @Override
    public void listening(int port) {
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.VncServerObserver#connectionClosed(java.lang.Object)
     */
    @Override
    public void connectionClosed(Object userData) {
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.VncServerObserver#newConnection(java.nio.channels.SocketChannel)
     */
    @Override
    public void newConnection(SocketChannel channel) {
        controller.acceptConnection(this, channel, true, null);
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
    	eventLoop.shutdownAllInTheGroup();
        
    }
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
    	VncServer server = new VncServer();
    	server.controller.start(true, server);
    	server.eventLoop.process();
    }

    
}
