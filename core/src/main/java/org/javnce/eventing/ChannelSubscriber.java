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

import java.nio.channels.SelectionKey;

/**
 * The interface for socket read/write event subscriber.
 *
 * @see org.javnce.eventing.EventLoop#subscribe(java.nio.channels.SelectableChannel, org.javnce.eventing.ChannelSubscriber, int) 
 * @see org.javnce.eventing.EventLoop#removeSubscribe(java.nio.channels.SelectableChannel) 
 */
public interface ChannelSubscriber {

    /**
     * The method channel is called when socket can be handled.
     *
     * @param key the key is the selector key for the socket.
     *
     */
    void channel(SelectionKey key);
}
