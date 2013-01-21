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
import java.nio.channels.GatheringByteChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import org.javnce.rfb.messages.Message;

/**
 * The Class SocketWriter writes messages to given non-blocking channel.
 *
 */
class SocketWriter {

    /**
     * The list of message to be written.
     */
    final private LinkedList<Message> messages;
    /**
     * The write buffer.
     */
    private ArrayList<ByteBuffer> buffers;

    /**
     * Instantiates a new socket writer.
     */
    SocketWriter() {
        this.messages = new LinkedList<>();
    }

    /**
     * Adds the.
     *
     * @param msg the msg
     */
    void add(Message msg) {
        if (null != msg && msg.isValid()) {
            messages.addLast(msg);
        }
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    boolean isEmpty() {
        return (null == buffers && messages.isEmpty());
    }

    /**
     * Refresh.
     */
    private void refresh() {

        if (null != buffers) {
            ListIterator<ByteBuffer> iter = buffers.listIterator(0);

            while (iter.hasNext()) {
                ByteBuffer buffer = iter.next();

                if (0 == buffer.remaining()) {
                    iter.remove();
                } else {
                    break;
                }
            }

            if (buffers.isEmpty()) {
                buffers = null;
            }
        }

        if (null == buffers && !messages.isEmpty()) {
            Message msg = messages.removeFirst();
            buffers = msg.marshal();

        }
    }

    /**
     * Write.
     *
     * @param channel the channel
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void write(GatheringByteChannel channel) throws IOException {

        refresh();
        long count = 1;

        while (null != buffers && 0 < count) {
            count = channel.write(buffers.toArray(new ByteBuffer[buffers.size()]));
            refresh();
        }
    }
}
