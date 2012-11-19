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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

/**
 * The Class AboutView for showing the About.hmtl.
 */
public class AboutView extends AnchorPane implements Initializable {

    /**
     * The fxml url.
     */
    private final static URL fxmlUrl = AboutView.class.getResource("AboutView.fxml");
    /**
     * The html url.
     */
    private final static URL htmlUrl = AboutView.class.getResource("About.html");
    /**
     * The web view.
     */
    @FXML
    WebView webView;

    /**
     * Instantiates a new about view.
     */
    public AboutView() {
    }

    /**
     * Creates the AboutView.
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
        webView.getEngine().load(htmlUrl.toExternalForm());
    }
}
