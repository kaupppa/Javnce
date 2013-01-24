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
import org.javnce.rfb.types.Point;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MsgPointerEventTest {

    MsgPointerEvent msg;
    private int mask;
    private Point point;

    @Before
    public void setUp() {
        mask = 0xFF;
        point = new Point(0xABBA, 0xBEEF);
    }

    class TestData {

        final int mask;
        final Point point;

        public TestData(int mask, Point point) {
            this.mask = mask;
            this.point = point;
        }
    }

    @Test
    public void testDemarshal() {
        TestData[] array = new TestData[]{
            new TestData(0, new Point(0, 0)),
            new TestData(mask, point),
            new TestData(0xFF, new Point(0xFFFF, 0xFFFF)),};

        for (int i = 0; i < array.length; i++) {

            msg = new MsgPointerEvent(array[i].mask, array[i].point);
            ArrayList<ByteBuffer> list = msg.marshal();
            assertEquals(1, list.size());

            msg = new MsgPointerEvent();
            assertTrue(msg.demarshal(MyByteBufferHelper.arrayListToBuffer(list)));
            assertTrue(msg.isValid());

            assertEquals(array[i].mask, msg.getMask());
            assertEquals(array[i].point, msg.getPoint());
        }
    }

    @Test
    public void testMarshal() {
        //Not valid
        msg = new MsgPointerEvent();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());

    }

    @Test
    public void testMsgPointerEvent() {
        msg = new MsgPointerEvent();
        assertNotNull(msg);
        assertFalse(msg.isValid());

    }

    @Test
    public void testMsgPointerEventIntPoint() {
        msg = new MsgPointerEvent(mask, point);
        assertNotNull(msg);
        assertTrue(msg.isValid());

    }

    @Test
    public void testGetId() {
        msg = new MsgPointerEvent();
        assertNotNull(msg);
        assertEquals(Id.PointerEvent, msg.getId());

        msg = new MsgPointerEvent(mask, point);
        assertNotNull(msg);
        assertEquals(Id.PointerEvent, msg.getId());
    }

    @Test
    public void testToString() {
        msg = new MsgPointerEvent();
        String text = msg.toString();
        assertNotNull(text);
    }
}
