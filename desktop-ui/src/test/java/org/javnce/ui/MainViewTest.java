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
import org.junit.ClassRule;
import org.junit.Test;

public class MainViewTest {

    @ClassRule
    public static FXRule classRule = new FXRule();

    static class MyView extends View {

        boolean init;
        boolean create;
        boolean exit;

        MyView(Controller controller) {
            super(controller);
            init = false;
            create = false;
            exit = false;
        }

        @Override
        public void initProperties(ViewProperties properties) {
            init = true;
        }

        @Override
        public Node createNode() throws IOException {
            create = true;
            return null;
        }

        @Override
        public void onExit() {
            exit = true;
        }

        @Override
        public ViewFactory createFactory() {
            return new ViewFactory() {
                @Override
                public View viewFactory(Controller controller) {
                    return new MyView(controller);
                }
            };

        }
    }

    @Test
    public void testSetView() throws Exception {
        TestController controller = new TestController();
        MyView view = new MyView(controller);
        MainView instance = new MainView(controller.getConfig());
        instance.setView(view);
        assertTrue(view.init);
    }

    @Test
    public void testGetNode() throws Exception {
        TestController controller = new TestController();
        MainView instance = new MainView(controller.getConfig());
        Node result = instance.getNode();
        assertNotNull(result);
    }

    @Test
    public void testExit() throws IOException {
        TestController controller = new TestController();
        MyView view = new MyView(controller);
        MainView instance = new MainView(controller.getConfig());
        instance.setView(view);
        instance.exit();
        assertTrue(view.exit);
    }

    @Test
    public void testGetProperties() {
        TestController controller = new TestController();
        MainView instance = new MainView(controller.getConfig());
        ViewProperties result = instance.getProperties();
        assertNotNull(result);
    }
}
