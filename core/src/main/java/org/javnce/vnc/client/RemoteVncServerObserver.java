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
package org.javnce.vnc.client;

import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * The VNC client observer interface.
 */
public interface RemoteVncServerObserver {

    /**
     * This method is called when connection closes.
     */
    void connectionClosed();

    /**
     * This method is called when framebuffer format and size is available.
     *
     * @param format the framebuffer format
     * @param size the framebuffer size
     */
    void initFramebuffer(PixelFormat format, Size size);

    /**
     * This method is called when framebuffer update is received.
     *
     * @param buffers the framebuffers
     */
    void framebufferUpdate(Framebuffer[] buffers);
}
