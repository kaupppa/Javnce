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
package org.javnce.ui;

import java.nio.ByteBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.javnce.rfb.types.Encoding;
import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.Size;

/**
 * The VNC client image.
 */
public class VncImage {

    /**
     * The image format in VNC style.
     */
    final private org.javnce.rfb.types.PixelFormat format;
    /**
     * The image.
     */
    final private WritableImage image;
    /**
     * The writer.
     */
    final private PixelWriter writer;
    /**
     * The image format in FX style.
     */
    final private PixelFormat fxFormat;

    /**
     * Convert pixel format.
     *
     * @param format the format
     * @return the org.javnce.rfb.types. pixel format
     */
    static public org.javnce.rfb.types.PixelFormat convertPixelFormat(PixelFormat format) {
        org.javnce.rfb.types.PixelFormat vncFormat = null;

        switch (format.getType()) {
            case BYTE_BGRA:
            case BYTE_BGRA_PRE:
                vncFormat = org.javnce.rfb.types.PixelFormat.createARGB888();
                break;
            //TODO RGB565 needed but how ....
            case BYTE_INDEXED:
                break;
            case BYTE_RGB:
                break;
            case INT_ARGB:
                break;
            case INT_ARGB_PRE:
                break;
            default:
                break;
        }

        return vncFormat;
    }

    /**
     * Instantiates a new VNC image.
     *
     * @param format the format
     * @param size the size
     */
    public VncImage(org.javnce.rfb.types.PixelFormat format, Size size) {
        this.format = format;
        image = new WritableImage(size.width(), size.height());
        writer = image.getPixelWriter();
        fxFormat = writer.getPixelFormat();
    }

    /**
     * Gets the image.
     *
     * @return the image
     */
    public WritableImage getImage() {
        return image;
    }

    /**
     * Update the image.
     *
     * @param buffers the buffers
     */
    public void write(Framebuffer[] buffers) {
        for (Framebuffer buffer : buffers) {
            int x = buffer.rect().x();
            int y = buffer.rect().y();
            int w = buffer.rect().width();
            int h = buffer.rect().height();

            if (Encoding.RAW == buffer.encoding()) {
                writeRaw(buffer.asOneBuffer(), x, y, w, h);
            } else if (Encoding.JaVNCeRLE == buffer.encoding()) {
                ByteBuffer decoded = rledecode(buffer.asOneBuffer(), w, h, format.bytesPerPixel());
                writeRaw(decoded, x, y, w, h);
            } else {
                System.exit(1);
            }
        }
    }

    /**
     * Write raw data.
     *
     * @param buffer the buffer
     * @param x the x
     * @param y the y
     * @param w the w
     * @param h the h
     */
    private void writeRaw(ByteBuffer buffer, int x, int y, int w, int h) {
        writer.setPixels(x, y, w, h, fxFormat, buffer, w * format.bytesPerPixel());
    }

    /**
     * Convert Run-length encoded data.
     *
     * @param src the src
     * @param width the width
     * @param height the height
     * @param bytePerPixel the byte per pixel
     * @return the byte buffer
     */
    static public ByteBuffer rledecode(ByteBuffer src, int width, int height, int bytePerPixel) {

        byte[] pixel = new byte[bytePerPixel];

        ByteBuffer dst = ByteBuffer.allocate(width * height * bytePerPixel);

        int rleSize = bytePerPixel + 1;

        while ((rleSize) <= src.remaining() && bytePerPixel <= dst.remaining()) {

            int count = src.get() & 0xff;
            count = count + 1;

            src.get(pixel);

            for (int i = 0; i < count; i++) {
                dst.put(pixel);
            }
        }

        dst.clear();
        return dst;
    }
}
