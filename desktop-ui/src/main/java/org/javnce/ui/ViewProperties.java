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

import javafx.beans.property.SimpleBooleanProperty;

/**
 * The Class ViewProperties contains MainView properties.
 */
public class ViewProperties {

    /** The back button disabled. */
    final private SimpleBooleanProperty backDisabled;
    
    /** The next button disabled. */
    final private SimpleBooleanProperty nextDisabled;
    
    /** The help button disabled. */
    final private SimpleBooleanProperty helpDisabled;
    
    /** The fullscreen button disabled. */
    final private SimpleBooleanProperty fullscreenDisabled;
    
    /** The MainView init done. */
    final private SimpleBooleanProperty initDone;
    
    /** The full screen mode. */
    final private SimpleBooleanProperty fullScreen;

    /**
     * Instantiates a new view properties.
     */
    public ViewProperties() {
        backDisabled = new SimpleBooleanProperty(false);
        nextDisabled = new SimpleBooleanProperty(false);
        helpDisabled = new SimpleBooleanProperty(false);
        initDone = new SimpleBooleanProperty(false);
        fullscreenDisabled = new SimpleBooleanProperty(false);
        fullScreen = new SimpleBooleanProperty(false);
    }

    /**
     * Gets the back button disabled property.
     *
     * @return the back disabled
     */
    public SimpleBooleanProperty getBackDisabled() {
        return backDisabled;
    }

    /**
     * Gets the next button disabled property.
     *
     * @return the next disabled
     */
    public SimpleBooleanProperty getNextDisabled() {
        return nextDisabled;
    }

    /**
     * Gets the help button disabled property.
     *
     * @return the help disabled
     */
    public SimpleBooleanProperty getHelpDisabled() {
        return helpDisabled;
    }

    /**
     * Gets the inits the done property.
     *
     * @return the inits the done
     */
    public SimpleBooleanProperty getInitDone() {
        return initDone;
    }

    /**
     * Gets the fullscreen button disabled property.
     *
     * @return the fullscreen disabled
     */
    public SimpleBooleanProperty getFullscreenDisabled() {
        return fullscreenDisabled;
    }

    /**
     * Gets the full screen property.
     *
     * @return the full screen
     */
    public SimpleBooleanProperty getFullScreen() {
        return fullScreen;
    }
}
