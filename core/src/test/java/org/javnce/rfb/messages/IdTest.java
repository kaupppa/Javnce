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
package org.javnce.rfb.messages;

import static org.junit.Assert.*;
import org.junit.Test;

public class IdTest {

    @Test
    public void testValueOf() {
        assertEquals(Id.Bell, Id.valueOf("Bell"));
        assertEquals(Id.ClientCutText, Id.valueOf("ClientCutText"));
        assertEquals(Id.ClientInit, Id.valueOf("ClientInit"));
        assertEquals(Id.Factory, Id.valueOf("Factory"));
        assertEquals(Id.FramebufferUpdate, Id.valueOf("FramebufferUpdate"));
        assertEquals(Id.FramebufferUpdateRequest, Id.valueOf("FramebufferUpdateRequest"));
        assertEquals(Id.KeyEvent, Id.valueOf("KeyEvent"));
        assertEquals(Id.PointerEvent, Id.valueOf("PointerEvent"));
        assertEquals(Id.ProtocolVersion, Id.valueOf("ProtocolVersion"));
        assertEquals(Id.SecurityResult, Id.valueOf("SecurityResult"));
        assertEquals(Id.SecurityTypeList, Id.valueOf("SecurityTypeList"));
        assertEquals(Id.SelectedSecurityType, Id.valueOf("SelectedSecurityType"));
        assertEquals(Id.ServerCutText, Id.valueOf("ServerCutText"));
        assertEquals(Id.ServerInit, Id.valueOf("ServerInit"));
        assertEquals(Id.SetColourMapEntries, Id.valueOf("SetColourMapEntries"));
        assertEquals(Id.SetEncodings, Id.valueOf("SetEncodings"));
        assertEquals(Id.SetPixelFormat, Id.valueOf("SetPixelFormat"));
        assertEquals(Id.Unknown, Id.valueOf("Unknown"));
    }

    @Test
    public void testValues() {
        
        Id[] values = Id.values();
        assertTrue(0< values.length);
    }
}
