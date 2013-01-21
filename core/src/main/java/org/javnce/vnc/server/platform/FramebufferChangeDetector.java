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
package org.javnce.vnc.server.platform;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.zip.Adler32;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.TimeOutCallback;
import org.javnce.eventing.Timer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Point;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;
import org.javnce.vnc.common.FbChangeEvent;

/**
 * The Class FramebufferChangeDetector is an platform independent class to
 * detect framebuffer changes.
 */
class FramebufferChangeDetector extends Thread implements TimeOutCallback {

    /**
     * The checksums for each row in framebuffer.
     */
    private long[] checksums;
    /**
     * The format.
     */
    private PixelFormat format;
    /**
     * The size.
     */
    private Size size;
    /**
     * The adler32.
     */
    final private Adler32 adler32;
    /**
     * The Constant MaxEmptyLines.
     */
    static final private int MaxEmptyLines = 50;
    /**
     * The event loop.
     */
    final private EventLoop eventLoop;
    
    final private FramebufferDevice fb;

    /**
     * Instantiates a new framebuffer change detector.
     */
    FramebufferChangeDetector() {
        setName("FramebufferChangeDetector");
        adler32 = new Adler32();
        eventLoop = new EventLoop();
        fb = PlatformController.instance().getPlatformManager().getFramebufferDevice();

        /*
         private MessageDigest md;
         private byte[] lastdigest;
         md  = MessageDigest.getInstance("MD5");
         md.reset();
         md.update(buffer);
         [] tempDigest = md.digest();

         Arrays.equals(tempDigest, lastdigest)
         */

    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        timeout();
        eventLoop.process();
    }

    /**
     * Compare.
     *
     * @return true, if successful
     */
    private boolean compare() {
        
        boolean changeFound = false;

        if (!fb.format().equals(format) || !fb.size().equals(size)) {
            checksums = null;
            format = fb.format();
            size = fb.size();
        }

        ArrayList<Rect> list;
        if (null == checksums) {
            checksums = new long[size.height()];
            list = new ArrayList<>();
            list.add(new Rect(new Point(0, 0), size));
            calcChecksums(fb);
        } else {
            list = calcChecksums(fb);
        }

        if (!list.isEmpty()) {
            changeFound = true;
            eventLoop.publish(new FbChangeEvent(list));
        }

        return changeFound;
    }

    /**
     * Calculates row checksums for framebuffer.
     *
     * @param fb the fb
     * @return the array list
     */
    private ArrayList<Rect> calcChecksums(FramebufferDevice fb) {
        ArrayList<Rect> list = new ArrayList<>();
        int min = size.height() + 1;
        int max = -1;


        ByteBuffer buffers[] = fb.buffer(0, 0, size.width(), size.height());
        byte[] bytes = new byte[buffers[0].capacity()];
        ByteBuffer.wrap(bytes).put(buffers[0]);

        int lineLength = size.width() * format.bytesPerPixel();
        int height = size.height();

        int offset = 0;

        for (int i = 0; i < height; i++) {
            adler32.reset();
            adler32.update(bytes, offset, lineLength);
            long value = adler32.getValue();

            if (checksums[i] != value) {
                checksums[i] = value;
                min = Math.min(min, i);
                max = Math.max(max, i);
            } else if (-1 != max && (max + MaxEmptyLines) < i) {
                Rect rect = new Rect(0, min, size.width(), max - min + 1);
                list.add(rect);
                min = size.height() + 1;
                max = -1;

            }
            offset += lineLength;
        }

        if (-1 != max) {
            Rect rect = new Rect(0, min, size.width(), max - min + 1);
            list.add(rect);
        }

        return list;
    }

    @Override
    public void timeout() {
    	fb.grabScreen();
        boolean changed = compare();
        int waitTime = 50;
        if (!changed) {
            //If nothing changed then increase wait time to reduce cpu load
            waitTime += 50;
        }
        Timer timer = new Timer(this, waitTime);
        eventLoop.addTimer(timer);
    }
}
