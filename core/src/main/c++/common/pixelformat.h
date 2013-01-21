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
#ifndef PIXELFORMAT_H
#define PIXELFORMAT_H

#include <jni.h>
namespace Javnce
{

/**
 * The Class PixelFormat contains framebuffer format information.
 */
class PixelFormat
{

public:
    PixelFormat();

    PixelFormat &operator=(const PixelFormat &other);

    /**
     * Gets framebuffer format in Java org.javnce.rfb.types.PixelFormat format.
     */
    jobject toJavaObject(JNIEnv *env);

    int         bitsPerPixel;
    int         depth;
    bool        bigEndian;
    bool        trueColour;
    int         redMax;
    int         greenMax;
    int         blueMax;
    int         redShift;
    int         greenShift;
    int         blueShift;
};
}//End of Javnce
#endif // PIXELFORMAT_H
