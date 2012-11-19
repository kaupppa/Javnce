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
 * The Class MsgClientInit is ClientInit RFB message sent by client.
 *
 */
public class MsgClientInit extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.ClientInit;
    /**
     * The shared.
     */
    private boolean shared;

    /**
     * Instantiates a new ClientInit message for receiving.
     */
    public MsgClientInit() {
        super(msgId);
        shared = false;
    }

    /**
     * Instantiates a new ClientInit message for sendng.
     *
     * @param shared the shared
     */
    public MsgClientInit(boolean shared) {
        super(msgId);
        this.shared = shared;
        setValid(true);
    }

    /**
     * Gets the shared mode.
     *
     * @return true, if shared
     */
    public boolean get() {
        return shared;

    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        while (!isValid() && 0 != buffer.remaining()) {
            byte value = buffer.get();
            shared = false;

            if (0 == value) {
                shared = true;
            }

            this.setValid(true);
        }

        return isValid();
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#marshal()
     */
    @Override
    public ArrayList<ByteBuffer> marshal() {
        ArrayList<ByteBuffer> list = new ArrayList<>();

        if (isValid()) {
            ByteBuffer buffer = ByteBuffer.allocate(1);
            buffer.put((byte) (shared ? 1 : 0));
            buffer.clear();
            list.add(buffer);
        }

        return list;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName()).append(" Object {").append(NEW_LINE);
        result.append(" Shared : ").append(shared).append(NEW_LINE);
        result.append("}");

        return result.toString();
    }
}
