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
 * The interface for an FramebufferDevice.
 */
public interface FramebufferDevice {

    /**
     * Size getter.
     *
     * @return the size
     */
    public abstract Size size();

    /**
     * Format getter.
     *
     * @return the pixel format
     */
    public abstract PixelFormat format();

    /**
     * Framebuffer getter.
     *
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     * @return the array of buffers
     */
    public abstract ByteBuffer[] buffer(int x, int y, int width, int height);

    /**
     * The method to grab screen. The method should take new screen capture.
     *
     */
    public abstract void grabScreen();
}
