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

public class TestConfig extends Config {

    final private SimpleDoubleProperty width;
    final private SimpleDoubleProperty height;
    final private SimpleStringProperty serverName;
    final private SimpleBooleanProperty fullAccessMode;
    final private SimpleBooleanProperty autoConnect;
    private RemoteServerInfo serverInfo;

    public TestConfig() {
        width = new SimpleDoubleProperty(100);
        height = new SimpleDoubleProperty(100);
        serverName = new SimpleStringProperty("Name");
        fullAccessMode = new SimpleBooleanProperty(false);
        autoConnect = new SimpleBooleanProperty(false);
        serverInfo = new RemoteServerInfo("id", "name", "127.0.0.1", 5900);
    }

    @Override
    public SimpleDoubleProperty height() {
        return height;
    }

    @Override
    public SimpleDoubleProperty width() {
        return width;
    }

    @Override
    public RemoteServerInfo getServerInfo() {
        return serverInfo;
    }

    @Override
    public void setServerInfo(RemoteServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public SimpleStringProperty serverName() {
        return serverName;
    }

    @Override
    public SimpleBooleanProperty fullAccessMode() {
        return fullAccessMode;
    }

    @Override
    public SimpleBooleanProperty autoConnect() {
        return autoConnect;
    }
}
