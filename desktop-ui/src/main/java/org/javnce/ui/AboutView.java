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
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

/**
 * The Class AboutView handles the webview in AboutView.fxml.
 *
 * The AboutView loads the About.html and handles the back and web history.
 */
public class AboutView extends View implements Initializable {

    /**
     * The web view.
     */
    @FXML
    WebView webView;

    /**
     * Instantiates a new about view.
     *
     * @param controller the controller
     */
    public AboutView(Controller controller) {
        super(controller);
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#createNode()
     */
    @Override
    public Node createNode() throws IOException {
        URL fxmlUrl = AboutView.class.getResource("AboutView.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        loader.setController(this);
        return (Node) loader.load();
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#initProperties(org.javnce.ui.ViewProperties)
     */
    @Override
    public void initProperties(ViewProperties properties) {
        super.initProperties(properties);
        properties.getHelpDisabled().set(true);
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#createFactory(org.javnce.ui.Controller)
     */
    @Override
    public ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public View viewFactory(Controller controller) {
                return new AboutView(controller);
            }
        };
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        URL htmlUrl = AboutView.class.getResource("About.html");
        webView.getEngine().load(htmlUrl.toExternalForm());
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#onBack()
     */
    @Override
    public void onBack() {
        boolean babkHandled = false;
        if (null != webView) {
            WebHistory history = webView.getEngine().getHistory();
            int index = history.getCurrentIndex();
            if (0 < index) {
                history.go(-1);
                babkHandled = true;
            }
        }
        if (!babkHandled) {
            super.onBack();
        }
    }
}
