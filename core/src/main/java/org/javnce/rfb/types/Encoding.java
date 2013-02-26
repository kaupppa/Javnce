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
 * The Class Encoding provides constants of frame buffer encoding values.
 *
 */
public class Encoding {

    /**
     * The mandatory RAW encoding.
     */
    public static final int RAW = 0;
    /**
     * The Run-length Encoding.
     *
     * The format is following:
     *
     * <table border="1"> <tr><th>No. of
     * bytes</th><th>Type</th><th>Description</th></tr>
     * <tr><td>4</td><td>U32</td><td>Rle data length in bytes</td></tr>
     * <tr><td>N*Rle Pixel</td><td>Rle Pixel</td><td>Array of Rle
     * Pixel</td></tr> </table> <p>The Rle Pixel format is following:</p> <table
     * border="1"> <tr><th>No. of
     * bytes</th><th>Type</th><th>Description</th></tr>
     * <tr><td>1</td><td>U8</td><td>Pixel run count -1 (0=1 runs, 1=2 runs...,
     * 0xff=256 runs)</td></tr> <tr><td>n</td><td>Pixel</td><td>Exactly as in
     * Raw format (including alpha)</td></tr> </table>
     */
    public static final int RLE = -666;
    /**
     * The LZ4 Encoding.
     *
     * The format is following:
     *
     * <table border="1"> <tr><th>No. of
     * bytes</th><th>Type</th><th>Description</th></tr>
     * <tr><td>4</td><td>U32</td><td>LZ4 data length in bytes</td></tr>
     * <tr><td>N</td><td>LZ4 compressed data</td><td>See <a
     * href="http://code.google.com/p/lz4/">LZ4 project page</a> </td></tr>
     * </table>
     */
    public static final int LZ4 = -667;
}
