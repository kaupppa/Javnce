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
package org.javnce.rfb.messages;

import java.nio.ByteBuffer;
import java.util.List;

public class MyByteBufferHelper {

    public static ByteBuffer arrayToBuffer(ByteBuffer buffers[]) {
        ByteBuffer buffer = null;

        if (null != buffers && 0 != buffers.length) {
            int size = 0;

            for (int i = 0; i < buffers.length; i++) {
                if (null != buffers[i]) {
                    size += buffers[i].capacity();
                }
            }

            if (0 != size) {
                buffer = ByteBuffer.allocate(size);

                for (int i = 0; i < buffers.length; i++) {
                    buffer.put(buffers[i]);
                    buffers[i].clear();
                }

                buffer.clear();
            }
        }

        return buffer;
    }

    public static ByteBuffer arrayListToBuffer(List<ByteBuffer> list) {
        return arrayToBuffer(list.toArray(new ByteBuffer[list.size()]));
    }
}
