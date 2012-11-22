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
import org.javnce.rfb.types.PixelFormat;

/**
 * A factory for receiving client RFB messages after handshake phase.
 *
 */
public class MsgClientFactory extends Message {

    /**
     * The Constant msgId.
     */
    final static private Id msgId = Id.Factory;
    /**
     * The msg.
     */
    private Message msg;
    
    private final PixelFormat format;

    /**
     * Instantiates a new receiving RFB message factory.
     *
     * @param format the framebuffer format
     */
    public MsgClientFactory(PixelFormat format) {
        super(msgId);
        this.format = format;
    }

    /**
     * Creates a new RFB message object sent by server.
     *
     * @param type the RFB message-type
     * @return the message
     */
    private Message createMsg(byte type) {
        Message newMsg;

        switch (type) {
            case 0:
                newMsg = new MsgFramebufferUpdate(format);
                break;
            case 1:
                newMsg = new MsgSetColourMapEntries();
                break;
            case 2:
                newMsg = new MsgBell();
                break;
            case 3:
                newMsg = new MsgServerCutText();
                break;
            default:
                newMsg = new MsgUnknown();
                break;
        }
        return newMsg;
    }

    /**
     * Gets the RFB message object.
     *
     * @return the message
     */
    public Message get() {
        return msg;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#demarshal(java.nio.ByteBuffer)
     */
    @Override
    public boolean demarshal(ByteBuffer buffer) {
        boolean allRead = (null != msg && msg.isValid());

        if (!allRead && 0 != buffer.remaining()) {
            if (null == msg) {
                msg = createMsg(buffer.get(buffer.position()));
            }

            allRead = msg.demarshal(buffer);
            setValid(allRead);
        }

        return allRead;
    }

    /* (non-Javadoc)
     * @see org.javnce.rfb.messages.Message#marshal()
     */
    @Override
    public ArrayList<ByteBuffer> marshal() {
        return new ArrayList<>();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName()).append(" Object {").append(NEW_LINE);

        if (null != msg) {
            result.append(" msg:").append(msg);
        }

        result.append(NEW_LINE).append("}");
        return result.toString();
    }
}
