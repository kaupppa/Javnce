/*
 * Copyright (C) 2013 Pauli Kauppinen
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
package org.javnce.vnc.server.platform;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * The Class RobotPointerDevice injects pointer events Robot class.
 */
class RobotPointerDevice extends PointerDevice {

    /**
     * The instance.
     */
    static private RobotPointerDevice device = null;
    /**
     * The robot.
     */
    private Robot robot;
    /**
     * The previousMask for scroll events.
     */
    static private final int ScrollMask = (1 << 3) | (1 << 4);
    /**
     * The mask of previously injected.
     */
    private int previousMask;
    /**
     * The last_x.
     */
    private int last_x;
    /**
     * The last_y.
     */
    private int last_y;

    /**
     * Instance.
     *
     * @return the robot pointer device
     */
    static RobotPointerDevice instance() {
        if (null == device) {
            device = new RobotPointerDevice();
        }
        return device;
    }

    /**
     * Checks if Robot is supported.
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
     * Instantiates a new robot pointer device.
     */
    private RobotPointerDevice() {
        previousMask = 0;
        last_x = -1;
        last_y = -1;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
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
     * @param mask the new mask
     * @param x the x
     * @param y the y
     */
    private void absoluteEvent(int mask, int x, int y) {
        for (int i = 0; i < 3; i++) {
            int bitMask = 1 << i;
            int javaMask = 0;

            if ((previousMask & bitMask) != (mask & bitMask)) {

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

                boolean is_press = ((mask & bitMask) == bitMask);

                if (is_press) {
                    robot.mousePress(javaMask);
                } else {
                    robot.mouseRelease(javaMask);
                }
            }
        }
        previousMask = mask;
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
}
