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
package org.javnce.ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.javnce.upnp.RemoteServerInfo;
import static org.junit.Assert.*;
import org.junit.Test;

public class ConfigTest {

    @Test
    public void testHeight() {
        Config instance = new Config();
        SimpleDoubleProperty result = instance.height();
        assertNotNull(result);
    }

    @Test
    public void testWidth() {
        Config instance = new Config();
        SimpleDoubleProperty result = instance.width();
        assertNotNull(result);
    }

    @Test
    public void testGetServerInfo() {
        Config instance = new Config();
        RemoteServerInfo result = instance.getServerInfo();
        assertNull(result);
    }

    @Test
    public void testSetServerInfo() {
        RemoteServerInfo serverInfo = new RemoteServerInfo("", "", "", 0);
        Config instance = new Config();
        instance.setServerInfo(serverInfo);
        assertEquals(serverInfo, instance.getServerInfo());
    }

    @Test
    public void testServerName() {
        Config instance = new Config();
        SimpleStringProperty result = instance.serverName();
        assertNotNull(result);
    }

    @Test
    public void testFullAccessMode() {
        Config instance = new Config();
        SimpleBooleanProperty result = instance.fullAccessMode();
        assertNotNull(result);
    }

    @Test
    public void testAutoConnect() {
        Config instance = new Config();
        SimpleBooleanProperty result = instance.autoConnect();
        assertNotNull(result);
    }
}
