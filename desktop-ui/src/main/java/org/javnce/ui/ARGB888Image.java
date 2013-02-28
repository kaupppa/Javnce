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
import org.javnce.eventing.EventLoop;
import org.javnce.rfb.types.Encoding;
import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.Size;
import org.javnce.util.ByteBuffers;
import org.javnce.util.LZ4Encoder;
import org.javnce.util.RunLengthEncoder;

/**
 * The Class ARGB888Image is a JavaFX WritableImage with ARGB888 format support.
 *
 * The ARGB888Image support writing of ARGB888 frame buffers.
 */
public class ARGB888Image {

    /**
     * The JavaFX WritableImage image.
     */
    final private WritableImage image;
    /**
     * The image writer.
     */
    final private PixelWriter writer;
    /**
     * The LZ¤ decoder.
     */
    final private LZ4Encoder lz4decoder;
    /**
     * The bytes per pixel.
     */
    final private int bytesPerPixel = 4;
    /**
     * The VNC server format.
     */
    static final private PixelFormat<java.nio.ByteBuffer> format = PixelFormat.getByteBgraPreInstance();

    /**
     * Instantiates a new WritableImage image.
     *
     * @param size the size
     */
    public ARGB888Image(Size size) {
        image = new WritableImage(size.width(), size.height());
        writer = image.getPixelWriter();
        lz4decoder = new LZ4Encoder();
    }

    /**
     * Gets the WritableImage image.
     *
     * @return the image
     */
    public WritableImage getImage() {
        return image;
    }

    /**
     * Write VNC server frame buffer data into image.
     *
     * @param buffers the buffers
     */
    public void write(Framebuffer[] buffers) {
        for (Framebuffer buffer : buffers) {
            final int x = buffer.rect().x();
            final int y = buffer.rect().y();
            final int width = buffer.rect().width();
            final int height = buffer.rect().height();

            if (Encoding.RAW == buffer.encoding()) {
                writeRaw(ByteBuffers.asBuffer(buffer.buffers()), x, y, width, height);
            } else if (Encoding.RLE == buffer.encoding()) {
                ByteBuffer decoded = RunLengthEncoder.decode(buffer.buffers(), width, height, bytesPerPixel);
                writeRaw(decoded, x, y, width, height);
            } else if (Encoding.LZ4 == buffer.encoding()) {
                ByteBuffer decoded = lz4decoder.decompress(buffer.buffers(), width * height * bytesPerPixel);
                writeRaw(decoded, x, y, width, height);
            } else {
                EventLoop.fatalError(this, new UnsupportedOperationException());
            }
        }
    }

    /**
     * Write VNC server raw frame buffer data into image.
     *
     * @param buffer the buffer
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     */
    private void writeRaw(ByteBuffer buffer, int x, int y, int width, int height) {
        writer.setPixels(x, y, width, height, format, buffer, width * bytesPerPixel);
    }
}
