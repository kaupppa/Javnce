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
 * The Class XTestPointerInputDevice is Linux specific way to inject pointer
 * events. Is a Jni implementation.
 */
class XTestPointerInputDevice implements PointerDevice {

    /**
     * The lib name.
     */
    final static private String libName = "XTestPointerInputDevice";

    static {

        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
            Logger.getLogger(XTestPointerInputDevice.class.getName())
                    .log(Level.INFO, "Couldn't load " + libName, e);
        }
    }

    /**
     * Checks if is pointer supported.
     *
     * @return true, if is pointer supported
     */
    static boolean isPointerSupported() {
        boolean valid = false;

        try {
            valid = new XTestPointerInputDevice().canSupport();
        } catch (Throwable e) {
            Logger.getLogger(XTestPointerInputDevice.class.getName())
                    .log(Level.INFO, "Couldn't load " + libName, e);
        }
        return valid;
    }

    /**
     * Can support.
     *
     * @return true, if successful
     */
    public native boolean canSupport();

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.PointerDevice#pointerEvent(int, int, int)
     */
    @Override
    public native void pointerEvent(int mask, int x, int y);
}
