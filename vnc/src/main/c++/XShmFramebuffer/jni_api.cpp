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

#include "fbxshm.h"
#include "org_javnce_vnc_server_platform_XShmFramebuffer.h"
#include "pixelformat.h"
#include "logger.h"
#include <X11/Xlib.h>
#include "mutexlocker.h"
#include "framebufferapi.h"

using namespace Javnce;

static void clear(void);
static void init();

static AbstractFrameBuffer *dev = 0;
static Mutex mutex;

JNIEXPORT jboolean JNICALL Java_org_javnce_vnc_server_platform_XShmFramebuffer_hasXShm(JNIEnv *, jobject)
{
    jboolean valid = false;

    if (FbXShm::isSupported())
    {
        valid = true;
    }

    return valid;
}

JNIEXPORT jobject JNICALL Java_org_javnce_vnc_server_platform_XShmFramebuffer_size(JNIEnv *env, jobject)
{
    init();
    return getSize(dev, env);
}

JNIEXPORT jobject JNICALL Java_org_javnce_vnc_server_platform_XShmFramebuffer_format(JNIEnv *env, jobject)
{
    init();
    return getFormat(dev, env);
}


JNIEXPORT jobjectArray JNICALL Java_org_javnce_vnc_server_platform_XShmFramebuffer_buffer
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

JNIEXPORT void JNICALL Java_org_javnce_vnc_server_platform_XShmFramebuffer_grabScreen(JNIEnv *, jobject)
{
    init();
    dev->grab();
}

static void init()
{
    MutexLocker locker(&mutex);

    if (NULL == dev)
    {
        XInitThreads();
        dev = new FbXShm();
        atexit(clear);
    }
}

static void clear(void)
{
    delete dev;
    dev = 0;
}
