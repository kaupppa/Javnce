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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The Class for handling timer and timeout
 */
class TimerContainer {

    /**
     * The collection of timers.
     */
    final private Set<Timer> timers;

    /**
     * Instantiates a new timer container.
     */
    public TimerContainer() {
        timers = new HashSet<>();
    }

    /**
     * Gets the time unit.
     *
     * @return the unit
     */
    public TimeUnit getUnit() {
        return TimeUnit.MILLISECONDS;
    }

    /**
     * Process timers.
     *
     * @return the timeout of next timer. Zero if no timers
     */
    public long process() {
        long timeout = 0;
        if (!isEmpty()) {
            timeout = process(System.currentTimeMillis());
        }
        return timeout;
    }

    /**
     * Do the process.
     *
     * @param currentTime the current time
     * @return the timeout of next timer. Zero if no timers
     */
    private long process(long currentTime) {
        long timeout = 0;
        for (Iterator<Timer> i = timers.iterator(); i.hasNext();) {
            Timer timer = i.next();
            long timerTimeout = timer.process(currentTime);
            if (0 >= timerTimeout) {
                i.remove();
            } else if (0 == timeout) {
                timeout = timerTimeout;
            } else {
                timeout = Math.min(timeout, timerTimeout);
            }
        }
        return timeout;
    }

    /**
     * Adds a timer.
     *
     * @param timer the timer
     */
    public void add(Timer timer) {
        if (null != timer) {
            timers.add(timer);
        }
    }

    /**
     * Tests if any timers
     *
     * @return true, if no timers
     */
    public boolean isEmpty() {
        return timers.isEmpty();
    }
}
