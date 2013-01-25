/* * Copyright (C) 2013  Pauli Kauppinen
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
package org.javnce.vnc.common;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import org.javnce.eventing.Event;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.EventSubscriber;
import org.javnce.eventing.LoopbackChannelPair;
import org.javnce.rfb.messages.Message;
import org.javnce.rfb.messages.MsgPointerEvent;
import org.javnce.rfb.messages.MsgServerFactory;
import org.javnce.rfb.types.Point;
import static org.junit.Assert.*;
import org.junit.Test;

public class MessageDispatcherTest {

    class Tester implements EventSubscriber, ReceiveMessageFactory {
        //Class that echos messages

        final private EventLoop eventLoop;
        final private MessageDispatcher dispatcher;

        Tester(SocketChannel channel) {
            eventLoop = new EventLoop(null);
            eventLoop.subscribe(ReceivedMsgEvent.eventId(), this);
            eventLoop.subscribe(SocketClosedEvent.eventId(), this);
            dispatcher = new MessageDispatcher(eventLoop, channel, this);
        }

        @Override
        public Message nextReceiveMessage() {
            return new MsgServerFactory();
        }

        @Override
        public void event(Event event) {
            if (event.Id() == ReceivedMsgEvent.eventId()) {
                MsgServerFactory msg = (MsgServerFactory) ((ReceivedMsgEvent) event).get();
                dispatcher.send(msg.get());
            }
            if (event.Id() == SocketClosedEvent.eventId()) {
                eventLoop.shutdown();
            }
        }

        public Thread createThread() {
            return new Thread(eventLoop);
        }
    }

    @Test
    public void testMessageDispatcher() throws Exception {
        try (LoopbackChannelPair loopback = new LoopbackChannelPair()) {
            Tester tester = new Tester(loopback.channel1());
            Thread thread = tester.createThread();
            thread.start();

            loopback.channel2().configureBlocking(true);

            Message msg = new MsgPointerEvent(0xFF, new Point(0xFFFF, 0xFFFF));
            ArrayList<ByteBuffer> buffers = msg.marshal();
            for (int i = 0; i < buffers.size(); i++) {
                loopback.channel2().write(buffers.get(i));
            }

            ByteBuffer data = ByteBuffer.allocate(10000);
            loopback.channel2().read(data);
            data.flip();

            //We should get back same message
            msg = new MsgPointerEvent();
            assertTrue(msg.demarshal(data));
            assertTrue(msg.isValid());

            //Disconnect socket
            loopback.channel2().close();
            thread.join(20);
            assertFalse(thread.isAlive());
        }
    }
}
