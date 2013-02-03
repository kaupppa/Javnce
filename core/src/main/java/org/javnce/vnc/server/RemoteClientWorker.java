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
import org.javnce.eventing.Event;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.EventSubscriber;
import org.javnce.vnc.common.SocketClosedEvent;

class RemoteClientWorker extends Thread implements EventSubscriber {

    final private EventLoop eventLoop;
    final private RemoteClient client;
    final private ProtocolHandler handler;
    private ClientFramebufferHandler frameHandler;

    RemoteClientWorker(RemoteClient client) {
        this.client = client;
        eventLoop = new EventLoop();
        eventLoop.moveToNewChildGroup();
        handler = new ProtocolHandler(eventLoop);
        setName("Javnce-RemoteClientWorker");
    }

    @Override
    public void run() {

        SocketChannel channel = client.giveChannel();
        try {
            eventLoop.subscribe(SocketClosedEvent.eventId(), this);

            //Create framebuffer handler
            frameHandler = new ClientFramebufferHandler(eventLoop);
            frameHandler.init();
            frameHandler.start();
            handler.init(channel);
            eventLoop.process();
        } finally {
            eventLoop.shutdownGroup();
            try {
                channel.close();
            } catch (IOException ex) {
                EventLoop.fatalError(this, ex);
            }
            client.disconnect();
        }
    }

    @Override
    public void event(Event event) {
        if (SocketClosedEvent.eventId().equals(event.Id())) {
            eventLoop.shutdownGroup();
        }
    }

    public void shutdown() {
        eventLoop.shutdown();
    }
}
