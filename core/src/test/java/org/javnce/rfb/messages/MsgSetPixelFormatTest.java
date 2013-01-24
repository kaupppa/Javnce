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
import org.javnce.rfb.types.Color;
import org.javnce.rfb.types.PixelFormat;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MsgSetPixelFormatTest {

    private MsgSetPixelFormat msg;
    private PixelFormat format;

    @Before
    public void setUp() {
        format = new PixelFormat(32, 24, false, true, new Color(123, 456, 789), new Color(1, 2, 3));
    }

    @Test
    public void testDemarshal() {
        PixelFormat[] array = new PixelFormat[]{new PixelFormat(0, 1, false, true, new Color(2, 3, 4), new Color(5, 6, 7)),
            new PixelFormat(0xF0, 0xF1, false, true, new Color(0xFFF2, 0xFFF3, 0xFFF4), new Color(0xF5, 0xF6, 0xF7)),};

        for (int i = 0; i < array.length; i++) {

            msg = new MsgSetPixelFormat(array[i]);
            ArrayList<ByteBuffer> list = msg.marshal();
            assertEquals(1, list.size());

            msg = new MsgSetPixelFormat();
            assertTrue(msg.demarshal(MyByteBufferHelper.arrayListToBuffer(list)));
            assertTrue(msg.isValid());

            assertEquals(array[i], msg.get());
        }
    }

    @Test
    public void testMarshal() {
        //Not valid
        msg = new MsgSetPixelFormat();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());

    }

    @Test
    public void testMsgSetPixelFormat() {
        msg = new MsgSetPixelFormat();
        assertNotNull(msg);
        assertFalse(msg.isValid());

    }

    @Test
    public void testMsgSetPixelFormatPixelFormat() {
        msg = new MsgSetPixelFormat(format);
        assertNotNull(msg);
        assertTrue(msg.isValid());

    }

    @Test
    public void testGet() {
        msg = new MsgSetPixelFormat(format);
        assertEquals(format, msg.get());
    }

    @Test
    public void testGetId() {
        msg = new MsgSetPixelFormat();
        assertNotNull(msg);
        assertEquals(Id.SetPixelFormat, msg.getId());

        msg = new MsgSetPixelFormat(format);
        assertNotNull(msg);
        assertEquals(Id.SetPixelFormat, msg.getId());
    }

    @Test
    public void testToString() {
        msg = new MsgSetPixelFormat();
        String text = msg.toString();
        assertNotNull(text);
    }
}
