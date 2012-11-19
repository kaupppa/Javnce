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
 * The Class PixelFormat provides framebuffer format.
 *
 * @see org.javnce.rfb.messages.MsgSetPixelFormat
 * @see org.javnce.rfb.messages.MsgServerInit
 */
public class PixelFormat {

    /**
     * The bits per pixel.
     */
    private final int bitsPerPixel;
    /**
     * The depth of RGB.
     */
    private final int depth;
    /**
     * The big endian.
     */
    private final boolean bigEndian;
    /**
     * The true colour.
     */
    private final boolean trueColour;
    /**
     * The max.
     */
    private final Color max;
    /**
     * The shift.
     */
    private final Color shift;
    /**
     * The bytes per pixel.
     */
    private final int bytesPerPixel;

    /**
     * Instantiates a new pixel format.
     *
     * @param bitsPerPixel the bits per pixel
     * @param depth the depth of RGB
     * @param bigEndian the big endian
     * @param trueColour the true colour
     * @param max the max
     * @param shift the shift
     */
    public PixelFormat(int bitsPerPixel, int depth, boolean bigEndian, boolean trueColour, Color max, Color shift) {
        this.bitsPerPixel = bitsPerPixel;
        this.depth = depth;
        this.bigEndian = bigEndian;
        this.trueColour = trueColour;
        this.max = max;
        this.shift = shift;
        bytesPerPixel = bitsPerPixel / 8;
    }

    /**
     * Bits per pixel getter.
     *
     * @return 32 for ARGB888, 16 for RGB565
     */
    public int bitsPerPixel() {
        return this.bitsPerPixel;
    }

    /**
     * Bytes per pixel getter.
     *
     * @return 4 for ARGB888, 2 for RGB565
     */
    public int bytesPerPixel() {
        return this.bytesPerPixel;
    }

    /**
     * RGB depth getter.
     *
     * @return 24 for ARGB888, 16 for RGB565
     */
    public int depth() {
        return this.depth;
    }

    /**
     * Big endian getter.
     *
     * @return true, if big endian
     */
    public boolean bigEndian() {
        return this.bigEndian;
    }

    /**
     * True colour.
     *
     * @return true, if true colour
     */
    public boolean trueColour() {
        return this.trueColour;
    }

    /**
     * RGB max getter.
     *
     * @return the max values of RGB
     */
    public Color max() {
        return this.max;
    }

    /**
     * RGB shift getter.
     *
     * @return the shift of RGB
     */
    public Color shift() {
        return this.shift;
    }

    /**
     * Checks if alpha is present.
     *
     * @return true, if bitsPerPixel > depth
     */
    public boolean hasAlpha() {
        return (bitsPerPixel > depth);
    }

    /**
     * Red mask getter.
     *
     * @return 0xFF0000 if ARGB888 and 0xF800 if RGB565;
     */
    public int redMask() {
        return max().red() << shift().red();
    }

    /**
     * Green mask getter.
     *
     * @return 0xFF00 if ARGB888 and 0x7E0 if RGB565;
     */
    public int greenMask() {
        return max().green() << shift().green();
    }

    /**
     * Blue mask getter.
     *
     * @return 0xFF if ARGB888 and 0x1F if RGB565;
     */
    public int blueMask() {
        return max().blue() << shift().blue();
    }

    /**
     * Alpha mask.
     *
     * @return 0xFF000000 if ARGB888 and zero if no alpha
     */
    public int alphaMask() {
        int alphaMask = 0;

        if (hasAlpha()) {
            alphaMask = 0xFF;

            if (32 == bitsPerPixel) {
                alphaMask = 0xFFFFFFFF;
            } else if (16 == bitsPerPixel) {
                alphaMask = 0xFFFF;
            }

            alphaMask ^= redMask() | greenMask() | blueMask();

        }

        return alphaMask;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        boolean areEqual = false;

        if (this == other) {
            areEqual = true;
        } else if ((other instanceof PixelFormat)) {
            PixelFormat theOther = (PixelFormat) other;

            areEqual = (this.bitsPerPixel == theOther.bitsPerPixel
                    && this.depth == theOther.depth
                    && this.bigEndian == theOther.bigEndian
                    && this.trueColour == theOther.trueColour
                    && this.max.equals(theOther.max)
                    && this.shift.equals(theOther.shift));
        }

        return areEqual;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return String.format("%s(bitsPerPixel:%d depth:%d)", this.getClass().getName(), bitsPerPixel, depth);
    }

    /**
     * Creates the RGB565 format.
     *
     * @return the pixel format
     */
    static public PixelFormat createRGB565() {
        return new PixelFormat(16, 16, false, true, new Color(31, 63, 31), new Color(11, 5, 0));
    }

    /**
     * Creates the ARGB888 format.
     *
     * @return the pixel format
     */
    static public PixelFormat createARGB888() {
        return new PixelFormat(32, 24, false, true, new Color(255, 255, 255), new Color(16, 8, 0));
    }
}
