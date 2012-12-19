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
#ifndef MUTEXLOCKER_H
#define MUTEXLOCKER_H

#include <mutex.h>
namespace Javnce
{

/**
 * The Class MutexLocker is helper class for handling Mutex.
 */
class MutexLocker
{
public:
    MutexLocker(Mutex *mutex)
        : mutex(mutex)
    {
        mutex->lock();
    }
    ~MutexLocker()
    {
        mutex->unlock();
    }

private:
    Mutex *mutex;
};
}//End of Javnce
#endif // MUTEXLOCKER_H
