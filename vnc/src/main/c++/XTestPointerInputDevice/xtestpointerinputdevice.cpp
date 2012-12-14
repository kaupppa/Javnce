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
#include <X11/Xutil.h>
#include <X11/extensions/XTest.h>
#include "xtestpointerinputdevice.h"
#include "logger.h"
namespace Javnce
{

class XTestPointerInputDevice::PrivateData
{
public:
    PrivateData()
        : display(XOpenDisplay(NULL))
        , mask(0)
        , x(-1)
        , y(-1)
    {
    }

    ~PrivateData()
    {
        XCloseDisplay(display);
    }

    Display *display;
    int     mask;
    int     x;
    int     y;
};

XTestPointerInputDevice::XTestPointerInputDevice()
    : d(new PrivateData())
{
}

XTestPointerInputDevice::~XTestPointerInputDevice()
{
    delete d;
    d = 0;
}

void XTestPointerInputDevice::pointerEvent(int mask, int x, int y)
{
    if (d->x != x || d->y != y)
    {
        d->x = x;
        d->y = y;
        XTestFakeMotionEvent(d->display, DefaultScreen(d->display), d->x, d->y, CurrentTime);
    }

    if (mask != d->mask)
    {
        for (int i = 0; i < MAX_POINTER_COUNT; i++)
        {
            int bitMask = 1 << i;

            if ((d->mask & bitMask) != (mask & bitMask))
            {
                bool is_press = ((mask & bitMask) == bitMask);
                XTestFakeButtonEvent(d->display, i + 1, is_press,  CurrentTime);
            }
        }
        d->mask = mask;
    }
    //XFlush(d->display);
    XSync(d->display,  False);
}

bool XTestPointerInputDevice::isSupported()
{
    bool supported = false;

    Display *display = XOpenDisplay(NULL);

    int major_opcode;
    int first_event;
    int first_error;

    if (XQueryExtension(display, "XTEST", &major_opcode, &first_event, &first_error))
    {
        int event_base;
        int error_base;
        int major_version;
        int minor_version;

        if (XTestQueryExtension(display, &event_base, &error_base, &major_version, &minor_version))
        {
            supported = true;
        }
    }

    XCloseDisplay(display);
    return supported;
}
}//End of Javnce
