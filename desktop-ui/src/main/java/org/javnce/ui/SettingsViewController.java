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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class SettingsViewController implements ViewController, Initializable {

    final static private String fullModeKey = "fullMode";
    final static private String nameKey = "name";
    final static private String defName = System.getProperty("user.name")
            + "@"
            + System.getProperty("os.name")
            + "-"
            + System.getProperty("os.arch");
    final static private ResourceBundle bundle = ResourceBundle.getBundle("org.javnce.ui.SettingsView", Locale.getDefault());
    final static private URL fxmlUrl = ViewController.class.getResource("SettingsView.fxml");
    private Node node;
    private boolean fullMode;
    @FXML
    TextField name;
    @FXML
    CheckBox fullAccess;
    @FXML
    CheckBox viewAccess;
    @FXML
    Button buttonShare;
    private String serverName;

    public SettingsViewController() {
        serverName = Settings.get().get(Settings.key(this.getClass(), nameKey), defName);
        fullMode = Settings.get().getBoolean(Settings.key(this.getClass(), fullModeKey), true);

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

    @Override
    public void exit() {
    }

    @Override
    public ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public ViewController viewFactory() {
                return new SettingsViewController();
            }
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        name.setText(serverName);
        setMode();
        viewAccess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onChecked();
            }
        });
        fullAccess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onChecked();
            }
        });
        buttonShare.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onShare();
            }
        });
    }

    public void onChecked() {
        if (fullAccess.isSelected() != fullMode) {
            fullMode = fullAccess.isSelected();
        } else if (viewAccess.isSelected() == fullMode) {
            fullMode = !viewAccess.isSelected();
        }
        setMode();
    }

    private void setMode() {
        fullAccess.setSelected(fullMode);
        viewAccess.setSelected(!fullMode);
    }

    private void onShare() {
        String temp = name.getText();
        Settings.get().putBoolean(Settings.key(this.getClass(), fullModeKey), fullMode);
        Settings.get().put(Settings.key(this.getClass(), nameKey), temp);
        MainViewController.setViewController(new ServerViewController(temp, fullMode));
    }
}
