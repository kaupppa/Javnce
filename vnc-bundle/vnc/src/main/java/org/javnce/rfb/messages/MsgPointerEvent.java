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
import org.javnce.rfb.types.Point;

/**
 * The Class MsgPointerEvent is the PointerEvent RFB message sent by client.
 *
 */
public class MsgPointerEvent extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.PointerEvent;
    /**
     * The RFB message-type.
     */
    final static private byte type = 5;
    /**
     * The mask.
     */
    private int mask;
    /**
     * The point.
     */
    private Point point;

    /**
     * Instantiates a new PointerEvent message for receiving.
     */
    public MsgPointerEvent() {
        super(msgId);
    }

    /**
     * Instantiates a new PointerEvent message for sending.
     *
     * @param mask the mask of mouse buttons
     * @param point the location of the mouse
     */
    public MsgPointerEvent(int mask, Point point) {
        super(msgId);
        this.mask = mask;
        this.point = point;
        setValid(true);
    }

    /**
     * Gets the mouse buttons mask.
     *
     * @return the mask
     */
    public int getMask() {
        return mask;
    }

    /**
     * Gets the point.
     *
     * @return the location of the mouse
     */
    public Point getPoint() {
        return point;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        if (!isValid() && 6 <= buffer.remaining()) {

            byte newtype = buffer.get();

            mask = buffer.get() & 0xFF;

            int x = buffer.getShort() & 0xFFFF;
            int y = buffer.getShort() & 0xFFFF;
            point = new Point(x, y);

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
            ByteBuffer buffer = ByteBuffer.allocate(6);

            buffer.put(type);
            buffer.put((byte) (mask & 0xff));

            buffer.putShort((short) point.x());
            buffer.putShort((short) point.y());

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
        result.append(" mask:").append(mask);
        result.append(" point:").append(point);
        result.append(NEW_LINE).append("}");
        return result.toString();
    }
}
