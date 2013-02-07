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
package org.javnce.ui.fx.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.javnce.ui.fx.MainFrame;
import org.javnce.ui.fx.MainFrameObserver;
import org.javnce.ui.fx.NodeFactory;
import org.javnce.upnp.client.RemoteServerInfo;
import org.javnce.upnp.client.UpnpClientController;
import org.javnce.upnp.client.UpnpClientObserver;

/**
 * The view for showing found Javnce servers.
 */
public class SelectServer extends AnchorPane implements Initializable, UpnpClientObserver, MainFrameObserver {

    /**
     * The fxml url.
     */
    private final static URL fxmlUrl = SelectServer.class.getResource("SelectServer.fxml");
    /**
     * The found server items.
     */
    final private ObservableList<RemoteServerInfo> items;
    /**
     * The UPnP controller.
     */
    final private UpnpClientController controller;
    /**
     * The progress indicator.
     */
    @FXML
    ProgressIndicator progressIndicator;
    /**
     * The list view.
     */
    @FXML
    ListView<RemoteServerInfo> listView;
    /**
     * The connect button.
     */
    @FXML
    Button connectButton;

    /**
     * Instantiates a new select server.
     */
    public SelectServer() {
        items = FXCollections.observableArrayList();
        controller = UpnpClientController.instance();
    }

    /**
     * Creates the SelectServer.
     *
     * @return the node
     * @throws Exception the exception
     */
    public static Node create() throws Exception {
        return FXMLLoader.load(fxmlUrl);
    }

    /**
     * Initialize.
     *
     * @param location the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller.start(this);
        listView.setItems(items);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        listView.setCellFactory(
                new Callback<ListView<RemoteServerInfo>, ListCell<RemoteServerInfo>>() {
                    @Override
                    public ListCell<RemoteServerInfo> call(ListView<RemoteServerInfo> p) {
                        return new ListCell<RemoteServerInfo>() {
                            @Override
                            protected void updateItem(RemoteServerInfo item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item != null && !empty) {
                                    setText(item.getName() + " at " + item.getAddress()
                                            .getAddress()
                                            .getCanonicalHostName());
                                }
                            }
                        };
                    }
                });


        MainFrame.getMainFrame().addObserver(this);
        update();
    }

    /* (non-Javadoc)
     * @see org.javnce.upnp.client.UpnpClientObserver#serverFound(org.javnce.upnp.client.RemoteServerInfo)
     */
    @Override
    public void serverFound(final RemoteServerInfo server) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                items.add(server);
                update();
                if (1 == items.size()) {
                    listView.getSelectionModel().select(0);
                }
            }
        });

    }

    /* (non-Javadoc)
     * @see org.javnce.upnp.client.UpnpClientObserver#serverLost(org.javnce.upnp.client.RemoteServerInfo)
     */
    @Override
    public void serverLost(final RemoteServerInfo server) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                items.remove(server);
                update();
            }
        });
    }

    /**
     * Update progress and list view.
     */
    private void update() {
        if (items.isEmpty()) {
            MainFrame.getMainFrame().setTitle("Searching for servers");
            progressIndicator.setProgress(-1.0f);
            progressIndicator.setVisible(true);
            listView.setVisible(false);
            connectButton.setVisible(false);

        } else {
            MainFrame.getMainFrame().setTitle("Select a server");
            progressIndicator.setVisible(false);
            listView.setVisible(true);
            progressIndicator.setProgress(0);
            connectButton.setVisible(true);
        }
    }

    /**
     * Connect to server.
     *
     * @param event the event
     * @throws Exception the exception
     */
    public void connect(ActionEvent event) throws Exception {
        ObservableList<RemoteServerInfo> selected = listView.getSelectionModel().getSelectedItems();

        if (selected.isEmpty()) {
            MainFrame.getMainFrame().setTitle("Please, select a server");
        } else if (1 < selected.size()) {
            MainFrame.getMainFrame().setTitle("Please, select one server");
        } else {
            final RemoteServerInfo server = selected.get(0);

            MainFrame.getMainFrame().add(
                    new NodeFactory() {
                        @Override
                        public Node create() throws Exception {
                            return VncView.create(server);
                        }
                    });

        }
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

    /**
     * Shutdown.
     */
    private void shutdown() {
        controller.shutdown();
        MainFrame.getMainFrame().removeObserver(this);
    }
}
