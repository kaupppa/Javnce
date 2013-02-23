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
#include "StdAfx.h"
#include "memorybuffer.h"
#include "logger.h"

namespace Javnce {

class MemoryBuffer::PrivateData {
public:

    PrivateData(uint32_t size)
        : ptr(NULL)
        , base(NULL)
        , size(size) {
    }

    ~PrivateData() {
        if (NULL != base) {
            if (0 == VirtualFree(base, 0, MEM_RELEASE)) {
                LOG_ERROR("VirtualFree failure");
            }
            base = 0;
        }
    }
    uint8_t *ptr;
    LPVOID base;
    uint32_t size;
};

MemoryBuffer::MemoryBuffer(uint32_t size)
    : d(new PrivateData(size)) {
}

MemoryBuffer::~MemoryBuffer(void) {
    delete d;
    d = 0;
}

void MemoryBuffer::allocate() {
    bool success = false;

    if (NULL == d->base) {
        d->base = VirtualAlloc(NULL, d->size, MEM_RESERVE | MEM_COMMIT, PAGE_READWRITE);
        d->ptr = (uint8_t *) d->base;
		memset(d->base, 0xff, d->size);
    }
    if (NULL == d->base) {
        LOG_ERROR("VirtualAlloc-MEM_RESERVE failure");
    }
}

uint8_t *MemoryBuffer::get() {
    allocate();
    return d->ptr;
}

}//End of Javnce