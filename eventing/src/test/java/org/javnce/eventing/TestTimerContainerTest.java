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

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestTimerContainerTest {

    @Test
    public void testTimerContainer() {
        TimerContainer container = new TimerContainer();
        assertNotNull(container.getUnit());
    }

    @Test
    public void testGetUnit() {
        TimerContainer container = new TimerContainer();
        assertEquals(TimeUnit.MILLISECONDS, container.getUnit());
    }

    @Test
    public void testProcess() throws InterruptedException {

        TimerContainer container = new TimerContainer();

        //Empty container
        assertEquals(0, container.process());

        final long Interval = 20;
        final int TimerCount = 4;
        TestTimerCallback[] callbacks = new TestTimerCallback[TimerCount];

        //Add timers in an interval
        for (int i = 0; i < callbacks.length; i++) {
            callbacks[i] = new TestTimerCallback();
            //Add timers in interval
            container.add(new Timer(callbacks[i], (i + 1) * Interval));
        }


        long nextTimeOut = Interval;
        for (int i = 0; i < callbacks.length; i++) {

            Thread.sleep(nextTimeOut);

            nextTimeOut = container.process();
            //nextTimeOut cannot be bigger then Interval
            assertTrue(Interval >= nextTimeOut);
            if ((i + 1) == callbacks.length) {
                //if last then nextTimeOut is zero
                assertTrue(0 == nextTimeOut);
            } else {
                //nextTimeOut is not zero if timers left
                assertTrue(0 < nextTimeOut);
            }

            for (int j = 0; j < callbacks.length; j++) {
                if (j <= i) {
                    assertEquals(1, callbacks[j].callCount);
                } else {
                    assertEquals(0, callbacks[j].callCount);
                }
            }
        }
    }

    @Test
    public void testAdd() throws InterruptedException {
        TimerContainer container = new TimerContainer();
        TestTimerCallback callback = new TestTimerCallback();
        long timeOut = 100;
        Timer timer = new Timer(callback, timeOut);
        container.add(timer);
        container.add(null);
        container.process();

        Thread.sleep(timeOut);
        container.process();
        assertEquals(1, callback.callCount);
    }
}
