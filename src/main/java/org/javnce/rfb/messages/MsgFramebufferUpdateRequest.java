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
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;

/**
 * The Class MsgFramebufferUpdateRequest is the FramebufferUpdateRequest RFB
 * message sent by client.
 *
 */
public class MsgFramebufferUpdateRequest extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.FramebufferUpdateRequest;
    /**
     * The Constant type.
     */
    final static private byte type = 3;
    /**
     * The incremental.
     */
    private boolean incremental;
    /**
     * The rect.
     */
    private Rect rect;

    /**
     * Instantiates a new FramebufferUpdateRequest for receiving.
     */
    public MsgFramebufferUpdateRequest() {
        super(msgId);
    }

    /**
     * Instantiates a new FramebufferUpdateRequest for sending.
     *
     * @param incremental the incremental
     * @param rect the requested area
     */
    public MsgFramebufferUpdateRequest(boolean incremental, Rect rect) {
        super(msgId);
        this.incremental = incremental;
        this.rect = rect;
        setValid(true);
    }

    /**
     * Gets the requested area.
     *
     * @return the rect
     */
    public Rect getRect() {
        return rect;
    }

    /**
     * Gets the incremental.
     *
     * @return True if incremental
     */
    public boolean getIncremental() {
        return incremental;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        if (!isValid() && 10 <= buffer.remaining()) {

            byte newtype = buffer.get();

            this.incremental = (0 == buffer.get() ? false : true);

            int x = buffer.getShort() & 0xFFFF;
            int y = buffer.getShort() & 0xFFFF;
            int width = buffer.getShort() & 0xFFFF;
            int height = buffer.getShort() & 0xFFFF;


            rect = new Rect(new Point(x, y), new Size(width, height));
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
            ByteBuffer buffer = ByteBuffer.allocate(10);

            buffer.put(type);

            buffer.put((byte) (incremental ? 1 : 0));

            buffer.putShort((short) rect.x());
            buffer.putShort((short) rect.y());
            buffer.putShort((short) rect.width());
            buffer.putShort((short) rect.height());

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

        if (null != rect) {
            result.append(" incremental:").append(incremental);
            result.append(" rect:").append(rect);
        }

        result.append(NEW_LINE).append("}");
        return result.toString();
    }
}
