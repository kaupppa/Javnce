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

import org.javnce.rfb.types.Rect;
import static org.junit.Assert.*;
import org.junit.Test;

public class FbRequestEventTest {
	private static final boolean incremental = true;
	private static final Rect rect = null;

    @Test
    public void testId() {
        assertEquals(FbRequestEvent.eventId(), new FbRequestEvent(incremental, rect).Id());
    }

    @Test
    public void testGet() {
        FbRequestEvent event = new FbRequestEvent(incremental, rect);
        assertEquals(incremental, event.incremental());
        assertEquals(rect, event.rect());
    }
}