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
 * The interface of an event. <p> The id must be class specific and created only
 * once for performance reasons. </p> <p> An example implementation: </p>
 * <pre>
 * {@code
 *  public class MyEvent implements Event {
 *    final static private EventId id = new EventId(MyEvent.class.getName());
 *     public EventId Id() {
 *       return id;
 *     }
 * }
 * }
 * </pre>
 *
 */
public interface Event {

    /**
     * The Id is the Event class wide identifier.
     *
     * @return the event id for the class
     */
    public EventId Id();
}
