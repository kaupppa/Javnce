package org.javnce.vnc.server.platform;

import java.awt.event.KeyEvent;
import org.javnce.vnc.common.KeyMap;
import static org.junit.Assert.*;
import org.junit.Test;

public class RobotKeyMapTest {

    class TestData {

        public final int keysym;
        public final int unicode;

        TestData(int keysym, int unicode) {
            this.keysym = keysym;
            this.unicode = unicode;
        }
    };

    @Test
    public void testKeysymToRobotCode() {
        TestData data[] = new TestData[]{
            new TestData(KeyMap.None, KeyEvent.VK_UNDEFINED), //None unicode
            new TestData(0x20, 0x20),
            new TestData(KeyMap.XK_Alt_L, KeyEvent.VK_ALT), //with mapping
            new TestData(KeyMap.XK_Alt_R, KeyEvent.VK_ALT), //with mapping
            new TestData('a', 'A'),
            new TestData('A', 'A'),
            new TestData('z', 'Z'),
            new TestData('Z', 'Z'),};
        KeysymToAwtKeyCode map = new KeysymToAwtKeyCode();
        for (int i = 0; i < data.length; i++) {
            int value = map.keysymToRobotCode(data[i].keysym);

            assertEquals(data[i].unicode, value);
        }
    }
}
