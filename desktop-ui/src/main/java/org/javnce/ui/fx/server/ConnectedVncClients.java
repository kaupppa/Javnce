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
package org.javnce.ui.fx.server;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.javnce.ui.fx.MainFrame;
import org.javnce.ui.fx.MainFrameObserver;
import org.javnce.ui.fx.MessageBox;
import org.javnce.upnp.server.UpnPServer;
import org.javnce.vnc.server.RemoteClient;
import org.javnce.vnc.server.RemoteClientObserver;
import org.javnce.vnc.server.VncServerController;

public class ConnectedVncClients extends AnchorPane implements Initializable, RemoteClientObserver, MainFrameObserver {

    private final static URL fxmlUrl = ConnectedVncClients.class.getResource("ConnectedVncClients.fxml");
    private VncServerController vnc;
    private UpnPServer upnp;
    final private String name;
    final private boolean fullAccessMode;
    final private ObservableList<String> items;
    @FXML
    ListView<String> listView;

    public ConnectedVncClients(String name, boolean fullAccessMode) {
        this.name = name;
        this.fullAccessMode = fullAccessMode;
        items = FXCollections.observableArrayList();
    }

    public static Node create(final String name, final boolean fullAccessMode) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        loader.setControllerFactory(new Callback() {
            @Override
            public Object call(Object p) {
                return new ConnectedVncClients(name, fullAccessMode);
            }
        });
        return (Node) loader.load();
    }

    @Override
    synchronized public void initialize(URL url, ResourceBundle rb) {
        MainFrame.getMainFrame().setTitle("Server running");
        MainFrame.getMainFrame().addObserver(this);

        listView.setItems(items);

        vnc = new VncServerController(fullAccessMode);
        vnc.addObserver(this);
        vnc.launch();
    }

    synchronized private void acceptConnection(RemoteClient client) {
        String text = new MessageBox("Accept client ?",
                "New client from " + client.address(), "Accept", "Decline").exec();
        boolean accept = text.equals("Accept");
        if (accept) {
            client.connect();
        } else {
            client.disconnect();
        }
    }

    synchronized private void shutdown() {
        vnc.removeObserver(this);
        upnp.shutdown();
        vnc.shutdown();
        MainFrame.getMainFrame().removeObserver(this);
    }

    @Override
    public void previousView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        });
    }

    @Override
    public void nextView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        });

    }

    @Override
    synchronized public void vncClientChanged(RemoteClient client) {
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
    synchronized public void portChanged(int port) {
        if (null == upnp && 0 < port) {
            upnp = new UpnPServer(name, port);
            upnp.start();
        }
    }
}
