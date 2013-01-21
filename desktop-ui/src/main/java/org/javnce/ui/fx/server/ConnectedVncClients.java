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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.SocketChannel;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.javnce.ui.model.ServerConfiguration;
import org.javnce.upnp.server.UpnpServerController;
import org.javnce.vnc.server.VncServerController;
import org.javnce.vnc.server.VncServerObserver;

/**
 * The VNC server view showing connected clients.
 */
public class ConnectedVncClients extends AnchorPane implements Initializable, VncServerObserver, MainFrameObserver {

    /**
     * The fxml url.
     */
    private final static URL fxmlUrl = ConnectedVncClients.class.getResource("ConnectedVncClients.fxml");
    /**
     * The VNC controller.
     */
    final private VncServerController vncController;
    /**
     * The UPnP controller.
     */
    final private UpnpServerController upnpController;
    /**
     * The server name.
     */
    final private String name;
    /**
     * The full access mode.
     */
    final private boolean fullAccessMode;
    /**
     * The clients.
     */
    final private ObservableList<String> items;
    /**
     * The list view.
     */
    @FXML
    ListView<String> listView;

    /**
     * Instantiates a new connected VNC clients.
     *
     * @param name the name
     * @param fullAccessMode the full access mode
     */
    public ConnectedVncClients(String name, boolean fullAccessMode) {
        this.name = name;
        this.fullAccessMode = fullAccessMode;
        vncController = ServerConfiguration.instance().getVncController();
        upnpController = ServerConfiguration.instance().getUpnpController();
        items = FXCollections.observableArrayList();
    }

    /**
     * Creates the ConnectedVncClients.
     *
     * @param name the name
     * @param fullAccessMode the full access mode
     * @return the node
     * @throws Exception the exception
     */
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

    /**
     * Initialize.
     *
     * @param url the url
     * @param rb the rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainFrame.getMainFrame().setTitle("Server running");
        MainFrame.getMainFrame().addObserver(this);
        vncController.start(fullAccessMode, this);
        listView.setItems(items);
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.VncServerObserver#listening(int)
     */
    @Override
    public void listening(final int port) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                upnpController.start(name, port);
            }
        });
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.VncServerObserver#connectionClosed(java.lang.Object)
     */
    @Override
    public void connectionClosed(Object userData) {
        final String address = (String) userData;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                items.remove(address);
            }
        });
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.server.VncServerObserver#newConnection(java.nio.channels.SocketChannel)
     */
    @Override
    public void newConnection(final SocketChannel channel) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                acceptnewConnection(channel);
            }
        });
    }

    /**
     * Accept new connection.
     *
     * @param channel the channel
     */
    private void acceptnewConnection(SocketChannel channel) {
        try {
            String address = ((InetSocketAddress) channel.getRemoteAddress())
                    .getAddress()
                    .getCanonicalHostName();
            String text = new MessageBox("Accept client ?",
                    "New client from " + address, "Accept", "Decline").exec();
            boolean accept = text.equals("Accept");
            if (accept) {
                items.add(address);
            }
            vncController.acceptConnection(this, channel, accept, address);
        } catch (IOException ex) {
            Logger.getLogger(ConnectedVncClients.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Shutdown.
     */
    private void shutdown() {
        ServerConfiguration.instance().shutdown();
        MainFrame.getMainFrame().removeObserver(this);
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.fx.MainFrameObserver#previousView()
     */
    @Override
    public void previousView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        });
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.fx.MainFrameObserver#nextView()
     */
    @Override
    public void nextView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        });

    }
}
