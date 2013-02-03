/*
 * Copyright (C) 2012 Pauli
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

public class Win32GdiFramebuffer extends FramebufferDevice {

    final static private String libName = getLibName();

    static {
        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
            //Logger.getLogger(Win32GdiFramebuffer.class.getName()).log(Level.INFO, "Couldn't load " + libName, e);
        }
    }

    static private String getLibName() {
        String name = "Win32GdiFramebuffer_x64";
        if (System.getProperty("os.arch").startsWith("x86")) {
            name = "Win32GdiFramebuffer_Win32";
        }
        return name;
    }

    static boolean isSupported() {
        boolean valid = false;

        try {
            valid = new Win32GdiFramebuffer().hasGdiFramebuffer();
        } catch (Throwable e) {
            // Logger.getLogger(XShmFramebuffer.class.getName()).log(Level.INFO, "Couldn't load " + libName, e);
        }
        return valid;
    }

    private native boolean hasGdiFramebuffer();

    @Override
    public native Size size();

    @Override
    public native PixelFormat format();

    @Override
    public native ByteBuffer[] buffer(int x, int y, int width, int height);

    @Override
    public native void grabScreen();
}
