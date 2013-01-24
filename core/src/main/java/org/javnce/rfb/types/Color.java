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
 * The Class Color wraps the RGB values to single class.
 *
 * @see PixelFormat
 */
public class Color {

    /**
     * The red value.
     */
    private final int red;
    /**
     * The green value.
     */
    private final int green;
    /**
     * The blue value.
     */
    private final int blue;

    /**
     * Instantiates a new color.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     */
    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Red value getter.
     *
     * @return the Red as int
     */
    public int red() {
        return this.red;
    }

    /**
     * Green value getter.
     *
     * @return the Green as int
     */
    public int green() {
        return this.green;
    }

    /**
     * Blue value getter.
     *
     * @return the Blue as int
     */
    public int blue() {
        return this.blue;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        boolean areEqual = false;

        if (this == other) {
            areEqual = true;
        } else if ((other instanceof Color)) {
            Color theOther = (Color) other;

            areEqual = (this.red == theOther.red
                    && this.green == theOther.green
                    && this.blue == theOther.blue);
        }

        return areEqual;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return String.format("%s(R:%d G:*%d B:*%d)", this.getClass().getName(), red, green, blue);
    }
}
