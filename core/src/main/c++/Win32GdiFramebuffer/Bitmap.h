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
#ifndef BITMAP_H
#define BITMAP_H

#include <stdint.h>

namespace Javnce
{
class DeviceContext;

/**
 * The Class Bitmap wraps handling of HBITMAP and gives access to buffer.
 */
class Bitmap
{
    class PrivateData;

public:
    Bitmap(HBITMAP bitmap);
    ~Bitmap();

    /**
     * Method to assign device to bitmap.
     * The ownership of context is not taken.
     *
     * @param context is the new device context.
     */
    void setContext(DeviceContext *context);

    /**
     * Buffer pointer getter.
     *
     * @return pointer to buffer.
     */
    uint8_t *getPixels();

    /**
     * Debugging helper, writes out bitmap info.
     */
    void dump() const;

    /**
     * Buffer size getter.
     *
     * @return count of bytes in buffer.
     */
    DWORD size() const;

    /**
     * Native bitmap handle getter.
     *
     * @return native bitmap handle.
     */
    HBITMAP getHandle();

    /**
     * Copies buffer from device context
     */
    void copy();

protected:
    /**
     * Forces transparency to non-transparent
     */
    void fixAlpha();

private:
    PrivateData *d;
};
}//End of Javnce
#endif // BITMAP_H
