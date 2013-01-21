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

/**
 * A Class for handling event loop state.
 */
class EventLoopState {

    /** The state is true if runnable. */
    private boolean state;
    
    /** The attached processing thread. */
    Thread processingThread;

    /**
     * Instantiates a new event loop state.
     */
    EventLoopState() {
        state = true;
    }

    /**
     * Attach to current thread.
     */
    synchronized void attachCurrentThread() {
        processingThread = Thread.currentThread();
    }

    /**
     * Detach from thread.
     */
    synchronized void detachCurrentThread() {
        processingThread = null;
    }

    /**
     * Tests if attached thread is runnable.
     *
     * @return true, if successful
     */
    private boolean threadIsRunnable() {
        boolean runnable = true;
        if (null != processingThread) {
            runnable = (!processingThread.isInterrupted() && processingThread.isAlive());
        }
        return runnable;
    }

    /**
     * Checks if is runnable.
     *
     * @return true, if is runnable
     */
    synchronized boolean isRunnable() {
        return (state && threadIsRunnable());
    }

    /**
     * Mark as not runnable.
     */
    synchronized void shutdown() {
        state = false;
    }

    /**
     * Checks if attached to thread.
     *
     * @return true, if is attached
     */
    synchronized public boolean isAttached() {
        return (null != processingThread);
    }
}
