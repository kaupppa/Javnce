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
#ifndef WIN32GDIFRAMEBUFFER_H
#define WIN32GDIFRAMEBUFFER_H

#include "abstractframebuffer.h"

namespace Javnce
{

/**
 * The Class Win32GdiFramebuffer is an AbstractFrameBuffer implementation with
 * Windows graphics device interface (GDI).
 */
class Win32GdiFramebuffer : public AbstractFrameBuffer
{
    class PrivateData;
public:
    Win32GdiFramebuffer();
    ~Win32GdiFramebuffer();

    /**
     * Tests if Win32GdiFramebuffer can be used.
     *
     * @return true if graphics device interface have needed capabilities.
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
#endif // WIN32GDIFRAMEBUFFER_H
