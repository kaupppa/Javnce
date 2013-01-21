/*
 * Copyright (C) 2013 Pauli Kauppinen
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
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.examples.PingPong;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.EventLoopErrorHandler;

/**
 * The Class MyLogger is a helper class .
 */
public class MyLogger implements EventLoopErrorHandler {

    /**
     * The Constant logger.
     */
    final static Logger logger = Logger.getLogger("PingPong");

    /* (non-Javadoc)
     * @see org.javnce.eventing.EventLoopErrorHandler#fatalError(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void fatalError(Object object, Throwable throwable) {
        logger.log(Level.SEVERE, null, throwable);
        EventLoop.shutdownAll();
    }

    /**
     * Logger that outputs thread name and given text.
     *
     * @param text the text to be written out.
     */
    public static void threadSays(String text) {
        System.out.println(Thread.currentThread().getName() + " says : " + text);
    }
}
