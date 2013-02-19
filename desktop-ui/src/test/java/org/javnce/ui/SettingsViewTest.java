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

public class SettingsViewTest {
    
    @ClassRule
    public static FXRule classRule = new FXRule();

    @Test
    public void testInitProperties() {
        TestController controller = new TestController();
        SettingsView instance = new SettingsView(controller);
        instance.initProperties(controller.properties);
        assertFalse(controller.properties.getHelpDisabled().get());
        assertFalse(controller.properties.getNextDisabled().get());
        assertFalse(controller.properties.getBackDisabled().get());
        assertFalse(controller.properties.getFullscreenDisabled().get());
    }

    @Test
    public void testCreateNode() throws Exception {
        TestController controller = new TestController();
        SettingsView instance = new SettingsView(controller);
        Node result = instance.createNode();
        assertNotNull(result);
    }

    @Test
    public void testCreateFactory() {
        TestController controller = new TestController();
        SettingsView instance = new SettingsView(controller);
        ViewFactory result = instance.createFactory();
        assertNotNull(result);
        assertTrue(result.viewFactory(controller) instanceof SettingsView);
    }

    @Test
    public void testOnNext() {
        TestController controller = new TestController();
        SettingsView instance = new SettingsView(controller);
        instance.onNext();
        assertTrue(controller.view instanceof ServerView);
    }
}
