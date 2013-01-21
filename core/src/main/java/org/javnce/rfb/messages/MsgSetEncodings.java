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
 * The Class MsgSetEncodings is the SetEncodings RFB message sent by client.
 *
 */
public class MsgSetEncodings extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.SetEncodings;
    /**
     * The Constant type.
     */
    final static private byte type = 2;
    /**
     * The encodings.
     */
    private int[] encodings;

    /**
     * Instantiates a new SetEncodings message for receiving.
     */
    public MsgSetEncodings() {
        super(msgId);
    }

    /**
     * Instantiates a new SetEncodings message for sending.
     *
     * @param encodings the encodings
     */
    public MsgSetEncodings(int[] encodings) {
        super(msgId);
        this.encodings = encodings;
        setValid(true);
    }

    /**
     * Gets the encodings supported by client.
     *
     * @return the array of encodings
     * @see org.javnce.rfb.types.Encoding
     */
    public int[] get() {
        return encodings;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        if (!isValid() && 0 != buffer.remaining()) {
            if (null == encodings && 4 <= buffer.remaining()) {
                byte newtype = buffer.get();

                if (type == newtype) {
                    //padding;
                    buffer.get();
                    int count = buffer.getShort() & 0xFFFF;
                    encodings = new int[count];
                } else {
                    return true;
                }
            }

            if (null != encodings && (encodings.length * 4) <= buffer.remaining()) {
                for (int i = 0; i < encodings.length; i++) {
                    encodings[i] = buffer.getInt();
                }

                setValid(true);
            }
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
            ByteBuffer buffer = ByteBuffer.allocate(4 + encodings.length * 4);

            buffer.put(type);

            buffer.put((byte) 0xff);

            buffer.putShort((short) encodings.length);

            for (int i = 0; i < encodings.length; i++) {
                buffer.putInt(encodings[i]);
            }

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
        try (Formatter formatter = new Formatter(result)) {
            for (int i = 0; null != encodings && i < encodings.length; i++) {
                formatter.format(" %d ", encodings[i]);
            }
        }
        result.append(NEW_LINE).append("}");
        return result.toString();
    }
}
