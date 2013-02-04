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

import java.util.ArrayList;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.TimeOutCallback;
import org.javnce.eventing.Timer;
import org.javnce.rfb.types.Rect;
import org.javnce.vnc.common.FbChangeEvent;

/**
 * The Class FramebufferHandler handles loading of frame buffer and the
 * notifying of changes in frame buffer.
 *
 * The FramebufferHandleris a thread that periodically loads frame buffer and
 * publish event when frame buffer has changes.
 *
 * @see org.javnce.vnc.common.FbChangeEvent
 */
public class FramebufferHandler implements TimeOutCallback {

    /**
     * The frame buffer loading interval.
     */
    static final private int FrameGrappingIntervalInMs = 50;
    /**
     * The event loop.
     */
    private EventLoop eventLoop;
    /**
     * The thread.
     */
    private Thread thread;
    /**
     * The dev.
     */
    private FramebufferDevice dev;
    /**
     * The change detector.
     */
    final private FrameBufferCompare compare;

    /**
     * Instantiates a new frame buffer handler.
     */
    public FramebufferHandler() {
        compare = new FrameBufferCompare();
    }

    /* (non-Javadoc)
     * @see org.javnce.eventing.TimeOutCallback#timeout()
     */
    @Override
    public void timeout() {
        if (null == dev) {
            dev = FramebufferDevice.factory();
        }
        dev.grabScreen();
        ArrayList<Rect> list = compare.compare(dev);

        int waitTime = FrameGrappingIntervalInMs;
        if (list.isEmpty()) {
            //If nothing changed then increase wait time to reduce cpu load
            waitTime += FrameGrappingIntervalInMs;
        } else {
            eventLoop.publish(new FbChangeEvent(list));
        }
        Timer timer = new Timer(this, waitTime);
        eventLoop.addTimer(timer);
    }

    /**
     * Launch.
     */
    public synchronized void launch() {
        if (null == eventLoop) {
            eventLoop = new EventLoop();
            Timer timer = new Timer(this, 10);
            eventLoop.addTimer(timer);
        }
        if (null == thread) {
            thread = new Thread(eventLoop, "Javnce-FramebufferHandler");
            thread.start();
        }
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        eventLoop.shutdown();
    }
}
