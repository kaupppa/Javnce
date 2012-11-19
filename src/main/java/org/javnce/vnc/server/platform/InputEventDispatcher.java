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

import org.javnce.eventing.Event;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.EventSubscriber;
import org.javnce.vnc.common.KeyEvent;
import org.javnce.vnc.common.PointerEvent;

/**
 * The Class InputEventDispatcher injects key and pointer events into platform.
 */
class InputEventDispatcher extends Thread implements EventSubscriber {

    /**
     * The event loop.
     */
    final private EventLoop eventLoop;

    /**
     * Instantiates a new input event dispatcher.
     */
    InputEventDispatcher() {
        eventLoop = new EventLoop();
        setName("InputEventDispatcher");
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        eventLoop.subscribe(KeyEvent.eventId(), this);
        eventLoop.subscribe(PointerEvent.eventId(), this);
        eventLoop.process();
    }

    /* (non-Javadoc)
     * @see org.javnce.eventing.EventSubscriber#event(org.javnce.eventing.Event)
     */
    @Override
    public void event(Event event) {
        if (KeyEvent.eventId().equals(event.Id())) {
            event((KeyEvent) event);
        } else if (PointerEvent.eventId().equals(event.Id())) {
            event((PointerEvent) event);
        } else {
            EventLoop.fatalError(this, new UnsupportedOperationException("Unsubscribed event " + event.getClass().getName()));
        }
    }

    /**
     * Pointer event handler.
     *
     * @param e the e
     */
    private void event(PointerEvent e) {
        PlatformManager manager = PlatformController.instance().getPlatformManager();
        manager.getPointerDevice().pointerEvent(e.mask(), e.point().x(), e.point().y());
    }

    /**
     * Key event handler.
     *
     * @param e the e
     */
    private void event(KeyEvent e) {
        PlatformManager manager = PlatformController.instance().getPlatformManager();
        manager.getKeyBoardDevice().keyEvent(e.down(), e.key());
    }
}
