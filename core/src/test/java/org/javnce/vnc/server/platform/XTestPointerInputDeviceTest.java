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

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import org.junit.Test;

public class XTestPointerInputDeviceTest {

    final private static boolean isLinux = System.getProperty("os.name").startsWith("Linux");

    @Test
    public void testIsPointerSupported() {
        assertEquals(isLinux, XTestPointerInputDevice.isPointerSupported());
    }

    @Test
    public void testCanSupport() {
        assumeTrue(isLinux);
        XTestPointerInputDevice t = new XTestPointerInputDevice();
        assertTrue(t.canSupport());
    }
}
