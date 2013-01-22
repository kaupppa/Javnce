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
package org.javnce.eventing;

/**
 * The Class EventId defines {@link Event} identifier.
 * 
 * The id is just a hash code of given string.
 * 
 * @see org.javnce.eventing.Event
 *
 */
public class EventId {

    /**
     * The id.
     */
    final private int id;

    /**
     * Instantiates a new event id.
     *
     * @param name the string from which hash code is taken.
     */
    public EventId(String name) {
        this.id = name.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        boolean areEqual = false;

        if (this == other) {
            areEqual = true;
        } else if (null != other && (other instanceof EventId)) {
            EventId theOther = (EventId) other;
            areEqual = (this.id == theOther.id);
        }

        return areEqual;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id;
    }
}
