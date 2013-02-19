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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * The Class SettingsView implements server settings view.
 */
public class SettingsView extends View implements Initializable {

    /**
     * The name.
     */
    @FXML
    TextField name;
    /**
     * The full access.
     */
    @FXML
    RadioButton fullAccess;
    /**
     * The view access.
     */
    @FXML
    RadioButton viewAccess;
    /**
     * The auto connect.
     */
    @FXML
    CheckBox autoConnect;

    /**
     * Instantiates a new settings view.
     *
     * @param controller the controller
     */
    public SettingsView(Controller controller) {
        super(controller);
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#initProperties(org.javnce.ui.ViewProperties)
     */
    @Override
    public void initProperties(ViewProperties properties) {
        super.initProperties(properties);
        properties.getNextDisabled().set(false);
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#createNode()
     */
    @Override
    public Node createNode() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("org.javnce.ui.SettingsView", Locale.getDefault());
        URL fxmlUrl = View.class.getResource("SettingsView.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        loader.setResources(bundle);
        loader.setController(this);
        return (Node) loader.load();
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#createFactory()
     */
    @Override
    public ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public View viewFactory(Controller controller) {
                return new SettingsView(controller);
            }
        };
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewAccess.selectedProperty().setValue(!getController().getConfig().fullAccessMode().getValue());
        fullAccess.selectedProperty().setValue(getController().getConfig().fullAccessMode().getValue());
        fullAccess.selectedProperty().bindBidirectional(getController().getConfig().fullAccessMode());
        autoConnect.selectedProperty().bindBidirectional(getController().getConfig().autoConnect());
        name.textProperty().bindBidirectional(getController().getConfig().serverName());
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#onNext()
     */
    @Override
    public void onNext() {
        getController().showView(new ServerView(getController()));
    }
}
