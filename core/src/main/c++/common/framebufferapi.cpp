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
#include "framebufferapi.h"
namespace Javnce
{

jobject getSize(AbstractFrameBuffer *fb, JNIEnv *env)
{
    jobject object = NULL;

    jclass clazz = env->FindClass("org/javnce/rfb/types/Size");

    if (NULL != clazz)
    {
        jmethodID id = env->GetMethodID(clazz, "<init>", "(II)V");
        object = env->NewObject(clazz, id, (jint)fb->getWidth(), (jint)fb->getHeight());
    }

    return object;
}

jobject getFormat(AbstractFrameBuffer *fb, JNIEnv *env)
{
    jobject object = NULL;
    PixelFormat format = fb->getFormat();
    object = format.toJavaObject(env);
    return object;
}

static jobject createByteBuffer(JNIEnv *env, uint8_t *buffer, int length)
{
    jobject object = NULL;
    object = env->NewDirectByteBuffer(buffer, length);
    return object;
}


jobjectArray getBuffer(AbstractFrameBuffer *fb, JNIEnv *env, jint x, jint y, jint width, jint height)
{
    jobjectArray array = NULL;
    uint8_t *buffer = fb->getData();
    int frameWidth = fb->getWidth();
    int bpp = fb->getBytesPerPixel();
    int lineLength  = bpp * frameWidth;

    jclass clazz = env->FindClass("java/nio/ByteBuffer");

    if (NULL != clazz)
    {
        if (0 == x && width == frameWidth)
        {
            jobject object = createByteBuffer(env, buffer + lineLength * y, height * lineLength);
            array = env->NewObjectArray(1, clazz, object);
            env->DeleteLocalRef(object);
        }
        else
        {
            array = env->NewObjectArray(height, clazz, 0);

            for (int i = 0; NULL != array && i < height; i++, y++)
            {
                uint8_t *ptr = buffer + lineLength * y + x * bpp;
                jobject object = createByteBuffer(env, ptr, width * bpp);
                env->SetObjectArrayElement(array, i, object);
                env->DeleteLocalRef(object);
            }
        }
    }

    return array;
}
}//End of Javnce
