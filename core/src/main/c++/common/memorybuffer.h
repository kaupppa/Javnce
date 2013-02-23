/*
 * Copyright (C) 2013  Pauli Kauppinen
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
#ifndef MEMORYBUFFER_H
#define MEMORYBUFFER_H

#include <stdint.h>

namespace Javnce
{
/**
 * The MemoryBuffer class wraps memory allocation (malloc, VirtualFree).
 */
class MemoryBuffer
{
    class PrivateData;
public:
    explicit MemoryBuffer(uint32_t size);

    ~MemoryBuffer();

    /**
     * Buffer pointer getter.
     * @returns pointer to allocated buffer.
     */
    uint8_t *get();

protected:
    void allocate();


private:
    PrivateData *d;
};
}//End of Javnce
#endif // MEMORYBUFFER_H

