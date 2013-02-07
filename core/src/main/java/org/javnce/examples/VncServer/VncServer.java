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
package org.javnce.examples.VncServer;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.EventLoopErrorHandler;
import org.javnce.vnc.server.RemoteClient;
import org.javnce.vnc.server.RemoteClientObserver;
import org.javnce.vnc.server.VncServerController;

public class VncServer {

    static class ErrorHandler implements EventLoopErrorHandler {

        final private Object wakeup;

        ErrorHandler() {
            wakeup = new Object();
        }

        void waitForError() throws InterruptedException {
            synchronized (wakeup) {
                wakeup.wait();
            }
        }

        @Override
        public void fatalError(Object object, Throwable throwable) {
            if (null == object) {
                object = this;
            }
            Logger.getLogger(object.getClass().getName()).log(Level.SEVERE, null, throwable);
            synchronized (wakeup) {
                wakeup.notifyAll();
            }
        }
    }

    public static void main(String[] args) {

        ErrorHandler error = new ErrorHandler();
        EventLoop.setErrorHandler(error);

        VncServerController controller = new VncServerController(true);

        controller.addObserver(new RemoteClientObserver() {
            @Override
            public void vncClientChanged(RemoteClient client) {
                if (RemoteClient.State.PendingConnection == client.state()) {
                    client.connect();
                }
            }

            @Override
            public void portChanged(int port) {
            }
        });

        controller.launch();

        //We are running untill an error occurs
        try {
            error.waitForError();
        } catch (InterruptedException ex) {
            Logger.getLogger(VncServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        controller.shutdown();
    }
}
