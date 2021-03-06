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
#ifndef XTESTKEYBOARD_H
#define XTESTKEYBOARD_H

#include "constants.h"

namespace Javnce
{
class XTestKeyBoard
{

    class PrivateData;

public:
    XTestKeyBoard();
    ~XTestKeyBoard();

    void event(const KeySym keySym,  bool down);

    void dump() const;

    static bool isSupported();
protected:
    static bool isValid(KeySym keySym);
    void send(KeyCode code,  bool down) const;

private:
    PrivateData *d;

};
}//End of Javnce
#endif // XTESTKEYBOARD_H
