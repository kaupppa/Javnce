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
package org.javnce.util;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * The Class ByteBuffers provides ByteBuffer helper methods.
 */
public class ByteBuffers {

    /**
     * Change ByteBuffer array into one buffer.
     *
     * @param buffers the ByteBuffer array
     * @return the ByteBuffer that has array.
     */
    public static ByteBuffer asBuffer(ByteBuffer buffers[]) {
        ByteBuffer buffer = null;

        if (null != buffers && 0 != buffers.length) {

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
     * Change ByteBuffer list into one buffer.
     *
     * @param list the ByteBuffer list
     * @return the ByteBuffer that has array.
     */
    public static ByteBuffer asBuffer(List<ByteBuffer> list) {
        ByteBuffer buffer = null;

        if (null != list) {
            buffer = asBuffer(list.toArray(new ByteBuffer[list.size()]));
        }

        return buffer;
    }
}
