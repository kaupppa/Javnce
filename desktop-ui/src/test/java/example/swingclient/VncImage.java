/*
 * Copyright (C) 2012 Pauli Kauppinen
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
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package example.swingclient;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import org.javnce.eventing.EventLoop;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * The VNC image for swing.
 */
public class VncImage {

    /**
     * The image.
     */
    private BufferedImage image;
    /**
     * The format.
     */
    private PixelFormat format;
    /**
     * The width.
     */
    private int width;
    /**
     * The height.
     */
    private int height;
    /**
     * The data buffer.
     */
    private RgbDataBuffer dataBuffer;
    /**
     * The instance.
     */
    static private VncImage instance = null;

    /**
     * Instance.
     *
     * @return the image
     */
    static public synchronized VncImage instance() {
        if (null == instance) {
            instance = new VncImage();
        }

        return instance;
    }

    /**
     * Instantiates a new image.
     */
    private VncImage() {
        format = PixelFormat.createRGB565();
        width = 0;
        height = 0;
    }

    /**
     * Swing image getter.
     *
     * @return the buffered image
     */
    synchronized BufferedImage image() {
        return image;
    }

    /**
     * Paint.
     *
     * @param g the g
     * @param width the width
     * @param heigh the heigh
     */
    public void paint(Graphics g, int width, int heigh) {
        BufferedImage temp = image();

        if (null != temp) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(temp, 0, 0, width, heigh, null);
        }
    }

    /**
     * Width.
     *
     * @return the int
     */
    synchronized public int width() {
        return width;
    }

    /**
     * Height.
     *
     * @return the int
     */
    synchronized public int height() {
        return height;
    }

    /**
     * Sets the format.
     *
     * @param size the size
     * @param format the format
     * @return the pixel format
     */
    synchronized public PixelFormat setFormat(Size size, PixelFormat format) {
        if (!format.bigEndian() && format.trueColour()) {
            //Use server format if not big endian and is true color
            this.format = format;
        }

        this.width = size.width();
        this.height = size.height();
        image = null;
        dataBuffer = null;

        try {
            dataBuffer = RgbDataBufferFactory.factory(size, format);
            image = RgbImageFactory.factory(dataBuffer, size, format);
        } catch (Exception e) {
            EventLoop.fatalError(this, e);
        }

        if (null == image) {
            EventLoop.fatalError(this, new UnsupportedOperationException());
        }

        return this.format;
    }

    /**
     * Data buffer.
     *
     * @return the rgb data buffer
     */
    synchronized public RgbDataBuffer dataBuffer() {

        return dataBuffer;
    }
}