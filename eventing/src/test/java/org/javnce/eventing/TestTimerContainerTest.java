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

        //Add timer
        TestTimerCallback callback = new TestTimerCallback();
        long timeOut = 100;
        Timer timer = new Timer(callback, timeOut);
        container.add(timer);
        assertTrue(100 >= container.process());
        assertTrue(90 <= container.process());
        assertFalse(callback.called);

        //Add second timer
        timer = new Timer(callback, timeOut * 2);
        container.add(timer);
        assertTrue(200 >= container.process());
        assertTrue(190 <= container.process());
        assertFalse(callback.called);
        //Timeout
        Thread.sleep(timeOut);
        container.process();
        assertTrue(callback.called);
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
        assertTrue(callback.called);
    }
}
