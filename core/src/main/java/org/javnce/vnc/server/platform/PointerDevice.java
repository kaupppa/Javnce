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

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class PointerDevice {

    public static PointerDevice factory(boolean fullAccessMode) {
        PointerDevice dev = null;
        if (fullAccessMode) {
            if (XTestPointerInputDevice.isPointerSupported()) {
                dev = new XTestPointerInputDevice();
            } else if (RobotPointerDevice.isSupported()) {
                dev = RobotPointerDevice.instance();
            }
        }
        if (null == dev) {
            dev = new DummyPointerDevice();
        }
        Logger.getLogger(PointerDevice.class.getName())
                .log(Level.INFO, "Using {0}", dev.getClass().getName());

        return dev;
    }

    public abstract void pointerEvent(int mask, int x, int y);
}
