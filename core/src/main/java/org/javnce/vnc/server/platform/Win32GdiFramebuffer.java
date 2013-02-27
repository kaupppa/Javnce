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
package org.javnce.vnc.server.platform;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import org.javnce.eventing.EventLoop;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * The Class Win32GdiFramebuffer provides access to frame buffer using Microsoft
 * Windows graphics device interface (GDI).
 *
 * The XShmFramebuffer is a native Microsoft Windows implementation.
 */
class Win32GdiFramebuffer extends FramebufferDevice {

    /**
     * The screen size.
     */
    static private Size size = null;
    /**
     * The screen format.
     */
    static private PixelFormat format = null;
    /**
     * The is needed GDI features supported.
     */
    static private AtomicBoolean supported = null;
    /**
     * The screen shot taker.
     */
    final static private Grabber grabber = new Grabber();
    /**
     * The thread.
     */
    static private Thread thread = null;

    /**
     * The Class Grabber handles the concurrency when taking screen shot.
     */
    static class Grabber {

        /**
         * Is screen grabbed or not.
         */
        private boolean grabbed;

        /**
         * Instantiates a new grabber.
         */
        Grabber() {
            grabbed = false;
        }

        /**
         * Grab.
         *
         * @throws InterruptedException the interrupted exception
         */
        synchronized void grab() throws InterruptedException {
            if (grabbed) {
                wait();
            }
            takeScreenShotJni();
            grabbed = true;
            notifyAll();
        }

        /**
         * Wait grapping.
         */
        synchronized void requestGrabbing() {
            if (!grabbed) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                }
            }
            grabbed = false;
            notifyAll();
        }
    }
    /**
     * The name of native implementation.
     */
    final static private String libName = getLibName();

    static {
        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
        }
    }

    /**
     * Wait grapping.
     */
    synchronized static private void grab() {

        if (null == thread) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    size = sizeJni();
                    format = formatJni();
                    while (true) {
                        try {
                            grabber.grab();
                        } catch (InterruptedException ex) {
                            EventLoop.fatalError(this, ex);
                            break;
                        }
                    }
                }
            };
            thread = new Thread(r, "Javnce-WindowsGDI");
            thread.setDaemon(true);
            thread.start();
        }
        grabber.requestGrabbing();
    }

    /**
     * Instantiates a new win32 gdi framebuffer.
     */
    Win32GdiFramebuffer() {
        //Make sure that first screenshot is taken
        grab();
    }

    /**
     * Take screen shot jni.
     */
    static private native void takeScreenShotJni();

    /**
     * Size jni.
     *
     * @return the size
     */
    static private native Size sizeJni();

    /**
     * Format jni.
     *
     * @return the pixel format
     */
    static private native PixelFormat formatJni();

    /**
     * Buffer jni.
     *
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     * @return the byte buffer[]
     */
    static private native ByteBuffer[] bufferJni(int x, int y, int width, int height);

    /**
     * Checks if is supported jni.
     *
     * @return true, if is supported jni
     */
    static private native boolean isSupportedJni();

    /**
     * Gets the x64 or x86 implementation name for current environment.
     *
     * @return true, if native implementation is supported
     */
    static private String getLibName() {
        String name = "Win32GdiFramebuffer_x64";
        if (System.getProperty("os.arch").startsWith("x86")) {
            name = "Win32GdiFramebuffer_Win32";
        }
        return name;
    }

    /**
     * Checks if native implementation is available in current environment.
     *
     * @return true, if native implementation is supported
     */
    synchronized static boolean isSupported() {

        if (null == supported) {
            supported = new AtomicBoolean(false);
            try {
                boolean valid = false;
                valid = isSupportedJni();
                supported.set(valid);
            } catch (Throwable e) {
            }
        }
        return supported.get();
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#size()
     */
    @Override
    public Size size() {
        return size;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#format()
     */
    @Override
    public PixelFormat format() {
        return format;
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#buffer(int, int, int, int)
     */
    @Override
    public ByteBuffer[] buffer(int x, int y, int width, int height) {
        return bufferJni(x, y, width, height);
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.platform.FramebufferDevice#grabScreen()
     */
    @Override
    public void grabScreen() {
        grab();
    }
}
