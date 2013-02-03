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

class XShmFramebuffer extends FramebufferDevice {

    final static private String libName = "XShmFramebuffer";

    static {

        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
            //Logger.getLogger(XShmFramebuffer.class.getName()).log(Level.INFO, "Couldn't load " + libName, e);
        }

    }

    static boolean isSupported() {
        boolean valid = false;

        try {
            valid = new XShmFramebuffer().hasXShm();
        } catch (Throwable e) {
            //Logger.getLogger(XShmFramebuffer.class.getName()).log(Level.INFO, "Couldn't load " + libName, e);
        }
        return valid;
    }

    private native boolean hasXShm();

    @Override
    public native Size size();

    @Override
    public native PixelFormat format();

    @Override
    public native ByteBuffer[] buffer(int x, int y, int width, int height);

    @Override
    public native void grabScreen();
}
