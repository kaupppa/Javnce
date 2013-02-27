/*
 * Copyright (C) 2012 Pauli Kauppinen
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

import java.awt.event.KeyEvent;
import org.javnce.vnc.common.KeyMap;
import static org.junit.Assert.*;
import org.junit.Test;

public class KeysymToAwtKeyCodeTest {

    class TestData {

        public final int keysym;
        public final int unicode;

        TestData(int keysym, int unicode) {
            this.keysym = keysym;
            this.unicode = unicode;
        }
    };

    @Test
    public void testKeysymToRobotCode() {
        TestData data[] = new TestData[]{
            new TestData(KeyMap.None, KeyEvent.VK_UNDEFINED), //None unicode
            new TestData(0x20, 0x20),
            new TestData(KeyMap.XK_Alt_L, KeyEvent.VK_ALT), //with mapping
            new TestData(KeyMap.XK_Alt_R, KeyEvent.VK_ALT), //with mapping
            new TestData('a', 'A'),
            new TestData('A', 'A'),
            new TestData('z', 'Z'),
            new TestData('Z', 'Z'),};
        for (int i = 0; i < data.length; i++) {
            int value = KeysymToAwtKeyCode.get(data[i].keysym);

            assertEquals(data[i].unicode, value);
        }
    }
}
