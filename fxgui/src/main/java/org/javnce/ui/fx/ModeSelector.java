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
package org.javnce.ui.fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.javnce.ui.fx.client.SelectServer;
import org.javnce.ui.fx.server.ServerSettings;

/**
 * The view to select Server or Client mode.
 */
public class ModeSelector extends AnchorPane implements Initializable {

    /**
     * The fxml url.
     */
    private final static URL fxmlUrl = ModeSelector.class.getResource("ModeSelector.fxml");

    /**
     * Creates the ModeSelector.
     *
     * @return the node
     * @throws Exception the exception
     */
    static Node create() throws Exception {
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
        MainFrame.getMainFrame().setTitle("Welcome");
    }

    /**
     * Creates the server view.
     *
     * @param event the event
     * @throws Exception the exception
     */
    public void createServer(ActionEvent event) throws Exception {
        MainFrame.getMainFrame().add(
                new NodeFactory() {
                    @Override
                    public Node create() throws Exception {
                        return ServerSettings.create();
                    }
                });
    }

    /**
     * Creates the client view.
     *
     * @param event the event
     * @throws Exception the exception
     */
    public void createClient(ActionEvent event) throws Exception {
        MainFrame.getMainFrame().add(
                new NodeFactory() {
                    @Override
                    public Node create() throws Exception {
                        return SelectServer.create();
                    }
                });

    }
}
