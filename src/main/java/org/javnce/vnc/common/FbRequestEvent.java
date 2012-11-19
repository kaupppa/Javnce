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
package org.javnce.vnc.common;

import org.javnce.eventing.Event;
import org.javnce.eventing.EventId;
import org.javnce.rfb.types.Rect;

/**
 * The framebuffer update request event.
 *
 */
public class FbRequestEvent implements Event {

    /**
     * The event id.
     */
    final static private EventId id = new EventId(FbRequestEvent.class.getName());
    /**
     * Incremental framebuffer.
     */
    private final boolean incremental;
    /**
     * The area.
     */
    private final Rect rect;

    /* (non-Javadoc)
     * @see org.javnce.eventing.Event#Id()
     */
    @Override
    public EventId Id() {
        return id;
    }

    /**
     * Static event id getter.
     *
     * @return the event id
     */
    static public EventId eventId() {
        return id;
    }

    /**
     * Instantiates a new framebuffer request event.
     *
     * @param incremental the incremental
     * @param rect the area
     */
    public FbRequestEvent(boolean incremental, Rect rect) {
        this.incremental = incremental;
        this.rect = rect;
    }

    /**
     * Incremental getter.
     *
     * @return true, if if Incremental
     */
    public boolean incremental() {
        return incremental;
    }

    /**
     * Area getter.
     *
     * @return the area
     */
    public Rect rect() {
        return rect;
    }
}
