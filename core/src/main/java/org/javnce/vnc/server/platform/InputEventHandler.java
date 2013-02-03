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

public class InputEventHandler implements EventSubscriber {

    private EventLoop eventLoop;
    private PointerDevice pointerDevice;
    private KeyBoardDevice keyBoardDevice;
    private Thread thread;
    final private boolean fullAccessMode;

    public InputEventHandler(boolean fullAccessMode) {
        this.fullAccessMode = fullAccessMode;

    }

    @Override
    public void event(Event event) {
        if (KeyEvent.eventId().equals(event.Id())) {
            event((KeyEvent) event);
        } else if (PointerEvent.eventId().equals(event.Id())) {
            event((PointerEvent) event);
        }
    }

    private void event(KeyEvent event) {
        if (null == keyBoardDevice) {
            keyBoardDevice = KeyBoardDevice.factory(fullAccessMode);
        }
        keyBoardDevice.keyEvent(event.down(), event.key());

    }

    private void event(PointerEvent event) {
        if (null == pointerDevice) {
            pointerDevice = PointerDevice.factory(fullAccessMode);
        }

        pointerDevice.pointerEvent(event.mask(), event.point().x(), event.point().y());

    }

    public synchronized void launch() {
        if (null == eventLoop) {
            eventLoop = new EventLoop();
            eventLoop.subscribe(KeyEvent.eventId(), this);
            eventLoop.subscribe(PointerEvent.eventId(), this);

        }
        if (null == thread) {
            thread = new Thread(eventLoop, "Javnce-InputEventHandler");
            thread.start();
        }
    }

    public void shutdown() {
        eventLoop.shutdown();
    }
}
