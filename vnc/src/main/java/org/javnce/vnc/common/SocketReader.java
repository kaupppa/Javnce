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
package org.javnce.vnc.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import org.javnce.eventing.EventLoop;
import org.javnce.rfb.messages.Message;

/**
 * The Class SocketReader reads messages from given non-blocking channel.
 *
 */
class SocketReader {

    /**
     * The read buffer.
     */
    final private ByteBuffer buffer;
    /**
     * The receive buffer size.
     */
    static final private int BLOCK_SIZE = 10 * 1024;
    /**
     * The factory to create next message to be received.
     */
    final private ReceiveMessageFactory factory;
    /**
     * The message currently received.
     */
    private Message current;

    /**
     * Instantiates a new socket reader.
     */
    SocketReader(ReceiveMessageFactory factory) {
        this.factory = factory;
        buffer = ByteBuffer.allocateDirect(BLOCK_SIZE);
        buffer.limit(0);
        current = null;
    }

    /**
     * Clears the buffer.
     */
    private void clear() {
        if (0 == buffer.remaining()) {
            buffer.clear();
            buffer.limit(0);
        }
    }


    /**
     * Reads message from buffer
     *
     * @return the message
     */
    private Message getMessage() {
        Message msg = null;

        if (null == current) {
            current = factory.nextReceiveMessage();
        }

        try {
            if (0 != buffer.remaining() && null != current) {
                if (current.demarshal(buffer)) {
                    msg = current;
                    current = null;
                }
            }

        } catch (Throwable e) {
            EventLoop.fatalError(this, e);
        }

        return msg;
    }

    /**
     * Read message from channel.
     *
     * @param channel the channel
     * @return the message
     * @throws IOException Signals that an I/O exception has occurred.
     */
    Message read(ReadableByteChannel channel) throws IOException {

        clear();

        int old_limit = buffer.limit();
        int old_pos = buffer.position();

        buffer.position(old_limit);
        buffer.limit(buffer.capacity());

        int bytesRead = channel.read(buffer);

        if (0 <= bytesRead) {
            buffer.position(old_pos);
            buffer.limit(old_limit + bytesRead);
        } else if (0 > bytesRead) {
            channel.close();
            return null;
        }

        Message msg = getMessage();
        return msg;
    }
}
