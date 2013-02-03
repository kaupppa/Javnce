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

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

public abstract class FramebufferDevice {

    public static FramebufferDevice factory() {
        FramebufferDevice dev = null;

        if (XShmFramebuffer.isSupported()) {
            dev = new XShmFramebuffer();
        } else if (Win32GdiFramebuffer.isSupported()) {
            dev = new Win32GdiFramebuffer();
        } else if (RobotFramebufferDevice.isSupported()) {
            dev = RobotFramebufferDevice.instance();
        }
        if (null == dev) {
            dev = new DummyFramebufferDevice();
        }
        Logger.getLogger(FramebufferDevice.class.getName())
                .log(Level.INFO, "Using {0}", dev.getClass().getName());

        return dev;
    }

    public abstract Size size();

    public abstract PixelFormat format();

    public abstract ByteBuffer[] buffer(int x, int y, int width, int height);

    public abstract void grabScreen();
}
