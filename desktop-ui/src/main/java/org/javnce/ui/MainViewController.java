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
import java.util.Stack;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.javnce.eventing.EventLoop;

public class MainViewController implements Initializable {

    private static MainViewController INSTANCE = new MainViewController();
    private Stage stage;
    final private Stack<ViewFactory> viewSatck;
    private ViewController childController;
    @FXML
    private Button backButton;
    @FXML
    private Button fullscreenButton;
    @FXML
    private Button helpButton;
    @FXML
    private AnchorPane child;
    @FXML
    private AnchorPane toolbar;
    @FXML
    private AnchorPane root;
    @FXML
    private VBox layout;

    private static MainViewController getInstance() {
        return INSTANCE;
    }

    private MainViewController() {
        viewSatck = new Stack<>();
    }

    public static void setScene(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(ResourceBundle.getBundle("org.javnce.ui.MainView", Locale.getDefault()));

        loader.setLocation(MainViewController.class.getResource("MainView.fxml"));
        loader.setController(getInstance());
        getInstance().setStage(stage);
        stage.setScene(new Scene((Parent) loader.load()));
    }

    private Stage getStage() {
        return stage;
    }

    private void setStage(Stage stage) {
        this.stage = stage;
    }

    private void onPreviousView() {

        if (!viewSatck.isEmpty()) {
            ViewFactory factory = viewSatck.pop();
            setController(factory.viewFactory());
            update();
        }
    }

    private void onfullScreen() {
        getStage().setFullScreen(true);
    }

    private void onHelp() {
        if (null == childController || !(childController instanceof AboutViewController)) {
            addController(new AboutViewController());
        }
    }

    public static void setViewController(ViewController viewController) {
        INSTANCE.addController(viewController);
    }

    public static void previous() {
        INSTANCE.onPreviousView();
    }

    private void addController(ViewController viewController) {

        ViewController previous = childController;
        if (setController(viewController) && null != previous) {
            viewSatck.push(previous.createFactory());
        }
        update();
    }

    private boolean setController(ViewController viewController) {

        boolean added = false;
        Node node = null;

        if (null != viewController) {
            try {
                node = viewController.getNode();
            } catch (IOException ex) {
                EventLoop.fatalError(this, ex);
            }
        }
        if (null != node) {
            clearCurrentController();

            childController = viewController;
            setView(node);
            added = true;
        }
        return added;
    }

    private void clearCurrentController() {

        if (null != childController) {
            childController.exit();
            childController = null;
        }
    }

    private void setView(Node node) {

        if (null != node) {
            child.getChildren().clear();
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            child.getChildren().add(node);
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onPreviousView();
            }
        });
        fullscreenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onfullScreen();
            }
        });
        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onHelp();
            }
        });

        final ChangeListener<Boolean> listener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                fullScreenChanged(newValue);
            }
        };

        double width = Settings.get().getDouble(Settings.key(this.getClass(), "width"), root.getWidth());
        root.setPrefWidth(width);
        double height = Settings.get().getDouble(Settings.key(this.getClass(), "height"), root.getHeight());
        root.setPrefHeight(height);

        getStage().fullScreenProperty().addListener(listener);
    }

    private void fullScreenChanged(Boolean mode) {
        if (mode) {
            layout.getChildren().remove(0);
        } else {
            layout.getChildren().add(0, toolbar);
        }
    }

    private void update() {
        backButton.setDisable(viewSatck.isEmpty());

        if (null != childController && childController instanceof AboutViewController) {
            helpButton.setDisable(true);
        } else {
            helpButton.setDisable(false);
        }
    }

    public static void exit() {
        INSTANCE.close();
    }

    private void close() {
        if (null != root) {
            Settings.get().putDouble(Settings.key(this.getClass(), "width"), root.getWidth());
            Settings.get().putDouble(Settings.key(this.getClass(), "height"), root.getHeight());
        }
        clearCurrentController();
    }
}
