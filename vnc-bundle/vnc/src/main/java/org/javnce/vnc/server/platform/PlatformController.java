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

/**
 * The Class PlatformController for controlling platform.
 */
public class PlatformController {

    /** The Constant manager. */
    final static private PlatformController manager = new PlatformController();
    
    /** The platform manager. */
    private PlatformManager platformManager;

    /**
     * Instance.
     *
     * @return the platform controller
     */
    public static PlatformController instance() {
        return manager;
    }

    /**
     * Instantiates a new platform controller.
     */
    private PlatformController() {
    }

    /**
     * Start.
     *
     * @param factory the factory
     */
    public void start(PlatformFactory factory) {
        shutdown();
        platformManager = new PlatformManager(factory);
        platformManager.start();
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        if (null != platformManager) {
            platformManager.shutdown();
            platformManager = null;
        }
    }

    /**
     * Gets the platform manager.
     *
     * @return the platform manager
     */
    public PlatformManager getPlatformManager() {
        return platformManager;
    }
}
