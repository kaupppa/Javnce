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

/**
 * The Interface Controller defines view controllers interface.
 */
public interface Controller {

    /**
     * Show given view.
     *
     * @param view the view
     */
    void showView(View view);

    /**
     * Previous view.
     */
    void previousView();

    /**
     * Exit.
     */
    void exit();

    /**
     * Gets the config.
     *
     * @return the config
     */
    Config getConfig();

    /**
     * Gets the parent.
     *
     * @return the parent
     * @throws IOException Signals that an I/O exception has occurred.
     */
    Parent getParent() throws IOException;

    /**
     * Show help view.
     */
    void showHelp();

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    ViewProperties getProperties();
}
