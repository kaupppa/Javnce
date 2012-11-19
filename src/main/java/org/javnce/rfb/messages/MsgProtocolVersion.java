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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.javnce.rfb.types.Version;

/**
 * The Class MsgProtocolVersion is the ProtocolVersion RFB message.
 *
 */
public class MsgProtocolVersion extends Message {

    /**
     * The version.
     */
    private Version version;
    /**
     * The Constant msgLength.
     */
    final static private int msgLength = 12;
    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.ProtocolVersion;

    /**
     * Instantiates a new ProtocolVersion message for receiving.
     */
    public MsgProtocolVersion() {
        super(msgId);
        this.version = new Version(-1, -1);
    }

    /**
     * Instantiates a new ProtocolVersion for sending.
     *
     * @param version the RFB protocol version
     */
    public MsgProtocolVersion(Version version) {
        super(msgId);
        this.version = version;
        setValid(true);
    }

    /**
     * Gets the RFB protocol version.
     *
     * @return the version
     */
    public Version get() {
        return this.version;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    public boolean demarshal(ByteBuffer buffer) {
        boolean allBytesRead = false;
        this.setValid(false);

        if (buffer.remaining() >= msgLength) {
            allBytesRead = true;
            byte[] array = new byte[msgLength];
            buffer.get(array);

            if ('R' == array[0]
                    && 'F' == array[1]
                    && 'B' == array[2]
                    && ' ' == array[3]
                    && '.' == array[7]
                    && '\n' == array[11]) {
                int major = (array[4] - '0') * 100
                        + (array[5] - '0') * 10
                        + (array[6] - '0');
                int minor = (array[8] - '0') * 100
                        + (array[9] - '0') * 10
                        + (array[10] - '0');
                this.version = new Version(major, minor);
                this.setValid(true);
            }
        }

        return allBytesRead;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#marshal()
     */
    @Override
    public ArrayList<ByteBuffer> marshal() {
        ArrayList<ByteBuffer> list = new ArrayList<>();

        if (isValid()) {
            ByteBuffer buffer = ByteBuffer.wrap(String.format("RFB %03d.%03d\n",
                    this.version.major(),
                    this.version.minor()).getBytes());
            buffer.clear();
            list.add(buffer);
        }

        return list;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName()).append(" Object {").append(NEW_LINE);
        result.append(" Version: ").append(version).append(NEW_LINE);
        result.append("}");

        return result.toString();
    }
}
