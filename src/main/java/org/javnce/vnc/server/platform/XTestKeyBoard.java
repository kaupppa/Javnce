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
 * The Class XTestKeyBoard is Linux specific way to inject key events. Is a Jni
 * implementation.
 */
class XTestKeyBoard implements KeyBoardDevice {

    /**
     * The lib name.
     */
    final static private String libName = "XTestKeyBoard";

    static {
        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
            Logger.getLogger(XTestKeyBoard.class.getName())
                    .log(Level.INFO, "Couldn't load " + libName, e);
        }
    }

    /**
     * Checks if is key board supported.
     *
     * @return true, if is key board supported
     */
    static boolean isKeyBoardSupported() {
        boolean valid = false;

        try {
            valid = new XTestKeyBoard().hasXTest();
        } catch (Throwable e) {
            Logger.getLogger(XTestKeyBoard.class.getName())
                    .log(Level.INFO, "Couldn't load " + libName, e);
        }

        return valid;
    }

    /**
     * Checks for xtest.
     *
     * @return true, if successful
     */
    public native boolean hasXTest();

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.KeyBoardDevice#keyEvent(boolean, long)
     */
    @Override
    public native void keyEvent(boolean down, long key);
}
