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
 * The Class Size wraps width and height.
 *
 */
public class Size {

    /**
     * The width.
     */
    private final int width;
    /**
     * The height.
     */
    private final int height;

    /**
     * Instantiates a new size.
     *
     * @param width the width
     * @param height the height
     */
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Width getter.
     *
     * @return the width
     */
    public int width() {
        return this.width;
    }

    /**
     * Height getter.
     *
     * @return the height
     */
    public int height() {
        return this.height;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        boolean areEqual = false;

        if (this == other) {
            areEqual = true;
        } else if ((other instanceof Size)) {
            Size theOther = (Size) other;

            areEqual = (this.width == theOther.width
                    && this.height == theOther.height);
        }

        return areEqual;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return String.format("%s(%d*%d)", this.getClass().getName(), width, height);
    }
}
