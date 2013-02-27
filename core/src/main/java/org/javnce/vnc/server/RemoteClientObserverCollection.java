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
package org.javnce.vnc.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * The Class RemoteClientObserverCollection implements a set of
 * RemoteClientObserver.
 *
 * The RemoteClientObserverCollection is thread safe.
 */
class RemoteClientObserverCollection implements RemoteClientObserver {

    /**
     * The observers.
     */
    final private Set<RemoteClientObserver> observers;
    /**
     * The lock.
     */
    final private Object lock;

    /**
     * Instantiates a new remote client observer collection.
     */
    RemoteClientObserverCollection() {
        observers = new HashSet<>();
        lock = new Object();
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.RemoteClientObserver#vncClientChanged(org.javnce.vnc.server.RemoteClient)
     */
    @Override
    public void vncClientChanged(RemoteClient client) {
        for (Iterator<RemoteClientObserver> i = observers().iterator(); i.hasNext();) {
            //Called outside the lock as it may change state of client
            i.next().vncClientChanged(client);
        }
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.RemoteClientObserver#portChanged(int)
     */
    @Override
    public void portChanged(int port) {
        for (Iterator<RemoteClientObserver> i = observers().iterator(); i.hasNext();) {
            //Called outside the lock
            i.next().portChanged(port);
        }
    }

    /**
     * Adds the.
     *
     * @param observer the observer
     */
    public void add(RemoteClientObserver observer) {
        synchronized (lock) {
            observers.add(observer);
        }
    }

    /**
     * Removes the.
     *
     * @param observer the observer
     */
    public void remove(RemoteClientObserver observer) {
        synchronized (lock) {
            observers.remove(observer);
        }
    }

    /**
     * Take snapshot of current observers.
     *
     * @return the list
     */
    private List<RemoteClientObserver> observers() {
        ArrayList<RemoteClientObserver> list;

        synchronized (lock) {
            list = new ArrayList<>(observers);
        }
        return list;
    }
}
