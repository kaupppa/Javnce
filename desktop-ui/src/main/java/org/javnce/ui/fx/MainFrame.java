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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * The Javnce Frame view class.
 */
public class MainFrame extends AnchorPane implements Initializable {

    /**
     * The fxml url.
     */
    private final static URL fxmlUrl = MainFrame.class.getResource("MainFrame.fxml");
    /**
     * The main frame.
     */
    private static MainFrame mainFrame;
    /**
     * The stack of view factories.
     */
    final private Stack<NodeFactory> stack;
    /**
     * The observers.
     */
    final private ArrayList<MainFrameObserver> observers;
    /**
     * The title text.
     */
    @FXML
    Label titleText;
    /**
     * The child.
     */
    @FXML
    AnchorPane child;
    /**
     * The back button.
     */
    @FXML
    Button backButton;

    /**
     * Instantiates a new main frame.
     */
    public MainFrame() {
        stack = new Stack<>();
        observers = new ArrayList<>();
    }

    /**
     * Adds the observer.
     *
     * @param observer the observer
     */
    public void addObserver(MainFrameObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes the observer.
     *
     * @param observer the observer
     */
    public void removeObserver(MainFrameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Gets the main frame.
     *
     * @return the main frame
     */
    static public MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Creates the MainFrame.
     *
     * @return the anchor pane
     * @throws Exception the exception
     */
    static public AnchorPane create() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        AnchorPane panel = (AnchorPane) loader.load();
        mainFrame = loader.getController();
        return panel;
    }

    /**
     * Sets the title.
     *
     * @param text the new title
     */
    public void setTitle(String text) {

        titleText.setText(text);
    }

    /**
     * Initialize.
     *
     * @param location the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateState();
    }

    /**
     * Clear current view.
     */
    private void clear() {

        child.getChildren().clear();
        updateState();
    }

    /**
     * Adds the next view.
     *
     * @param factory the factory
     */
    public void add(NodeFactory factory) {

        clear();
        insert(factory);
    }

    /**
     * Insert next view.
     *
     * @param factory the factory
     */
    private void insert(NodeFactory factory) {
        Node item = null;
        try {
            MainFrameObserver[] array = observers.toArray(new MainFrameObserver[observers.size()]);
            for (int i = 0; i < array.length; i++) {
                array[i].nextView();
            }
            item = factory.create();
        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            //TODO
            Platform.exit();
        }

        if (null != item) {
            stack.push(factory);
            insert(item);

        } else {
            backAction();
        }
    }

    /**
     * Insert the node.
     *
     * @param item the item
     */
    private void insert(Node item) {
        AnchorPane.setTopAnchor(item, 0.0);
        AnchorPane.setBottomAnchor(item, 0.0);
        AnchorPane.setLeftAnchor(item, 0.0);
        AnchorPane.setRightAnchor(item, 0.0);

        child.getChildren().add(item);
        updateState();
    }

    /**
     * Update back button.
     */
    private void updateState() {
        backButton.setVisible(1 < stack.size());
    }

    /**
     * Back button action.
     */
    public void backAction() {

        if (1 < stack.size()) {
            MainFrameObserver[] array = observers.toArray(new MainFrameObserver[observers.size()]);
            for (int i = 0; i < array.length; i++) {
                array[i].previousView();
            }
            stack.remove(stack.size() - 1);
            insert(stack.pop());
        }
    }

    /**
     * About button action.
     */
    public void aboutAction() {

        MainFrame.getMainFrame().add(
                new NodeFactory() {
                    @Override
                    public Node create() throws Exception {
                        return AboutView.create();
                    }
                });
    }
}
