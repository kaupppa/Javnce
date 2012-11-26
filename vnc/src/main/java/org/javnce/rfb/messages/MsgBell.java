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
 * The Class MsgBell is the Bell RFB message sent by server. The MsgBell does
 * not contain any data.
 *
 */
public class MsgBell extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.Bell;
    /**
     * The Constant type.
     */
    final static private byte type = 2;
    /**
     * The readtype.
     */
    private byte readtype;

    /**
     * Instantiates a new Bell message for sending and receiving.
     */
    public MsgBell() {
        super(msgId);
        readtype = 0;
        setValid(true);
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        boolean allRead = (readtype == type);

        if (!allRead && 0 != buffer.remaining()) {

            readtype = buffer.get();
            setValid(readtype == type);
            allRead = true;
        }

        return allRead;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#marshal()
     */
    @Override
    public ArrayList<ByteBuffer> marshal() {
        ArrayList<ByteBuffer> list = new ArrayList<>();

        ByteBuffer buffer = ByteBuffer.allocate(1);

        buffer.put(type);
        buffer.clear();
        list.add(buffer);

        return list;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {}");
        return result.toString();
    }
}
