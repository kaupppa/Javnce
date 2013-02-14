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

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import org.javnce.upnp.UpnPServer;
import org.javnce.vnc.server.RemoteClient;
import org.javnce.vnc.server.RemoteClientObserver;
import org.javnce.vnc.server.VncServerController;

public class ServerViewController implements ViewController, Initializable, RemoteClientObserver {

    final static private ResourceBundle bundle = ResourceBundle.getBundle("org.javnce.ui.ServerView", Locale.getDefault());
    final static private URL fxmlUrl = ViewController.class.getResource("ServerView.fxml");
    private Node node;
    final private VncServerController vnc;
    private UpnPServer upnp;
    final private String name;
    final private ObservableList<String> items;
    final private boolean fullAccessMode;
    @FXML
    ListView<String> listView;

    public ServerViewController(String name, boolean fullAccessMode) {
        this.name = name;
        this.fullAccessMode = fullAccessMode;
        items = FXCollections.observableArrayList();
        vnc = new VncServerController(fullAccessMode);
    }

    @Override
    public Node getNode() throws IOException {
        if (null == node) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(fxmlUrl);
            loader.setResources(bundle);
            loader.setController(this);
            node = (Node) loader.load();
        }
        return node;

    }

    @Override
    public void exit() {
        if (null != upnp) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    upnp.shutdown();
                }
            }).start();
        }
        vnc.removeObserver(this);
        vnc.shutdown();
    }

    @Override
    public ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public ViewController viewFactory() {
                return new ServerViewController(name, fullAccessMode);
            }
        };

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listView.setItems(items);
        vnc.addObserver(this);
        vnc.launch();
    }

    @Override
    public void vncClientChanged(RemoteClient client) {
        Runnable r = null;

        if (RemoteClient.State.PendingConnection == client.state()) {
            final RemoteClient temp = client;
            r = new Runnable() {
                @Override
                public void run() {
                    acceptConnection(temp);
                }
            };
        } else if (RemoteClient.State.Connected == client.state()) {
            final String address = client.address();
            r = new Runnable() {
                @Override
                public void run() {
                    items.add(address);
                }
            };
        } else if (RemoteClient.State.Disconnected == client.state()) {
            final String address = client.address();
            r = new Runnable() {
                @Override
                public void run() {
                    items.remove(address);
                }
            };
        }

        if (null != r) {
            Platform.runLater(r);
        }

    }

    @Override
    public void portChanged(int port) {
        if (null == upnp && 0 < port) {
            upnp = new UpnPServer(name, port);
            new Thread(upnp).start();
        }
    }

    private void acceptConnection(RemoteClient client) {
        String text = new MessageBox("Accept client ?",
                "New client from " + client.address(), "Accept", "Decline").exec();
        boolean accept = text.equals("Accept");
        if (accept) {
            client.connect();
        } else {
            client.disconnect();
        }
    }
}
