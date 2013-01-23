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
package org.javnce.eventing;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventTester implements EventSubscriber, ChannelSubscriber {

    EventLoop eventLoop;
    ArrayList<Event> events;
    ByteBuffer buffer;
    EventTester testers[];
    Thread thread;

    EventTester(EventLoop parent) {
        eventLoop = new EventLoop(parent);
        events = new ArrayList<>();
        buffer = ByteBuffer.allocate(100);
        thread = new Thread(eventLoop);
    }

    void createValidGroup(int count) {
        testers = new EventTester[count];

        for (int i = 0; i < testers.length; i++) {
            testers[i] = new EventTester(eventLoop);
            try {
                testers[i].thread.join(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventTester.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        }
    }

    @Override
    public void event(Event event) {
        events.add(event);
    }

    @Override
    public void channel(SelectionKey key) {
        SocketChannel ch = (SocketChannel) key.channel();
        try {
            ch.read(buffer);
        } catch (IOException ex) {
            Logger.getLogger(EventTester.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    public void handleEvent(EventId id) {
        eventLoop.subscribe(id, this);

        for (int i = 0; null != testers && i < testers.length; i++) {
            testers[i].eventLoop.subscribe(id, testers[i]);
        }
    }

    void startAll() {

        for (int i = 0; null != testers && i < testers.length; i++) {
            try {
                testers[i].thread.start();
                testers[i].thread.join(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventTester.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        }
        thread.start();
    }
}
