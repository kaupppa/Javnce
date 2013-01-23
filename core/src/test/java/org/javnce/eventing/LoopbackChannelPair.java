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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The LoopbackChannelPair provides two SocketChannels connected with eachother.
 *
 * The LoopbackChannelPair just a Pipe with two-way communication.
 */
public class LoopbackChannelPair implements AutoCloseable {

    /**
     * The first channel.
     */
    private SocketChannel ch1;
    /**
     * The second channel.
     */
    private SocketChannel ch2;

    /**
     * Instantiates a new socket channel pair.
     */
    public LoopbackChannelPair() {
        ch1 = null;
        ch2 = null;
    }

    /**
     * Creates and connects the channels if not already connected.
     *
     * @throws Exception Signals that an exception has occurred.
     */
    private void init() throws Exception {
        if (null == ch1 && null == ch2) {
            try (ServerSocketChannel server = ServerSocketChannel.open()) {
                server.bind(new InetSocketAddress(InetAddress.getLocalHost(), 0));

                //Accept incoming connection in a thread (because is blocking).
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ch2 = server.accept();
                        } catch (IOException ex) {
                            Logger.getLogger(this.getClass().getName())
                                    .log(Level.SEVERE, null, ex);
                            System.exit(1);
                        }
                    }
                });
                thread.start();

                //Open a SocketChannel and connect to server
                ch1 = SocketChannel.open(server.getLocalAddress());

                //Wait thread to finish
                thread.join();

                //Set no delay
                ch1.socket().setTcpNoDelay(true);
                ch2.socket().setTcpNoDelay(true);

                ch1.configureBlocking(false);
                ch2.configureBlocking(false);
            }
        }
    }

    /**
     * Channel 1 getter.
     *
     * @return the first socket channel
     * @throws Exception the exception if connection fails.
     */
    public SocketChannel channel1() throws Exception {
        init();
        return ch1;
    }

    /**
     * Channel 2 getter.
     *
     * @return the second socket channel
     * @throws Exception the exception if connection fails
     */
    public SocketChannel channel2() throws Exception {
        init();
        return ch2;
    }

    /* (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        if (null != ch1) {
            ch1.close();
        }
        if (null != ch2) {
            ch2.close();
        }
    }
}
