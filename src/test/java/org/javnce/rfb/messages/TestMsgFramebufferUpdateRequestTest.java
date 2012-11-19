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
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestMsgFramebufferUpdateRequestTest {

    private MsgFramebufferUpdateRequest msg;
    private boolean incremental;
    private Rect rect;

    class TestData {

        final public boolean incremental;
        final public Rect rect;

        public TestData(boolean incremental, Rect rect) {
            this.incremental = incremental;
            this.rect = rect;
        }
    }

    @Before
    public void setUp() {
        rect = new Rect(new Point(4321, 8765), new Size(1234, 5678));
        incremental = true;
    }

    @Test
    public void testDemarshal() {
        TestData[] array = new TestData[]{
            new TestData(false, new Rect(new Point(1, 2), new Size(3, 4))),
            new TestData(true, new Rect(new Point(1, 2), new Size(3, 4))),
            new TestData(true, new Rect(new Point(0xFFFF, 0xFFFF), new Size(0xFFFF, 0xFFFF))),};

        for (int i = 0; i < array.length; i++) {

            msg = new MsgFramebufferUpdateRequest(array[i].incremental, array[i].rect);
            ArrayList<ByteBuffer> list = msg.marshal();
            assertEquals(1, list.size());


            msg = new MsgFramebufferUpdateRequest();
            assertTrue(msg.demarshal(MyByteBufferHelper.arrayListToBuffer(list)));
            assertTrue(msg.isValid());

            assertEquals(array[i].incremental, msg.getIncremental());
            assertEquals(array[i].rect, msg.getRect());
        }
    }

    @Test
    public void testMarshal() {
        //Not valid
        msg = new MsgFramebufferUpdateRequest();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());
    }

    @Test
    public void testMsgFramebufferUpdateRequest() {
        msg = new MsgFramebufferUpdateRequest();
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgFramebufferUpdateRequestBooleanRect() {
        msg = new MsgFramebufferUpdateRequest(incremental, rect);
        assertNotNull(msg);
        assertTrue(msg.isValid());
    }

    @Test
    public void testGetId() {
        msg = new MsgFramebufferUpdateRequest();
        assertNotNull(msg);
        assertEquals(Id.FramebufferUpdateRequest, msg.getId());

        msg = new MsgFramebufferUpdateRequest(incremental, rect);
        assertNotNull(msg);
        assertEquals(Id.FramebufferUpdateRequest, msg.getId());
    }
}
