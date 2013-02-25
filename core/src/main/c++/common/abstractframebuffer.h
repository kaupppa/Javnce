/*
 * Copyright (C) 2013  Pauli Kauppinen
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
#ifndef FRAMEBUFFER_H
#define FRAMEBUFFER_H

#include <stdint.h>
#include "pixelformat.h"

namespace Javnce
{

/**
 * The Class AbstractFrameBuffer defines interface for getting framebuffer.
 */
class AbstractFrameBuffer
{
public:

    virtual ~AbstractFrameBuffer() {}
    /**
     * Width getter.
     *
     * @return width of framebuffer in pixels.
     */
    virtual int getWidth() const = 0;

    /**
     * Height getter.
     *
     * @return height of framebuffer in pixels.
     */
    virtual int getHeight() const = 0;


    /**
     * Bytes per pixel getter.
     *
     * @return number of bytes per pixel.
     */
    virtual int getBytesPerPixel() const = 0;

    /**
     * Format getter.
     *
     * @return format of framebuffer.
     */
    virtual PixelFormat getFormat() const = 0;

    /**
     * Updates framebuffer i.e. graps new screen capture.
     */
    virtual void grab() = 0;

    /**
     * Framebuffer data access.
     *
     * @return pointer to first pixel.
     */
    virtual uint8_t *getData()  = 0;
};
}//End of Javnce
#endif // FRAMEBUFFER_H
