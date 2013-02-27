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
    static final private int FrameGrappingIntervalInMs = 75;
    /**
     * The event loop.
     */
    final private EventLoop eventLoop;
    /**
     * The thread.
     */
    final private Thread thread;
    /**
     * The dev.
     */
    final private FramebufferDevice dev;
    /**
     * The change detector.
     */
    final private FrameBufferBlockCompare compare;

    /**
     * Instantiates a new frame buffer handler.
     */
    public FramebufferHandler() {
        dev = FramebufferDevice.factory();
        compare = new FrameBufferBlockCompare(dev);
        eventLoop = new EventLoop();
        thread = new Thread(eventLoop, "Javnce-FramebufferHandler");
    }

    /* (non-Javadoc)
     * @see org.javnce.eventing.TimeOutCallback#timeout()
     */
    @Override
    public void timeout() {
        dev.grabScreen();
        ArrayList<Rect> list = compare.compare();
        if (!list.isEmpty()) {
            eventLoop.publish(new FbChangeEvent(list));
        }
        Timer timer = new Timer(this, FrameGrappingIntervalInMs);
        eventLoop.addTimer(timer);
    }

    /**
     * Launch.
     */
    public synchronized void launch() {
        Timer timer = new Timer(this, 10);
        eventLoop.addTimer(timer);
        thread.start();
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        eventLoop.shutdown();
    }
}
