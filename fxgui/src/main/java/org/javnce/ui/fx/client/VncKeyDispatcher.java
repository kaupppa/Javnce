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

import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.javnce.ui.model.ClientConfiguration;
import org.javnce.vnc.client.VncClientController;
import org.javnce.vnc.common.KeyMap;

/**
 * The class for dispatch key events to server.
 */
public class VncKeyDispatcher {

    /**
     * The controller.
     */
    final private VncClientController controller;
    /**
     * The key mapping.
     */
    final private KeyMap<KeyCode> map;

    /**
     * Instantiates a new dispatcher.
     */
    public VncKeyDispatcher() {
        controller = ClientConfiguration.instance().getVncController();
        map = new KeyMap<>();

        map.addMapping(KeyCode.SHIFT, KeyMap.XK_Shift_L);
        map.addMapping(KeyCode.CONTROL, KeyMap.XK_Control_L);
        map.addMapping(KeyCode.ALT, KeyMap.XK_Alt_L);
        map.addMapping(KeyCode.ALT_GRAPH, KeyMap.XK_Super_L);

        map.addMapping(KeyCode.WINDOWS, KeyMap.XK_Hyper_L);
        map.addMapping(KeyCode.CONTEXT_MENU, KeyMap.XK_Menu);

        map.addMapping(KeyCode.CAPS, KeyMap.XK_Caps_Lock);
        map.addMapping(KeyCode.NUM_LOCK, KeyMap.XK_Num_Lock);

        map.addMapping(KeyCode.TAB, KeyMap.XK_Tab);
        map.addMapping(KeyCode.BACK_SPACE, KeyMap.XK_BackSpace);
        map.addMapping(KeyCode.ENTER, KeyMap.XK_Return);
        map.addMapping(KeyCode.PAUSE, KeyMap.XK_Pause);
        map.addMapping(KeyCode.SCROLL_LOCK, KeyMap.XK_Scroll_Lock);
        map.addMapping(KeyCode.ESCAPE, KeyMap.XK_Escape);
        map.addMapping(KeyCode.DELETE, KeyMap.XK_Delete);

        map.addMapping(KeyCode.HOME, KeyMap.XK_Home);
        map.addMapping(KeyCode.LEFT, KeyMap.XK_Left);
        map.addMapping(KeyCode.UP, KeyMap.XK_Up);
        map.addMapping(KeyCode.RIGHT, KeyMap.XK_Right);
        map.addMapping(KeyCode.DOWN, KeyMap.XK_Down);
        map.addMapping(KeyCode.PAGE_UP, KeyMap.XK_Page_Up);
        map.addMapping(KeyCode.PAGE_DOWN, KeyMap.XK_Page_Down);
        map.addMapping(KeyCode.END, KeyMap.XK_End);
        map.addMapping(KeyCode.BEGIN, KeyMap.XK_Begin);
        map.addMapping(KeyCode.PRINTSCREEN, KeyMap.XK_Print);
        map.addMapping(KeyCode.INSERT, KeyMap.XK_Insert);
        map.addMapping(KeyCode.UNDO, KeyMap.XK_Undo);

        map.addMapping(KeyCode.FIND, KeyMap.XK_Find);
        map.addMapping(KeyCode.CANCEL, KeyMap.XK_Cancel);
        map.addMapping(KeyCode.HELP, KeyMap.XK_Help);
        map.addMapping(KeyCode.MODECHANGE, KeyMap.XK_Mode_switch);
    }

    /**
     * Register view for listening key events.
     *
     * @param node the node
     */
    public void register(Node node) {
        final EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                handlekeyEvent(t);
            }
        };
        node.getScene().setOnKeyPressed(handler);
        node.getScene().setOnKeyReleased(handler);
    }

    /**
     * Key event handler.
     *
     * @param event the event
     */
    private void handlekeyEvent(KeyEvent event) {

        if (KeyEvent.KEY_PRESSED == event.getEventType()) {
            sendKeyEvent(true, event);
            event.consume();
        } else if (KeyEvent.KEY_RELEASED == event.getEventType()) {
            sendKeyEvent(false, event);
            event.consume();
        }
    }

    /**
     * Dispatch key event.
     *
     * @param down the down
     * @param event the event
     */
    private void sendKeyEvent(boolean down, KeyEvent event) {

        final String keyChar = event.getText();
        final KeyCode keyCode = event.getCode();
        int keysym = KeyMap.None;

        if (keyChar.length() == 1) {
            //If char then check if unicode
            keysym = KeyMap.unicodeToKeySym(keyChar.charAt(0));
        }

        if (KeyMap.None == keysym) {
            //Check if mapped value

            if (KeyCode.UNDEFINED != keyCode) {
                keysym = map.getMapped(keyCode);
            }
        }

        if (KeyMap.None != keysym) {
            controller.keyEvent(down, keysym);
        }
    }
}
