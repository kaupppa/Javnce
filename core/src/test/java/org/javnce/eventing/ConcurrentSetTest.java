/*
 * Copyright (C) 2013  Pauli Kauppinen
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
package org.javnce.eventing;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConcurrentSetTest {

    @Test
    public void testConcurrentSet() {
        ConcurrentSet<String> set = new ConcurrentSet<>();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testAdd() {
        ConcurrentSet<String> set = new ConcurrentSet<>();

        set.add(null);
        assertTrue(set.isEmpty());

        String item1 = "TADAAA";
        String item2 = "Jihaa";
        set.add(item1);
        set.add(item2);
        assertFalse(set.isEmpty());
    }

    @Test
    public void testRemove() {
        ConcurrentSet<String> set = new ConcurrentSet<>();

        set.add(null);
        String item1 = "TADAAA";
        String item2 = "Jihaa";
        set.add(item1);
        set.add(item2);

        set.remove(item1);
        assertFalse(set.isEmpty());

        set.remove(item2);
        assertTrue(set.isEmpty());

        set.remove(null);
        assertTrue(set.isEmpty());

        set.remove("Something else");
        assertTrue(set.isEmpty());
    }

    @Test
    public void testGet() {
        ConcurrentSet<String> set = new ConcurrentSet<>();

        assertTrue(set.get().isEmpty());

        set.add("Jihaa");
        assertFalse(set.get().isEmpty());
    }
}
