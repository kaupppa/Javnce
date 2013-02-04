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
 * The Class Rect contains area information (x,y,width,height).
 *
 */
public class Rect {

    /**
     * The upper left point of area.
     */
    private final int x1;
    private final int y1;
    /**
     * The lower right point of area.
     */
    private final int x2;
    private final int y2;

    /**
     * Instantiates a new rect.
     *
     * @param x is the x-coordinate
     * @param y is the y-coordinate
     * @param width is the width
     * @param height is the height
     */
    public Rect(int x, int y, int width, int height) {
        this.x1 = x;
        this.y1 = y;
        this.x2 = x + width - 1;
        this.y2 = y + height - 1;
    }

    /**
     * Instantiates a new rect.
     *
     * @param point the point
     * @param size the size
     */
    public Rect(Point point, Size size) {
        this.x1 = point.x();
        this.y1 = point.y();
        this.x2 = this.x1 + size.width() - 1;
        this.y2 = this.y1 + size.height() - 1;
    }

    /**
     * Instantiates a new rect.
     *
     * @param other the other
     */
    public Rect(Rect other) {
        this.x1 = other.x1;
        this.y1 = other.y1;
        this.x2 = other.x2;
        this.y2 = other.y2;
    }

    /**
     * Point getter.
     *
     * @return the upper left point of area
     */
    public Point point() {
        return new Point(x1, y1);
    }

    /**
     * X-coordinate getter.
     *
     * @return the x-coordinate
     */
    public int x() {
        return x1;
    }

    /**
     * Y-coordinate getter.
     *
     * @return the y-coordinate
     */
    public int y() {
        return y1;
    }

    /**
     * Size getter.
     *
     * @return the size of the area
     */
    public Size size() {
        return new Size(x2 - x1 + 1, y2 - y1 + 1);
    }

    /**
     * Width getter.
     *
     * @return the width
     */
    public int width() {
        return x2 - x1 + 1;
    }

    /**
     * Height getter.
     *
     * @return the height
     */
    public int height() {
        return y2 - y1 + 1;
    }

    /**
     * Tests if areas are overlapping
     *
     * @param other is the other area
     *
     * @return true if overlapping or partial overlapping
     */
    public Boolean overlaps(Rect other) {
        Boolean overlapping = true;

        if (other.x2 < x1
                || other.x1 > x2
                || other.y2 < y1
                || other.y1 > y2) {
            overlapping = false;
        }

        return overlapping;
    }

    /**
     * Tests if other area is inside this area.
     *
     * @param other is the other area
     *
     * @return true if other is inside this area
     */
    public Boolean contains(Rect other) {
        Boolean contains = false;

        if (other.x1 >= x1
                && other.x2 <= x2
                && other.y1 >= y1
                && other.y2 <= y2) {
            contains = true;
        }

        return contains;
    }

    /**
     * Returns an overlapping area
     *
     * @param other is the other area
     *
     * @return overlapping area or null if not overlapping
     */
    public Rect overlapping(Rect other) {
        Rect overlapping = null;

        if (overlaps(other)) {
            int r_x1 = Math.max(this.x1, other.x1);
            int r_y1 = Math.max(this.y1, other.y1);
            int r_x2 = Math.min(this.x2, other.x2);
            int r_y2 = Math.min(this.y2, other.y2);
            overlapping = new Rect(r_x1, r_y1, r_x2 - r_x1 + 1, r_y2 - r_y1 + 1);

        }

        return overlapping;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        boolean areEqual = false;

        if (this == other) {
            areEqual = true;
        } else if ((other instanceof Rect)) {
            Rect theOther = (Rect) other;

            areEqual = (this.x1 == theOther.x1
                    && this.y1 == theOther.y1
                    && this.x2 == theOther.x2
                    && this.y2 == theOther.y2);
        }

        return areEqual;
    }

    /**
     * Tests if areas are overlapping or share edge
     *
     * @param other is the other area
     *
     * @return true if no space between areas
     */
    public Boolean connecting(Rect other) {
        Boolean overlapping = true;

        if ((other.x2 + 1) < x1
                || other.x1 > (x2 + 1)
                || (other.y2 + 1) < y1
                || other.y1 > (y2 + 1)) {
            overlapping = false;
        }

        return overlapping;
    }

    /**
     * Get area that contains both areas
     *
     * @param other is the other area
     *
     * @return new area that contains both areas
     */
    public Rect bounding(Rect other) {
        int r_x1 = Math.min(this.x1, other.x1);
        int r_y1 = Math.min(this.y1, other.y1);
        int r_x2 = Math.max(this.x2, other.x2);
        int r_y2 = Math.max(this.y2, other.y2);
        return new Rect(r_x1, r_y1, r_x2 - r_x1 + 1, r_y2 - r_y1 + 1);
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return String.format("%s(%s,%s)", this.getClass().getName(), point(), size());
    }
}
