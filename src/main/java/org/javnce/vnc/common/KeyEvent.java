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
 * The key event.
 *
 */
public class KeyEvent implements Event {

    /**
     * The event id.
     */
    final static private EventId id = new EventId(KeyEvent.class.getName());
    /**
     * The down.
     */
    private final boolean down;
    /**
     * The key.
     */
    private final long key;

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
     * Instantiates a new key event.
     *
     * @param down the down
     * @param key the key
     */
    public KeyEvent(boolean down, long key) {
        this.down = down;
        this.key = key;
    }

    /**
     * Down.
     *
     * @return true, if down
     */
    public boolean down() {
        return down;
    }

    /**
     * Key.
     *
     * @return the keysym
     */
    public long key() {
        return key;
    }
}
