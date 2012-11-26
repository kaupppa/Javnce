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
package example.VncServer;

import java.nio.channels.SocketChannel;
import org.javnce.eventing.EventLoop;
import org.javnce.vnc.server.VncServerController;
import org.javnce.vnc.server.VncServerObserver;

public class VncServer implements VncServerObserver {

    final private VncServerController controller;
    final private EventLoop eventLoop;

    public VncServer() {
        controller = VncServerController.instance();
        eventLoop = new EventLoop();
    }

    public void launch() {
        controller.start(true, this);
        new Thread(eventLoop).start();
    }

    @Override
    public void listening(int port) {
    }

    @Override
    public void connectionClosed(Object userData) {
    }

    @Override
    public void newConnection(SocketChannel channel) {
        controller.acceptConnection(this, channel, true, null);
    }

    public void shutdown() {
    	eventLoop.shutdownAllInTheGroup();
        
    }
    
}
