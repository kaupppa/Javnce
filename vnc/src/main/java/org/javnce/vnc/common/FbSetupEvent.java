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
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Size;

/**
 * The framebuffer setup event.
 *
 */
public class FbSetupEvent implements Event {

    /**
     * The event id.
     */
    final static private EventId id = new EventId(FbSetupEvent.class.getName());
    /**
     * The format.
     */
    private final PixelFormat format;
    /**
     * The size.
     */
    private final Size size;
    /**
     * The name.
     */
    private final String name;


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
     * Instantiates a new setup event.
     *
     * @param format the format
     * @param size the size
     * @param name the name
     */
    public FbSetupEvent(PixelFormat format, Size size, String name) {
        this.format = format;
        this.size = size;
        this.name = name;
    }

    /**
     * Format getter.
     *
     * @return the pixel format
     */
    public PixelFormat format() {
        return format;
    }

    /**
     * Size getter.
     *
     * @return the size
     */
    public Size size() {
        return size;
    }

    /**
     * Name getter.
     *
     * @return the string
     */
    public String name() {
        return name;
    }
}
