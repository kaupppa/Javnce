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

/**
 * The Class Worker handles one client.
 */
class Worker extends Thread implements EventSubscriber {

    /**
     * The channel.
     */
    final private SocketChannel channel;
    /**
     * The event loop.
     */
    final private EventLoop eventLoop;
    /**
     * The observer.
     */
    final private VncServerObserver observer;
    /**
     * The user data.
     */
    final private Object userData;

    /**
     * Instantiates a new worker.
     *
     * @param observer the observer
     * @param channel the channel
     * @param userData the user data that is just returned untouched in {@link VncServerObserver#connectionClosed(java.lang.Object)
     * }
     */
    Worker(VncServerObserver observer, SocketChannel channel, Object userData) {
        this.observer = observer;
        this.channel = channel;
        eventLoop = new EventLoop();
        this.userData = userData;
        setName("Worker");
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {

        eventLoop.moveToNewChildGroup();
        eventLoop.subscribe(SocketClosedEvent.eventId(), this);

        //Create framebuffer handler
        ClientFramebuffer ct = new ClientFramebuffer(eventLoop);
        ct.start();

        //Create protocol handler 
        ServerProtocolHandler protocolHandler = new ServerProtocolHandler(eventLoop, channel);

        eventLoop.process();
        try {
            channel.close();
        } catch (IOException ex) {
            //Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        observer.connectionClosed(userData);
        eventLoop.shutdownAllInTheGroup();
    }

    /* (non-Javadoc)
     * @see org.javnce.eventing.EventSubscriber#event(org.javnce.eventing.Event)
     */
    @Override
    public void event(Event event) {
        if (SocketClosedEvent.eventId().equals(event.Id())) {
            eventLoop.shutdownAllInTheGroup();
        } else {
            EventLoop.fatalError(this, new UnsupportedOperationException("Unsubscribed event " + event.getClass().getName()));
        }
    }
}
