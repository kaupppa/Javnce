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
 * The Timer Class provides timer is a timer with millisecond accuracy.
 */
public class Timer {

    /**
     * The timeout of timer in milliseconds.
     */
    private long timeout;
    /**
     * The callback.
     */
    final private TimeOutCallback callback;
    /**
     * The active.
     */
    private boolean active;

    /**
     * Instantiates a new timer.
     *
     * @param object the callback object
     * @param timeInMilliSeconds the time in milliseconds
     */
    public Timer(TimeOutCallback object, long timeInMilliSeconds) {
        timeout = timeInMilliSeconds;
        callback = object;
        active = (null != callback);
    }

    /**
     * Process timer.
     *
     * @param elapsedTime the elapsed time
     * @return the time left to timer expires, zero or negative if expired
     */
    long process(long elapsedTime) {
        timeout -= elapsedTime;
        if (0 >= timeout && isActive()) {
            callback.timeout();
            active = false;
        }

        return (isActive() ? timeout : 0);
    }

    /**
     * Disable timer expire.
     */
    public void stop() {
        active = false;
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    boolean isActive() {
        return active && null != callback;
    }
}
