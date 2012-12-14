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
#include <stdlib.h>

#include "jni_api.h"
#include "Win32GdiFramebuffer.h"

#include "pixelformat.h"
#include "logger.h"
#include "mutexlocker.h"
#include "framebufferapi.h"

using namespace Javnce;

void clear(void);
static void init();

static Win32GdiFramebuffer *dev = 0;
static Mutex mutex;

JNIEXPORT jboolean JNICALL Java_org_javnce_vnc_server_platform_Win32GdiFramebuffer_hasGdiFramebuffer(JNIEnv *, jobject)
{
    jboolean valid = false;

    if (Win32GdiFramebuffer::isSupported())
    {
        valid = true;
    }

    return valid;
}

JNIEXPORT jobject JNICALL Java_org_javnce_vnc_server_platform_Win32GdiFramebuffer_size(JNIEnv *env, jobject)
{
    init();
    return getSize(dev, env);
}

JNIEXPORT jobject JNICALL Java_org_javnce_vnc_server_platform_Win32GdiFramebuffer_format(JNIEnv *env, jobject)
{
    init();
    return getFormat(dev, env);
}


JNIEXPORT jobjectArray JNICALL Java_org_javnce_vnc_server_platform_Win32GdiFramebuffer_buffer
(
    JNIEnv *env,
    jobject,
    jint x,
    jint y,
    jint width,
    jint height
)
{
    init();
    return getBuffer(dev, env, x, y, width, height);
}

static void init()
{
    MutexLocker locker(&mutex);

    if (NULL == dev)
    {
        dev = new Win32GdiFramebuffer();

    }
}

void clear(void)
{
    MutexLocker locker(&mutex);
    delete dev;
    dev = 0;
}
