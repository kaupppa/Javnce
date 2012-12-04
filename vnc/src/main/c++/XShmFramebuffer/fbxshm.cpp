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
#include <X11/Xlib.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <X11/extensions/XShm.h>
#include <X11/Xutil.h>

#include "fbxshm.h"
#include "logger.h"

namespace Javnce
{
class FbXShm::PrivateData
{
public:
    PrivateData()
        : ximage(0)
        , display(XOpenDisplay(NULL))
        , width(0)
        , height(0)
        , bytesPerPixel(0)

    {
        shminfo.shmaddr = 0;
        window = RootWindowOfScreen(XDefaultScreenOfDisplay(display));
    }

    ~PrivateData()
    {
        if (0 < shminfo.shmaddr)
        {
            XShmDetach(display, &shminfo);
        }

        if (0 != ximage)
        {
            XDestroyImage(ximage);
        }

        if (0 < shminfo.shmaddr)
        {
            shmdt(shminfo.shmaddr);
        }

        XCloseDisplay(display);
    }

    XImage          *ximage;
    XShmSegmentInfo shminfo;
    Display         *display;
    int             width;
    int             height;
    PixelFormat     format;
    Window          window;
    int             bytesPerPixel;

};

FbXShm::FbXShm()
    : d(new PrivateData)
{
    init();
    initFormat();
    d->bytesPerPixel = d->format.bitsPerPixel / 8;
}

FbXShm::~FbXShm()
{
    delete d;
    d = 0;
}


void FbXShm::init()
{
    Screen *screen = XDefaultScreenOfDisplay(d->display);

    d->width = WidthOfScreen(screen);
    d->height = HeightOfScreen(screen);

    if (0 == (d->ximage = XShmCreateImage(d->display, DefaultVisualOfScreen(screen),
                                          DefaultDepthOfScreen(screen),
                                          ZPixmap,
                                          NULL,
                                          &d->shminfo,
                                          d->width,
                                          d->height)))
    {
        LOG_ERROR("XShmCreateImage failure");
        return;
    }

    /* Get the shared memory and check for errors */
    if (0 > (d->shminfo.shmid = shmget(IPC_PRIVATE, d->ximage->bytes_per_line * d->ximage->height,
                                       IPC_CREAT | 0777)))
    {
        XDestroyImage(d->ximage);
        d->ximage = 0;
        LOG_ERROR("shmget failure");
        return;
    }

    /* attach, and check for errrors */
    d->shminfo.shmaddr = d->ximage->data = (char *)shmat(d->shminfo.shmid, 0, 0);

    if (d->shminfo.shmaddr == (char *) - 1)
    {
        XDestroyImage(d->ximage);
        d->ximage = 0;
        LOG_ERROR("shmget failure");
        return;
    }

    /* set as read/write, and attach to the display */
    d->shminfo.readOnly = False;
    XShmAttach(d->display, &d->shminfo);
}

void FbXShm::initFormat()
{
    if (0 != d->ximage)
    {
        d->format.bitsPerPixel = d->ximage->bits_per_pixel;
        d->format.depth = d->ximage->depth;
        d->format.bigEndian = (MSBFirst == d->ximage->byte_order);
        d->format.trueColour = true;

        struct colorMapping
        {
            unsigned long mask;
            int *max;
            int *shift;
        } const array[] =
        {
            { d->ximage->red_mask,      &d->format.redMax,      &d->format.redShift}
            , {d->ximage->green_mask,    &d->format.greenMax,    &d->format.greenShift}
            , {d->ximage->blue_mask,     &d->format.blueMax,     &d->format.blueShift}
        };

        for (size_t i = 0; i < (sizeof(array) / sizeof(array[0])); i++)
        {
            for (int bit = 0; bit < 32; bit++)
            {
                unsigned long mask = array[i].mask >> bit;

                if ((mask & 1) == 1)
                {
                    *array[i].max = mask;
                    *array[i].shift = bit;
                    break;
                }
            }
        }
    }
}


bool FbXShm::isSupported()
{
    bool hasXShm = false;

    int major_opcode;
    int first_event;
    int first_error;


    Display *display = XOpenDisplay(NULL);

    if (XQueryExtension(display, "MIT-SHM", &major_opcode, &first_event, &first_error))
    {
        int major, minor;
        Bool pixmaps;

        if (XShmQueryVersion(display, &major, &minor, &pixmaps))
        {
            hasXShm = true;
        }
    }

    XCloseDisplay(display);
    return hasXShm;
}

int FbXShm::getWidth() const
{
    return d->width;
}

int FbXShm::getHeight() const
{
    return d->height;
}

PixelFormat FbXShm::getFormat() const
{
    return d->format;
}

uint8_t *FbXShm::getData()
{
    unsigned long plane_mask = AllPlanes;
    XShmGetImage(d->display, d->window, d->ximage, 0, 0, plane_mask);

    return (uint8_t *)d->ximage->data;
}

int FbXShm::getBytesPerPixel() const
{
    return d->bytesPerPixel;
}
}//End of Javnce
