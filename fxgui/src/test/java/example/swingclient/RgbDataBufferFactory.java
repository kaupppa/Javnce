/*
 * Copyright (C) 2012 Pauli Kauppinen
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
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package example.swingclient;

import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * A factory for creating RgbDataBuffer object of given format and size.
 */
public class RgbDataBufferFactory {

    /**
     * Factory.
     *
     * @param size the size
     * @param format the format
     * @return the rgb data buffer
     */
    public static RgbDataBuffer factory(Size size, PixelFormat format) {
        RgbDataBuffer dataBuffer = null;

        try {
            dataBuffer = create(size, format);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return dataBuffer;

    }

    /**
     * Creates the buffer.
     *
     * @param size the size
     * @param format the format
     * @return the rgb data buffer
     * @throws Exception the exception
     */
    private static RgbDataBuffer create(Size size, PixelFormat format) throws Exception {
        RgbDataBuffer dataBuffer = null;

        if (32 == format.bitsPerPixel()) {
            dataBuffer = new RgbDataBuffer32Bit(size, format);
        } else {
            throw new Exception("Not yet implemented");
        }

        return dataBuffer;

    }
}