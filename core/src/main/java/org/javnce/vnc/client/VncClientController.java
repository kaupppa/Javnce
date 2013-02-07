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
import org.javnce.eventing.EventLoop;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.vnc.common.FbFormatEvent;
import org.javnce.vnc.common.FbRequestEvent;
import org.javnce.vnc.common.KeyEvent;
import org.javnce.vnc.common.PointerEvent;

/**
 * The Class VncClientController handles the VNC socket thread.
 *
 * The VncClientController is thread safe.
 */
public class VncClientController {

    /**
     * The client.
     */
    private VncClient client;

    /**
     * Instantiates a new VNC client controller.
     */
    public VncClientController() {
    }

    /**
     * Launch the protocol thread.
     *
     * Can be called once.
     *
     * @param address the address of server
     * @param observer the observer
     */
    synchronized public void launch(InetSocketAddress address, RemoteVncServerObserver observer) {
        if (null == client) {
            client = new VncClient(address, observer);
            client.start();
        }
    }

    /**
     * Shutdown the protocol thread.
     */
    synchronized public void shutdown() {
        if (null != client) {
            client.shutdown();
            client = null;
        }
    }

    /**
     * Send pointer event.
     *
     * @param mask the mask
     * @param x the x
     * @param y the y
     */
    static public void pointerEvent(int mask, int x, int y) {
        EventLoop.publishToRootGroup(new PointerEvent(mask, new Point(x, y)));
    }

    /**
     * Send key event.
     *
     * @param down the down
     * @param key the key
     */
    static public void keyEvent(boolean down, long key) {
        EventLoop.publishToRootGroup(new KeyEvent(down, key));
    }

    /**
     * Send set pixel format.
     *
     * @param format the new format
     */
    static public void setFormat(PixelFormat format) {
        EventLoop.publishToRootGroup(new FbFormatEvent(format));
    }

    /**
     * Request frame buffer.
     *
     * @param incremental the incremental
     * @param rect the area
     */
    static public void requestFramebuffer(boolean incremental, Rect rect) {
        EventLoop.publishToRootGroup(new FbRequestEvent(incremental, rect));
    }
}
