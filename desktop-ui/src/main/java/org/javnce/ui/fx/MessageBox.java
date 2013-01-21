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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * The Javafx v.2.2 does not have MessageBox so this is a workaround.
 */
public class MessageBox extends AnchorPane implements Initializable {

    /**
     * The fxml url.
     */
    private final static URL fxmlUrl = AboutView.class.getResource("MessageBox.fxml");
    /**
     * The stage.
     */
    final private Stage stage;
    /**
     * The title.
     */
    final private String title;
    /**
     * The message.
     */
    final private String message;
    /**
     * The button labels.
     */
    final private String[] labels;
    /**
     * The selected button label.
     */
    private String selectedButtonLabel;
    @FXML
    Label label;
    @FXML
    HBox hBox;

    /**
     * Instantiates a new message box.
     *
     * @param title the title
     * @param message the message
     * @param labels the labels
     */
    public MessageBox(String title, String message, String... labels) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        this.title = title;
        this.message = message;
        this.labels = labels;
        selectedButtonLabel = "";

    }

    /**
     * Inits the stage.
     */
    private void initStage() {
        final Object controller = this;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        loader.setControllerFactory(new Callback() {
            @Override
            public Object call(Object p) {
                return controller;
            }
        });

        try {
            AnchorPane panel = (AnchorPane) loader.load();
            stage.setScene(new Scene(panel));
        } catch (IOException ex) {
            Logger.getLogger(MessageBox.class.getName()).log(Level.SEVERE, null, ex);
            Platform.exit();
        }
        stage.setTitle(title);
    }

    /**
     * Show and wait.
     *
     * @return the pushed button label or empty.
     */
    public String exec() {
        initStage();
        stage.showAndWait();
        return selectedButtonLabel;
    }

    /**
     * Adds the buttons.
     */
    private void addButtons() {
        hBox.getChildren().clear();
        for (String name : labels) {
            Button button = new Button(name);
            button.setAlignment(Pos.CENTER);
            hBox.getChildren().add(button);

            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    selectedButtonLabel = ((Button) event.getSource()).getText();
                    stage.close();
                }
            });
        }
    }

    /**
     * Initialize.
     *
     * @param url the url
     * @param rb the rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setText(message);
        addButtons();
    }
}
