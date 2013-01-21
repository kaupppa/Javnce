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
 * The Class MsgKeyEvent is the KeyEvent RFB message sent by client.
 *
 */
public class MsgKeyEvent extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.KeyEvent;
    /**
     * The RFB message-type.
     */
    final static private byte type = 4;
    /**
     * The down.
     */
    private boolean down;
    /**
     * The key.
     */
    private long key;

    /**
     * Instantiates a new KeyEvent message for receiving.
     */
    public MsgKeyEvent() {
        super(msgId);
    }

    /**
     * Instantiates a new KeyEvent message for sending.
     *
     * @param down the down is true if key is pressed
     * @param key the key is the keysym
     */
    public MsgKeyEvent(boolean down, long key) {
        super(msgId);
        this.down = down;
        this.key = key;
        setValid(true);
    }

    /**
     * Gets the key.
     *
     * @return the keysym
     */
    public long getKey() {
        return key;
    }

    /**
     * Gets the down.
     *
     * @return true if key is down
     */
    public boolean getDown() {
        return down;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        if (!isValid() && 8 <= buffer.remaining()) {

            byte newtype = buffer.get();
            this.down = (0 == buffer.get() ? false : true);

            //padding;
            buffer.get();
            buffer.get();

            key = buffer.getInt() & 0xFFFFFFFFL;

            this.setValid(type == newtype);
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
            ByteBuffer buffer = ByteBuffer.allocate(8);

            buffer.put(type);

            buffer.put((byte) (down ? 1 : 0));

            buffer.put((byte) 0xff);
            buffer.put((byte) 0xff);

            buffer.putInt((int) (key & 0xFFFFFFFF));

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
        result.append(" down:").append(down);
        result.append(" key:").append(key);
        result.append(NEW_LINE).append("}");
        return result.toString();
    }
}
