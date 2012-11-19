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
import org.javnce.rfb.types.SecurityType;

/**
 * The Class MsgSelectedSecurityType is Security-type RFB message sent by
 * client.
 *
 */
public class MsgSelectedSecurityType extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.SelectedSecurityType;
    /**
     * The type.
     */
    private byte type;

    /**
     * Instantiates a new Security-type message for receiving.
     */
    public MsgSelectedSecurityType() {
        super(msgId);
    }

    /**
     * Instantiates a new Security-type message for sending.
     *
     * @param type the type
     */
    public MsgSelectedSecurityType(SecurityType type) {
        super(msgId);
        this.type = type.id();
        setValid(true);
    }

    /**
     * Gets the security type.
     *
     * @return the security type
     */
    public SecurityType get() {
        return SecurityType.create(type);
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {

        while (!isValid() && 0 != buffer.remaining()) {
            type = buffer.get();
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
            buffer.put(type);
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
        result.append(" Type : ").append(type).append(NEW_LINE);
        result.append("}");

        return result.toString();
    }
}
