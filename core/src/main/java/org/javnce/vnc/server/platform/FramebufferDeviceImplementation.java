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

/**
 * The Class FramebufferDeviceImplementation wraps the real frame buffer access
 * class.
 *
 * The class is just for reduce JNI calls.
 */
class FramebufferDeviceImplementation extends FramebufferDevice {

    private final Size size;
    private final PixelFormat format;
    private final FramebufferDevice dev;

    FramebufferDeviceImplementation() {
        dev = create();
        size = dev.size();
        format = dev.format();
    }

    @Override
    public Size size() {
        return size;
    }

    @Override
    public PixelFormat format() {
        return format;
    }

    @Override
    public ByteBuffer[] buffer(int x, int y, int width, int height) {
        return dev.buffer(x, y, width, height);
    }

    @Override
    public void grabScreen() {
        dev.grabScreen();
    }

    /**
     * Creates FramebufferDevice for current environment.
     *
     * @return the frame buffer device
     */
    static private FramebufferDevice create() {
        FramebufferDevice dev = null;

        if (XShmFramebuffer.isSupported()) {
            dev = new XShmFramebuffer();
        } else if (Win32GdiFramebuffer.isSupported()) {
            dev = new Win32GdiFramebuffer();
        } else {
            throw new RuntimeException("No FramebufferDevice implementation found");
        }
        Logger.getLogger(FramebufferDeviceImplementation.class.getName())
                .log(Level.INFO, "Using {0}", dev.getClass().getName());

        return dev;
    }
}