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
package org.javnce.vnc.client;

import org.javnce.eventing.EventLoop;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Rect;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Test;

public class VncClientControllerTest {

    @After
    public void tearDown() throws Exception {
        assertFalse(EventLoop.exists());
    }

    @Test
    public void testLaunch() throws Throwable {
        VncClientController controller = new VncClientController();

        TestServer server = new TestServer();
        controller.setObserver(new TestObserver());
        controller.launch(server.address());
        server.accept();
        controller.shutdown();
        server.close();
    }

    @Test
    public void testPointerEvent() {
        VncClientController.pointerEvent(0, 0, 0);
    }

    @Test
    public void testKeyEvent() {
        VncClientController.keyEvent(true, 0);
    }

    @Test
    public void testSetFormat() {
        VncClientController.setFormat(PixelFormat.createRGB565());
    }

    @Test
    public void testRequestFramebuffer() {
        VncClientController.requestFramebuffer(false, new Rect(0, 0, 10, 10));
    }
}
