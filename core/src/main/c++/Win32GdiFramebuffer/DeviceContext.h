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
#ifndef DEVICECONTEXT_H
#define DEVICECONTEXT_H

#include "Bitmap.h"

namespace Javnce
{

/**
 * The Class DeviceContext wraps handling of device context (HDC).
 */
class DeviceContext
{
    class PrivateData;
public:
    /**
     * Constructor that creates memory device context compatible with given device.
     *
     * @param parent is the given device
     */
    DeviceContext(const DeviceContext &parent);

    /**
     * Constructor that creates device context of whole screen.
     *
     * @param parent is the given device
     */
    DeviceContext();

    ~DeviceContext();

    /**
     * Bits per pixel getter.
     *
     * @return number of bits per pixel.
     */
    int getBitsPerPixel();

    /**
     * Width getter.
     *
     * @return width in pixels.
     */
    DWORD getWidth();

    /**
     * Height getter.
     *
     * @return height in pixels.
     */
    DWORD getHeight();


    /**
     * Tests if needed RASTERCAPS capabilities are supported.
     *
     * @return true if needed features are supported.
     */
    bool supportsDIBits();

    /**
     * Creates Bitmap compatible with device context.
     * The ownership of created object is given.
     *
     * @return bitmap object compatible with device context.
     */
    Bitmap *createBitmap();

    /**
     * Takes given bitmap and assign it to device context.
     * The ownership of given objet is taken.
     *
     * @param bitmap is the new bitmap for device context.
     */
    void setBitmap(Bitmap *bitmap);

    /**
     * Provides access to device context specific bitmap.
     * The ownership of bitmap is not given.
     *
     * @return bitmap of device context.
     */
    Bitmap * getBitmap();

    /**
     * Debugging helper, writes out device context info.
     */
    void dump() const;

    /**
     * Copies device context.
     *
     * @param source is the source device context.
     */
    void copy(const DeviceContext &source);

    /**
     * Similar to copy but flips vertically at copy.
     *
     * @param source is the source device context.
     */
    void copyAndFlip(const DeviceContext &source);

    /**
     * Native device context handle getter.
     *
     * @return native device context handle.
     */
    HDC getHandle();

private:
    PrivateData *d;
};
}//End of Javnce
#endif // DEVICECONTEXT_H

