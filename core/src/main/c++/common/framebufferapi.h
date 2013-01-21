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
#ifndef FRAMEBUFFERAPI_H
#define FRAMEBUFFERAPI_H

#include <jni.h>
#include "abstractframebuffer.h"
namespace Javnce
{

/**
 * The getSize function gets AbstractFrameBuffer size in Java org.javnce.rfb.types.Size format.
 */
jobject getSize(AbstractFrameBuffer *fb, JNIEnv *env);

/**
 * The getFormat function gets AbstractFrameBuffer format in Java org.javnce.rfb.types.PixelFormat format.
 */
jobject getFormat(AbstractFrameBuffer *fb, JNIEnv *env);

/**
 * The getBuffer function gets AbstractFrameBuffer data in Java java.nio.ByteBuffer format.
 */
jobjectArray getBuffer(AbstractFrameBuffer *fb, JNIEnv *env, jint x, jint y, jint width, jint height);
}//End of Javnce

#endif // FRAMEBUFFERAPI_H
