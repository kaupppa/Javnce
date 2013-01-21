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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * The Class XShmFramebuffer is Linux specific way to grab framebuffer. Is a Jni
 * implementation.
 */
class XShmFramebuffer implements FramebufferDevice {

    /**
     * The lib name.
     */
    final static private String libName = "XShmFramebuffer";

    static {

        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
             Logger.getLogger(XShmFramebuffer.class.getName()).log(Level.INFO, "Couldn't load " + libName, e);
        }

    }

    /**
     * Checks if is supported.
     *
     * @return true, if is supported
     */
    static boolean isSupported() {
        boolean valid = false;

        try {
            valid = new XShmFramebuffer().hasXShm();
        } catch (Throwable e) {
            //Logger.getLogger(XShmFramebuffer.class.getName()).log(Level.INFO, "Couldn't load " + libName, e);
        }
        return valid;
    }

    /**
     * Checks for xshm.
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

	@Override
	public native void grabScreen();
}
