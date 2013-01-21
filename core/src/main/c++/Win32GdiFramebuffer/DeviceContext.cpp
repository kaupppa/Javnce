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
#include "DeviceContext.h"
#include "logger.h"

namespace Javnce
{
class DeviceContext::PrivateData
{
public:
    PrivateData()
        : context(NULL)
        , bitmap(NULL)
        , isScreen(false)
        , width(GetSystemMetrics(SM_CXSCREEN))
        , height(GetSystemMetrics(SM_CYSCREEN))
    {
    }

    ~PrivateData()
    {
        if (NULL != bitmap)
        {
            delete bitmap;
            bitmap = NULL;
        }
        if (NULL != context)
        {
            if (isScreen)
            {
                ReleaseDC(NULL, context);
            }
            else
            {
                DeleteObject(context);
            }
            context=NULL;
        }
    }
    HDC				context;
    Javnce::Bitmap  *bitmap;
    bool			isScreen;
    DWORD			width;
    DWORD			height;
};

DeviceContext::DeviceContext()
    :d(new PrivateData)
{
    d->isScreen = true;
    if (NULL == (d->context=GetDC(NULL)))
    {
        LOG_ERROR("GetDC failure");
    }
}

DeviceContext::DeviceContext(const DeviceContext &parent)
    :d(new PrivateData)
{
    d->isScreen = false;
    if (NULL == (d->context=CreateCompatibleDC(parent.d->context)))
    {
        LOG_ERROR("CreateCompatibleDC failure");
    }
}


DeviceContext::~DeviceContext()
{
    delete d;
    d = 0;
}

int DeviceContext::getBitsPerPixel()
{
    return GetDeviceCaps(d->context, BITSPIXEL);
}

bool DeviceContext::supportsDIBits()
{
    int value = GetDeviceCaps(d->context, RASTERCAPS);
    int mask = RC_BITBLT | RC_DI_BITMAP;
    return (mask & value) == mask;
}

DWORD DeviceContext::getWidth()
{
    return d->width;
}

DWORD DeviceContext::getHeight()
{
    return d->height;
}

Bitmap *DeviceContext::createBitmap()
{
    Bitmap *retval=NULL;

    HBITMAP bitmapHandle = CreateCompatibleBitmap(d->context, getWidth(), getHeight());
    if (NULL == bitmapHandle)
    {
        LOG_ERROR("CreateCompatibleBitmap failure");
    }
    else
    {
        retval = new Javnce::Bitmap(bitmapHandle);
    }
    return retval;
}

void DeviceContext::setBitmap(Bitmap *bitmap)
{
    if (NULL != d->bitmap)
    {
        delete d->bitmap;
        d->bitmap = 0;
    }
    d->bitmap = bitmap;
    SelectObject(d->context, d->bitmap->getHandle());
    d->bitmap->setContext(this);
}

void DeviceContext::copy(const DeviceContext &source)
{
    if (NULL != d->context && NULL != source.d->context)
    {
        if(!BitBlt(d->context,
                   0,0,
                   getWidth(),
                   getHeight(),
                   source.d->context,
                   0,0,
                   SRCCOPY|CAPTUREBLT))
        {
            LOG_ERROR("BitBlt failure");
        }
    }
}

void DeviceContext::copyAndFlip(const DeviceContext &source)
{
    if (NULL != d->context && NULL != source.d->context)
    {
        POINT points[3];

        //Upper-left point
        points[0].x=0;
        points[0].y=getHeight();

        //Upper-right point
        points[1].x=getWidth();
        points[1].y=getHeight();

        //Lower-left point
        points[2].x=0;
        points[2].y=0;


        if(!PlgBlt( d->context,
                    points,
                    source.d->context,
                    0,0,
                    getWidth(),
                    getHeight(),
                    0,0,0))
        {
            LOG_ERROR("PlgBlt failure");
        }
    }
}


HDC DeviceContext::getHandle()
{
    return d->context;
}

Bitmap * DeviceContext::getBitmap()
{
    return d->bitmap;
}

void DeviceContext::dump() const
{
    HBITMAP bitmap = CreateCompatibleBitmap(d->context, 1, 1);

    struct BitmapInfo {
        BITMAPINFOHEADER bmiHeader;
        union {
            struct {
                DWORD red;
                DWORD green;
                DWORD blue;
            } mask;
            RGBQUAD color[1];
        };
    } bitmapInfo;



    memset(&bitmapInfo, 0, sizeof(bitmapInfo));
    bitmapInfo.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);

    if (!::GetDIBits(d->context, bitmap, 0, 1, NULL, (LPBITMAPINFO)&bitmapInfo, DIB_RGB_COLORS)) {
        LOG_ERROR("unable to determine device pixel format");
    }

    LOG_DEBUG("biSize = %d", bitmapInfo.bmiHeader.biSize);
    LOG_DEBUG("trueColour = %s", (bitmapInfo.bmiHeader.biBitCount > 8 ? "TRUE" : "FALSE"));
    LOG_DEBUG("bpp = %d", bitmapInfo.bmiHeader.biBitCount);
    LOG_DEBUG("biCompression  = 0x%X", bitmapInfo.bmiHeader.biCompression );
    LOG_DEBUG("biCompression is BI_RGB = %s", (BI_RGB == bitmapInfo.bmiHeader.biCompression ? "TRUE" : "FALSE"));
    LOG_DEBUG("biCompression is BI_RLE8 = %s", (BI_RLE8 == bitmapInfo.bmiHeader.biCompression ? "TRUE" : "FALSE"));
    LOG_DEBUG("biCompression is BI_RLE4 = %s", (BI_RLE4 == bitmapInfo.bmiHeader.biCompression ? "TRUE" : "FALSE"));
    LOG_DEBUG("biCompression is BI_BITFIELDS = %s", (BI_BITFIELDS == bitmapInfo.bmiHeader.biCompression ? "TRUE" : "FALSE"));
    LOG_DEBUG("biCompression is BI_JPEG = %s", (BI_JPEG == bitmapInfo.bmiHeader.biCompression ? "TRUE" : "FALSE"));
    LOG_DEBUG("biCompression is BI_PNG = %s", (BI_PNG == bitmapInfo.bmiHeader.biCompression ? "TRUE" : "FALSE"));

    LOG_DEBUG("biWidth = %d", bitmapInfo.bmiHeader.biWidth);
    LOG_DEBUG("biHeight = %d", bitmapInfo.bmiHeader.biHeight);
    LOG_DEBUG("biPlanes = %d", bitmapInfo.bmiHeader.biPlanes);
    LOG_DEBUG("biSizeImage = %d", bitmapInfo.bmiHeader.biSizeImage);
    LOG_DEBUG("biXPelsPerMeter = %d", bitmapInfo.bmiHeader.biXPelsPerMeter);
    LOG_DEBUG("biYPelsPerMeter = %d", bitmapInfo.bmiHeader.biYPelsPerMeter);
    LOG_DEBUG("biClrUsed = %d", bitmapInfo.bmiHeader.biClrUsed);
    LOG_DEBUG("biClrImportant = %d", bitmapInfo.bmiHeader.biClrImportant);

    if (BI_BITFIELDS == bitmapInfo.bmiHeader.biCompression)
    {
        LOG_DEBUG("rMask = 0x%X", bitmapInfo.mask.red);
        LOG_DEBUG("gMask = 0x%X", bitmapInfo.mask.green);
        LOG_DEBUG("bMask = 0x%X", bitmapInfo.mask.blue);
    }
    else
    {
        LOG_DEBUG("rMask = 0x%X", bitmapInfo.color[0].rgbRed);
        LOG_DEBUG("gMask = 0x%X", bitmapInfo.color[0].rgbGreen);
        LOG_DEBUG("bMask = 0x%X", bitmapInfo.color[0].rgbBlue);
    }
}

}//End of Javnce