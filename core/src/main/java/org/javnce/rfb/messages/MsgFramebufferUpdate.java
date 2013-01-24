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
import org.javnce.rfb.types.Encoding;
import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;

/**
 * The Class MsgFramebufferUpdate is FramebufferUpdate RFB message sent by
 * server.
 */
public class MsgFramebufferUpdate extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.FramebufferUpdate;
    /**
     * The Constant type.
     */
    final static byte type = 0;
    /**
     * The fb.
     */
    private Framebuffer[] fb;
    /**
     * The cur.
     */
    private Framebuffer cur;
    /**
     * The byte per pixel.
     */
    final private int bytePerPixel;

    /**
     * Instantiates a new FramebufferUpdate message for receiving.
     * 
     * @param format tells byte per pixel that raw format have.
     */
    public MsgFramebufferUpdate(PixelFormat format) {
        super(msgId);
        bytePerPixel = format.bytesPerPixel();
    }

    /**
     * Instantiates a new FramebufferUpdate message for sending.
     * 
     * @param format tells byte per pixel that raw format have.
     * @param fb the frame buffers to be sent
     */
    public MsgFramebufferUpdate(PixelFormat format, Framebuffer[] fb) {
        super(msgId);
        this.fb = fb;
        setValid(true);
        bytePerPixel = format.bytesPerPixel();
    }

    /**
     * Gets the frame buffers.
     *
     * @return the array of frame buffers
     */
    public Framebuffer[] get() {
        return fb;
    }

    /**
     * Parses the message header.
     *
     * @param buffer the buffer
     * @return true, if whole header is read
     */
    private boolean parseMsgHeader(ByteBuffer buffer) {

        if (null == fb && 4 <= buffer.remaining()) {
            byte newtype = buffer.get();
            // padding
            buffer.get();
            int count = buffer.getShort() & 0xFFFF;
            fb = new Framebuffer[count];
            cur = null;
        }

        return (null != fb);
    }

    /**
     * Parses the frame buffer header.
     *
     * @param buffer the buffer
     * @return true, if whole frame buffer header is read.
     */
    private boolean parseFBHeader(ByteBuffer buffer) {

        if (null == cur && 12 <= buffer.remaining()) {

            int x = buffer.getShort() & 0xFFFF;
            int y = buffer.getShort() & 0xFFFF;
            int width = buffer.getShort() & 0xFFFF;
            int height = buffer.getShort() & 0xFFFF;
            int encoding = buffer.getInt();
            Rect rect = new Rect(new Point(x, y), new Size(width, height));

            if (encoding == Encoding.RAW) {
                cur = new Framebuffer(rect, encoding,
                        new ByteBuffer[]{ByteBuffer.allocate(width * height * bytePerPixel)});
            } else {
                cur = new Framebuffer(rect, encoding, null);
            }
        }
        return (null != cur);
    }

    private boolean parseBufferLength(ByteBuffer buffer) {
        if (null == this.cur.buffers()) {
            if (4 <= buffer.remaining()) {
                long length = buffer.getInt() & 0xFFFFFFFFl;
                cur = new Framebuffer(cur.rect(), cur.encoding(),
                        new ByteBuffer[]{ByteBuffer.allocate((int) length)});
            }
        }

        return (null != this.cur.buffers());
    }

    /**
     * Parses data buffer.
     *
     * @param buffer the buffer
     * @return true, if whole data is read
     */
    private boolean parseBuffer(ByteBuffer buffer) {

        for (ByteBuffer temp : cur.buffers()) {
            while (0 != buffer.remaining() && 0 != temp.remaining()) {
                temp.put(buffer.get());
            }
        }

        boolean allRead = true;

        for (ByteBuffer temp : cur.buffers()) {
            if (0 != temp.remaining()) {
                allRead = false;
                break;
            }
        }

        return allRead;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {

        if (!isValid() && 0 != buffer.remaining()) {

            boolean allRead = parseMsgHeader(buffer);

            int count = 0;

            while (allRead && count != fb.length) {
                if (allRead) {
                    allRead = parseFBHeader(buffer);
                }

                if (allRead) {
                    allRead = parseBufferLength(buffer);
                }

                if (allRead) {
                    allRead = parseBuffer(buffer);
                }

                if (allRead) {
                    count = 0;

                    for (int i = 0; i < fb.length; i++) {
                        if (null == fb[i]) {
                            for (ByteBuffer temp : cur.buffers()) {
                                temp.clear();
                            }

                            fb[i] = cur;
                            cur = null;
                            count = i + 1;
                            break;
                        }
                    }
                }
            }

            if (allRead) {
                setValid(count == fb.length);
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
            //First have message and fb header

            int size = 4;

            if (null != fb && 0 < fb.length) {
                size += 12;

                if (fb[0].encoding() == Encoding.JaVNCeRLE) {
                    size += 4;
                }
            }

            ByteBuffer buffer = ByteBuffer.allocate(size);

            int fbCount = 0;
            if (null != fb) {
                fbCount = fb.length;
            }
            //Write msg header
            buffer.put(type);
            buffer.put((byte) 0xff);
            buffer.putShort((short) fbCount);

            for (int i = 0; i < fbCount; i++) {

                if (null == buffer) {
                    size = 12;

                    if (fb[i].encoding() == Encoding.JaVNCeRLE) {
                        size += 4;
                    }

                    buffer = ByteBuffer.allocate(size);
                }

                //Write fb header
                buffer.putShort((short) fb[i].rect().x());
                buffer.putShort((short) fb[i].rect().y());
                buffer.putShort((short) fb[i].rect().width());
                buffer.putShort((short) fb[i].rect().height());
                buffer.putInt(fb[i].encoding());

                if (fb[i].encoding() == Encoding.JaVNCeRLE) {
                    int count = 0;

                    for (ByteBuffer temp : fb[i].buffers()) {
                        count += temp.capacity();
                    }

                    buffer.putInt(count);
                }

                buffer.clear();
                list.add(buffer);
                buffer = null;

                for (ByteBuffer temp : fb[i].buffers()) {
                    temp.clear();
                    list.add(temp);
                }
            }
            if (null != buffer)
            {
                buffer.clear();
                list.add(buffer);
                buffer = null;
            }

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

        for (int i = 0; null != fb && i < fb.length; i++) {
            if (null != fb[i]) {
                result.append(" rect:").append(fb[i].rect()).append(NEW_LINE);
            }
        }

        result.append(NEW_LINE).append("}");
        return result.toString();
    }
}
