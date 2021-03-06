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
#include "logger.h"
#include "Bitmap.h"
#include "DeviceContext.h"
#include "memorybuffer.h"

namespace Javnce
{
class Bitmap::PrivateData
{
public:
    PrivateData(HBITMAP bitmap)
        : bitmap(bitmap)
        , buffer(NULL)
        , size(0)
        , context(NULL)
    {
        memset(&bitmapInfo, 0, sizeof(bitmapInfo));
        memset(&header, 0, sizeof(header));
        header.bmiHeader.biSize = sizeof(header.bmiHeader);
    }

    ~PrivateData()
    {
        if (NULL != bitmap)
        {
            DeleteObject(bitmap);
            bitmap = NULL;
        }


        delete buffer;
        buffer=0;
    }
    HBITMAP				bitmap;
    MemoryBuffer		*buffer;
    BITMAP				bitmapInfo;
    DWORD				size;
    BITMAPINFO			header;
    DeviceContext		*context;
};

Bitmap::Bitmap(HBITMAP bitmap)
    :d(new PrivateData(bitmap))
{
    if (NULL == d->bitmap || 0 == GetObject(d->bitmap, sizeof(BITMAP), (LPVOID)&d->bitmapInfo))
    {
        LOG_ERROR("GetObject failure");
    }
    else
    {
        d->size = d->bitmapInfo.bmBitsPixel/8 * d->bitmapInfo.bmWidth * d->bitmapInfo.bmHeight;
        d->buffer = new MemoryBuffer(d->size);
        d->header.bmiHeader.biWidth = d->bitmapInfo.bmWidth;
        d->header.bmiHeader.biHeight = d->bitmapInfo.bmHeight;
        d->header.bmiHeader.biPlanes = d->bitmapInfo.bmPlanes;
        d->header.bmiHeader.biBitCount = d->bitmapInfo.bmBitsPixel;
        d->header.bmiHeader.biCompression = BI_RGB;
    }
}


Bitmap::~Bitmap()
{
    delete d;
    d = 0;
}

void Bitmap::copy()
{
    if (NULL != d->context && NULL != d->buffer)
    {
        if(!GetDIBits( d->context->getHandle(),
                       d->bitmap,
                       0,
                       (UINT)d->bitmapInfo.bmHeight,
                       d->buffer->get(),
                       &d->header,
                       DIB_RGB_COLORS))
        {
            LOG_ERROR("GetDIBits failure");
        }
        fixAlpha();
    }
}

void Bitmap::fixAlpha()
{
    //TODO A hack; needs better solution
    if (NULL != d->buffer && 32 == d->bitmapInfo.bmBitsPixel)
    {
        uint8_t *ptr = d->buffer->get();
        for(DWORD i=3; i < d->size; i +=4)
        {
            ptr[i] = 0xFF;
        }
    }
}

uint8_t *Bitmap::getPixels()
{
    return d->buffer->get();
}

DWORD Bitmap::size() const
{
    return d->size;
}

void Bitmap::setContext(DeviceContext *context)
{
    d->context = context;
}

HBITMAP Bitmap::getHandle()
{
    return d->bitmap;
}

void Bitmap::dump() const
{
    LOG_DEBUG("bitmapInfo.bmBits = %d", d->bitmapInfo.bmBits);
    LOG_DEBUG("bitmapInfo.bmBitsPixel = %d", d->bitmapInfo.bmBitsPixel);
    LOG_DEBUG("bitmapInfo.bmHeight = %d", d->bitmapInfo.bmHeight);
    LOG_DEBUG("bitmapInfo.bmPlanes = %d", d->bitmapInfo.bmPlanes);
    LOG_DEBUG("bitmapInfo.bmWidth = %d", d->bitmapInfo.bmWidth);
    LOG_DEBUG("bitmapInfo.bmWidthBytes = %d", d->bitmapInfo.bmWidthBytes);
}

}//End of Javnce
