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

import java.util.prefs.Preferences;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.javnce.upnp.RemoteServerInfo;


/**
 * The Class Config contains user specific persistent and non-persistent data.
 * 
 * In windows see HKEY_CURRENT_USER\Software\JavaSoft\Prefs\org\javnce
 */
public class Config {

    /** The class name used in Preferences. */
    final private String className;
    
    /** The persistent data store. */
    final private Preferences prefs;
    
    /** The width. */
    final private SimpleDoubleProperty width;
    
    /** The height. */
    final private SimpleDoubleProperty height;
    
    /** The server name. */
    final private SimpleStringProperty serverName;
    
    /** The full access mode. */
    final private SimpleBooleanProperty fullAccessMode;
    
    /** The auto connect. */
    final private SimpleBooleanProperty autoConnect;
    
    /** The server info. */
    private RemoteServerInfo serverInfo;

    /**
     * Instantiates a new config.
     */
    public Config() {
        className = getClass().getName();
        prefs = Preferences.userNodeForPackage(getClass());

        width = new SimpleDoubleProperty();
        height = new SimpleDoubleProperty();
        serverName = new SimpleStringProperty();
        fullAccessMode = new SimpleBooleanProperty();
        autoConnect = new SimpleBooleanProperty(false);
        init();
    }

    /**
     * Bind double property with preferences.
     *
     * @param prop the prop
     * @param key the key
     * @param defaultValue the default value
     */
    private void bindDoubleProp(SimpleDoubleProperty prop, final String key, double defaultValue) {
        prop.set(prefs.getDouble(key, defaultValue));
        prop.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                prefs.putDouble(key, newValue.doubleValue());
            }
        });
    }

    /**
     * Bind string property with preferences..
     *
     * @param prop the prop
     * @param key the key
     * @param defaultValue the default value
     */
    private void bindStringProp(SimpleStringProperty prop, final String key, String defaultValue) {
        prop.set(prefs.get(key, defaultValue));
        prop.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                prefs.put(key, newValue);
            }
        });
    }

    /**
     * Bind boolean property with preferences..
     *
     * @param prop the prop
     * @param key the key
     * @param defaultValue the default value
     */
    private void bindBooleanProp(SimpleBooleanProperty prop, final String key, boolean defaultValue) {
        prop.set(prefs.getBoolean(key, defaultValue));
        prop.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                prefs.putBoolean(key, newValue);
            }
        });
    }

    /**
     * Init the binding.
     */
    private void init() {
        bindDoubleProp(width, askey("width"), 0);
        bindDoubleProp(height, askey("height"), 0);
        String defaultName = System.getProperty("user.name") + "@" + System.getProperty("os.name") + "-" + System.getProperty("os.arch");
        bindStringProp(serverName, askey("serverName"), defaultName);
        bindBooleanProp(fullAccessMode, askey("fullAccessMode"), false);
    }

    /**
     * Height.
     *
     * @return the simple double property
     */
    public SimpleDoubleProperty height() {
        return height;
    }

    /**
     * Width.
     *
     * @return the simple double property
     */
    public SimpleDoubleProperty width() {
        return width;
    }

    /**
     * Get unified case insensetive preferences key.
     *
     * @param name the name
     * @return the string
     */
    private String askey(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(className)
                .append(".")
                .append(name);

        return sb.toString().toLowerCase();
    }

    /**
     * Gets the server info.
     *
     * @return the server info
     */
    public RemoteServerInfo getServerInfo() {
        return serverInfo;
    }

    /**
     * Sets the server info.
     *
     * @param serverInfo the new server info
     */
    public void setServerInfo(RemoteServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    /**
     * Server name.
     *
     * @return the simple string property
     */
    public SimpleStringProperty serverName() {
        return serverName;
    }

    /**
     * Full access mode.
     *
     * @return the simple boolean property
     */
    public SimpleBooleanProperty fullAccessMode() {
        return fullAccessMode;
    }

    /**
     * Auto connect.
     *
     * @return the simple boolean property
     */
    public SimpleBooleanProperty autoConnect() {
        return autoConnect;
    }
}