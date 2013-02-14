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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.web.WebView;

public class AboutViewController implements ViewController, Initializable {

    private final static URL fxmlUrl = AboutViewController.class.getResource("AboutView.fxml");
    private final static URL htmlUrl = AboutViewController.class.getResource("About.html");
    private Node node;
    @FXML
    WebView webView;

    @Override
    public Node getNode() throws IOException {
        if (null == node) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(fxmlUrl);
            loader.setController(this);
            node = (Node) loader.load();
        }
        return node;
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
                return new AboutViewController();
            }
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webView.getEngine().load(htmlUrl.toExternalForm());
    }
}
