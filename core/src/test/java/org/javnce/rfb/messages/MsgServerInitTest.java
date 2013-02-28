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
import org.javnce.rfb.types.*;
import org.javnce.util.ByteBuffers;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MsgServerInitTest {

    private MsgServerInit msg;
    private PixelFormat format;
    private Size size;
    private String name;

    class TestData {

        final public PixelFormat format;
        final public Size size;
        final public String name;

        public TestData(PixelFormat format, Size size, String name) {
            this.format = format;
            this.size = size;
            this.name = name;
        }
    }

    @Before
    public void setUp() {
        format = new PixelFormat(32, 24, false, true, new Color(123, 456, 789), new Color(1, 2, 3));
        size = new Size(1600, 1200);
        name = "Tadaaaaaaaaaa";
    }

    @Test
    public void testDemarshal() {

        TestData[] array = new TestData[]{
            new TestData(new PixelFormat(0, 1, false, true, new Color(2, 3, 4), new Color(5, 6, 7)),
            new Size(8, 9),
            "10"),
            new TestData(new PixelFormat(0xF0, 0xF1, false, true, new Color(0xFFF2, 0xFFF3, 0xFFF4), new Color(0xF5, 0xF6, 0xF7)),
            new Size(0xFFF8, 0xFFF9),
            "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF10")
        };

        for (int i = 0; i < array.length; i++) {

            msg = new MsgServerInit(array[i].format, array[i].size, array[i].name);
            ArrayList<ByteBuffer> list = msg.marshal();
            assertEquals(1, list.size());
            assertTrue(msg.isValid());

            msg = new MsgServerInit();
            assertTrue(msg.demarshal(ByteBuffers.asBuffer(list)));
            assertTrue(msg.isValid());

            assertEquals(array[i].format, msg.getFormat());
            assertEquals(array[i].size, msg.getSize());
            assertEquals(array[i].name, msg.getName());
        }
    }

    @Test
    public void testMarshal() {
        //See testDemarshal

        //Not valid
        msg = new MsgServerInit();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());

    }

    @Test
    public void testMsgServerInit() {
        msg = new MsgServerInit();
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgServerInitPixelFormatSizeString() {
        msg = new MsgServerInit(format, size, name);
        assertNotNull(msg);
        assertTrue(msg.isValid());
    }

    @Test
    public void testGetFormat() {
        msg = new MsgServerInit(format, size, name);
        assertEquals(format, msg.getFormat());
    }

    @Test
    public void testGetSize() {
        msg = new MsgServerInit(format, size, name);
        assertEquals(size, msg.getSize());
    }

    @Test
    public void testGetName() {
        msg = new MsgServerInit(format, size, name);
        assertEquals(name, msg.getName());
    }

    @Test
    public void testGetId() {
        msg = new MsgServerInit();
        assertNotNull(msg);
        assertEquals(Id.ServerInit, msg.getId());

        msg = new MsgServerInit(format, size, name);
        assertNotNull(msg);
        assertEquals(Id.ServerInit, msg.getId());
    }

    @Test
    public void testToString() {
        msg = new MsgServerInit();
        String text = msg.toString();
        assertNotNull(text);

        msg = new MsgServerInit(format, size, name);
        text = msg.toString();
        assertNotNull(text);
    }
}
