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

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * A factory for creating BufferedImage objects of given size and format.
 */
public class RgbImageFactory {

    /**
     * Factory.
     *
     * @param dataBuffer the data buffer
     * @param size the size
     * @param format the format
     * @return the buffered image
     */
    public static BufferedImage factory(RgbDataBuffer dataBuffer, Size size, PixelFormat format) {

        ColorModel model = createColorModel(format);

        SampleModel sample = createSampleModel(dataBuffer.dataBuffer().getDataType(), size, format);

        WritableRaster raster = createWritableRaster(sample, dataBuffer.dataBuffer());

        BufferedImage image = new BufferedImage(model, raster, true, null);

        return image;
    }

    /**
     * Creates a new ColorModel object.
     *
     * @param format the format
     * @return the color model
     */
    static private ColorModel createColorModel(PixelFormat format) {

        int redMask = format.redMask();
        int greenMask = format.greenMask();
        int blueMask = format.blueMask();
        DirectColorModel model = new DirectColorModel(format.depth(), redMask, greenMask, blueMask);
        return model;
    }

    /**
     * Creates a new SampleModel object.
     *
     * @param type the type
     * @param size the size
     * @param format the format
     * @return the sample model
     */
    static private SampleModel createSampleModel(int type, Size size, PixelFormat format) {

        int redMask = format.redMask();
        int greenMask = format.greenMask();
        int blueMask = format.blueMask();

        int[] bitOffsets = new int[]{redMask, greenMask, blueMask};
        SampleModel sample = new SinglePixelPackedSampleModel(type, size.width(), size.height(), bitOffsets);
        return sample;
    }

    /**
     * Creates a new WritableRaster object.
     *
     * @param model the model
     * @param dataBuffer the data buffer
     * @return the writable raster
     */
    static private WritableRaster createWritableRaster(SampleModel model, DataBuffer dataBuffer) {
        return Raster.createWritableRaster(model, dataBuffer, null);
    }

    private RgbImageFactory() {
    }
}