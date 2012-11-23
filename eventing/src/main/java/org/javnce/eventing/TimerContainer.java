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

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The Class for handling timer and timeout
 */
class TimerContainer {

    /**
     * The last process time stamp.
     */
    private long lastTime;
    /**
     * The collection of timers.
     */
    final private ConcurrentSet<Timer> timers;

    /**
     * Instantiates a new timer container.
     */
    public TimerContainer() {
        lastTime = System.currentTimeMillis();
        timers = new ConcurrentSet<>();
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
     * @return the long
     */
    public long process() {
        long now = System.currentTimeMillis();
        long timeout = process(now - lastTime);
        lastTime = now;

        return timeout;
    }

    /**
     * Do the process.
     *
     * @param elapsedTime the elapsed time
     * @return the long
     */
    private long process(long elapsedTime) {
        long timeout = 0;
        List<Timer> list = timers.get();
        for (Timer timer : list) {
            long timerTimeout = timer.process(elapsedTime);
            if (0 >= timerTimeout) {
                timers.remove(timer);
            } else {
                timeout = Math.max(timeout, timerTimeout);
            }
        }


        return timeout;
    }

    /**
     * Adds the timer.
     *
     * @param timer the timer
     */
    public void add(Timer timer) {
        if (null != timer) {
            //process to sync time
            process();
            timers.add(timer);
        }
    }

    /**
     * Tests if any timers
     *
     * @param false if no timers
     */
    public boolean isEmpty() {
        return timers.isEmpty();
    }
}
