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
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 * The Class RobotKeyBoardDevice injects key events using Robot class.
 */
class RobotKeyBoardDevice extends KeyBoardDevice {

    /**
     * The instance.
     */
    static private RobotKeyBoardDevice device = null;
    /**
     * The robot.
     */
    private Robot robot;

    /**
     * Instance.
     *
     * @return the robot keyboard device
     */
    static RobotKeyBoardDevice instance() {
        if (null == device) {
            device = new RobotKeyBoardDevice();
        }
        return device;
    }

    /**
     * Checks if robot supported.
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
     * Instantiates a new robot key board device.
     */
    private RobotKeyBoardDevice() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
        }
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.KeyBoardDevice#keyEvent(boolean, long)
     */
    @Override
    public void keyEvent(boolean down, long key) {
        int keysym = (int) (key & 0xFFFFFFFFl);
        int code = KeysymToAwtKeyCode.get(keysym);

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
}
