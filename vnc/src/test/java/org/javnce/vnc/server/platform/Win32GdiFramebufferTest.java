package org.javnce.vnc.server.platform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;
import static org.junit.Assert.*;
import org.junit.Test;

public class Win32GdiFramebufferTest {

    private Win32GdiFramebuffer dev;
    final private static boolean isWindows = System.getProperty("os.name").startsWith("Windows");

    @Test
    public void testIsSupported() {
        assertEquals(isWindows, Win32GdiFramebuffer.isSupported());
    }

    @Test
    public void testSize() {
        if (isWindows) {
            dev = new Win32GdiFramebuffer();
            assertTrue(0 != dev.size().width());
            assertTrue(0 != dev.size().height());

        }
    }

    @Test
    public void testFormat() {
        if (isWindows) {
            dev = new Win32GdiFramebuffer();
            assertTrue(dev.format().trueColour());
            assertTrue(!dev.format().bigEndian());
            assertTrue(dev.format().bitsPerPixel() >= dev.format().depth());

        }
    }

    class TestData {

        int x;
        int y;
        int width;
        int height;

        TestData(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    @Test
    public void testBuffer() {
        if (isWindows) {
            dev = new Win32GdiFramebuffer();
            PixelFormat format = dev.format();
            Size size = dev.size();

            TestData[] array = new TestData[]{
                new TestData(0, 0, size.width(), 1), //One full line
                new TestData(0, 0, size.width(), size.height()), //Whole framebuffer
                new TestData(0, 100, size.width(), 100), //Several full lines
                new TestData(0, 0, size.width() - 10, 1),
                new TestData(0, 0, size.width() - 10, size.height()),
                new TestData(10, 0, size.width() - 20, size.height()),};

            for (int i = 0; i < array.length; i++) {
                ByteBuffer[] buffers = dev.buffer(array[i].x, array[i].y, array[i].width, array[i].height);

                if (array[i].x == 0 && array[i].width == size.width()) {
                    assertEquals(1, buffers.length);
                } else {
                    assertEquals(array[i].height, buffers.length);
                }

                int length = 0;

                for (ByteBuffer b : buffers) {
                    length += b.capacity();
                    assertEquals(ByteOrder.BIG_ENDIAN, b.order());
                }

                assertEquals(array[i].width * array[i].height * format.bytesPerPixel(), length);
            }
        }
    }

    @Test
    public void testGrabScreen() {
        if (isWindows) {
            dev = new Win32GdiFramebuffer();
            dev.grabScreen();
        }
    }
}
