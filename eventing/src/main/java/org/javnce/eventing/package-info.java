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
/**
 * The package provides event publish/subscribe between threads.
 *
 * <p>
 * The event loop are implemented as runnable with following features:
 * <ul>
 * <li>Events between threads</li>
 * <li>Non-blocking socket handling</li>
 * <li>Event groups</li>
 * </ul>
 * </p>
 *
 * <p>
 * An event loop always belong to one group. The published event is handled by 
 * all subscribers within the group. If no subscriber is found in group then 
 * event is published to all groups.
 * </p>
 * <p>
 * For example VNC server creates new sub group for each client.
 * </p>
 * 
 */
package org.javnce.eventing;