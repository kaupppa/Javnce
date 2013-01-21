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
#ifndef KEYMAP_H
#define KEYMAP_H

#include <X11/Xutil.h>
#include "constants.h"

namespace Javnce
{

class KeyMap
{
    class PrivateData;

public:
    KeyMap(Display *display);
    ~KeyMap();

    //Returns all possible modifiers in a mask
    ModifierMask_t findKeyCode(const KeySym keySym,  KeyCode &keyCode) const;

    void dump() const;

private:
    PrivateData *d;

};

}//End of Javnce
#endif // KEYMAP_H
