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
package org.examples.PingPong;

import org.javnce.eventing.Event;
import org.javnce.eventing.EventId;

/**
 * The Class EventPong implements an empty Event.
 */
public class EventPong implements Event {

    /**
     * The constant event id.
     */
    final static private EventId id = new EventId(EventPong.class.getName());
    final static private String data = "Pong-event";

    /* (non-Javadoc)
     * @see org.javnce.eventing.Event#Id()
     */
    @Override
    public EventId Id() {
        return id;
    }

    /**
     * Event id getter.
     *
     * @return the event id
     */
    static public EventId eventId() {
        return id;
    }
    
    /**
     * An event data getter.
     *
     * @return the event data
     */
    public String pong()
    {
        return data;
    }
}
