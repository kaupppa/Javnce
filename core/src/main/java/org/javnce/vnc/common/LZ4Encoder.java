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
package org.javnce.vnc.common;

import java.nio.ByteBuffer;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Decompressor;
import net.jpountz.lz4.LZ4Factory;

/**
 * The Class LZ4Encoder wraps the LZ4 compressor from net.jpountz.lz4.
 *
 * @see <a href="http://code.google.com/p/lz4/">http://code.google.com/p/lz4/</a>
 */
public class LZ4Encoder {

    /**
     * The compressor.
     */
    private LZ4Compressor compressor;
    /**
     * The decompressor.
     */
    private LZ4Decompressor decompressor;

    /**
     * Instantiates a new l z4 encoder.
     */
    public LZ4Encoder() {
    }

    /**
     * Gets ByteBuffer array as one one buffer with array() support.
     *
     * @param buffers the buffers
     * @return the byte buffer
     */
    static ByteBuffer asOneBuffer(ByteBuffer[] buffers) {
        ByteBuffer buffer = null;

        if (null != buffers) {
            if (1 == buffers.length && buffers[0].hasArray()) {
                buffer = buffers[0];
            } else {
                int count = 0;
                for (ByteBuffer temp : buffers) {
                    count += temp.capacity();
                }

                buffer = ByteBuffer.allocate(count);

                for (ByteBuffer temp : buffers) {
                    buffer.put(temp);
                    temp.clear();
                }

                buffer.clear();
            }
        }
        return buffer;
    }

    /**
     * Compress.
     *
     * @param buffers the buffers
     * @return the byte buffer
     */
    public ByteBuffer compress(ByteBuffer[] buffers) {

        ByteBuffer dataBuffer = asOneBuffer(buffers);

        return compress(dataBuffer.array());
    }

    /**
     * Compress.
     *
     * @param data the data
     * @return the byte buffer
     */
    public ByteBuffer compress(byte[] data) {
        if (null == compressor) {
            LZ4Factory factory = LZ4Factory.fastestInstance();
            compressor = factory.fastCompressor();
        }
        int maxSize = compressor.maxCompressedLength(data.length);
        byte[] compressed = new byte[maxSize];
        int compressedSize = compressor.compress(data, 0, data.length, compressed, 0, maxSize);
        return ByteBuffer.wrap(compressed, 0, compressedSize).slice();
    }

    /**
     * Decompress.
     *
     * @param buffers the buffers
     * @param size the size
     * @return the byte buffer
     */
    public ByteBuffer decompress(ByteBuffer[] buffers, int size) {

        ByteBuffer dataBuffer = asOneBuffer(buffers);

        return decompress(dataBuffer.array(), size);
    }

    /**
     * Decompress.
     *
     * @param data the data
     * @param size the size
     * @return the byte buffer
     */
    public ByteBuffer decompress(byte[] data, int size) {
        if (null == decompressor) {
            LZ4Factory factory = LZ4Factory.fastestInstance();
            decompressor = factory.decompressor();
        }
        byte[] restored = new byte[size];
        decompressor.decompress(data, 0, restored, 0, size);
        return ByteBuffer.wrap(restored);
    }
}
