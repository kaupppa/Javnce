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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.javnce.ui.fx.MainFrame;
import org.javnce.ui.fx.NodeFactory;

/**
 * The server settings view.
 */
public class ServerSettings extends AnchorPane implements Initializable {

    /**
     * The fxml url.
     */
    private final static URL fxmlUrl = ServerSettings.class.getResource("ServerSettings.fxml");
    /**
     * The full mode.
     */
    private boolean fullMode;
    /**
     * The name.
     */
    @FXML
    TextField name;
    /**
     * The full access.
     */
    @FXML
    CheckBox fullAccess;
    /**
     * The view access.
     */
    @FXML
    CheckBox viewAccess;

    /**
     * Creates the ServerSettings.
     *
     * @return the node
     * @throws Exception the exception
     */
    public static Node create() throws Exception {
        return FXMLLoader.load(fxmlUrl);
    }

    /**
     * Instantiates a new server settings.
     */
    public ServerSettings() {
        fullMode = true;
    }

    /**
     * Initialize.
     *
     * @param url the url
     * @param rb the rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainFrame.getMainFrame().setTitle("Server settings");
        name.setText(System.getProperty("user.name")
                + "@"
                + System.getProperty("os.name")
                + "-"
                + System.getProperty("os.arch"));
        setMode();
    }

    /**
     * User changed the mode.
     */
    public void changed() {
        if (fullAccess.isSelected() != fullMode) {
            fullMode = fullAccess.isSelected();
        } else if (viewAccess.isSelected() == fullMode) {
            fullMode = !viewAccess.isSelected();
        }
        setMode();
    }

    /**
     * Sets the mode.
     */
    private void setMode() {
        fullAccess.setSelected(fullMode);
        viewAccess.setSelected(!fullMode);
    }

    /**
     * Start VNC server.
     *
     * @throws Exception the exception
     */
    public void start() throws Exception {
        MainFrame.getMainFrame().add(
                new NodeFactory() {
                    @Override
                    public Node create() throws Exception {
                        return ConnectedVncClients.create(name.getText(), fullMode);
                    }
                });


    }
}
