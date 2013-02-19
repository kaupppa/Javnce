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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * The Class MainView implements the View framework with a "toolbar".
 *
 */
public class MainView implements Initializable {

    /**
     * The current view.
     */
    private View currentView;
    /**
     * The properties.
     */
    final private ViewProperties properties;
    /**
     * The config.
     */
    final private Config config;
    /**
     * The back button.
     */
    @FXML
    private Button backButton;
    /**
     * The next button.
     */
    @FXML
    private Button nextButton;
    /**
     * The fullscreen button.
     */
    @FXML
    private Button fullscreenButton;
    /**
     * The help button.
     */
    @FXML
    private Button helpButton;
    /**
     * The child.
     */
    @FXML
    private AnchorPane child;
    /**
     * The toolbar.
     */
    @FXML
    private AnchorPane toolbar;
    /**
     * The root.
     */
    @FXML
    private AnchorPane root;
    /**
     * The layout.
     */
    @FXML
    private VBox layout;

    /**
     * Instantiates a new main view.
     *
     * @param config the config
     */
    public MainView(Config config) {
        this.config = config;
        properties = new ViewProperties();
    }

    /**
     * Removes the current view.
     */
    private void removeCurrentView() {
        if (null != currentView) {
            currentView.onExit();
        }
        currentView = null;
    }

    /**
     * Sets a view.
     *
     * The previous is removed, if any.
     *
     * @param view the new view
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setView(View view) throws IOException {
        removeCurrentView();
        currentView = view;
        currentView.initProperties(properties);
        setCurrentView();
    }
    /**
     * Adds current view.
     *
     */
    private void setCurrentView() throws IOException {
        if (null != child && null != currentView) {
            Node childNode = currentView.createNode();
            child.getChildren().clear();
            AnchorPane.setTopAnchor(childNode, 0.0);
            AnchorPane.setBottomAnchor(childNode, 0.0);
            AnchorPane.setLeftAnchor(childNode, 0.0);
            AnchorPane.setRightAnchor(childNode, 0.0);
            child.getChildren().add(childNode);
        }
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            setCurrentView();
            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (null != currentView) {
                        currentView.onBack();
                    }
                }
            });
            nextButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (null != currentView) {
                        currentView.onNext();
                    }
                }
            });

            fullscreenButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (null != currentView) {
                        currentView.onFullScreen();
                    }
                }
            });
            helpButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (null != currentView) {
                        currentView.onHelp();
                    }
                }
            });
            bindFullscreen();
            bindSize();
            bindProperties();
            properties.getInitDone().set(true);
        } catch (IOException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            Platform.exit();
        }
    }

    /**
     * Bind properties with buttons.
     */
    private void bindProperties() {
        backButton.disableProperty().bindBidirectional(properties.getBackDisabled());
        nextButton.disableProperty().bindBidirectional(properties.getNextDisabled());
        fullscreenButton.disableProperty().bindBidirectional(properties.getFullscreenDisabled());
        helpButton.disableProperty().bindBidirectional(properties.getHelpDisabled());
    }

    /**
     * Bind size root node.
     */
    private void bindSize() {
        SimpleDoubleProperty width = config.width();
        if (0 != width.get()) {
            root.setPrefWidth(width.get());
        }
        width.bind(root.widthProperty());

        SimpleDoubleProperty height = config.height();
        if (0 != height.get()) {
            root.setPrefHeight(height.get());
        }
        height.bind(root.heightProperty());
    }

    /**
     * Gets the root node.
     *
     * @return the node
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public Node getNode() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("org.javnce.ui.MainView", Locale.getDefault());
        URL fxmlUrl = View.class.getResource("MainView.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        loader.setResources(bundle);
        loader.setController(this);
        return (Node) loader.load();
    }

    /**
     * Bind fullscreen property.
     */
    private void bindFullscreen() {
        properties.getFullScreen().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    layout.getChildren().remove(0);
                } else {
                    layout.getChildren().add(0, toolbar);
                }
            }
        });
    }

    /**
     * Exit.
     */
    public void exit() {
        removeCurrentView();
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public ViewProperties getProperties() {
        return properties;
    }
}
