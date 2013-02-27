/*
 * Copyright (C) 2013 Pauli
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
package org.javnce.vnc.server.platform;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import org.javnce.vnc.common.KeyMap;

/**
 * The Class KeysymToAwtKeyCode provides keysym to AWT key code mapping.
 */
class KeysymToAwtKeyCode {

    /**
     * The map with keysym as key and AWT key code as value
     */
    final static private Map<Integer, Integer> keySymMap = createMap();

    /**
     * Instantiates a new KeysymToAwtKeyCode.
     */
    private KeysymToAwtKeyCode() {
    }

    /**
     * Creates the map.
     */
    static private Map<Integer, Integer> createMap() {
        Map<Integer, Integer> map = new HashMap<>();
        //TODO The real thing. Read keyboard layout and build real code and modifier mapping. 

        //Add a-z & A-Z
        for (int i = 'A'; i <= 'Z'; i++) {
            //Capital
            map.put(new Integer(i), new Integer(i));
            //Small
            map.put(new Integer(i + 32), new Integer(i));
        }
        //Add 0-9
        for (int i = '0'; i <= '9'; i++) {
            map.put(new Integer(i), new Integer(i));
        }
        map.put(new Integer(KeyMap.XK_Alt_L), new Integer(KeyEvent.VK_ALT));
        map.put(new Integer(KeyMap.XK_Alt_R), new Integer(KeyEvent.VK_ALT));
        map.put(new Integer(KeyMap.XK_ampersand), new Integer(KeyEvent.VK_AMPERSAND));
        map.put(new Integer(KeyMap.XK_apostrophe), new Integer(KeyEvent.VK_QUOTE));
        map.put(new Integer(KeyMap.XK_asciicircum), new Integer(KeyEvent.VK_CIRCUMFLEX));
        map.put(new Integer(KeyMap.XK_asterisk), new Integer(KeyEvent.VK_ASTERISK));
        map.put(new Integer(KeyMap.XK_at), new Integer(KeyEvent.VK_AT));
        map.put(new Integer(KeyMap.XK_backslash), new Integer(KeyEvent.VK_BACK_SLASH));
        map.put(new Integer(KeyMap.XK_BackSpace), new Integer(KeyEvent.VK_BACK_SPACE));
        map.put(new Integer(KeyMap.XK_Begin), new Integer(KeyEvent.VK_BEGIN));
        map.put(new Integer(KeyMap.XK_braceleft), new Integer(KeyEvent.VK_BRACELEFT));
        map.put(new Integer(KeyMap.XK_braceright), new Integer(KeyEvent.VK_BRACERIGHT));
        map.put(new Integer(KeyMap.XK_bracketleft), new Integer(KeyEvent.VK_OPEN_BRACKET));
        map.put(new Integer(KeyMap.XK_bracketright), new Integer(KeyEvent.VK_CLOSE_BRACKET));
        map.put(new Integer(KeyMap.XK_Cancel), new Integer(KeyEvent.VK_CANCEL));
        map.put(new Integer(KeyMap.XK_Caps_Lock), new Integer(KeyEvent.VK_CAPS_LOCK));
        map.put(new Integer(KeyMap.XK_Clear), new Integer(KeyEvent.VK_CLEAR));
        map.put(new Integer(KeyMap.XK_colon), new Integer(KeyEvent.VK_COLON));
        map.put(new Integer(KeyMap.XK_comma), new Integer(KeyEvent.VK_COMMA));
        map.put(new Integer(KeyMap.XK_Control_L), new Integer(KeyEvent.VK_CONTROL));
        map.put(new Integer(KeyMap.XK_Control_R), new Integer(KeyEvent.VK_CONTROL));
        map.put(new Integer(KeyMap.XK_Delete), new Integer(KeyEvent.VK_DELETE));
        map.put(new Integer(KeyMap.XK_dollar), new Integer(KeyEvent.VK_DOLLAR));
        map.put(new Integer(KeyMap.XK_Down), new Integer(KeyEvent.VK_DOWN));
        map.put(new Integer(KeyMap.XK_End), new Integer(KeyEvent.VK_END));
        map.put(new Integer(KeyMap.XK_equal), new Integer(KeyEvent.VK_EQUALS));
        map.put(new Integer(KeyMap.XK_Escape), new Integer(KeyEvent.VK_ESCAPE));
        map.put(new Integer(KeyMap.XK_exclam), new Integer(KeyEvent.VK_EXCLAMATION_MARK));
        map.put(new Integer(KeyMap.XK_exclamdown), new Integer(KeyEvent.VK_INVERTED_EXCLAMATION_MARK));
        map.put(new Integer(KeyMap.XK_Find), new Integer(KeyEvent.VK_FIND));
        map.put(new Integer(KeyMap.XK_grave), new Integer(KeyEvent.VK_BACK_QUOTE));
        map.put(new Integer(KeyMap.XK_greater), new Integer(KeyEvent.VK_GREATER));
        map.put(new Integer(KeyMap.XK_Help), new Integer(KeyEvent.VK_HELP));
        map.put(new Integer(KeyMap.XK_Home), new Integer(KeyEvent.VK_HOME));
        map.put(new Integer(KeyMap.XK_Insert), new Integer(KeyEvent.VK_INSERT));
        map.put(new Integer(KeyMap.XK_KP_0), new Integer(KeyEvent.VK_NUMPAD0));
        map.put(new Integer(KeyMap.XK_KP_1), new Integer(KeyEvent.VK_NUMPAD1));
        map.put(new Integer(KeyMap.XK_KP_2), new Integer(KeyEvent.VK_NUMPAD2));
        map.put(new Integer(KeyMap.XK_KP_3), new Integer(KeyEvent.VK_NUMPAD3));
        map.put(new Integer(KeyMap.XK_KP_4), new Integer(KeyEvent.VK_NUMPAD4));
        map.put(new Integer(KeyMap.XK_KP_5), new Integer(KeyEvent.VK_NUMPAD5));
        map.put(new Integer(KeyMap.XK_KP_6), new Integer(KeyEvent.VK_NUMPAD6));
        map.put(new Integer(KeyMap.XK_KP_7), new Integer(KeyEvent.VK_NUMPAD7));
        map.put(new Integer(KeyMap.XK_KP_8), new Integer(KeyEvent.VK_NUMPAD8));
        map.put(new Integer(KeyMap.XK_KP_9), new Integer(KeyEvent.VK_NUMPAD9));
        map.put(new Integer(KeyMap.XK_KP_Add), new Integer(KeyEvent.VK_ADD));
        map.put(new Integer(KeyMap.XK_KP_Begin), new Integer(KeyEvent.VK_BEGIN));
        map.put(new Integer(KeyMap.XK_KP_Decimal), new Integer(KeyEvent.VK_DECIMAL));
        map.put(new Integer(KeyMap.XK_KP_Delete), new Integer(KeyEvent.VK_DELETE));
        map.put(new Integer(KeyMap.XK_KP_Divide), new Integer(KeyEvent.VK_DIVIDE));
        map.put(new Integer(KeyMap.XK_KP_Down), new Integer(KeyEvent.VK_KP_DOWN));
        map.put(new Integer(KeyMap.XK_KP_End), new Integer(KeyEvent.VK_END));
        map.put(new Integer(KeyMap.XK_KP_Enter), new Integer(KeyEvent.VK_ENTER));
        map.put(new Integer(KeyMap.XK_KP_Equal), new Integer(KeyEvent.VK_EQUALS));
        map.put(new Integer(KeyMap.XK_KP_Home), new Integer(KeyEvent.VK_HOME));
        map.put(new Integer(KeyMap.XK_KP_Insert), new Integer(KeyEvent.VK_INSERT));
        map.put(new Integer(KeyMap.XK_KP_Left), new Integer(KeyEvent.VK_KP_LEFT));
        map.put(new Integer(KeyMap.XK_KP_Multiply), new Integer(KeyEvent.VK_MULTIPLY));
        map.put(new Integer(KeyMap.XK_KP_Next), new Integer(KeyEvent.VK_PAGE_DOWN));
        map.put(new Integer(KeyMap.XK_KP_Page_Down), new Integer(KeyEvent.VK_PAGE_DOWN));
        map.put(new Integer(KeyMap.XK_KP_Page_Up), new Integer(KeyEvent.VK_PAGE_UP));
        map.put(new Integer(KeyMap.XK_KP_Prior), new Integer(KeyEvent.VK_PAGE_UP));
        map.put(new Integer(KeyMap.XK_KP_Right), new Integer(KeyEvent.VK_KP_RIGHT));
        map.put(new Integer(KeyMap.XK_KP_Separator), new Integer(KeyEvent.VK_SEPARATOR));
        map.put(new Integer(KeyMap.XK_KP_Space), new Integer(KeyEvent.VK_SPACE));
        map.put(new Integer(KeyMap.XK_KP_Subtract), new Integer(KeyEvent.VK_SUBTRACT));
        map.put(new Integer(KeyMap.XK_KP_Tab), new Integer(KeyEvent.VK_TAB));
        map.put(new Integer(KeyMap.XK_KP_Up), new Integer(KeyEvent.VK_KP_UP));
        map.put(new Integer(KeyMap.XK_Left), new Integer(KeyEvent.VK_LEFT));
        map.put(new Integer(KeyMap.XK_less), new Integer(KeyEvent.VK_LESS));
        map.put(new Integer(KeyMap.XK_Linefeed), new Integer(KeyEvent.VK_ENTER));
        map.put(new Integer(KeyMap.XK_Menu), new Integer(KeyEvent.VK_CONTEXT_MENU));
        map.put(new Integer(KeyMap.XK_Meta_L), new Integer(KeyEvent.VK_META));
        map.put(new Integer(KeyMap.XK_Meta_R), new Integer(KeyEvent.VK_META));
        map.put(new Integer(KeyMap.XK_minus), new Integer(KeyEvent.VK_MINUS));
        map.put(new Integer(KeyMap.XK_Mode_switch), new Integer(KeyEvent.VK_ALT_GRAPH));
        map.put(new Integer(KeyMap.XK_Multi_key), new Integer(KeyEvent.VK_COMPOSE));
        map.put(new Integer(KeyMap.XK_Next), new Integer(KeyEvent.VK_PAGE_DOWN));
        map.put(new Integer(KeyMap.XK_Num_Lock), new Integer(KeyEvent.VK_NUM_LOCK));
        map.put(new Integer(KeyMap.XK_numbersign), new Integer(KeyEvent.VK_NUMBER_SIGN));
        map.put(new Integer(KeyMap.XK_Page_Down), new Integer(KeyEvent.VK_PAGE_DOWN));
        map.put(new Integer(KeyMap.XK_Page_Up), new Integer(KeyEvent.VK_PAGE_UP));
        map.put(new Integer(KeyMap.XK_parenleft), new Integer(KeyEvent.VK_LEFT_PARENTHESIS));
        map.put(new Integer(KeyMap.XK_parenright), new Integer(KeyEvent.VK_RIGHT_PARENTHESIS));
        map.put(new Integer(KeyMap.XK_Pause), new Integer(KeyEvent.VK_PAUSE));
        map.put(new Integer(KeyMap.XK_period), new Integer(KeyEvent.VK_PERIOD));
        map.put(new Integer(KeyMap.XK_plus), new Integer(KeyEvent.VK_PLUS));
        map.put(new Integer(KeyMap.XK_Print), new Integer(KeyEvent.VK_PRINTSCREEN));
        map.put(new Integer(KeyMap.XK_Prior), new Integer(KeyEvent.VK_PAGE_UP));
        map.put(new Integer(KeyMap.XK_quotedbl), new Integer(KeyEvent.VK_QUOTEDBL));
        map.put(new Integer(KeyMap.XK_Redo), new Integer(KeyEvent.VK_AGAIN));
        map.put(new Integer(KeyMap.XK_Return), new Integer(KeyEvent.VK_ENTER));
        map.put(new Integer(KeyMap.XK_Right), new Integer(KeyEvent.VK_RIGHT));
        map.put(new Integer(KeyMap.XK_Scroll_Lock), new Integer(KeyEvent.VK_SCROLL_LOCK));
        map.put(new Integer(KeyMap.XK_semicolon), new Integer(KeyEvent.VK_SEMICOLON));
        map.put(new Integer(KeyMap.XK_Shift_L), new Integer(KeyEvent.VK_SHIFT));
        map.put(new Integer(KeyMap.XK_Shift_R), new Integer(KeyEvent.VK_SHIFT));
        map.put(new Integer(KeyMap.XK_slash), new Integer(KeyEvent.VK_SLASH));
        map.put(new Integer(KeyMap.XK_space), new Integer(KeyEvent.VK_SPACE));
        map.put(new Integer(KeyMap.XK_Super_L), new Integer(KeyEvent.VK_WINDOWS));
        map.put(new Integer(KeyMap.XK_Super_R), new Integer(KeyEvent.VK_WINDOWS));
        map.put(new Integer(KeyMap.XK_Tab), new Integer(KeyEvent.VK_TAB));
        map.put(new Integer(KeyMap.XK_underscore), new Integer(KeyEvent.VK_UNDERSCORE));
        map.put(new Integer(KeyMap.XK_Undo), new Integer(KeyEvent.VK_UNDO));
        map.put(new Integer(KeyMap.XK_Up), new Integer(KeyEvent.VK_UP));
        return map;
    }

    /**
     * Converts keysym to AWT key code.
     *
     * @param keysym the keysym
     * @return the key code or KeyEvent.VK_UNDEFINED is most cases
     */
    static int get(int keysym) {
        int code = KeyEvent.VK_UNDEFINED;

        if (keySymMap.containsKey(keysym)) {
            code = keySymMap.get(keysym).intValue();
        }
        return code;
    }
}
