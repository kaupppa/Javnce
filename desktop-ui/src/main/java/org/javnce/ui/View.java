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

/**
 * The Class View is the interface and default implementation for a view.
 */
abstract public class View {

    /**
     * The controller.
     */
    final private Controller controller;

    /**
     * Instantiates a new view.
     *
     * @param controller the controller
     */
    public View(Controller controller) {
        this.controller = controller;
    }

    /**
     * Called before view is added into MainView.
     *
     * The default implementation disables next button.
     *
     * @param properties the properties
     */
    public void initProperties(ViewProperties properties) {
        properties.getBackDisabled().set(false);
        properties.getNextDisabled().set(true);
        properties.getHelpDisabled().set(false);
        properties.getFullscreenDisabled().set(false);
    }

    /**
     * Method to get root node of view.
     *
     * @return the node
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract Node createNode() throws IOException;

    /**
     * Called when view is removed from MainView.
     *
     * The default implementation does nothing.
     */
    public void onExit() {
        //Do nothing
    }

    /**
     * Creates the factory.
     *
     * The factory is used in view stack to support back button.
     *
     * @return the view factory
     */
    public abstract ViewFactory createFactory();

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public ViewProperties getProperties() {
        return controller.getProperties();
    }

    /**
     * Gets the controller.
     *
     * @return the controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Called when back button pressed.
     *
     * The default implementation switches view to previous one.
     */
    public void onBack() {
        controller.previousView();
    }

    /**
     * Called when next button pressed.
     *
     * The default implementation does nothing.
     */
    public void onNext() {
        //
    }

    /**
     * Called when help button pressed.
     *
     * The default implementation shows the main help.
     */
    public void onHelp() {
        controller.showHelp();
    }

    /**
     * Called when ful screen button pressed.
     *
     * The default implementation updates the property.
     */
    public void onFullScreen() {
        controller.getProperties().getFullScreen().set(true);
    }
}
