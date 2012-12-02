package org.javnce.vnc.server.platform;

import static org.junit.Assert.*;
import org.junit.Test;

public class Win32GdiFramebufferTest {

    private Win32GdiFramebuffer buffer;
    final private static boolean isWindows = System.getProperty("os.name").startsWith("Windows");

    @Test
    public void testIsSupported() {
        assertEquals(isWindows, Win32GdiFramebuffer.isSupported());
    }

    @Test
    public void testSize() {
        if (isWindows) {
            fail("Not yet implemented");
        }
    }

    @Test
    public void testFormat() {
        if (isWindows) {
            fail("Not yet implemented");
        }
    }

    @Test
    public void testBuffer() {
        if (isWindows) {
            buffer = new Win32GdiFramebuffer();
            fail("Not yet implemented");
        }
    }
}
