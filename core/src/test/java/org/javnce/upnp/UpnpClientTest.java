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

public class UpnpClientTest {

    @Test
    public void test() throws InterruptedException {
        //No throws
        {
            UpnpClient client = new UpnpClient();
            client.shutdown();
        }
        {
            UpnpClient client = new UpnpClient();
            Thread t = new Thread(client);
            t.start();
            client.shutdown();
            t.join();
        }
        {
            UpnpClient client = new UpnpClient();
            client.run();
            client.shutdown();
            client.shutdown();
            client.shutdown();
        }
    }
}
