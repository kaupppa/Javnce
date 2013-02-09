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

import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.meta.LocalService;
import static org.junit.Assert.*;
import org.junit.Test;

public class VncServiceTest {

    @Test
    public void test() {
        @SuppressWarnings("unchecked")
        LocalService<VncService> service = new AnnotationLocalServiceBinder().read(VncService.class);
        assertNotNull(service);

        assertTrue(service.hasActions());

        service.setManager(new DefaultServiceManager<>(service, VncService.class));
        VncService vnc = service.getManager().getImplementation();

        int port = 0;
        assertEquals(port, vnc.port());

        port = 5900;
        vnc.setPort(port);
        assertEquals(port, vnc.port());
    }
}
