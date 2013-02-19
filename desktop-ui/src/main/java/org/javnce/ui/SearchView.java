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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;
import org.javnce.upnp.RemoteServerInfo;
import org.javnce.upnp.UpnpClient;
import org.javnce.upnp.UpnpClientObserver;

/**
 * The Class SearchView implements search and selection of servers view.
 */
public class SearchView extends View implements Initializable, UpnpClientObserver {

    /** The items. */
    final private ObservableList<RemoteServerInfo> items;
    
    /** The upnp. */
    final private UpnpClient upnp;
    
    /** The bundle. */
    final private ResourceBundle bundle;
    
    /** The progress indicator. */
    @FXML
    ProgressIndicator progressIndicator;
    
    /** The list view. */
    @FXML
    ListView<RemoteServerInfo> listView;
    
    /** The label. */
    @FXML
    Label label;

    /**
     * Instantiates a new search view.
     *
     * @param controller the controller
     */
    public SearchView(Controller controller) {
        super(controller);
        items = FXCollections.observableArrayList();
        upnp = new UpnpClient();
        bundle = ResourceBundle.getBundle("org.javnce.ui.ServerSearchView", Locale.getDefault());
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#createNode()
     */
    @Override
    public Node createNode() throws IOException {
        URL fxmlUrl = View.class.getResource("ServerSearchView.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        loader.setResources(bundle);
        loader.setController(this);
        return (Node) loader.load();
    }

    /**
     * The Class MyCell.
     */
    static class MyCell extends ListCell<RemoteServerInfo> {

        /* (non-Javadoc)
         * @see javafx.scene.control.Cell#updateItem(java.lang.Object, boolean)
         */
        @Override
        protected void updateItem(RemoteServerInfo item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                String name = item.getName() + " at " + item.getAddress().getAddress().getCanonicalHostName();
                setText(name);
            }
        }
    }

    /**
     * A factory for creating MyCell objects.
     */
    static class MyCellFactory implements Callback<ListView<RemoteServerInfo>, ListCell<RemoteServerInfo>> {

        /* (non-Javadoc)
         * @see javafx.util.Callback#call(java.lang.Object)
         */
        @Override
        public ListCell<RemoteServerInfo> call(ListView<RemoteServerInfo> p) {
            return new MyCell();
        }
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listView.setCellFactory(new MyCellFactory());
        listView.setItems(items);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //Call update when items changes
        items.addListener(new ListChangeListener<RemoteServerInfo>() {
            @Override
            public void onChanged(Change<? extends RemoteServerInfo> change) {
                update();
            }
        });
        //Call update when listView selection changes
        listView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<RemoteServerInfo>() {
            @Override
            public void changed(ObservableValue<? extends RemoteServerInfo> ov, RemoteServerInfo t, RemoteServerInfo t1) {
                update();
            }
        });
        upnp.setObserver(this);
        new Thread(upnp).start();
        update();
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#onNext()
     */
    @Override
    public void onNext() {
        ObservableList<RemoteServerInfo> selected = listView.getSelectionModel().getSelectedItems();
        if (1 >= selected.size()) {
            final RemoteServerInfo server = selected.get(0);
            getController().getConfig().setServerInfo(server);
            getController().showView(new ClientView(getController()));
        }
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#onExit()
     */
    @Override
    public void onExit() {
        upnp.setObserver(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                upnp.shutdown();
            }
        }).start();
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#createFactory()
     */
    @Override
    public ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public View viewFactory(Controller controller) {
                return new SearchView(controller);
            }
        };
    }

    /* (non-Javadoc)
     * @see org.javnce.upnp.UpnpClientObserver#serverFound(org.javnce.upnp.RemoteServerInfo)
     */
    @Override
    public void serverFound(final RemoteServerInfo server) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Remove in case of update
                remove(server);
                items.add(server);
            }
        });
    }

    /* (non-Javadoc)
     * @see org.javnce.upnp.UpnpClientObserver#serverLost(org.javnce.upnp.RemoteServerInfo)
     */
    @Override
    public void serverLost(final RemoteServerInfo server) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                remove(server);
            }
        });
    }

    /**
     * Removes the.
     *
     * @param server the server
     */
    private void remove(RemoteServerInfo server) {
        for (Iterator<RemoteServerInfo> i = items.iterator(); i.hasNext();) {
            if (server.getId().equals(i.next().getId())) {
                i.remove();
            }
        }
    }

    /**
     * Update.
     */
    private void update() {
        if (items.isEmpty()) {
            //Bug in ListView, selection not cleared if empty ?
            listView.getSelectionModel().clearSelection();
            progressIndicator.setProgress(-1.0f);
            progressIndicator.setVisible(true);
            listView.setVisible(false);
            label.setText(bundle.getString("searching.title.text"));
            getProperties().getNextDisabled().set(true);

        } else {
            progressIndicator.setVisible(false);
            listView.setVisible(true);
            progressIndicator.setProgress(0);
            label.setText(bundle.getString("select.title.text"));

            ObservableList<RemoteServerInfo> selected = listView.getSelectionModel().getSelectedItems();
            if (1 <= selected.size()) {
                getProperties().getNextDisabled().set(false);
            } else {
                getProperties().getNextDisabled().set(true);
            }
        }
    }
}
