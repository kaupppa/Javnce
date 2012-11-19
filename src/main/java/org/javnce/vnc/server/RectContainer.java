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
package org.javnce.vnc.server;

import java.util.ArrayList;
import java.util.ListIterator;
import org.javnce.rfb.types.Rect;

/**
 * Class for handling framebuffer change areas.
 */
class RectContainer {

    /**
     * The list of areas.
     */
    final private ArrayList<Rect> list;

    /**
     * Instantiates a new rect container.
     */
    RectContainer() {
        list = new ArrayList<>();
    }

    /**
     * Clear.
     */
    void clear() {
        list.clear();
    }

    /**
     * Gets list of changed areas. Overlapped areas are combined.
     *
     * @return the rect[]
     */
    Rect[] get() {
        mergeAreas();
        return list.toArray(new Rect[list.size()]);
    }

    /**
     * Checks if is empty.
     *
     * @return the boolean
     */
    Boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Adds the into list.
     *
     * @param areas the areas
     */
    void add(ArrayList<Rect> areas) {

        ListIterator<Rect> iter = areas.listIterator(0);

        while (iter.hasNext()) {
            Rect current = iter.next();
            add(current);
        }
    }

    /**
     * Adds the.
     *
     * @param rect the rect
     */
    private void add(Rect rect) {
        boolean added = false;
        ListIterator<Rect> iter = list.listIterator(0);

        while (iter.hasNext()) {
            Rect current = iter.next();

            if (rect.connecting(current)) {
                iter.set(rect.bounding(current));
                added = true;
                break;
            }
        }

        if (!added) {
            list.add(rect);
        }
    }

    /**
     * Merge areas.
     */
    private void mergeAreas() {
        ListIterator<Rect> iter = list.listIterator(0);

        while (iter.hasNext()) {
            Rect area = iter.next();

            if (findAndMerge(iter.nextIndex(), area)) {
                iter.remove();
            }
        }
    }

    /**
     * Find and merge areas.
     *
     * @param index the index
     * @param area the area
     * @return true, if area merged with overlapping area
     */
    private boolean findAndMerge(int index, Rect area) {
        boolean replaced = false;

        ListIterator<Rect> iter = list.listIterator(index);

        while (iter.hasNext()) {
            Rect current = iter.next();

            if (current.connecting(area)) {
                iter.set(current.bounding(area));
                replaced = true;
                break;
            }
        }

        return replaced;
    }
}
