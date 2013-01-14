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

import static org.junit.Assert.*;
import org.junit.Test;

public class KeyMapTest {

    class TestData {

        public final int keysym;
        public final int unicode;

        TestData(int keysym, int unicode) {
            this.keysym = keysym;
            this.unicode = unicode;
        }
    };

    @Test
    public void testUnicodeToKeySym() {
        TestData data[] = new TestData[]{
            new TestData(KeyMap.None, 0x19), //None unicode
            new TestData(0x20, 0x20), //First unicode
            new TestData(0xff, 0xff),
            new TestData(KeyMap.XK_soliddiamond, 0x25C6), //Unicode with mapping
            new TestData(KeyMap.XK_Aogonek, 0x104), //Unicode with mapping
            new TestData(KeyMap.None, 0x10FFFF + 1), //None unicode
            new TestData(0x1000174, 0x174), //Unicode with plus 0x1000000
        };

        for (int i = 0; i < data.length; i++) {
            int value = KeyMap.unicodeToKeySym(data[i].unicode);

            assertEquals(data[i].keysym, value);
        }
    }

    @Test
    public void testMapping() {
        KeyMap<Integer> map = new KeyMap<>();

        final int Max = 100;
        for (int i = 1; i <= Max; i++) {
            map.addMapping(i, i);
        }

        for (int i = 1; i <= Max; i++) {
            int value = map.getMapped(i);
            assertEquals(i, value);
        }
        for (int i = Max + 1; i <= 2 * Max; i++) {
            int value = map.getMapped(i);
            assertEquals(KeyMap.None, value);
        }
    }
}
