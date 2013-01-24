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
 * The Class Version contains RFB protocol version number.
 *
 * @see org.javnce.rfb.messages.MsgProtocolVersion
 */
public class Version {

    /**
     * The major.
     */
    private final int major;
    /**
     * The minor.
     */
    private final int minor;

    /**
     * Instantiates a new version.
     *
     * @param major the major
     * @param minor the minor
     */
    public Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    /**
     * Major getter.
     *
     * @return the int
     */
    public int major() {
        return this.major;
    }

    /**
     * Minor getter.
     *
     * @return the int
     */
    public int minor() {
        return this.minor;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        boolean areEqual = false;

        if (this == other) {
            areEqual = true;
        } else if ((other instanceof Version)) {
            Version theOther = (Version) other;

            areEqual = (this.major == theOther.major
                    && this.minor == theOther.minor);
        }

        return areEqual;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return String.format("%s(%d.%d)", this.getClass().getName(), major, minor);
    }
}
