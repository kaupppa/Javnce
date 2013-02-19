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

import javafx.scene.Node;
import static org.junit.Assert.*;
import org.junit.ClassRule;
import org.junit.Test;

public class ModeViewTest {

    @ClassRule
    public static FXRule classRule = new FXRule();

    @Test
    public void testInitProperties() {
        TestController controller = new TestController();
        ModeView instance = new ModeView(controller);
        instance.initProperties(controller.properties);
        assertFalse(controller.properties.getHelpDisabled().get());
        assertTrue(controller.properties.getNextDisabled().get());
        assertTrue(controller.properties.getBackDisabled().get());
        assertFalse(controller.properties.getFullscreenDisabled().get());
    }

    @Test
    public void testCreateNode() throws Exception {
        TestController controller = new TestController();
        ModeView instance = new ModeView(controller);
        Node result = instance.createNode();
        assertNotNull(result);
    }

    @Test
    public void testOnConnect() {
        TestController controller = new TestController();
        ModeView instance = new ModeView(controller);
        instance.onConnect();
        assertNotNull(controller.view);
        assertTrue(controller.view instanceof SearchView);
    }

    @Test
    public void testOnShare() {
        TestController controller = new TestController();
        ModeView instance = new ModeView(controller);
        instance.onShare();
        assertNotNull(controller.view);
        assertTrue(controller.view instanceof SettingsView);
    }

    @Test
    public void testCreateFactory() {
        TestController controller = new TestController();
        ModeView instance = new ModeView(controller);
        ViewFactory result = instance.createFactory();
        assertNotNull(result);
        assertTrue(result.viewFactory(controller) instanceof ModeView);
    }
}
