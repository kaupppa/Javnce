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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Class for handling event loop state.
 */
class EventLoopState {

    /**
     * The state is true if runnable.
     */
    final private AtomicBoolean state;
    /**
     * The attached processing thread.
     */
    final private AtomicBoolean attached;

    /**
     * Instantiates a new event loop state.
     */
    EventLoopState() {
        state = new AtomicBoolean(true);
        attached = new AtomicBoolean(false);
    }

    /**
     * Attach to current thread.
     */
    void attachCurrentThread() {
        attached.set(true);
    }

    /**
     * Detach from thread.
     */
    void detachCurrentThread() {
        attached.set(false);
    }

    /**
     * Checks if is runnable.
     *
     * @return true, if is runnable
     */
    boolean isRunnable() {
        return state.get();
    }

    /**
     * Mark as not runnable.
     */
    void shutdown() {
        state.set(false);
    }

    /**
     * Checks if attached to thread.
     *
     * @return true, if is attached
     */
    public boolean isAttached() {
        return attached.get();
    }
}
