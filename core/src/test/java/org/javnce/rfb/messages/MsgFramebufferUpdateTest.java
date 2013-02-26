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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.javnce.rfb.types.Encoding;
import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;
import static org.junit.Assert.*;
import org.junit.Test;

public class MsgFramebufferUpdateTest {

    static final private PixelFormat format = PixelFormat.createRGB565();
    final private static Random rnd = new Random();
    MsgFramebufferUpdate msg;

    static ByteBuffer[] generateRandom(Size size, int bpp) {

        ByteBuffer buf = ByteBuffer.allocate(size.width() * size.height() * bpp);
        byte bytes[] = new byte[bpp];

        while (0 != buf.remaining()) {
            rnd.nextBytes(bytes);
            buf.put(bytes);
        }

        buf.clear();
        return new ByteBuffer[]{buf};
    }

    static Framebuffer[] generateRandomArray(Size size, int bpp) {

        Framebuffer array[] = new Framebuffer[3];

        Rect rect = new Rect(new Point(0, 0), size);
        array[0] = new Framebuffer(rect, Encoding.RAW, generateRandom(size, bpp));
        array[1] = new Framebuffer(rect, Encoding.RLE, generateRandom(size, bpp));
        array[2] = new Framebuffer(rect, Encoding.LZ4, generateRandom(size, bpp));
        return array;
    }

    @Test
    public void testDemarshal() {
        Size array[] = new Size[]{
            new Size(100, 100),
            new Size(800, 480),
            new Size(1440, 900),
            new Size(1920, 1080),};


        for (Size size : array) {
            Framebuffer[] fb = generateRandomArray(size, format.bytesPerPixel());

            msg = new MsgFramebufferUpdate(format, fb);
            ArrayList<ByteBuffer> list = msg.marshal();

            assertTrue(msg.isValid());

            msg = new MsgFramebufferUpdate(format);
            ByteBuffer rec = MyByteBufferHelper.arrayListToBuffer(list);
            assertTrue(msg.demarshal(rec));
            assertTrue(msg.isValid());
            assertEquals(0, rec.remaining());

            Framebuffer[] result = msg.get();
            assertEquals(fb.length, result.length);

            for (int i = 0; i < fb.length; i++) {
                assertEquals(fb[i].rect(), result[i].rect());
                assertEquals(fb[i].encoding(), result[i].encoding());
                assertTrue(Arrays.equals(fb[i].buffers(), result[i].buffers()));

                for (ByteBuffer temp : fb[i].buffers()) {
                    assertEquals(0, temp.position());
                }
            }
        }
    }

    @Test
    public void testDemarshalEmpty() {

        msg = new MsgFramebufferUpdate(format, new Framebuffer[0]);
        ArrayList<ByteBuffer> list = msg.marshal();
        assertTrue(msg.isValid());

        msg = new MsgFramebufferUpdate(format);
        ByteBuffer rec = MyByteBufferHelper.arrayListToBuffer(list);
        assertTrue(msg.demarshal(rec));
        assertTrue(msg.isValid());
        assertEquals(0, rec.remaining());

        assertTrue(0 == msg.get().length);
    }

    @Test
    public void testMarshal() {
        //Not valid
        msg = new MsgFramebufferUpdate(format);
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());

        Size size = new Size(100, 100);
        Framebuffer array[] = new Framebuffer[1];
        Rect rect = new Rect(new Point(0, 0), size);


        //Only Rle
        array[0] = new Framebuffer(rect, Encoding.RLE, new ByteBuffer[]{ByteBuffer.allocate(100)});
        msg = new MsgFramebufferUpdate(format, array);
        list = msg.marshal();
        assertEquals(2, list.size());

        //Empty
        msg = new MsgFramebufferUpdate(format, null);
        list = msg.marshal();
        assertEquals(1, list.size());

        //Empty
        msg = new MsgFramebufferUpdate(format, new Framebuffer[0]);
        list = msg.marshal();
        assertEquals(1, list.size());
    }

    @Test
    public void testMsgFramebufferUpdate() {
        msg = new MsgFramebufferUpdate(format);
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgFramebufferUpdateFramebufferArray() {

        msg = new MsgFramebufferUpdate(format, generateRandomArray(new Size(100, 100), format.bytesPerPixel()));
        assertNotNull(msg);
        assertTrue(msg.isValid());
    }

    @Test
    public void testGetId() {
        msg = new MsgFramebufferUpdate(format);
        assertNotNull(msg);
        assertEquals(Id.FramebufferUpdate, msg.getId());

        msg = new MsgFramebufferUpdate(format, generateRandomArray(new Size(100, 100), format.bytesPerPixel()));

        assertNotNull(msg);
        assertEquals(Id.FramebufferUpdate, msg.getId());
    }

    @Test
    public void testToString() {
        msg = new MsgFramebufferUpdate(format);
        String text = msg.toString();
        assertNotNull(text);

        Framebuffer array[] = new Framebuffer[1];
        Rect rect = new Rect(new Point(0, 0), new Size(100, 100));
        array[0] = new Framebuffer(rect, Encoding.RLE, new ByteBuffer[]{ByteBuffer.allocate(100)});
        msg = new MsgFramebufferUpdate(format, array);

        text = msg.toString();
        assertNotNull(text);
    }
}
