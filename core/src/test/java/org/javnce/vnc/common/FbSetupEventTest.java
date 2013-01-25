/* * Copyright (C) 2013  Pauli Kauppinen
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
package org.javnce.vnc.common;

import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;
import static org.junit.Assert.*;
import org.junit.Test;

public class FbSetupEventTest {
	private static final PixelFormat format = null;
	private static final Size size = null;
	private static final String name = null;

    @Test
    public void testId() {
        assertEquals(FbSetupEvent.eventId(), new FbSetupEvent(format, size, name).Id());
    }

    @Test
    public void testGet() {
        FbSetupEvent event = new FbSetupEvent(format, size, name);
        assertEquals(format, event.format());
        assertEquals(size, event.size());
        assertEquals(name, event.name());
    }
}