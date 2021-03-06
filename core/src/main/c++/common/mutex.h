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
#ifndef MUTEX_H
#define MUTEX_H
namespace Javnce
{

/**
 * The Class Mutex is a mutex.
 */
class Mutex
{
    class PrivateData;
public:
    Mutex();
    ~Mutex();

    /**
     * Locks mutex.
     */
    void lock();

    /**
     * Unlocks mutex.
     */
    void unlock();
private:
    PrivateData *d;
};
}//End of Javnce
#endif // MUTEX_H
