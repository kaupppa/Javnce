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
 * The Class MsgSecurityResult is the SecurityResult RFB message sent by server.
 *
 */
public class MsgSecurityResult extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.SecurityResult;
    /**
     * The value.
     */
    private int value;
    /**
     * The text.
     */
    private byte[] text;
    /**
     * The temp.
     */
    private ByteBuffer temp;

    /**
     * Instantiates a new SecurityResult message for receiving.
     */
    public MsgSecurityResult() {
        super(msgId);
        value = -1;
    }

    /**
     * Instantiates a new SecurityResult message for sending without reason
     * text.
     *
     * @param status the status
     */
    public MsgSecurityResult(boolean status) {
        super(msgId);
        this.value = (status ? 0 : 1);
        this.setValid(true);
    }

    /**
     * Instantiates a new SecurityResult message for sending with reason text.
     * The status is set to false.
     *
     * @param text the text
     */
    public MsgSecurityResult(String text) {
        super(msgId);
        this.text = text.getBytes();
        this.value = 1;
        setValid(true);
    }

    /**
     * Gets the reason text.
     *
     * @return the text or null
     */
    public String getText() {
        String result = null;

        if (null != text && 0 != text.length) {
            result = new String(text);
        }

        return result;
    }

    /**
     * Gets the status.
     *
     * @return true if OK
     */
    public boolean getStatus() {
        return (0 == value);
    }

    /**
     * Parses the message header.
     *
     * @param buffer the buffer
     * @return true, if whole header was read
     */
    private boolean parseMsgHeader(ByteBuffer buffer) {

        if (-1 == value && 0 != buffer.remaining()) {
            long lvalue = buffer.getInt() & 0xFFFFFFFFl;
            value = (int) lvalue;
        }

        if (0 != value && null == text && 4 <= buffer.remaining()) {

            long length = buffer.getInt() & 0xFFFFFFFFl;
            text = new byte[(int) length];
            temp = ByteBuffer.wrap(text);
        }

        return !(-1 == value || (0 != value && null == text));
    }

    /**
     * Parses the text data.
     *
     * @param buffer the buffer
     * @return true, if whole reason text was read
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

            if (allRead && null != text) {
                allRead = parseData(buffer);
            }

            if (allRead) {
                setValid(true);
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

            int length = 4;

            if (0 != value) {
                length += 4;
            }

            if (null != text) {
                length += text.length;
            }

            ByteBuffer buffer = ByteBuffer.allocate(length);
            buffer.putInt(value);

            if (0 != value) {
                if (null != text) {
                    buffer.putInt(text.length);
                    buffer.put(text);
                } else {
                    buffer.putInt(0);
                }
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
        result.append(" Status : ").append(value).append(NEW_LINE);
        result.append("}");

        return result.toString();
    }
}
