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
import org.javnce.rfb.types.PixelFormat;

/**
 * The Class MsgSetPixelFormat is the SetPixelFormat RFB message sent by client.
 *
 */
public class MsgSetPixelFormat extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.SetPixelFormat;
    /**
     * The Constant type.
     */
    final static private byte type = 0;
    /**
     * The format.
     */
    private PixelFormat format;

    /**
     * Instantiates a new SetPixelFormat message for receiving.
     */
    public MsgSetPixelFormat() {
        super(msgId);
    }

    /**
     * Instantiates a new SetPixelFormat message for sending.
     *
     * @param format the new framebuffer format
     */
    public MsgSetPixelFormat(PixelFormat format) {
        super(msgId);
        this.format = format;
        setValid(true);
    }

    /**
     * Gets the requested format by client.
     *
     * @return the pixel format
     */
    public PixelFormat get() {
        return format;
    }

    /**
     * Parses the pixel format.
     *
     * @param buffer the buffer
     * @return true, if whole format was read
     */
    private boolean parsePixelFormat(ByteBuffer buffer) {
        if (null == format && 16 <= buffer.remaining()) {
            int bits_per_pixel = buffer.get() & 0xFF;
            int depth = buffer.get() & 0xFF;
            int big_endian_flag = buffer.get() & 0xFF;
            int true_colour_flag = buffer.get() & 0xFF;
            int red_max = buffer.getShort() & 0xFFFF;
            int green_max = buffer.getShort() & 0xFFFF;
            int blue_max = buffer.getShort() & 0xFFFF;
            int red_shift = buffer.get() & 0xFF;
            int green_shift = buffer.get() & 0xFF;
            int blue_shift = buffer.get() & 0xFF;

            //padding;
            buffer.get();
            buffer.get();
            buffer.get();

            format = new PixelFormat(bits_per_pixel,
                    depth,
                    (big_endian_flag != 0 ? true : false),
                    (true_colour_flag != 0 ? true : false),
                    new Color(red_max, green_max, blue_max),
                    new Color(red_shift, green_shift, blue_shift));

        }

        return (null != format);
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        boolean allRead = isValid();

        if (!isValid() && 20 <= buffer.remaining()) {
            byte newtype;
            newtype = buffer.get();
            //padding;
            buffer.get();
            buffer.get();
            buffer.get();

            parsePixelFormat(buffer);
            this.setValid(type == newtype);
            allRead = true;
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
            ByteBuffer buffer = ByteBuffer.allocate(20);

            buffer.put(type);

            buffer.put((byte) 0xff);
            buffer.put((byte) 0xff);
            buffer.put((byte) 0xff);


            buffer.put((byte) format.bitsPerPixel());
            buffer.put((byte) format.depth());
            buffer.put((byte) (format.bigEndian() ? 1 : 0));
            buffer.put((byte) (format.trueColour() ? 1 : 0));

            buffer.putShort((short) format.max().red());
            buffer.putShort((short) format.max().green());
            buffer.putShort((short) format.max().blue());

            buffer.put((byte) format.shift().red());
            buffer.put((byte) format.shift().green());
            buffer.put((byte) format.shift().blue());

            buffer.put((byte) 0xFF);
            buffer.put((byte) 0xFF);
            buffer.put((byte) 0xFF);

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

        if (null != format) {
            result.append(" Format:").append(format);
        }

        result.append(NEW_LINE).append("}");
        return result.toString();
    }
}
