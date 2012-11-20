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
#ifndef CONSTANTS_H
#define CONSTANTS_H

#include <stdint.h>
#include <X11/extensions/XTest.h>

#define KEYCODE_NONE 0 //<! Invalid key code


typedef uint8_t ModifierMask_t;

#define MODIFIER_COUNT 8

#define Modifier_None       0
#define Modifier_Shift      1<<0
#define Modifier_Lock       1<<1
#define Modifier_Control    1<<2
#define Modifier_Mod1       1<<3
#define Modifier_Mod2       1<<4
#define Modifier_Mod3       1<<5
#define Modifier_Mod4       1<<6
#define Modifier_Mod5       1<<7



typedef struct
{
    bool    down;
    KeyCode code;
} Modifierdata_t;

typedef Modifierdata_t ModifierArray_t[MODIFIER_COUNT];


#endif // CONSTANTS_H
