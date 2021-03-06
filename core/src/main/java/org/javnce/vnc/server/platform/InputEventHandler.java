/*
 * Copyright (C) 2013 Pauli Kauppinen
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

import org.javnce.eventing.Event;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.EventSubscriber;
import org.javnce.vnc.common.KeyEvent;
import org.javnce.vnc.common.PointerEvent;

/**
 * The Class InputEventHandler handles the PointerEvent and KeyEvent events.
 *
 * The InputEventHandler has an event loop and is a thread that injects
 * PointerEvent and KeyEvent event into platform.
 *
 * @see org.javnce.vnc.common.PointerEvent
 * @see org.javnce.vnc.common.KeyEvent
 */
public class InputEventHandler implements EventSubscriber {

    /**
     * The event loop.
     */
    final private EventLoop eventLoop;
    /**
     * The pointer device.
     */
    private PointerDevice pointerDevice;
    /**
     * The keyboard device.
     */
    private KeyBoardDevice keyBoardDevice;
    /**
     * The thread.
     */
    final private Thread thread;
    /**
     * The access mode.
     */
    final private boolean fullAccessMode;

    /**
     * Instantiates a new input event handler.
     *
     * @param fullAccessMode if false then pseudo devices is used.
     *
     * @see KeyBoardDevice
     * @see PointerDevice
     */
    public InputEventHandler(boolean fullAccessMode) {
        this.fullAccessMode = fullAccessMode;
        eventLoop = new EventLoop();
        thread = new Thread(eventLoop, "Javnce-InputEventHandler");
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
        }
    }

    /**
     * KeyEvent handler.
     *
     * @param event the event
     */
    private void event(KeyEvent event) {
        if (null == keyBoardDevice) {
            keyBoardDevice = KeyBoardDevice.factory(fullAccessMode);
        }
        keyBoardDevice.keyEvent(event.down(), event.key());
    }

    /**
     * PointerEvent handler.
     *
     * @param event the event
     */
    private void event(PointerEvent event) {
        if (null == pointerDevice) {
            pointerDevice = PointerDevice.factory(fullAccessMode);
        }
        pointerDevice.pointerEvent(event.mask(), event.point().x(), event.point().y());
    }

    /**
     * Launch a thread.
     */
    public synchronized void launch() {
        eventLoop.subscribe(KeyEvent.eventId(), this);
        eventLoop.subscribe(PointerEvent.eventId(), this);
        thread.start();
    }

    /**
     * Shutdown the InputEventHandler.
     */
    public void shutdown() {
        eventLoop.shutdown();
    }
}
