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
package org.javnce.eventing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A generic and thread safe {@link HashSet}
 *
 * @param <T> the generic type
 */
class ConcurrentSet<T> {

    /**
     * The set.
     */
    final private HashSet<T> set;

    /**
     * Instantiates a new concurrent set.
     */
    ConcurrentSet() {
        set = new HashSet<>();
    }

    /**
     * Adds item into set.
     *
     * @param item the item to be added
     */
    synchronized void add(T item) {
        if (null != item) {
            set.add(item);
        }
    }

    /**
     * Removes item from the set.
     *
     * @param item the item to be removed
     */
    synchronized void remove(T item) {
        set.remove(item);
    }

    /**
     * Checks if set is empty.
     *
     * @return true, if is empty
     */
    synchronized boolean isEmpty() {
        return set.isEmpty();
    }

    /**
     * Gets set as list.
     *
     * @return the list
     */
    synchronized List<T> get() {
        return new ArrayList<>(set);
    }
}
