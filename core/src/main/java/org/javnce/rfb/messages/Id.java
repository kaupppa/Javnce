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
package org.javnce.rfb.messages;

/**
 * The message identifiers.
 *
 */
public enum Id {

    /**
     * The ProtocolVersion message id.
     */
    ProtocolVersion,
    /**
     * The message id for Security types sent by server.
     */
    SecurityTypeList,
    /**
     * The message id for selected security type sent by client.
     */
    SelectedSecurityType,
    /**
     * The SecurityResult sent by server.
     */
    SecurityResult,
    /**
     * The ClientInit.
     */
    ClientInit,
    /**
     * The ServerInit.
     */
    ServerInit,
    // Following are Client messages

    /**
     * The SetPixelFormat.
     */
    SetPixelFormat,
    /**
     * The SetEncodings.
     */
    SetEncodings,
    /**
     * The FramebufferUpdateRequest.
     */
    FramebufferUpdateRequest,
    /**
     * The KeyEvent.
     */
    KeyEvent,
    /**
     * The PointerEvent.
     */
    PointerEvent,
    /**
     * The ClientCutText.
     */
    ClientCutText,
    // Following are Server messages

    /**
     * The FramebufferUpdate.
     */
    FramebufferUpdate,
    /**
     * The SetColourMapEntries.
     */
    SetColourMapEntries,
    /**
     * The Bell.
     */
    Bell,
    /**
     * The ServerCutText.
     */
    ServerCutText,
    // Following are JaVNCe specific

    /**
     * The Factory.
     */
    Factory,
    /**
     * The Unknown.
     */
    Unknown
}
