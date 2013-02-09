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
package org.javnce.upnp;

import org.junit.Test;

public class UpnPServerTest {

    @Test
    public void test() throws InterruptedException {
        //No throws
        {
            UpnPServer server = new UpnPServer("", 0);
            server.shutdown();
        }
        {
            UpnPServer server = new UpnPServer("", 0);
            Thread t = new Thread(server);
            t.start();
            server.shutdown();
            t.join();
        }
        {
            UpnPServer server = new UpnPServer("", 0);
            server.run();
            server.shutdown();
            server.shutdown();
            server.shutdown();
        }
    }
}
