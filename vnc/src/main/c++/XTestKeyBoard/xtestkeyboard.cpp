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
#include <X11/Xutil.h>
#include <X11/extensions/XTest.h>
#include <X11/XKBlib.h>

#include "keymap.h"
#include "modifiermap.h"
#include "xtestkeyboard.h"

namespace Javnce
{

class XTestKeyBoard::PrivateData
{
public:
    PrivateData()
        : display(XOpenDisplay(NULL))
        , keyMap(display)
        , modifierMap(display)
    {
    }

    ~PrivateData()
    {
        XCloseDisplay(display);
    }
    Display         *display;
    KeyMap          keyMap;
    ModifierMap     modifierMap;
};


XTestKeyBoard::XTestKeyBoard()
    : d(new PrivateData)
{
}

XTestKeyBoard::~XTestKeyBoard()
{
    delete d;
    d = 0;
}

void XTestKeyBoard::event(const KeySym keySym,  bool down)
{
    if (down && isValid(keySym))
    {
        KeyCode code;
        ModifierMask_t mask = d->keyMap.findKeyCode(keySym, code);

        if (KEYCODE_NONE != code)
        {
            ModifierArray_t array;
            d->modifierMap.getDownModifiers(array, mask);

            //Send modifiers
            for (int i = 0;  i < MODIFIER_COUNT; i++)
            {
                if (KEYCODE_NONE == array[i].code)
                {
                    break;
                }

                send(array[i].code, true);
            }

            send(code, true);

            //Now release all
            send(code, false);

            //Send modifiers
            for (int i = 0;  i < MODIFIER_COUNT; i++)
            {
                if (KEYCODE_NONE == array[i].code)
                {
                    break;
                }

                send(array[i].code, false);
            }

            XFlush(d->display);

        }
    }
}

void XTestKeyBoard::send(KeyCode code,  bool down) const
{
    if (KEYCODE_NONE != code)
    {
        XTestFakeKeyEvent(d->display, code, down, CurrentTime);
    }
}

bool XTestKeyBoard::isValid(KeySym keySym)
{
    bool valid = false;

    if (NoSymbol != keySym && !IsModifierKey(keySym))
    {
        valid = true;
    }

    return valid;
}


void XTestKeyBoard::dump() const
{
    d->modifierMap.dump();
    d->keyMap.dump();
}

bool XTestKeyBoard::isSupported()
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
