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

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * Read/write event subscriber handling class.
 *
 */
class ChannelSubscriberList {

    /**
     * The selector.
     */
    private Selector selector;

    /**
     * Instantiates a new channel subscriber list.
     */
    ChannelSubscriberList() {
        selector = null;
    }

    /**
     * Opens the selector if not existing
     */
    private void init() throws IOException {
        if (null == selector) {
            selector = Selector.open();
        }
    }

    /**
     * Method to wake up the selector.
     */
    void wakeup() {
        if (null != selector) {
            selector.wakeup();
        }
    }

    /**
     * Closes the selector.
     */
    void close() {
        if (null != selector) {
            try {
                selector.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    boolean isEmpty() {
        return (null == selector
                || false == selector.isOpen()
                || selector.keys().isEmpty());
    }

    /**
     * Process the selector. Blocks until channel event occurs, wakeup is called
     * or timeouts.
     *
     * @param timeOut Max wait time
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void process(long timeOut) throws IOException {
        if (!isEmpty()) {
            if (0 < selector.select(Math.max(timeOut, 0))) {
                processSelectionKeys(selector.selectedKeys());
            }
        }
    }

    /**
     * Process the selection keys.
     *
     */
    private void processSelectionKeys(Set<SelectionKey> keys) {
        for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext();) {
            processSelectionKey(i.next());
        }
    }

    /**
     * Process the selection keys.
     *
     */
    private void processSelectionKey(SelectionKey key) {
        Object obj = key.attachment();
        if (null != obj && obj instanceof WeakReference) {

            @SuppressWarnings("unchecked")
            WeakReference<ChannelSubscriber> ref = (WeakReference<ChannelSubscriber>) obj;
            ChannelSubscriber callback = (ChannelSubscriber) ref.get();
            if (null != callback) {
                callback.channel(key);
            }
        }
    }

    /**
     * Adds new subscriber. If channel already have subscriber then old one is
     * replaced.
     *
     * @param channel the non-blocking channel
     * @param object the callback object
     * @param ops Same as in register in the 
     * {@link java.nio.channels.SelectableChannel#validOps() }
     * @throws IOException the closed channel exception
     */
    void add(SelectableChannel channel, ChannelSubscriber object, int ops) throws IOException {
        init();
        if (null != selector) {
            channel.register(selector, ops, new WeakReference<>(object));
        }
    }

    /**
     * Removes the subscriber of given channel.
     *
     * @param channel the channel
     */
    void remove(SelectableChannel channel) {
        if (!isEmpty()) {
            Set<SelectionKey> selectedKeys = selector.keys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                if (key.channel().equals(channel)) {
                    key.cancel();
                    wakeup();
                    break;
                }
            }
        }
    }
}
