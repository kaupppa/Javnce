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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A factory for creating platform with full access.
 */
public class FullAccessPlatformFactory implements PlatformFactory {

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.PlatformFactory#createFramebufferDevice()
     */
    @Override
    public FramebufferDevice createFramebufferDevice() {
        FramebufferDevice dev;
        if (XShmFramebuffer.isSupported()) {
            dev = new XShmFramebuffer();
        } else if (RobotDevice.isSupported()) {
            dev = RobotDevice.instance();
        } else {
            dev = new DummyDevice();
        }
        Logger.getLogger(FullAccessPlatformFactory.class.getName())
                .log(Level.INFO, "Using {0}", dev.getClass().getName());
        return dev;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.PlatformFactory#createPointerDevice()
     */
    @Override
    public PointerDevice createPointerDevice() {
        PointerDevice dev;

        if (XTestPointerInputDevice.isPointerSupported()) {
            dev = new XTestPointerInputDevice();
        } else if (RobotDevice.isSupported()) {
            dev = RobotDevice.instance();
        } else {
            dev = new DummyDevice();
        }
        Logger.getLogger(FullAccessPlatformFactory.class.getName())
                .log(Level.INFO, "Using {0}", dev.getClass().getName());
        return dev;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.PlatformFactory#createKeyBoardDevice()
     */
    @Override
    public KeyBoardDevice createKeyBoardDevice() {
        KeyBoardDevice dev;
        if (XTestKeyBoard.isKeyBoardSupported()) {
            dev = new XTestKeyBoard();
        } else if (RobotDevice.isSupported()) {
            dev = RobotDevice.instance();
        } else {
            dev = new DummyDevice();
        }

        Logger.getLogger(FullAccessPlatformFactory.class.getName())
                .log(Level.INFO, "Using {0}", dev.getClass().getName());
        return dev;
    }
}
