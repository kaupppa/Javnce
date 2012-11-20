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
#ifndef MODIFIERMAP_H
#define MODIFIERMAP_H

#include "constants.h"

class ModifierMap
{
    class PrivateData;

public:
    ModifierMap(Display *display);
    ~ModifierMap();

    void getDownModifiers(ModifierArray_t &array, const ModifierMask_t mask) const;

    void dump() const;

protected:
    ModifierMask_t currentMask() const;
    KeyCode findCode(const int index) const;

    static void initArray(ModifierArray_t &array);

private:

    PrivateData *d;

};

#endif // MODIFIERMAP_H
