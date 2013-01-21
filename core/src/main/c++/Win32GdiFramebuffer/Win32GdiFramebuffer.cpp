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
#include "DeviceContext.h"

namespace Javnce
{

class Win32GdiFramebuffer::PrivateData
{
public:
    PrivateData()
        : mem(root)
        , bitmap(NULL)
    {
    }

    ~PrivateData()
    {
    }
    DeviceContext	root;
    DeviceContext	mem;
    PixelFormat     format;
    int             bytesPerPixel;
    Bitmap			*bitmap;
};

Win32GdiFramebuffer::Win32GdiFramebuffer()
    : d(new PrivateData)
{
    init();
    initFormat();
}

Win32GdiFramebuffer::~Win32GdiFramebuffer()
{
    delete d;
    d = 0;
}


void Win32GdiFramebuffer::init()
{
    d->mem.setBitmap(d->root.createBitmap());

    d->bytesPerPixel = (d->root.getBitsPerPixel() + 7)/8;
}

void Win32GdiFramebuffer::initFormat()
{
    d->format.bigEndian = false;
    d->format.trueColour = true;

    if (4 == d->bytesPerPixel)
    {
        //ARGB888
        d->format.bitsPerPixel = 32;
        d->format.depth = 24;
        d->format.redMax = 255;
        d->format.greenMax = 255;
        d->format.blueMax = 255;
        d->format.redShift = 16;
        d->format.greenShift = 8;
        d->format.blueShift = 0;
    }
    else if (2 == d->bytesPerPixel)
    {
        //RGB565???
        d->format.bitsPerPixel = 16;
        d->format.depth = 16;
        d->format.redMax = 31;
        d->format.greenMax = 63;
        d->format.blueMax = 31;
        d->format.redShift = 11;
        d->format.greenShift = 5;
        d->format.blueShift = 0;
    }
}


bool Win32GdiFramebuffer::isSupported()
{
    bool hasGdiFramebuffer = false;

    DeviceContext context;
    if (context.supportsDIBits() && (16==context.getBitsPerPixel()||32==context.getBitsPerPixel()))
    {
        hasGdiFramebuffer = true;
    }

    return hasGdiFramebuffer;
}

int Win32GdiFramebuffer::getWidth() const
{
    return d->root.getWidth();
}

int Win32GdiFramebuffer::getHeight() const
{
    return d->root.getHeight();
}

PixelFormat Win32GdiFramebuffer::getFormat() const
{
    return d->format;
}

uint8_t *Win32GdiFramebuffer::getData()
{
    uint8_t *ptr=NULL;
    if (NULL == d->bitmap)
    {
        grab();
    }
    if (NULL != d->bitmap)
    {
        ptr=d->bitmap->getPixels();
    }

    return ptr;
}

void Win32GdiFramebuffer::grab()
{
    d->mem.copyAndFlip(d->root);
    d->bitmap = d->mem.getBitmap();
    d->bitmap->copy();
}


int Win32GdiFramebuffer::getBytesPerPixel() const
{
    return d->bytesPerPixel;
}
}//End of Javnce
