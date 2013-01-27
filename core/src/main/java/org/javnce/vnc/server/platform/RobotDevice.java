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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javnce.rfb.types.Color;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * The Class RobotDevice uses {@link Robot } to inject events and grabbing
 * framebuffer.
 */
class RobotDevice implements FramebufferDevice, KeyBoardDevice, PointerDevice {

    /**
     * The instance.
     */
    static private RobotDevice device = null;
    /**
     * The robot.
     */
    private Robot robot;
    /**
     * The size.
     */
    private Size size;
    /**
     * The Constant ScrollMask.
     */
    static private final int ScrollMask = (1 << 3) | (1 << 4);
    /**
     * The mask.
     */
    private int mask;
    /**
     * The last_x.
     */
    private int last_x;
    /**
     * The last_y.
     */
    private int last_y;
    
    /** The key mapping. */
    final private RobotKeyMap map;
    
    /** The framebuffer data. */
    private byte[] frameBuffer;

    /**
     * Instance.
     *
     * @return the robot device
     */
    static RobotDevice instance() {
        if (null == device) {
            device = new RobotDevice();
        }
        return device;
    }

    /**
     * Checks if is supported.
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
     * Instantiates a new robot device.
     */
    private RobotDevice() {
        mask = 0;
        last_x = -1;
        last_y = -1;
        map = new RobotKeyMap();
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(RobotDevice.class.getName()).log(Level.SEVERE, null, ex);
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
     * @see org.javnce.vnc.server.platform.KeyBoardDevice#keyEvent(boolean, long)
     */
    @Override
    public void keyEvent(boolean down, long key) {
        int keysym = (int) (key & 0xFFFFFFFFl);
        int code = map.keysymToRobotCode(keysym);

        if (KeyEvent.VK_UNDEFINED != code) {
            try {
                if (down) {
                    robot.keyPress(code);
                } else {
                    robot.keyRelease(code);
                }
            } catch (Throwable e) {
                StringBuilder builder = new StringBuilder(1000);
                builder.append("Key event failure:");
                builder.append(" down=").append(down);
                builder.append(" keysym=").append(keysym).append(" 0x").append(Integer.toHexString(keysym));
                builder.append(" Java code=").append(code).append(" 0x").append(Integer.toHexString(code));
                Logger.getLogger(this.getClass().getName()).info(builder.toString());
            }
        }
    }

    /**
     * Checks if is scroll event.
     *
     * @param mask the mask
     * @return true, if is scroll event
     */
    private boolean isScrollEvent(int mask) {
        boolean isScroll = (0 != (mask & ScrollMask));
        return isScroll;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.PointerDevice#pointerEvent(int, int, int)
     */
    @Override
    public void pointerEvent(int mask, int x, int y) {
        if (x != last_x || y != last_y) {
            last_x = x;
            last_y = y;
            robot.mouseMove(last_x, last_y);
        }
        if (isScrollEvent(mask)) {
            scrollEvent(mask);
        } else {
            absoluteEvent(mask, x, y);
        }
    }

    /**
     * Absolute event.
     *
     * @param newMask the new mask
     * @param x the x
     * @param y the y
     */
    private void absoluteEvent(int newMask, int x, int y) {
        for (int i = 0; i < 3; i++) {
            int bitMask = 1 << i;
            int javaMask = 0;

            if ((mask & bitMask) != (newMask & bitMask)) {

                switch (i) {
                    case 0:
                        javaMask = InputEvent.BUTTON1_MASK;
                        break;
                    case 1:
                        javaMask = InputEvent.BUTTON2_MASK;
                        break;
                    case 2:
                        javaMask = InputEvent.BUTTON3_MASK;
                        break;
                }

                boolean is_press = ((newMask & bitMask) == bitMask);

                if (is_press) {
                    robot.mousePress(javaMask);
                } else {
                    robot.mouseRelease(javaMask);
                }
            }
        }
        mask = newMask;
    }

    /**
     * Scroll event.
     *
     * @param mask the mask
     */
    private void scrollEvent(int mask) {

        if (0 != (mask & (1 << 3))) {
            //Up scroll
            robot.mouseWheel(-1);
        } else if (0 != (mask & (1 << 4))) {
            //Down scroll
            robot.mouseWheel(1);
        }
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
