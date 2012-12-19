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
#ifndef FBXSHM_H
#define FBXSHM_H

#include "abstractframebuffer.h"

namespace Javnce
{

/**
 * The Class FbXShm is an AbstractFrameBuffer implementation with
 * Linux X and XShm.
 */
class FbXShm : public AbstractFrameBuffer
{
    class PrivateData;

public:
    FbXShm();
    ~FbXShm();

    /**
     * Tests if FbXShm can be used.
     *
     * @return true if xshm is available.
     */
    static bool isSupported();

    int getWidth() const;
    int getHeight() const;
    PixelFormat getFormat() const;
    uint8_t *getData();
    int getBytesPerPixel() const;
    void grab();

private:
    void init();
    void initFormat();

private:
    PrivateData *d;

};
}//End of Javnce
#endif // FBXSHM_H
