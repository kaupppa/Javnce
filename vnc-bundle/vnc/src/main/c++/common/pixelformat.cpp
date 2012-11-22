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
#include "logger.h"

#include "pixelformat.h"

PixelFormat::PixelFormat()
    : bitsPerPixel(0)
    , depth(0)
    , bigEndian(false)
    , trueColour(true)
    , redMax(0)
    , greenMax(0)
    , blueMax(0)
    , redShift(0)
    , greenShift(0)
    , blueShift(0)
{
}

PixelFormat &PixelFormat::operator=(const PixelFormat &other)
{

    this->bitsPerPixel = other.bitsPerPixel;
    this->depth = other.depth;
    this->bigEndian = other.bigEndian;
    this->trueColour = other.trueColour;
    this->redMax = other.redMax;
    this->greenMax = other.greenMax;
    this->blueMax = other.blueMax;
    this->redShift = other.redShift;
    this->greenShift = other.greenShift;
    this->blueShift = other.blueShift;
    return *this;
}

jobject PixelFormat::toJavaObject(JNIEnv *env)
{
    jclass clazz = env->FindClass("org/javnce/rfb/types/Color");

    if (NULL == clazz)
    {
        LOG_ERROR("no Color Class");
        return NULL;
    }

    jmethodID id = env->GetMethodID(clazz, "<init>", "(III)V");
    jobject max = env->NewObject(clazz, id, (jint)redMax, (jint)greenMax, (jint)blueMax);
    jobject shift = env->NewObject(clazz, id, (jint)redShift, (jint)greenShift, (jint)blueShift);

    if (NULL == max || NULL == shift)
    {
        LOG_ERROR("no color object");
        return NULL;
    }

    clazz = env->FindClass("org/javnce/rfb/types/PixelFormat");

    if (NULL == clazz)
    {
        LOG_ERROR("no PixelFormat Class");
        return NULL;
    }

    id = env->GetMethodID(clazz, "<init>", "(IIZZLorg/javnce/rfb/types/Color;Lorg/javnce/rfb/types/Color;)V");

    jobject object = env->NewObject(clazz, id, (jint)bitsPerPixel, (jint)depth,
                                    (jboolean)bigEndian, (jboolean)trueColour,
                                    max, shift);

    if (NULL == object)
    {
        LOG_ERROR("no PixelFormat object");
    }

    return object;
}
