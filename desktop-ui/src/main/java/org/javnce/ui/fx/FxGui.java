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

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.EventLoopErrorHandler;

/**
 * The Class FxGui is the main.
 */
public class FxGui extends Application {

    /**
     * Start.
     *
     * @param stage the stage
     * @throws Exception the exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        initErrorHandler();
        AnchorPane mainFrame = MainFrame.create();

        Scene scene = new Scene(mainFrame);

        stage.setScene(scene);
        stage.setTitle("Javnce - Java VNC");
        stage.getIcons().add(new Image(FxGui.class.getResourceAsStream("logo.png")));

        MainFrame.getMainFrame().add(
                new NodeFactory() {
                    @Override
                    public Node create() throws Exception {
                        return ModeSelector.create();
                    }
                });

        stage.show();
    }

    /**
     * Stop.
     */
    @Override
    public void stop() {
        EventLoop.shutdownAll();
        Platform.exit();
    }

    /**
     * Init error handler.
     */
    private void initErrorHandler() {
        EventLoop.setErrorHandler(
                new EventLoopErrorHandler() {
                    @Override
                    public void fatalError(final Object object, final Throwable throwable) {
                        //New thread to make sure that no blocking 
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Logger.getLogger(object.getClass().getName()).log(Level.SEVERE, null, throwable);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        stop();
                                    }
                                });

                            }
                        }, "Javnce-fatalError");
                        t.start();

                    }
                });
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
