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
#include "StdAfx.h"
#include "Win32GdiFramebuffer.h"
#include "logger.h"

class Win32GdiFramebuffer::PrivateData
{
public:
    PrivateData()
    {
    }

    ~PrivateData()
    {
    }
    int             bytesPerPixel;
};

Win32GdiFramebuffer::Win32GdiFramebuffer()
    : d(new PrivateData)
{
    init();
    initFormat();
}

Win32GdiFramebuffer::~Win32GdiFramebuffer()
{
}


void Win32GdiFramebuffer::init()
{
}

void Win32GdiFramebuffer::initFormat()
{
}


bool Win32GdiFramebuffer::isSupported()
{
    bool hasGdiFramebuffer = false;

    return hasGdiFramebuffer;
}

int Win32GdiFramebuffer::getWidth() const
{
    return 0;
}

int Win32GdiFramebuffer::getHeight() const
{
    return 0;
}

PixelFormat Win32GdiFramebuffer::getFormat() const
{
	static PixelFormat pixelFormat;
    return pixelFormat;
}

uint8_t *Win32GdiFramebuffer::getData()
{
    return (uint8_t *)0;
}

int Win32GdiFramebuffer::getBytesPerPixel() const
{
    return d->bytesPerPixel;
}
