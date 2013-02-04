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
package org.javnce.vnc.server.platform;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.javnce.rfb.types.Color;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * The Class RobotFramebufferDevice provides access to frame buffer with Robot
 * class.
 */
class RobotFramebufferDevice extends FramebufferDevice {

    /**
     * The instance.
     */
    static private RobotFramebufferDevice device = null;
    /**
     * The robot.
     */
    private Robot robot;
    /**
     * The size.
     */
    private Size size;
    /**
     * The frame buffer.
     */
    private byte[] frameBuffer;

    /**
     * Instance.
     *
     * @return the robot framebuffer device
     */
    static RobotFramebufferDevice instance() {
        if (null == device) {
            device = new RobotFramebufferDevice();
        }
        return device;
    }

    /**
     * Checks if Robot supported.
     *
     * @return true, if is supported
     */
    static boolean isSupported() {
        boolean supported = false;

        try {
            Robot robot = new Robot();
            if (null != robot) {
                supported = true;
            }
        } catch (AWTException e) {
        }

        return supported;
    }

    /**
     * Instantiates a new robot framebuffer device.
     */
    private RobotFramebufferDevice() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
        }
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#size()
     */
    @Override
    public Size size() {
        if (null == size) {
            Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
            size = new Size(screensize.width, screensize.height);
        }
        return size;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#format()
     */
    @Override
    public PixelFormat format() {
        PixelFormat format = new PixelFormat(32, 24, false, true, new Color(255, 255, 255), new Color(16, 8, 0));
        return format;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#buffer(int, int, int, int)
     */
    @Override
    public ByteBuffer[] buffer(int x, int y, int width, int height) {
        if (null == frameBuffer) {
            grabScreen();
        }
        ByteBuffer[] buffers = null;
        if (null != frameBuffer) {
            int bpp = 4;
            int lineLength = size.width() * bpp;

            if (x == 0 && width == size.width()) {
                buffers = new ByteBuffer[]{ByteBuffer.wrap(frameBuffer, lineLength * y, lineLength * height).slice()};
            } else {

                buffers = new ByteBuffer[height];
                int copyLength = width * bpp;

                for (int i = 0; i < height; i++) {
                    buffers[i] = ByteBuffer.wrap(frameBuffer, lineLength * (y + i) + x * bpp, copyLength).slice();
                }
            }
        }
        return buffers;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#grabScreen()
     */
    @Override
    public void grabScreen() {
        size();
        Rectangle rect = new Rectangle(0, 0, size.width(), size.height());
        BufferedImage image = robot.createScreenCapture(rect);
        Raster raster = image.getRaster();
        DataBuffer buffer = raster.getDataBuffer();

        if (DataBuffer.TYPE_INT == buffer.getDataType()) {
            int[] data = ((DataBufferInt) buffer).getData();

            ByteBuffer byteBuffer = ByteBuffer.allocate(data.length * 4);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.asIntBuffer().put(data);
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
            frameBuffer = byteBuffer.array();
        } else if (DataBuffer.TYPE_SHORT == buffer.getDataType()) {
            short[] data = ((DataBufferShort) buffer).getData();

            ByteBuffer byteBuffer = ByteBuffer.allocate(data.length * 4);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.asShortBuffer().put(data);
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
            frameBuffer = byteBuffer.array();
        } else if (DataBuffer.TYPE_BYTE == buffer.getDataType()) {

            frameBuffer = ((DataBufferByte) buffer).getData();
        }
    }
}
