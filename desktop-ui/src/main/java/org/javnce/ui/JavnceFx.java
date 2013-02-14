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

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.javnce.eventing.EventLoop;
import org.javnce.eventing.EventLoopErrorHandler;

public class JavnceFx extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        initErrorHandler();
        MainViewController.setScene(stage);
        MainViewController.setViewController(new ModeViewController());

        stage.setTitle("Javnce - Java VNC");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        MainViewController.exit();
    }

    private void initErrorHandler() {
        EventLoop.setErrorHandler(new EventLoopErrorHandler() {
            @Override
            public void fatalError(final Object object, final Throwable throwable) {
                Logger.getLogger(object.getClass().getName()).log(Level.SEVERE, null, throwable);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Platform.exit();
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}