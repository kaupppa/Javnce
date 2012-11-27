/*
 * Copyright (C) 2012 Pauli Kauppinen
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
package example.swingclient;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import org.javnce.ui.model.ClientConfiguration;
import org.javnce.vnc.client.VncClientController;

/**
 * The class for dispatching key events to server.
 */
public class VncKeyDispatcher implements KeyEventDispatcher {

    /**
     * The controller.
     */
    final private VncClientController controller;

    /**
     * Instantiates a new vnc key dispatcher.
     */
    public VncKeyDispatcher() {
        controller = ClientConfiguration.instance().getVncController();
    }

    /**
     * Register swing component.
     *
     * @param component the component
     */
    public void register(Component component) {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }

    /* (non-Javadoc)
     * @see java.awt.KeyEventDispatcher#dispatchKeyEvent(java.awt.event.KeyEvent)
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int id = event.getID();

        if (id == KeyEvent.KEY_PRESSED) {
            sendKeyEvent(true, event);
        } else if (id == KeyEvent.KEY_RELEASED) {
            sendKeyEvent(false, event);
        }
        return false;
    }

    /**
     * Send key event.
     *
     * @param down the down
     * @param event the event
     */
    private void sendKeyEvent(boolean down, KeyEvent event) {
        char keyChar = event.getKeyChar();
        int keyCode = event.getKeyCode();

        //logger.info("keyPressed : " + down + " " + keyChar + String.format(" (0x%08X)", (long)keyChar) + String.format(" (0x%08X)", keyCode));

        if (0x20 > keyCode && 0 != keyCode) {
            controller.keyEvent(down, keyCode | 0xFF00);
        } else if (KeyEvent.CHAR_UNDEFINED != keyChar) {
            controller.keyEvent(down, keyChar);
        } else {
            //TODO More java key to keysym mapping needed
            /*
             if (down) {
             logger.warning("Unhandled key event : " + KeyEvent.getKeyText(keyCode)
             + String.format(" char=(0x%08X)", (long) keyChar)
             + String.format(" code=(0x%08X)", keyCode));
             }
             */
        }
    }
}