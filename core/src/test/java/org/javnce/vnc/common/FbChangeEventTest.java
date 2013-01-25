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

import java.util.ArrayList;
import org.javnce.rfb.types.Rect;
import static org.junit.Assert.*;
import org.junit.Test;

public class FbChangeEventTest {
	private static final ArrayList<Rect> list = null;
    @Test
    public void testId() {
        assertEquals(FbChangeEvent.eventId(), new FbChangeEvent(list).Id());
    }

    @Test
    public void testGet() {
        
        FbChangeEvent event = new FbChangeEvent(list);
        assertEquals(list, event.get());
    }
}
