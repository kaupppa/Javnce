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
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;
import org.javnce.upnp.RemoteServerInfo;
import org.javnce.upnp.UpnpClient;
import org.javnce.upnp.UpnpClientObserver;

public class ServerSearchViewController implements ViewController, Initializable, UpnpClientObserver {

    final static private ResourceBundle bundle = ResourceBundle.getBundle("org.javnce.ui.ServerSearchView", Locale.getDefault());
    final static private URL fxmlUrl = ViewController.class.getResource("ServerSearchView.fxml");
    final private ObservableList<RemoteServerInfo> items;
    final private UpnpClient upnp;
    private Node node;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    ListView<RemoteServerInfo> listView;
    @FXML
    Button connectButton;

    public ServerSearchViewController() {
        items = FXCollections.observableArrayList();
        upnp = new UpnpClient();
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

    static class MyCell extends ListCell<RemoteServerInfo> {

        @Override
        protected void updateItem(RemoteServerInfo item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                String name = item.getName() + " at " + item.getAddress().getAddress().getCanonicalHostName();
                setText(name);
            }
        }
    }

    static class MyCellFactory implements Callback<ListView<RemoteServerInfo>, ListCell<RemoteServerInfo>> {

        @Override
        public ListCell<RemoteServerInfo> call(ListView<RemoteServerInfo> p) {
            return new MyCell();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onConnect();
            }
        });
        listView.setCellFactory(new MyCellFactory());
        listView.setItems(items);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        upnp.setObserver(this);
        new Thread(upnp).start();
        update();
    }

    public void onConnect() {
        ObservableList<RemoteServerInfo> selected = listView.getSelectionModel().getSelectedItems();
        if (1 >= selected.size()) {
            final RemoteServerInfo server = selected.get(0);
            MainViewController.setViewController(new ClientViewController(server));
        }
    }

    @Override
    public void exit() {
        upnp.setObserver(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                upnp.shutdown();
            }
        }).start();
    }

    @Override
    public ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public ViewController viewFactory() {
                return new ServerSearchViewController();
            }
        };
    }

    @Override
    public void serverFound(final RemoteServerInfo server) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Remove in case of update
                remove(server);
                items.add(server);
                update();
                if (1 == items.size()) {
                    listView.getSelectionModel().select(0);
                }
            }
        });
    }

    @Override
    public void serverLost(final RemoteServerInfo server) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                remove(server);
                update();
            }
        });
    }

    private void remove(RemoteServerInfo server) {
        for (Iterator<RemoteServerInfo> i = items.iterator(); i.hasNext();) {
            if (server.getId().equals(i.next().getId())) {
                i.remove();
            }
        }
    }

    private void update() {
        if (items.isEmpty()) {
            progressIndicator.setProgress(-1.0f);
            progressIndicator.setVisible(true);
            listView.setVisible(false);
            connectButton.setVisible(false);

        } else {
            progressIndicator.setVisible(false);
            listView.setVisible(true);
            progressIndicator.setProgress(0);
            connectButton.setVisible(true);
        }
    }
}
