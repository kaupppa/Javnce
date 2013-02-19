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
import javafx.scene.Parent;

public class TestController implements Controller {

    View view;
    boolean previous;
    boolean help;
    TestConfig testConfig;
    ViewProperties properties;

    public TestController() {
        previous = false;
        help = false;
        testConfig = new TestConfig();
        properties = new ViewProperties();
    }

    @Override
    public void showView(View view) {
        this.view = view;
    }

    @Override
    public void previousView() {
        previous = true;
    }

    @Override
    public void exit() {
    }

    @Override
    public Config getConfig() {
        return testConfig;
    }

    @Override
    public Parent getParent() throws IOException {
        return null;
    }

    @Override
    public void showHelp() {
        help = true;
    }

    @Override
    public ViewProperties getProperties() {
        return properties;
    }
}
