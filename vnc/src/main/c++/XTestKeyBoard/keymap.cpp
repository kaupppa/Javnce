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
#include <X11/XKBlib.h>
#include <stdio.h>

#include "keymap.h"
#include "logger.h"

namespace Javnce
{
class KeyMap::PrivateData
{
public:
    PrivateData(Display *display)
        : display(display)
    {
        minkey = 0;
        maxkey = 0;
        symsPerKeycode = 0;

        XDisplayKeycodes(display, &minkey, &maxkey);

        map = XGetKeyboardMapping(display, minkey, (maxkey - minkey + 1), &symsPerKeycode);

    }

    ~PrivateData()
    {
        XFree(map);
    }
    Display *display;
    KeySym  *map;
    int     minkey;
    int     maxkey;
    int     symsPerKeycode;

};


KeyMap::KeyMap(Display *display)
    : d(new PrivateData(display))
{
}

KeyMap::~KeyMap()
{
    delete d;
    d = 0;
}

ModifierMask_t KeyMap::findKeyCode(const KeySym keySym,  KeyCode &keyCode) const
{
    ModifierMask_t modifierMask = 0;
    keyCode = KEYCODE_NONE;


    for (int i = d->minkey; NoSymbol != keySym && 0 != d->map && i <= d->maxkey; i++)
    {
        for (int j = 0; j < d->symsPerKeycode; j++)
        {
            if (keySym == d->map[(i - d->minkey) * d->symsPerKeycode + j ])
            {
                if (0 != j)
                {
                    modifierMask |= 1 << (j - 1);
                }

                keyCode = i;
                break;
            }
        }
    }

    return modifierMask;
}

void KeyMap::dump() const
{
    char rowData[1024] = {0};

    for (int i = d->minkey; 0 != d->map && i <= d->maxkey; i++)
    {
        char *text = rowData;

        for (int j = 0; j < d->symsPerKeycode; j++)
        {
            KeySym keySym = d->map[(i - d->minkey) * d->symsPerKeycode + j ];
            text += sprintf(text, "0x%lX (%s)   ", keySym, XKeysymToString(keySym));
        }

        LOG_DEBUG("%d: %s\n", i, rowData);
    }
}

}//End of Javnce
