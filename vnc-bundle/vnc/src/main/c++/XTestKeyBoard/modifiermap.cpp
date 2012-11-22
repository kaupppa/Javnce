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
#include <stdio.h>

#include "logger.h"
#include "modifiermap.h"


class ModifierMap::PrivateData
{
public:
    PrivateData(Display *display)
        : display(display)
    {
        map = XGetModifierMapping(display);
    }

    ~PrivateData()
    {
        XFreeModifiermap(map);
        map = 0;
    }

    Display         *display;
    XModifierKeymap *map;
};


ModifierMap::ModifierMap(Display *display)
    : d(new PrivateData(display))
{
}

ModifierMap::~ModifierMap()
{
    delete d;
    d = 0;
}

KeyCode ModifierMap::findCode(const int index) const
{
    KeyCode code = KEYCODE_NONE;

    if (0 != d->map)
    {
        for (int i = 0; i < d->map->max_keypermod; i++)
        {

            KeyCode tempCode = d->map->modifiermap[(index * d->map->max_keypermod) + i];

            if (KEYCODE_NONE != tempCode)
            {
                code = tempCode;
                break;
            }
        }
    }

    return code;
}

ModifierMask_t ModifierMap::currentMask() const
{
    XkbStateRec kbstate;
    ModifierMask_t mask = 0;
    XkbGetState(d->display, XkbUseCoreKbd, &kbstate);
    mask = kbstate.compat_state & 0xf;

    return mask;

}

void ModifierMap::initArray(ModifierArray_t &array)
{
    for (int i = 0;  i < MODIFIER_COUNT; i++)
    {
        array[i].code = KEYCODE_NONE;
    }
}

void ModifierMap::getDownModifiers(ModifierArray_t &array, const ModifierMask_t mask) const
{
    initArray(array);

    ModifierMask_t current = currentMask();

    int arrayIndex = 0;

    if (current != mask)
    {
        if (0 != (Modifier_Lock & current))
        {
            //If caps lock then change it to shft
            current &= ~Modifier_Lock;
            current |= Modifier_Shift;
        }

        for (int i = 0; i < MODIFIER_COUNT; i++)
        {
            const int modifier = 1 << i;

            if ((modifier & mask) != (modifier & current))
            {
                array[arrayIndex].code = findCode(i);
                array[arrayIndex].down = (0 != (modifier & mask));
                arrayIndex++;
            }
        }
    }
}



void ModifierMap::dump() const
{
    for (int i = 0; 0 != d->map && i < 8 ; i++)
    {
        for (int j = 0; j < d->map->max_keypermod; j++)
        {
            KeyCode keyCode = d->map->modifiermap[(i * d->map->max_keypermod) + j];
            LOG_DEBUG("Mods %d : %d : %d (%s)\n", i, j, keyCode, XKeysymToString(XkbKeycodeToKeysym(d->display, keyCode, 0, 0)));

        }
    }

    XkbStateRec kbstate;
    XkbGetState(d->display, XkbUseCoreKbd, &kbstate);
    LOG_DEBUG("Current modifier state : compat_state = 0x%4.4X\n", kbstate.compat_state);

    LOG_DEBUG("Current modifier state \n"
              "\tgroup = 0x%4.4X\n"
              "\tlocked_group = 0x%4.4X\n"
              "\tbase_group = 0x%4.4X\n"
              "\tlatched_group = 0x%4.4X\n"
              "\tmods = 0x%4.4X\n"
              "\tbase_mods = 0x%4.4X\n"
              "\tlatched_mods = 0x%4.4X\n"
              "\tlocked_mods = 0x%4.4X\n"
              "\tcompat_state = 0x%4.4X\n"
              "\tgrab_mods = 0x%4.4X\n"
              "\tcompat_grab_mods = 0x%4.4X\n"
              "\tlookup_mods = 0x%4.4X\n"
              "\tcompat_lookup_mods = 0x%4.4X\n"
              "\tptr_buttons = 0x%4.4X\n",
              kbstate.group,
              kbstate.locked_group,
              kbstate.base_group,
              kbstate.latched_group,
              kbstate.mods,
              kbstate.base_mods,
              kbstate.latched_mods,
              kbstate.locked_mods,
              kbstate.compat_state,
              kbstate.grab_mods,
              kbstate.compat_grab_mods,
              kbstate.lookup_mods,
              kbstate.compat_lookup_mods,
              kbstate.ptr_buttons);

}
