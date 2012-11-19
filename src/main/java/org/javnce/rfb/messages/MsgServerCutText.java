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
 * The Class MsgServerCutText is the ServerCutText RFB message sent by server.
 *
 */
public class MsgServerCutText extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.ServerCutText;
    /**
     * The RFB message-type.
     */
    final static private byte type = 3;
    /**
     * The readtype.
     */
    private byte readtype;
    /**
     * The text.
     */
    private byte[] text;
    /**
     * The temp.
     */
    private ByteBuffer temp;

    /**
     * Instantiates a new ServerCutText message for receiving.
     */
    public MsgServerCutText() {
        super(msgId);
    }

    /**
     * Instantiates a new ServerCutText message for sending.
     *
     * @param text the text in cut buffer
     */
    public MsgServerCutText(String text) {
        super(msgId);
        this.text = text.getBytes();
        setValid(true);
    }

    /**
     * Gets the cut buffer text.
     *
     * @return the string
     */
    public String get() {
        String result = null;

        if (null != text) {
            result = new String(text);
        }

        return result;
    }

    /**
     * Parses the message header.
     *
     * @param buffer the buffer
     * @return true, if whole header was read
     */
    private boolean parseMsgHeader(ByteBuffer buffer) {

        if (null == text && 8 <= buffer.remaining()) {
            readtype = buffer.get();
            // padding
            buffer.get();
            buffer.get();
            buffer.get();

            long count = buffer.getInt() & 0xFFFFFFFFl;
            text = new byte[(int) count];
            temp = ByteBuffer.wrap(text);
        }

        return (null != text);
    }

    /**
     * Parses the data.
     *
     * @param buffer the buffer
     * @return true, if whole text was read
     */
    private boolean parseData(ByteBuffer buffer) {

        while (0 != buffer.remaining() && 0 != temp.remaining()) {
            temp.put(buffer.get());
        }

        return (0 == temp.remaining());
    }


    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        boolean allRead = isValid();

        if (!isValid() && 0 != buffer.remaining()) {

            allRead = parseMsgHeader(buffer);

            if (allRead) {
                allRead = parseData(buffer);
            }

            if (allRead) {
                setValid(readtype == type);
            }
        }

        return allRead;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#marshal()
     */
    @Override
    public ArrayList<ByteBuffer> marshal() {
        ArrayList<ByteBuffer> list = new ArrayList<>();

        if (isValid()) {
            ByteBuffer buffer = ByteBuffer.allocate(8 + text.length);

            buffer.put(type);
            buffer.put((byte) 0xff);//Padding
            buffer.put((byte) 0xff);//Padding
            buffer.put((byte) 0xff);//Padding
            buffer.putInt(text.length);
            buffer.put(text);
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

        if (null != text) {
            result.append(" text:").append(new String(text));
        }

        result.append(NEW_LINE).append("}");
        return result.toString();
    }
}
