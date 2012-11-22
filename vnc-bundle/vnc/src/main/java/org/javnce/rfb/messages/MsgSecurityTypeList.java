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
 * The Class MsgSecurityTypeList is the Security types RFB message sent by
 * server.
 *
 */
public class MsgSecurityTypeList extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.SecurityTypeList;
    /**
     * The array.
     */
    private SecurityType[] array;
    /**
     * The reason text.
     */
    private byte[] text;
    /**
     * The receiving helper.
     */
    private byte[] data;
    /**
     * A receiving helper.
     */
    private ByteBuffer temp;
    /**
     * The count.
     */
    private int count;

    /**
     * Instantiates a new Security types message for receiving.
     */
    public MsgSecurityTypeList() {
        super(msgId);
        count = -1;
    }

    /**
     * Instantiates a new Security types message for sending without reason
     * text.
     *
     * @param array the array of security types
     */
    public MsgSecurityTypeList(SecurityType[] array) {
        super(msgId);
        this.array = array;
        setValid(true);
    }

    /**
     * Instantiates a new Security types message for sending with reason text.
     *
     * @param text the reason text
     */
    public MsgSecurityTypeList(String text) {
        super(msgId);
        this.text = text.getBytes();
        setValid(true);
    }

    /**
     * Gets the security types.
     *
     * @return the security types or null
     */
    public SecurityType[] getTypes() {
        return array;
    }

    /**
     * Gets the reason text.
     *
     * @return the reason text or null
     */
    public String getText() {
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

        if (-1 == count && 0 != buffer.remaining()) {
            count = buffer.get();

            if (0 != count) {
                data = new byte[count];
                temp = ByteBuffer.wrap(data);
            }
        }

        if (0 == count && null == data && 4 <= buffer.remaining()) {

            long length = buffer.getInt() & 0xFFFFFFFFl;
            data = new byte[(int) length];
            temp = ByteBuffer.wrap(data);
        }

        return (-1 != count && null != data);
    }

    /**
     * Parses the data.
     *
     * @param buffer the buffer
     * @return true, if whole data was read
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
                setValid(true);

                if (0 == count) {
                    text = data;
                } else {
                    array = new SecurityType[count];

                    for (int i = 0; i < count; i++) {
                        array[i] = SecurityType.create(data[i]);
                    }

                }

                data = null;
                temp = null;
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
            ByteBuffer buffer;

            if (null != array) {
                buffer = ByteBuffer.allocate(array.length + 1);
                buffer.put((byte) array.length);

                for (int i = 0; i < array.length; i++) {
                    buffer.put(array[i].id());
                }
            } else {
                buffer = ByteBuffer.allocate(text.length + 5);
                buffer.put((byte) 0);
                buffer.putInt(text.length);
                buffer.put(text);
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

        if (null != array) {
            result.append(" Types : ");

            for (int i = 0; i < array.length; i++) {
                result.append(array[i]).append(" ");
            }

            result.append(NEW_LINE);
        }

        result.append("}");

        return result.toString();
    }
}
