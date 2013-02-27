/*
 * Copyright (C) 2013 Pauli Kauppinen
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
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.javnce.vnc.server.platform;

import java.nio.ByteBuffer;

/**
 * The Class Checksum is a hack to reduce memory usage.
 *
 * The Java 7 Checksum does not support direct allocated ByteBuffer. Using Java
 * 7 Checksum causes making of an extra copy of frame buffer.
 *
 * The Class Checksum just adds very limited direct allocated ByteBuffer
 * support.
 *
 */
public class Checksum {

    /**
     * The JNI lib name.
     */
    final static private String libName = getLibName();

    static {
        System.loadLibrary(libName);
    }

    /**
     * Gets the lib name.
     *
     * @return the lib name
     */
    static private String getLibName() {
        String name = "checksum";
        if (System.getProperty("os.name").startsWith("Windows")) {
            name = "Checksum_x64";
            if (System.getProperty("os.arch").startsWith("x86")) {
                name = "Checksum_Win32";
            }
        }
        return name;
    }

    /**
     * Instantiates a new checksum.
     */
    private Checksum() {
    }

    /**
     * Calculate Adler32 checksum.
     *
     * @param buffer the buffer
     * @param offset the offset
     * @param length the length
     * @return the checksum
     */
    static public long adler32(ByteBuffer buffer, int offset, int length) {
        if (null == buffer) {
            throw new NullPointerException();
        }
        if (offset < 0 || length < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (buffer.capacity() < (offset + length)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        long crc = 0;
        if (buffer.isDirect()) {
            crc = adler32ByteBuffer(buffer, offset, length);
        } else if (buffer.hasArray()) {
            crc = adler32ByteArray(buffer.array(), offset, length);
        } else {
            throw new UnsupportedOperationException();
        }
        return crc;
    }

    /**
     * Native Adler32 from given byte array
     *
     * @param buffer the buffer
     * @param offset the offset
     * @param length the length
     * @return the long
     */
    private native static long adler32ByteArray(byte[] buffer, int offset, int length);

    /**
     * Native Adler32 from given ByteBuffer.
     *
     * Note that ByteBuffer must be direct allocated.
     *
     * @param buffer the buffer
     * @param offset the offset
     * @param length the length
     * @return the long
     */
    private native static long adler32ByteBuffer(ByteBuffer buffer, int offset, int length);
}
