/*
 * Copyright (C) 2012 Pauli Kauppinen
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

import java.nio.ByteBuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * The Class Win32GdiFramebuffer provides access to frame buffer using Microsoft
 * Windows graphics device interface (GDI).
 *
 * The XShmFramebuffer is a native Microsoft Windows implementation.
 */
class Win32GdiFramebuffer extends FramebufferDevice {

    /**
     * The name of native implementation.
     */
    final static private String libName = getLibName();

    static {
        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
        }
    }

    /**
     * Gets the x64 or x86 implementation name for current environment.
     *
     * @return true, if native implementation is supported
     */
    static private String getLibName() {
        String name = "Win32GdiFramebuffer_x64";
        if (System.getProperty("os.arch").startsWith("x86")) {
            name = "Win32GdiFramebuffer_Win32";
        }
        return name;
    }

    /**
     * Checks if native implementation is available in current environment.
     *
     * @return true, if native implementation is supported
     */
    static boolean isSupported() {
        boolean valid = false;

        try {
            valid = new Win32GdiFramebuffer().hasGdiFramebuffer();
        } catch (Throwable e) {
            // Logger.getLogger(XShmFramebuffer.class.getName()).log(Level.INFO, "Couldn't load " + libName, e);
        }
        return valid;
    }

    /**
     * Checks that needed GDI features are supported.
     *
     * @return true, if supported
     */
    private native boolean hasGdiFramebuffer();

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
