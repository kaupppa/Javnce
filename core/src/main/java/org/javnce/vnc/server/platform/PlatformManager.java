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

import org.javnce.eventing.EventLoop;

/**
 * The Class PlatformManager handles the devices, framebufferChangeDetector and
 * InputEventDispatcher.
 */
public class PlatformManager {

    /**
     * The framebuffer device.
     */
    final private FramebufferDevice framebufferDevice;
    /**
     * The pointer device.
     */
    final private PointerDevice pointerDevice;
    /**
     * The key board device.
     */
    final private KeyBoardDevice keyBoardDevice;
    /**
     * The framebuffer change detector.
     */
    private FramebufferChangeDetector framebufferChangeDetector;

    /**
     * Instantiates a new platform manager.
     *
     * @param factory the factory
     */
    public PlatformManager(PlatformFactory factory) {
        framebufferDevice = factory.createFramebufferDevice();
        pointerDevice = factory.createPointerDevice();
        keyBoardDevice = factory.createKeyBoardDevice();
    }

    /**
     * Start.
     */
    public void start() {
        InputEventDispatcher inputEventDispatcher = new InputEventDispatcher();
        inputEventDispatcher.start();

        framebufferChangeDetector = new FramebufferChangeDetector();
        framebufferChangeDetector.start();
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        EventLoop.shutdownAll();
        if (null != framebufferChangeDetector) {
            framebufferChangeDetector.interrupt();
            framebufferChangeDetector = null;
        }
    }

    /**
     * Gets the framebuffer device.
     *
     * @return the framebuffer device
     */
    public FramebufferDevice getFramebufferDevice() {
        return framebufferDevice;
    }

    /**
     * Gets the pointer device.
     *
     * @return the pointer device
     */
    public PointerDevice getPointerDevice() {
        return pointerDevice;
    }

    /**
     * Gets the key board device.
     *
     * @return the key board device
     */
    public KeyBoardDevice getKeyBoardDevice() {
        return keyBoardDevice;
    }
}
