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
import org.javnce.util.ByteBuffers;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MsgServerCutTextTest {

    MsgServerCutText msg;
    String data;

    @Before
    public void setUp() throws Exception {
        data = "ASDASDASDASDASDasdasddfkäaglasfldfal,sfasfd";
    }

    @Test
    public void testDemarshal() {
        msg = new MsgServerCutText(data);
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(1, list.size());
        assertTrue(msg.isValid());

        msg = new MsgServerCutText();
        assertTrue(msg.demarshal(ByteBuffers.asBuffer(list)));
        assertTrue(msg.isValid());

        assertEquals(data, msg.get());
    }

    @Test
    public void testMarshal() {
        //Not valid
        msg = new MsgServerCutText();
        ArrayList<ByteBuffer> list = msg.marshal();
        assertEquals(0, list.size());
    }

    @Test
    public void testMsgServerCutText() {
        msg = new MsgServerCutText();
        assertNotNull(msg);
        assertFalse(msg.isValid());
    }

    @Test
    public void testMsgServerCutTextString() {
        msg = new MsgServerCutText(data);
        assertNotNull(msg);
        assertTrue(msg.isValid());

    }

    @Test
    public void testGet() {
        msg = new MsgServerCutText();
        assertNull(msg.get());

        msg = new MsgServerCutText(data);
        assertNotNull(msg.get());
        assertEquals(data, msg.get());
    }

    @Test
    public void testGetId() {
        msg = new MsgServerCutText();
        assertNotNull(msg);
        assertEquals(Id.ServerCutText, msg.getId());

        msg = new MsgServerCutText(data);
        assertNotNull(msg);
        assertEquals(Id.ServerCutText, msg.getId());

    }

    @Test
    public void testToString() {
        msg = new MsgServerCutText();
        String text = msg.toString();
        assertNotNull(text);
    }
}
