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
package org.javnce.eventing;

/**
 * The Interface EventDispatcher defines ChannelSubscriber controllers interface.
 */
public interface EventDispatcher {

    /**
     * Dispatch of published event.
     *
     * @param event the published event
     * @return true, if event supported
     */
    boolean dispatchEvent(Event event);

    /**
     * Requests to shutdown the dispatcher.
     *
     * @param group the closing group where dispatcher belongs to.
     */
    void shutdown(EventGroup group);
}
