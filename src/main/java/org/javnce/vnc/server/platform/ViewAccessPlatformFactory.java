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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A factory for creating platform objects for view access. The PointerDevice
 * and KeyBoardDevice are dummy implementations.
 */
public class ViewAccessPlatformFactory extends FullAccessPlatformFactory {

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FullAccessPlatformFactory#createPointerDevice()
     */
    @Override
    public PointerDevice createPointerDevice() {

        PointerDevice dev = new DummyDevice();
        Logger.getLogger(FullAccessPlatformFactory.class.getName())
                .log(Level.INFO, "Using {0}", dev.getClass().getName());
        return dev;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FullAccessPlatformFactory#createKeyBoardDevice()
     */
    @Override
    public KeyBoardDevice createKeyBoardDevice() {
        KeyBoardDevice dev = new DummyDevice();

        Logger.getLogger(FullAccessPlatformFactory.class.getName())
                .log(Level.INFO, "Using {0}", dev.getClass().getName());
        return dev;
    }
}
