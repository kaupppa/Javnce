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
package org.javnce.vnc.server;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;
import org.javnce.rfb.messages.MyByteBufferHelper;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRunLengthEncoderTest {

    final private static Random rnd = new Random();
    static private ByteBuffer[] fb8Bit;
    static private ByteBuffer[] fb16Bit;
    static private ByteBuffer[] fb24Bit;
    static private ByteBuffer[] fb32Bit;

    static ByteBuffer generateSame(int size, int bpp) {
        ByteBuffer buf = ByteBuffer.allocate(size * bpp);

        for (int i = 0; i < buf.capacity(); i++) {
            buf.put((byte) 0);
        }

        buf.clear();
        return buf;
    }

    static ByteBuffer generateRandom(int size, int bpp) {

        ByteBuffer buf = ByteBuffer.allocate(size * bpp);
        byte bytes[] = new byte[bpp];

        while (0 != buf.remaining()) {
            rnd.nextBytes(bytes);
            buf.put(bytes);
        }

        buf.clear();
        return buf;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        int array[] = new int[]{
            800 * 480,
            1440 * 900,
            1920 * 1080
        };

        fb8Bit = new ByteBuffer[array.length];
        fb16Bit = new ByteBuffer[array.length];
        fb24Bit = new ByteBuffer[array.length];
        fb32Bit = new ByteBuffer[array.length];

        for (int i = 0; i < array.length; i++) {
            ByteBuffer temp = generateRandom(array[i], 4);
            fb8Bit[i] = ByteBuffer.wrap(temp.array(), 0, array[i] * 1);
            fb16Bit[i] = ByteBuffer.wrap(temp.array(), 0, array[i] * 2);
            fb24Bit[i] = ByteBuffer.wrap(temp.array(), 0, array[i] * 3);
            fb32Bit[i] = ByteBuffer.wrap(temp.array(), 0, array[i] * 4);
        }
    }

    @Test
    public void testEncodeOneRun() {
        int array[] = new int[]{
            1,
            2,
            3,
            100,
            RunLengthEncoder.MaxRunLength - 2,
            RunLengthEncoder.MaxRunLength - 1,
            RunLengthEncoder.MaxRunLength,};

        for (int bytePerPixel = 1; bytePerPixel <= 4; bytePerPixel++) {
            for (int i = 0; i < array.length; i++) {
                ByteBuffer buffer = generateSame(array[i], bytePerPixel);

                //Encode
                List<ByteBuffer> list = RunLengthEncoder.encode(buffer, bytePerPixel);

                for (ByteBuffer encBuffer : list) {
                    assertNotNull(encBuffer);
                    assertEquals(0, buffer.remaining());
                }

                ByteBuffer encBuffer = MyByteBufferHelper.arrayListToBuffer(list);
                assertEquals(bytePerPixel + 1, encBuffer.capacity());

                int count = encBuffer.get() & 0xff;
                count++;
                assertEquals(count, buffer.capacity() / bytePerPixel);
            }
        }

    }

    @Test
    public void testEncodeTwoRuns() {
        int array[] = new int[]{
            RunLengthEncoder.MaxRunLength + 1,
            RunLengthEncoder.MaxRunLength + 2,
            RunLengthEncoder.MaxRunLength + 3,
            RunLengthEncoder.MaxRunLength + RunLengthEncoder.MaxRunLength - 1,
            RunLengthEncoder.MaxRunLength + RunLengthEncoder.MaxRunLength,};

        for (int bytePerPixel = 1; bytePerPixel <= 4; bytePerPixel++) {
            for (int i = 0; i < array.length; i++) {
                ByteBuffer buffer = generateSame(array[i], bytePerPixel);

                //Encode
                List<ByteBuffer> list = RunLengthEncoder.encode(buffer, bytePerPixel);

                for (ByteBuffer encBuffer : list) {
                    assertNotNull(encBuffer);
                    assertEquals(0, buffer.remaining());
                }

                ByteBuffer encBuffer = MyByteBufferHelper.arrayListToBuffer(list);
                assertEquals((bytePerPixel + 1) * 2, encBuffer.capacity());
            }
        }
    }

    @Test
    public void testEncodeByte() {

        int bytePerPixel = 1;

        for (int i = 0; i < fb8Bit.length; i++) {
            //Encode
            List<ByteBuffer> list = RunLengthEncoder.encode(fb8Bit[i], bytePerPixel);

            assertNotNull(list);
            assertEquals(0, fb8Bit[i].remaining());
        }
    }

    @Test
    public void testEncodeShort() {

        int bytePerPixel = 2;

        for (int i = 0; i < fb16Bit.length; i++) {
            //Encode
            List<ByteBuffer> list = RunLengthEncoder.encode(fb16Bit[i], bytePerPixel);
            assertNotNull(list);
            assertEquals(0, fb16Bit[i].remaining());
        }
    }

    @Test
    public void testEncode24Bit() {

        int bytePerPixel = 3;

        for (int i = 0; i < fb24Bit.length; i++) {
            //Encode
            List<ByteBuffer> list = RunLengthEncoder.encode(fb24Bit[i], bytePerPixel);
            assertNotNull(list);
            assertEquals(0, fb24Bit[i].remaining());
        }
    }

    @Test
    public void testEncodeInt() {

        int bytePerPixel = 4;

        for (int i = 0; i < fb32Bit.length; i++) {

            //Encode
            List<ByteBuffer> list = RunLengthEncoder.encode(fb32Bit[i], bytePerPixel);
            assertNotNull(list);
            assertEquals(0, fb32Bit[i].remaining());
        }
    }
}
