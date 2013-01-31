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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The Class for handling timer and timeout
 */
class TimerContainer {

    /**
     * The collection of timers.
     */
    final private List<Timer> timers;

    /**
     * Instantiates a new timer container.
     */
    public TimerContainer() {
        timers = new ArrayList<>();
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
            clear();
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
        long timeout = Long.MAX_VALUE;

        //Take a snapshot as timer process may modidy the orginal list
        ArrayList<Timer> list = new ArrayList<>(timers);
        for (Iterator<Timer> i = list.iterator(); i.hasNext();) {
            Timer timer = i.next();
            long timerTimeout = timer.process(currentTime);
            if (timer.isActive()) {
                timeout = Math.min(timeout, timerTimeout);
            }
        }
        return (timeout == Long.MAX_VALUE ? 0 : timeout);
    }

    /**
     * Remove expired timers.
     */
    private void clear() {
        for (Iterator<Timer> i = timers.iterator(); i.hasNext();) {
            if (!i.next().isActive()) {
                i.remove();
            }
        }
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
