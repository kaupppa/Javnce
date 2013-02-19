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
package org.javnce.ui;

import java.io.IOException;
import javafx.scene.Node;
import static org.junit.Assert.*;
import org.junit.Test;

public class ViewTest {

    class MyView extends View {

        public MyView(Controller controller) {
            super(controller);
        }

        @Override
        public Node createNode() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ViewFactory createFactory() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Test
    public void testInitProperties() {
        TestController controller = new TestController();
        MyView instance = new MyView(controller);
        instance.initProperties(controller.properties);
        assertFalse(controller.properties.getHelpDisabled().get());
        assertTrue(controller.properties.getNextDisabled().get());
        assertFalse(controller.properties.getBackDisabled().get());
        assertFalse(controller.properties.getFullscreenDisabled().get());
    }

    @Test
    public void testGetProperties() {
        TestController controller = new TestController();
        MyView instance = new MyView(controller);
        ViewProperties result = instance.getProperties();
        assertNotNull(result);
    }

    @Test
    public void testGetController() {
        TestController controller = new TestController();
        MyView instance = new MyView(controller);
        Controller result = instance.getController();
        assertNotNull(result);
    }

    @Test
    public void testOnBack() {
        TestController controller = new TestController();
        MyView instance = new MyView(controller);
        instance.onBack();
        assertTrue(controller.previous);
    }

    @Test
    public void testOnHelp() {
        TestController controller = new TestController();
        MyView instance = new MyView(controller);
        instance.onHelp();
        assertTrue(controller.help);
    }

    @Test
    public void testOnFullScreen() {
        TestController controller = new TestController();
        MyView instance = new MyView(controller);
        instance.onFullScreen();
        assertTrue(controller.properties.getFullScreen().get());
    }
}
