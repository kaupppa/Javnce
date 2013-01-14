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
import org.javnce.vnc.common.KeyMap;

/**
 * The class for dispatching key events to server.
 */
public class VncKeyDispatcher implements KeyEventDispatcher {

    /**
     * The controller.
     */
    final private VncClientController controller;
    /**
     * The key mapping.
     */
    final private KeyMap<Integer> map;

    /**
     * Instantiates a new vnc key dispatcher.
     */
    public VncKeyDispatcher() {
        controller = ClientConfiguration.instance().getVncController();
        map = new KeyMap<>();

        map.addMapping(KeyEvent.VK_ALT, KeyMap.XK_Alt_L);
        map.addMapping(KeyEvent.VK_SHIFT, KeyMap.XK_Shift_L);
        map.addMapping(KeyEvent.VK_CONTROL, KeyMap.XK_Control_L);
        map.addMapping(KeyEvent.VK_CAPS_LOCK, KeyMap.XK_Caps_Lock);
        map.addMapping(KeyEvent.VK_NUM_LOCK, KeyMap.XK_Num_Lock);
        map.addMapping(KeyEvent.VK_WINDOWS, KeyMap.XK_Super_L);

        map.addMapping(KeyEvent.VK_TAB, KeyMap.XK_Tab);
        map.addMapping(KeyEvent.VK_BACK_SPACE, KeyMap.XK_BackSpace);
        map.addMapping(KeyEvent.VK_ENTER, KeyMap.XK_Return);
        map.addMapping(KeyEvent.VK_PAUSE, KeyMap.XK_Pause);
        map.addMapping(KeyEvent.VK_SCROLL_LOCK, KeyMap.XK_Scroll_Lock);
        map.addMapping(KeyEvent.VK_ESCAPE, KeyMap.XK_Escape);
        map.addMapping(KeyEvent.VK_DELETE, KeyMap.XK_Delete);

        map.addMapping(KeyEvent.VK_HOME, KeyMap.XK_Home);
        map.addMapping(KeyEvent.VK_LEFT, KeyMap.XK_Left);
        map.addMapping(KeyEvent.VK_UP, KeyMap.XK_Up);
        map.addMapping(KeyEvent.VK_RIGHT, KeyMap.XK_Right);
        map.addMapping(KeyEvent.VK_DOWN, KeyMap.XK_Down);
        map.addMapping(KeyEvent.VK_PAGE_UP, KeyMap.XK_Page_Up);
        map.addMapping(KeyEvent.VK_PAGE_DOWN, KeyMap.XK_Page_Down);
        map.addMapping(KeyEvent.VK_END, KeyMap.XK_End);
        map.addMapping(KeyEvent.VK_BEGIN, KeyMap.XK_Begin);
        map.addMapping(KeyEvent.VK_PRINTSCREEN, KeyMap.XK_Sys_Req);
        map.addMapping(KeyEvent.VK_INSERT, KeyMap.XK_Insert);
        map.addMapping(KeyEvent.VK_UNDO, KeyMap.XK_Undo);
        map.addMapping(KeyEvent.VK_CONTEXT_MENU, KeyMap.XK_Menu);
        map.addMapping(KeyEvent.VK_FIND, KeyMap.XK_Find);
        map.addMapping(KeyEvent.VK_CANCEL, KeyMap.XK_Cancel);
        map.addMapping(KeyEvent.VK_HELP, KeyMap.XK_Help);
        map.addMapping(KeyEvent.VK_MODECHANGE, KeyMap.XK_Mode_switch);
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

        final char keyChar = event.getKeyChar();
        final int keyCode = event.getKeyCode();
        int keysym = KeyMap.None;

        if (KeyEvent.CHAR_UNDEFINED != keyChar) {
            keysym = KeyMap.unicodeToKeySym(keyChar);
        }
        if (KeyMap.None == keysym && KeyEvent.VK_UNDEFINED != keyCode) {
            //Check if mapped value
            keysym = map.getMapped(keyCode);
        }

        if (KeyMap.None != keysym) {
            controller.keyEvent(down, keysym);
        }

    }
}