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

    /**
     * The state is true if runnable.
     */
    private boolean runState;
    /**
     * The processing state.
     */
    private boolean processingState;

    /**
     * Instantiates a new event loop state.
     */
    EventLoopState() {
        runState = true;
        processingState = false;
    }

    /**
     * Set processing state.
     */
    void processing(boolean state) {
        processingState = state;
    }

    /**
     * Checks if is runnable.
     *
     * @return true, if is runnable
     */
    boolean isRunnable() {
        return runState;
    }

    /**
     * Mark as not runnable.
     */
    void shutdown() {
        runState = false;
    }

    /**
     * Checks if processing
     *
     * @return true, if is processing
     */
    public boolean isProcessing() {
        return processingState;
    }
}
