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
package org.javnce.vnc.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.javnce.eventing.EventLoop;
import org.javnce.vnc.server.platform.FramebufferHandler;
import org.javnce.vnc.server.platform.InputEventHandler;

public class VncServerController {

    final private boolean fullAccessMode;
    final private AtomicInteger port;
    private Listener listener;
    private InputEventHandler inputEventHandler;
    private FramebufferHandler framebufferHandler;
    final private Set<RemoteClientObserver> observers;
    final private Set<RemoteClient> clients;
    final private Object lock;

    public VncServerController(boolean fullAccessMode) {
        this.fullAccessMode = fullAccessMode;
        port = new AtomicInteger(0);
        observers = new HashSet<>();
        clients = new HashSet<>();
        lock = new Object();
    }

    public void launch() {
        synchronized (lock) {
            if (null == listener) {
                listener = new Listener(this);
                listener.start();
            }
        }
    }

    void setPort(int listenerPort) {
        synchronized (port) {
            port.set(listenerPort);
            port.notifyAll();
        }
    }

    public int getPort() {

        int temp = 0;
        synchronized (port) {
            if (0 == port.get()) {
                try {
                    port.wait();
                } catch (InterruptedException ex) {
                    EventLoop.fatalError(this, ex);
                }
            }
            temp = port.get();
        }
        return temp;
    }

    void addClient(RemoteClient client) {
        client.setController(this);
        synchronized (lock) {
            clients.add(client);
        }
        clientChanged(client);
    }

    void clientChanged(RemoteClient client) {

        for (Iterator<RemoteClientObserver> i = observers().iterator(); i.hasNext();) {
            i.next().vncClientChanged(client);
        }
        if (RemoteClient.State.Connected == client.state()) {
            synchronized (lock) {
                if (null == inputEventHandler) {
                    inputEventHandler = new InputEventHandler(fullAccessMode);
                    inputEventHandler.launch();
                }
                if (null == framebufferHandler) {
                    framebufferHandler = new FramebufferHandler();
                    framebufferHandler.launch();
                }
            }
        }
        if (RemoteClient.State.Disconnected == client.state()) {
            boolean empty = false;
            synchronized (lock) {
                clients.remove(client);
                empty = clients.isEmpty();
            }
            if (empty) {
                stopHandlers();
            }
        }
    }

    private List<RemoteClientObserver> observers() {
        ArrayList<RemoteClientObserver> list;

        synchronized (lock) {
            list = new ArrayList<>(observers);
        }
        return list;
    }

    private List<RemoteClient> clients() {
        ArrayList<RemoteClient> list;

        synchronized (lock) {
            list = new ArrayList<>(clients);
        }
        return list;
    }

    public void addObserver(RemoteClientObserver observer) {

        synchronized (lock) {
            observers.add(observer);
        }

        for (Iterator<RemoteClient> i = clients().iterator(); i.hasNext();) {
            observer.vncClientChanged(i.next());
        }

    }

    public void removeObserver(RemoteClientObserver observer) {

        synchronized (lock) {
            observers.remove(observer);
        }
    }

    public void shutdown() {
        synchronized (lock) {
            if (null != listener) {
                //listener.shutdown();
                //listener = null;
            }
            observers.clear();

            for (Iterator<RemoteClient> i = clients.iterator(); i.hasNext();) {
                i.next().disconnect();
                i.remove();
            }
        }
        stopHandlers();

    }

    private void stopHandlers() {
        synchronized (lock) {
            if (null != inputEventHandler) {
                inputEventHandler.shutdown();
                inputEventHandler = null;
            }
            if (null != framebufferHandler) {
                framebufferHandler.shutdown();
                framebufferHandler = null;
            }
        }
        //Now there should be lots to clean up so we run cleaner
        System.gc();
    }
}
