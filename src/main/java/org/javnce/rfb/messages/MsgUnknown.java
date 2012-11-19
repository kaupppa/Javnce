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
import java.util.Formatter;

/**
 * The Class MsgUnknown is message class that eats up every thing in buffer.
 *
 */
public class MsgUnknown extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.Unknown;
    /**
     * The eat all.
     */
    byte[] eatAll;

    /**
     * Instantiates a new msg unknown.
     */
    public MsgUnknown() {
        super(msgId);
    }


    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        eatAll = new byte[buffer.remaining()];
        buffer.get(eatAll);
        this.setValid(true);
        return true;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#marshal()
     */
    @Override
    public ArrayList<ByteBuffer> marshal() {
        return new ArrayList<>();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName()).append(" Object {").append(NEW_LINE);
        Formatter formatter;
        formatter = new Formatter(result);

        for (int i = 0; null != eatAll && i < eatAll.length; i++) {
            formatter.format(" %02X ", eatAll[i]);
        }

        formatter.close();
        result.append(NEW_LINE).append("}");
        return result.toString();
    }
}
