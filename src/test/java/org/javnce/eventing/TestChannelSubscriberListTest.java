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
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SourceChannel;
import java.nio.channels.SelectionKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestChannelSubscriberListTest {

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
            Pipe.SourceChannel ch = (SourceChannel) key.channel();
            try {
                bytesRead = ch.read(buffer);
            } catch (IOException ex) {
                Logger.getLogger(TestChannelSubscriberListTest.class.getName()).log(Level.SEVERE, null, ex);
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
                    list.processOnce();
                } catch (IOException ex) {
                    Logger.getLogger(TestChannelSubscriberListTest.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(1);
                }
            }
        };
        return r;
    }

    @Test
    public void testWakeup() throws Exception {

        list = new ChannelSubscriberList();

        Pipe pipe = Pipe.open();
        pipe.source().configureBlocking(false);

        TestClass tc = new TestClass();

        list.add(pipe.source(), tc, SelectionKey.OP_READ);

        Thread r = createThread(list);

        r.start();

        r.join(10);
        assertTrue(r.isAlive());

        list.wakeup();

        r.join(10);
        assertFalse(r.isAlive());
    }

    @Test
    public void testClose() throws Exception {
        list = new ChannelSubscriberList();

        Pipe pipe = Pipe.open();
        pipe.source().configureBlocking(false);

        TestClass tc = new TestClass();

        list.add(pipe.source(), tc, SelectionKey.OP_READ);

        Thread r = createThread(list);

        r.start();

        r.join(10);
        assertTrue(r.isAlive());

        list.close();

        r.join(10);
        assertFalse(r.isAlive());
    }

    @Test
    public void testProcessOnce() throws Exception {
        list = new ChannelSubscriberList();

        Pipe pipe = Pipe.open();
        pipe.source().configureBlocking(false);

        TestClass tc = new TestClass();

        list.add(pipe.source(), tc, SelectionKey.OP_READ);

        Thread r = createThread(list);

        r.start();

        r.join(10);
        assertTrue(r.isAlive());

        int length = 10;
        pipe.sink().write(ByteBuffer.allocate(length));


        r.join(10);
        assertFalse(r.isAlive());
        assertEquals(length, tc.bytesRead);
    }

    @Test
    public void testRemove() throws Exception {
        list = new ChannelSubscriberList();

        Pipe pipe = Pipe.open();
        pipe.source().configureBlocking(false);

        TestClass tc = new TestClass();

        list.add(pipe.source(), tc, SelectionKey.OP_READ);

        Thread r = createThread(list);
        r.start();

        r.join(10);
        assertTrue(r.isAlive());

        list.remove(pipe.source());

        r.join(10);
        assertFalse(r.isAlive());
    }
}
