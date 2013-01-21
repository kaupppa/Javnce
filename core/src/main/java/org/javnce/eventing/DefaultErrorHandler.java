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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The default event loop error handler .
 */
public class DefaultErrorHandler implements EventLoopErrorHandler {

    /* (non-Javadoc)
     * @see org.javnce.eventing.EventLoopErrorHandler#fatalError(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void fatalError(Object object, Throwable throwable) {
        Logger.getLogger(object.getClass().getName()).log(Level.SEVERE, null, throwable);
        EventLoopGroup.shutdown(EventLoopGroup.instance());
        System.exit(1);
    }
}
