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

import static org.junit.Assert.*;
import org.junit.Test;

public class EventLoopStateTest {

    static void waitThreadState(boolean alive, Thread thread) {
        while (alive != thread.isAlive()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Test
    public void testEventLoopState() {
        EventLoopState state = new EventLoopState();
        assertNotNull(state);
        assertTrue(state.isRunnable());
    }

    @Test
    public void testAttachCurrentThread() throws InterruptedException {
        final EventLoopState state = new EventLoopState();

        assertTrue(state.isRunnable());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                state.attachCurrentThread();
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                }
            }
        });

        t.start();

        waitThreadState(true, t);

        assertTrue(state.isRunnable());

        t.interrupt();
        waitThreadState(false, t);

        assertFalse(state.isRunnable());
    }

    @Test
    public void testDetachCurrentThread() throws InterruptedException {

        final EventLoopState state = new EventLoopState();

        assertTrue(state.isRunnable());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                state.attachCurrentThread();
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    state.detachCurrentThread();
                    Thread.interrupted();
                }

                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                }
            }
        });

        t.start();

        waitThreadState(true, t);

        assertTrue(state.isRunnable());


        t.interrupt();
        Thread.sleep(4);

        //After first interrupt state is isRunnable as it is detached 
        assertTrue(state.isRunnable());

        t.interrupt();
        waitThreadState(false, t);

        //isRunnable as not attached
        assertTrue(state.isRunnable());
    }

    @Test
    public void testShutdown() {
        final EventLoopState state = new EventLoopState();

        assertTrue(state.isRunnable());
        state.shutdown();
        assertFalse(state.isRunnable());
    }

    @Test
    public void testIsAttached() {
        final EventLoopState state = new EventLoopState();

        assertFalse(state.isAttached());

        state.attachCurrentThread();
        assertTrue(state.isAttached());

        state.detachCurrentThread();
        assertFalse(state.isAttached());
    }
}
