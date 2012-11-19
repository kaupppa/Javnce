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

/**
 * The supported encodings change event.
 *
 */
public class FbEncodingsEvent implements Event {

    /**
     * The event id.
     */
    final static private EventId id = new EventId(FbEncodingsEvent.class.getName());
    /**
     * The encodings.
     */
    private final int[] encodings;

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
     * Instantiates a new encodings event.
     *
     * @param encodings the encodings
     */
    public FbEncodingsEvent(int[] encodings) {
        this.encodings = encodings;
    }

    /**
     * Gets the encodings.
     *
     * @return array of encodings
     */
    public int[] get() {
        return encodings;
    }
}
