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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * The Class ModeView implements the server/client selection view.
 */
public class ModeView extends View implements Initializable {

    /**
     * The share button.
     */
    @FXML
    private Button shareButton;
    /**
     * The connect button.
     */
    @FXML
    private Button connectButton;

    /**
     * Instantiates a new mode view.
     *
     * @param controller the controller
     */
    ModeView(Controller controller) {
        super(controller);
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#initProperties(org.javnce.ui.ViewProperties)
     */
    @Override
    public void initProperties(ViewProperties properties) {
        super.initProperties(properties);
        properties.getBackDisabled().set(true);
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#createNode()
     */
    @Override
    public Node createNode() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("org.javnce.ui.ModeView", Locale.getDefault());
        URL fxmlUrl = ViewController.class.getResource("ModeView.fxml");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        loader.setResources(bundle);
        loader.setController(this);
        return (Node) loader.load();
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        shareButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onShare();
            }
        });
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onConnect();
            }
        });
    }

    /**
     * On client mode selected.
     */
    public void onConnect() {
        getController().showView(new SearchView(getController()));
    }

    /**
     * On server mode selected.
     */
    public void onShare() {
        getController().showView(new SettingsView(getController()));
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#createFactory()
     */
    @Override
    public ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public View viewFactory(Controller controller) {
                return new ModeView(controller);
            }
        };
    }
}
