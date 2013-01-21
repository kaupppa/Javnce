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
#include "StdAfx.h"

#include "mutex.h"
namespace Javnce
{
class Mutex::PrivateData
{
public:
    PrivateData()
    {
        mutex = CreateMutex( NULL, FALSE, NULL);
    }
    ~PrivateData()
    {
        if (NULL != mutex)
        {
            CloseHandle(mutex);
        }
    }
    HANDLE mutex;
};

Mutex::Mutex()
    : d(new PrivateData)
{
}

Mutex::~Mutex()
{
    delete d;
    d = 0;
}

void Mutex::lock()
{
    if (NULL != d->mutex)
    {
        WaitForSingleObject(d->mutex, INFINITE);
    }
}

void Mutex::unlock()
{
    if (NULL != d->mutex)
    {
        ReleaseMutex(d->mutex);
    }
}
}//End of Javnce
