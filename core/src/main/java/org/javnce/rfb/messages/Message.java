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
package org.javnce.rfb.messages;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * The Class Message is an abstract base class for RFB messages.
 *
 */
public abstract class Message {

    /**
     * The id.
     */
    private final Id id;
    /**
     * The valid.
     */
    private boolean valid;

    /**
     * Instantiates a new message.
     *
     * @param id the message identifier
     */
    public Message(Id id) {
        this.id = id;
        this.valid = false;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Id getId() {
        return this.id;
    }

    /**
     * Checks if is valid. Message is valid if it can be written into socket or
     * if all data was read from socket.
     *
     * @return true, if is valid
     */
    public boolean isValid() {
        return this.valid;
    }

    /**
     * Sets the valid.
     *
     * @param valid the new valid
     */
    void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Method to read message data from buffer.
     *
     * @param buffer the buffer
     * @return true, if all message data was read
     */
    abstract public boolean demarshal(ByteBuffer buffer);

    /**
     * Method to write message data into buffer.
     *
     * @return the byte buffer list
     */
    abstract public ArrayList<ByteBuffer> marshal();
}
