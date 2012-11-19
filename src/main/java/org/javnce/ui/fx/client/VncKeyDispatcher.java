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
package org.javnce.ui.fx.client;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import org.javnce.ui.model.ClientConfiguration;
import org.javnce.vnc.client.VncClientController;

/**
 * The class for dispatch key events to server.
 */
public class VncKeyDispatcher {

    /**
     * The controller.
     */
    final private VncClientController controller;

    /**
     * Instantiates a new dispatcher.
     */
    public VncKeyDispatcher() {
        controller = ClientConfiguration.instance().getVncController();
    }

    /**
     * Register view for listening key events.
     *
     * @param node the node
     */
    public void register(Node node) {
        node.getScene().setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent t) {
                        handlekeyEvent(t);
                    }
                });
        node.getScene().setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent t) {
                        handlekeyEvent(t);
                    }
                });
    }

    /**
     * Key event handler.
     *
     * @param event the event
     */
    private void handlekeyEvent(KeyEvent event) {
        if (KeyEvent.KEY_PRESSED == event.getEventType()) {
            sendKeyEvent(true, event);
        } else if (KeyEvent.KEY_RELEASED == event.getEventType()) {
            sendKeyEvent(false, event);
        }

    }

    /**
     * Dispatch key event.
     *
     * @param down the down
     * @param event the event
     */
    private void sendKeyEvent(boolean down, KeyEvent event) {
        String keyChar = event.getText();
        //KeyCode keyCode = event.getCode();

        if (keyChar.length() == 1) {
            controller.keyEvent(down, keyChar.charAt(0));
        } else {
            //FIXME Missing proper Java key to VNC key conversion
        }
    }
}
