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
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

public class ChannelSubscriberListTest {

    private ChannelSubscriberList list;

    class TestClass implements ChannelSubscriber {

        int bytesRead;
        ByteBuffer buffer;

        TestClass() {
            bytesRead = 0;
            buffer = ByteBuffer.allocate(100);
        }

        @Override
        public void channel(SelectionKey key) {
            SocketChannel ch = (SocketChannel) key.channel();
            try {
                bytesRead = ch.read(buffer);
            } catch (IOException ex) {
                Logger.getLogger(ChannelSubscriberListTest.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        }
    }

    @Test
    public void testChannelSubscriberList() {
        ChannelSubscriberList ch = new ChannelSubscriberList();
        assertNotNull(ch);
        assertTrue(ch.isEmpty());
    }

    private Thread createThread(final ChannelSubscriberList list) {
        Thread r = new Thread() {
            @Override
            public void run() {
                try {
                    list.process(0);
                } catch (IOException ex) {
                    Logger.getLogger(ChannelSubscriberListTest.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(1);
                }
            }
        };
        return r;
    }

    @Test
    public void testWakeup() throws Exception {

        list = new ChannelSubscriberList();

        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            TestClass tc = new TestClass();
            list.add(loopback.channel1(), tc, SelectionKey.OP_READ);

            Thread r = createThread(list);
            r.start();
            r.join(10);
            assertTrue(r.isAlive());

            list.wakeup();

            r.join(10);
            assertFalse(r.isAlive());
        }
    }

    @Test
    public void testClose() throws Exception {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            list = new ChannelSubscriberList();

            TestClass tc = new TestClass();

            list.add(loopback.channel1(), tc, SelectionKey.OP_READ);

            Thread r = createThread(list);

            r.start();

            r.join(10);
            assertTrue(r.isAlive());

            list.close();

            r.join(10);
            assertFalse(r.isAlive());
        }
    }

    @Test
    public void testProcess() throws Exception {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            list = new ChannelSubscriberList();

            TestClass tc = new TestClass();

            list.add(loopback.channel1(), tc, SelectionKey.OP_READ);

            Thread r = createThread(list);

            r.start();

            r.join(10);
            assertTrue(r.isAlive());

            int length = 10;
            loopback.channel2().write(ByteBuffer.allocate(length));

            r.join(10);
            assertFalse(r.isAlive());
            assertEquals(length, tc.bytesRead);
        }
    }

    @Test
    public void testRemove() throws Exception {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            list = new ChannelSubscriberList();

            TestClass tc = new TestClass();

            list.add(loopback.channel1(), tc, SelectionKey.OP_READ);

            Thread r = createThread(list);
            r.start();

            r.join(10);
            assertTrue(r.isAlive());
            list.remove(null); //No throw from this one
            list.remove(loopback.channel2()); //No throw from this one
            list.remove(loopback.channel1());

            r.join(10);
            assertFalse(r.isAlive());
        }

    }
}