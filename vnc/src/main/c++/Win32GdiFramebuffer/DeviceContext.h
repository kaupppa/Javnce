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
class DeviceContext
{
    class PrivateData;
public:
    DeviceContext(const DeviceContext &parent);
    DeviceContext();
    ~DeviceContext();

    int getBitsPerPixel();
    DWORD getWidth();
    DWORD getHeight();
    bool supportsDIBits();

    //Ownership is given
    Bitmap *createBitmap();

    //Ownership is taken
    void setBitmap(Bitmap *bitmap);
    //Ownership is not given
    Bitmap * getBitmap();

    void dump() const;

    void copyFrom(const DeviceContext &source);

    HDC getHandle();
private:
    PrivateData *d;
};
}//End of Javnce
#endif // DEVICECONTEXT_H

