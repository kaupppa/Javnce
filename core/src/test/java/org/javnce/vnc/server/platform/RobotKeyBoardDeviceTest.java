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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class RobotKeyBoardDeviceTest {

    @Test
    public void testInstance() {
        RobotKeyBoardDevice dev = RobotKeyBoardDevice.instance();

        assertNotNull(dev);
    }

    @Test
    public void testIsSupported() {
        assertTrue(RobotKeyBoardDevice.isSupported());
    }

    @Test
    public void testKeyEvent() {
        RobotKeyBoardDevice dev = RobotKeyBoardDevice.instance();
        //keysym 0 should not cause an event
        dev.keyEvent(true, 0x0);
    }
}
