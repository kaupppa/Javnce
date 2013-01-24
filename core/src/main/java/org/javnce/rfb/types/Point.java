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
package org.javnce.rfb.types;

/**
 * The Class Point wraps x and y coordinate.
 *
 * @see Rect
 */
public class Point {

    /**
     * The x-coordinate.
     */
    private final int x;
    /**
     * The y-coordinate.
     */
    private final int y;

    /**
     * Instantiates a new point.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * X-coordinate getter.
     *
     * @return the x-coordinate
     */
    public int x() {
        return this.x;
    }

    /**
     * Y-coordinate getter.
     *
     * @return the y-coordinate
     */
    public int y() {
        return this.y;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        boolean areEqual = false;

        if (this == other) {
            areEqual = true;
        } else if ((other instanceof Point)) {
            Point theOther = (Point) other;

            areEqual = (this.x == theOther.x
                    && this.y == theOther.y);
        }

        return areEqual;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return String.format("%s(%d*%d)", this.getClass().getName(), x, y);
    }
}
