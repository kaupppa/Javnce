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
#include <sched.h>
#include <stdlib.h>

#include "org_javnce_vnc_server_platform_XTestKeyBoard.h"
#include "xtestkeyboard.h"
#include "mutexlocker.h"

static void jni_clear(void);
static void jni_init(void);

static XTestKeyBoard *dev = 0;
static Mutex mutex;

JNIEXPORT jboolean JNICALL Java_org_javnce_vnc_server_platform_XTestKeyBoard_hasXTest(JNIEnv *, jobject)

{
    jboolean valid = false;

    if (XTestKeyBoard::isSupported())
    {
        valid = true;
    }

    return valid;
}
JNIEXPORT void JNICALL Java_org_javnce_vnc_server_platform_XTestKeyBoard_keyEvent
(JNIEnv *, jobject, jboolean down, jlong key)
{
    jni_init();

    if (0 != dev)
    {
        {
            MutexLocker locker(&mutex);
            dev->event(key, down);
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
        dev = new XTestKeyBoard();
        atexit(jni_clear);
    }

}

static void jni_clear(void)
{
    delete dev;
    dev = 0;
}
