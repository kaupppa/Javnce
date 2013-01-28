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

import java.lang.ref.WeakReference;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TimerTest {

    private long currentTime;

    @Before
    public void setUp() throws Exception {
        currentTime = System.currentTimeMillis();
    }

    @Test
    public void testTimer() {
        Timer timer = new Timer(null, 100);
        assertNotNull(timer);
    }

    @Test
    public void testProcessNormal() {
        TestTimerCallback callback = new TestTimerCallback();
        long timeOut = 100;
        Timer timer = new Timer(callback, timeOut);

        //No elapsed time
        long nextTimeOut = timer.process(currentTime);
        assertTrue(timeOut >= nextTimeOut);
        assertEquals(0, callback.callCount);

        //half of elapsed time
        nextTimeOut = timer.process(currentTime + timeOut / 2);
        assertTrue(timeOut / 2 == nextTimeOut);
        assertEquals(0, callback.callCount);

        //timeout
        nextTimeOut = timer.process(currentTime + timeOut);
        assertTrue(0 == nextTimeOut);
        assertEquals(1, callback.callCount);

        //overtime
        nextTimeOut = timer.process(currentTime + timeOut * 2);
        assertTrue(0 == nextTimeOut);
        assertEquals(1, callback.callCount);

    }

    @Test
    public void testProcessNull() {
        long timeOut = 100;
        Timer timer = new Timer(null, timeOut);
        long nextTimeOut = timer.process(currentTime);
        assertEquals(0, nextTimeOut);

    }

    @Test
    public void testProcessNoTimeout() {
        long timeOut = -100;
        TestTimerCallback callback = new TestTimerCallback();
        Timer timer = new Timer(callback, timeOut);
        long nextTimeOut = timer.process(currentTime);
        assertEquals(0, nextTimeOut);

        //Timeout smaller the 1 millisecond -> called immediately
        assertEquals(1, callback.callCount);
    }

    @Test
    public void testStop() {
        TestTimerCallback callback = new TestTimerCallback();
        long timeOut = 100;
        Timer timer = new Timer(callback, timeOut);

        //No elapsed time
        long nextTimeOut = timer.process(currentTime);
        assertTrue(timeOut >= nextTimeOut);
        assertEquals(0, callback.callCount);

        //half of elapsed time
        nextTimeOut = timer.process(currentTime + timeOut / 2);
        assertTrue(timeOut / 2 == nextTimeOut);
        assertEquals(0, callback.callCount);

        timer.stop();

        //timeout even if half of elapsed time 
        nextTimeOut = timer.process(currentTime + timeOut / 2);
        assertTrue(0 == nextTimeOut);
        assertEquals(0, callback.callCount);
    }

    @Test
    public void testIsActive() {
        long timeOut = 100;
        Timer timer = new Timer(null, timeOut);
        assertFalse(timer.isActive());

        TestTimerCallback callback = new TestTimerCallback();
        timer = new Timer(callback, timeOut);
        currentTime = System.currentTimeMillis();
        assertTrue(timer.isActive());

        timer.process(currentTime + timeOut);
        assertFalse(timer.isActive());
    }

    @Test
    public void testWeakReference() {
        long timeOut = 100;

        TestTimerCallback callback = new TestTimerCallback();
        
        Timer timer = new Timer(callback, timeOut);
        assertTrue(timer.isActive());
        
        WeakReference<TestTimerCallback> ref = new WeakReference<>(callback);
        assertNotNull(ref.get());

        callback = null;
        System.gc();
        
        //Check that callback is cleaned
        assertNull(ref.get());

        //Check that timer is not active
        assertFalse(timer.isActive());

    }
}
