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
#include <stdlib.h>
#include <X11/Xlib.h>

#include "org_javnce_vnc_server_platform_XTestPointerInputDevice.h"
#include "xtestpointerinputdevice.h"
#include "logger.h"
#include "mutexlocker.h"
#include <sched.h>

using namespace Javnce;

static void jni_clear(void);
static void jni_init(void);

static XTestPointerInputDevice *dev = 0;
static Mutex mutex;

JNIEXPORT jboolean JNICALL Java_org_javnce_vnc_server_platform_XTestPointerInputDevice_canSupport(JNIEnv *, jobject)
{
    jboolean valid = false;

    if (XTestPointerInputDevice::isSupported())
    {
        valid = true;
    }

    return valid;
}

JNIEXPORT void JNICALL Java_org_javnce_vnc_server_platform_XTestPointerInputDevice_pointerEvent(JNIEnv *, jobject, jint mask, jint x, jint y)
{
    jni_init();

    if (0 != dev)
    {
        {
            MutexLocker locker(&mutex);
            dev->pointerEvent(mask, x, y);
        }
        //sched_yield();
    }

}

static void jni_init(void)
{
    if (NULL == dev)
    {
        XInitThreads();
        MutexLocker locker(&mutex);
        dev = new XTestPointerInputDevice();
        atexit(jni_clear);
    }

}

static void jni_clear(void)
{
    delete dev;
    dev = 0;
}
