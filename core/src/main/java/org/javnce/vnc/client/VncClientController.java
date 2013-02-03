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

import java.net.InetSocketAddress;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Rect;

/**
 * The VNC client controller class.
 */
public class VncClientController {

    /**
     * The controller instance.
     */
    final static private VncClientController controller = new VncClientController();
    /**
     * The VNC client.
     */
    private VncClient client;

    /**
     * Instance.
     *
     * @return the VNC client controller
     */
    public static VncClientController instance() {
        return controller;
    }

    /**
     * Instantiates a new VNC client controller.
     */
    private VncClientController() {
        client = null;
    }

    /**
     * Start a VNC client.
     *
     * @param address the address
     * @param observer the observer
     */
    public void start(InetSocketAddress address, RemoteVncServerObserver observer) {
        shutdown();
        client = new VncClient(address, observer);
        client.start();
    }

    /**
     * Shutdown VNC client.
     */
    public void shutdown() {
        //logger.info("");
        if (null != client) {
            client.shutdown();
            client = null;
        }
    }

    /**
     * Pointer event.
     *
     * @param mask the mask
     * @param x the x
     * @param y the y
     */
    public void pointerEvent(int mask, int x, int y) {
        //logger.info("");
        if (null != client) {
            client.pointerEvent(mask, x, y);
        }
    }

    /**
     * Key event.
     *
     * @param down the down
     * @param key the key
     */
    public void keyEvent(boolean down, long key) {
        //logger.info("");
        if (null != client) {
            client.keyEvent(down, key);
        }
    }

    /**
     * Sets the format.
     *
     * @param format the new format
     */
    public void setFormat(PixelFormat format) {
        //logger.info("");
        if (null != client) {
            client.setFormat(format);
        }
    }

    /**
     * Request framebuffer.
     *
     * @param incremental the incremental
     * @param rect the rect
     */
    public void requestFramebuffer(boolean incremental, Rect rect) {
        //logger.info("");
        if (null != client) {
            client.requestFramebuffer(incremental, rect);
        }
    }
}
