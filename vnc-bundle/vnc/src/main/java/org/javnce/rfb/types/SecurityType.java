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
package org.javnce.rfb.types;

/**
 * The SecurityType values used in handshaking messages.
 *
 * @see org.javnce.rfb.messages.MsgSecurityTypeList
 * @see org.javnce.rfb.messages.MsgSelectedSecurityType
 */
public enum SecurityType {

    /**
     * The invalid security type.
     */
    Invalid(0),
    /**
     * No security.
     */
    None(1),
    /**
     * Unsupported security type.
     */
    UnSupported(-1);
    /**
     * The id.
     */
    private final byte id;

    /**
     * Instantiates a new security type.
     *
     * @param id the id
     */
    SecurityType(int id) {
        this.id = (byte) id;
    }

    /**
     * Id getter.
     *
     * @return the security type as byte
     */
    public byte id() {
        return id;
    }

    /**
     * Creates the SecurityType object from give id..
     *
     * @param id the security type as byte
     * @return the security type object
     */
    static public SecurityType create(byte id) {
        SecurityType type = UnSupported;

        for (SecurityType t : SecurityType.values()) {
            if (id == t.id) {
                type = t;
                break;
            }
        }

        return type;
    }
}
