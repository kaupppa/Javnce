/*
 * Copyright (C) 2013  Pauli Kauppinen
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
#include "jni_api.h"
#include "logger.h"
#include "checksum.h"


JNIEXPORT jlong JNICALL Java_org_javnce_vnc_server_platform_Checksum_adler32ByteArray(JNIEnv *env, jclass, jbyteArray byteArray, jint offset, jint length)
{
    jlong checksum=0;
    uint8_t  *buffer = (uint8_t  *)env->GetPrimitiveArrayCritical((jarray )byteArray, 0);
    if (NULL != buffer)
    {
        checksum = Javnce::adler32(buffer + offset, length);
        env->ReleasePrimitiveArrayCritical(byteArray, buffer, 0);
    }
    else
    {
        LOG_ERROR("GetPrimitiveArrayCritical failure");
    }
    return checksum;
}

JNIEXPORT jlong JNICALL Java_org_javnce_vnc_server_platform_Checksum_adler32ByteBuffer(JNIEnv *env, jclass, jobject byteBuffer, jint offset, jint length)
{
    jlong checksum=0;
    uint8_t *buffer = (uint8_t*)env->GetDirectBufferAddress(byteBuffer);
    if (NULL != buffer)
    {
        checksum = Javnce::adler32(buffer + offset, length);
    }
    else
    {
        LOG_ERROR("GetDirectBufferAddress failure");
    }
    return checksum;
}

