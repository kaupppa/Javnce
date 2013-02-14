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

public class ModeViewController implements ViewController, Initializable {

    final static private ResourceBundle bundle = ResourceBundle.getBundle("org.javnce.ui.ModeView", Locale.getDefault());
    final static private URL fxmlUrl = MainViewController.class.getResource("ModeView.fxml");
    private Node node;
    @FXML
    private Button shareButton;
    @FXML
    private Button connectButton;

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

    public void onConnect() {
        MainViewController.setViewController(new ServerSearchViewController());
    }

    public void onShare() {
        MainViewController.setViewController(new SettingsViewController());
    }

    @Override
    public void exit() {
        //Nothing to do
    }

    @Override
    public ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public ViewController viewFactory() {
                return new ModeViewController();
            }
        };
    }
}
