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
package org.javnce.vnc.server.platform;

import java.util.ArrayList;
import org.javnce.rfb.types.Rect;

/**
 * The interface FrameBufferCompare defines frame buffer change detector.
 *
 */
interface FrameBufferCompare {

    /**
     * Detect changes in given frame buffer.
     *
     * @param fb the frame buffer device
     * @return the list of changed areas in frame buffer.
     */
    ArrayList<Rect> compare(FramebufferDevice fb);
}
