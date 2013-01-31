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
 *
 * @see org.javnce.eventing.EventLoop#addTimer(org.javnce.eventing.Timer)
 */
public class Timer {

    /**
     * The timeout of timer in milliseconds. Zero if not active.
     */
    private long timeout;
    /**
     * The callback.
     */
    final private TimeOutCallback callback;

    /**
     * Instantiates a new timer.
     *
     * @param callback the callback object
     * @param timeInMilliSeconds the time in milliseconds
     */
    public Timer(TimeOutCallback callback, long timeInMilliSeconds) {
        timeout = 0;
        if (null != callback) {
            timeout = timeInMilliSeconds + System.currentTimeMillis();
        }
        this.callback = callback;
    }

    /**
     * Process timer.
     *
     * @param currentTime as in {@link System.currentTimeMillis()};
     * @return the time left to timer expires, zero if expired
     */
    long process(long currentTime) {
        if (isActive() && currentTime >= timeout) {
            stop();
            if (null != callback) {
                callback.timeout();
            }
        }
        return Math.max(timeout - currentTime, 0);
    }

    /**
     * Disable timer.
     */
    public void stop() {
        timeout = 0;
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    boolean isActive() {
        boolean active = false;
        if (timeout != 0 && null != callback) {
            active = true;
        }
        return active;
    }
}
