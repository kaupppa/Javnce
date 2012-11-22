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
import org.javnce.rfb.types.Color;

/**
 * The Class MsgSetColourMapEntries is the SetColourMapEntries RFB message sent
 * by server.
 *
 */
public class MsgSetColourMapEntries extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.SetColourMapEntries;
    /**
     * The Constant type.
     */
    final static private byte type = 1;
    /**
     * The first colour.
     */
    private int firstColour;
    /**
     * The array.
     */
    private Color[] array;
    /**
     * The readtype.
     */
    private byte readtype;

    /**
     * Instantiates a new SetColourMapEntries for receiving.
     */
    public MsgSetColourMapEntries() {
        super(msgId);
        firstColour = 0;
    }

    /**
     * Instantiates a new SetColourMapEntries message for sending.
     *
     * @param firstColour the first colour
     * @param array the array
     */
    public MsgSetColourMapEntries(int firstColour, Color[] array) {
        super(msgId);
        this.firstColour = firstColour;
        this.array = array;
        setValid(true);
    }

    /**
     * Gets the first colour.
     *
     * @return the first colour
     */
    public int getFirstColour() {
        return firstColour;
    }

    /**
     * Gets the colours.
     *
     * @return the colours
     */
    public Color[] getColours() {
        return array;
    }

    /**
     * Parses the msg header.
     *
     * @param buffer the buffer
     * @return true, if successful
     */
    private boolean parseMsgHeader(ByteBuffer buffer) {

        if (null == array && 6 <= buffer.remaining()) {
            readtype = buffer.get();
            // padding
            buffer.get();

            firstColour = buffer.getShort() & 0xFFFF;
            int count = buffer.getShort() & 0xFFFF;
            array = new Color[count];
        }

        return (null != array);
    }

    /**
     * Parses the data.
     *
     * @param buffer the buffer
     * @return true, if successful
     */
    private boolean parseData(ByteBuffer buffer) {
        int last = 0;

        for (int i = 0; i < array.length; i++) {
            if (null == array[i]) {
                if (6 <= buffer.remaining()) {
                    int red = buffer.getShort() & 0xFFFF;
                    int green = buffer.getShort() & 0xFFFF;
                    int blue = buffer.getShort() & 0xFFFF;
                    array[i] = new Color(red, green, blue);
                    last = i + 1;
                } else {
                    break;
                }
            } else {
                last = i + 1;
            }

        }

        return (last == array.length);
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
            ByteBuffer buffer = ByteBuffer.allocate(6 + array.length * 6);

            buffer.put(type);
            buffer.put((byte) 0xff);//Padding

            buffer.putShort((short) firstColour);
            buffer.putShort((short) array.length);

            for (int i = 0; i < array.length; i++) {
                buffer.putShort((short) array[i].red());
                buffer.putShort((short) array[i].green());
                buffer.putShort((short) array[i].blue());
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
        result.append(NEW_LINE).append("}");
        return result.toString();
    }
}
