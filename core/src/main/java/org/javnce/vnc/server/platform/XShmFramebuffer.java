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

import java.nio.ByteBuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * The Class XShmFramebuffer provides access to frame buffer using MIT-SHM
 * extension library.
 *
 * The XShmFramebuffer is a native Linux implementation.
 */
class XShmFramebuffer extends FramebufferDevice {

    /**
     * The name of native implementation.
     */
    final static private String libName = "XShmFramebuffer";

    static {

        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
        }
    }

    /**
     * Checks if native implementation is available in current environment.
     *
     * @return true, if native implementation is supported
     */
    static boolean isSupported() {
        boolean valid = false;

        try {
            valid = new XShmFramebuffer().hasXShm();
        } catch (Throwable e) {
        }
        return valid;
    }

    /**
     * Checks if MIT-SHM is available
     *
     * @return true, if successful
     */
    private native boolean hasXShm();

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#size()
     */
    @Override
    public native Size size();

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#format()
     */
    @Override
    public native PixelFormat format();

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#buffer(int, int, int, int)
     */
    @Override
    public native ByteBuffer[] buffer(int x, int y, int width, int height);

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#grabScreen()
     */
    @Override
    public native void grabScreen();
}
