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
import org.javnce.rfb.types.Size;

/**
 * The Class MsgServerInit is the ServerInit RFB message sent by server.
 *
 */
public class MsgServerInit extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.ServerInit;
    /**
     * The format.
     */
    private PixelFormat format;
    /**
     * The size.
     */
    private Size size;
    /**
     * The name.
     */
    private String name;
    /**
     * The name buffer.
     */
    private ByteBuffer nameBuffer;

    /**
     * Instantiates a new ServerInit message for receiving.
     */
    public MsgServerInit() {
        super(msgId);
    }

    /**
     * Instantiates a new ServerInit message for sending.
     *
     * @param format the frame buffer format
     * @param size the frame buffer size
     * @param name the name
     */
    public MsgServerInit(PixelFormat format, Size size, String name) {
        super(msgId);
        this.format = format;
        this.size = size;
        this.name = name;
        setValid(true);
    }

    /**
     * Gets the frame buffer format.
     *
     * @return the format
     */
    public PixelFormat getFormat() {
        return format;
    }

    /**
     * Gets the frame buffer size.
     *
     * @return the size
     */
    public Size getSize() {
        return size;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Parses the size.
     *
     * @param buffer the buffer
     * @return true, if whole size was read
     */
    private boolean parseSize(ByteBuffer buffer) {

        if (null == size && 4 <= buffer.remaining()) {
            int width = buffer.getShort() & 0xFFFF;
            int height = buffer.getShort() & 0xFFFF;
            size = new Size(width, height);
        }

        return (null != size);
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

    /**
     * Parses the name.
     *
     * @param buffer the buffer
     * @return true, if whole name was read
     */
    private boolean parseName(ByteBuffer buffer) {

        if (null == name) {
            if (null == nameBuffer) {
                if (4 <= buffer.remaining()) {
                    long length = buffer.getInt() & 0xFFFFFFFFL;
                    nameBuffer = ByteBuffer.allocate((int) length);
                }
            }

            while (0 != buffer.remaining() && 0 != nameBuffer.remaining()) {
                nameBuffer.put(buffer.get());
            }

            if (0 == nameBuffer.remaining()) {
                nameBuffer.clear();
                byte[] temp = new byte[nameBuffer.limit()];
                nameBuffer.get(temp);
                name = new String(temp);
                nameBuffer = null;
            }
        }

        return (null != name);
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        boolean allRead = isValid();

        if (!isValid() && 0 != buffer.remaining()) {

            allRead = parseSize(buffer);

            if (allRead) {
                allRead = parsePixelFormat(buffer);
            }

            if (allRead) {
                allRead = parseName(buffer);
            }

            if (allRead) {
                this.setValid(true);
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
            ByteBuffer buffer = ByteBuffer.allocate(24 + name.length());

            buffer.putShort((short) size.width());
            buffer.putShort((short) size.height());

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

            buffer.putInt(name.length());
            buffer.put(name.getBytes());
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

        if (null != size) {
            result.append(" Size:").append(size);
        }

        if (null != format) {
            result.append(" Format:").append(format);
        }

        if (null != name) {
            result.append(" Name:").append(name);
        }

        result.append(NEW_LINE).append("}");

        return result.toString();
    }
}
