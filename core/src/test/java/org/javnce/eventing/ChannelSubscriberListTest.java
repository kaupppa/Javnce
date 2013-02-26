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

    final static int WaitTime = 100;
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
                EventLoop.fatalError(this, ex);
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
                    EventLoop.fatalError(this, ex);
                }
            }
        };
        r.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(ChannelSubscriberListTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    @Test
    public void testWakeupWhileRunning() throws Exception {

        list = new ChannelSubscriberList();

        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            TestClass tc = new TestClass();
            list.add(loopback.channel1(), tc, SelectionKey.OP_READ);

            Thread r = createThread(list);
            assertTrue(r.isAlive());

            list.wakeup();

            //Wakeup cause list.process(0) to exit
            r.join(WaitTime);
            assertFalse(r.isAlive());
        }
    }

    @Test
    public void testWakeupBeforeRunning() throws Exception {

        list = new ChannelSubscriberList();

        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            TestClass tc = new TestClass();
            list.add(loopback.channel1(), tc, SelectionKey.OP_READ);

            list.wakeup();

            Thread r = createThread(list);

            //Wakeup cause list.process(0) to exit
            r.join(WaitTime);
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
            assertTrue(r.isAlive());

            list.close();

            //close cause list.process(0) to exit
            r.join(WaitTime);
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
            assertTrue(r.isAlive());

            int length = 10;
            loopback.channel2().write(ByteBuffer.allocate(length));

            //Socket event cause list.process(0) to exit
            r.join(WaitTime);
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
            assertTrue(r.isAlive());
            list.remove(null); //No throw from this one
            list.remove(loopback.channel2()); //No throw from this one
            list.remove(loopback.channel1());

            //remove cause list.process(0) to exit
            r.join(WaitTime);
            assertFalse(r.isAlive());
        }

    }

    @Test
    public void testAnonymousCallback() throws Exception {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            list = new ChannelSubscriberList();

            final ByteBuffer buffer = ByteBuffer.allocate(100);

            list.add(loopback.channel1(), new ChannelSubscriber() {
                @Override
                public void channel(SelectionKey key) {
                    try {
                        ((SocketChannel) key.channel()).read(buffer);
                    } catch (IOException e) {
                    }
                }
            }, SelectionKey.OP_READ);

            //Call garbage collector to test that callback is not cleared
            System.gc();

            Thread r = createThread(list);
            assertTrue(r.isAlive());

            int length = 10;
            loopback.channel2().write(ByteBuffer.allocate(length));

            r.join(WaitTime);

            assertEquals(length, buffer.position());
        }
    }
}
