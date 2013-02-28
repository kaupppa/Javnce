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
package org.javnce.rfb.types;

import java.nio.ByteBuffer;

/**
 * The Class Framebuffer wraps a framebuffer from the
 * {@link org.javnce.rfb.messages.MsgFramebufferUpdate} message. The framebuffer
 * contains of area, encoding and ByteBuffer.
 *
 * The framebuffer does not contain byte order info. It's byte order is always
 * default (big endian). The byte order is defined in {@link PixelFormat}.
 *
 * @see org.javnce.rfb.messages.MsgFramebufferUpdate
 * @see PixelFormat
 *
 */
public class Framebuffer {

    /**
     * The rect is the area of framebuffer.
     */
    private final Rect rect;
    /**
     * The encoding of the framebuffer.
     *
     * @see Encoding
     */
    private final int encoding;
    /**
     * The buffer as ByteBuffer.
     */
    private final ByteBuffer buffers[];

    /**
     * Instantiates a new framebuffer.
     *
     * @param rect the area of buffer
     * @param encoding the encoding of buffer
     * @param buffers the buffer itself
     */
    public Framebuffer(Rect rect, int encoding, ByteBuffer buffers[]) {
        this.rect = rect;
        this.encoding = encoding;
        this.buffers = buffers;
    }

    /**
     * Rect getter.
     *
     * @return the area of buffer
     */
    public Rect rect() {
        return rect;
    }

    /**
     * Encoding getter.
     *
     * @return the encoding of buffer
     * @see Encoding
     */
    public int encoding() {
        return encoding;
    }

    /**
     * Buffer getter. The byte order is always default (big endian).
     *
     * @return the byte buffer
     *
     */
    public ByteBuffer[] buffers() {
        return buffers;
    }
}
