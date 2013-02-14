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
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.PixelFormat;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;
import org.javnce.upnp.RemoteServerInfo;
import org.javnce.vnc.client.RemoteVncServerObserver;
import org.javnce.vnc.client.VncClientController;

public class ClientViewController implements ViewController, Initializable, RemoteVncServerObserver {

    final static private URL fxmlUrl = ViewController.class.getResource("ClientView.fxml");
    private Node node;
    final private RemoteServerInfo serverInfo;
    final private VncClientController controller;
    private VncImage image;
    private VncPointerDispatcher mouseEventDispatcher;
    private VncKeyDispatcher keyEventDispatcher;
    private Rect rect;
    @FXML
    ImageView imageView;
    @FXML
    AnchorPane anchorPane;

    public ClientViewController(RemoteServerInfo serverInfo) {
        controller = new VncClientController();
        this.serverInfo = serverInfo;
    }

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
        controller.setObserver(null);
        controller.shutdown();
    }

    @Override
    public ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public ViewController viewFactory() {
                return new ClientViewController(serverInfo);
            }
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller.setObserver(this);
        controller.launch(serverInfo.getAddress());
    }

    @Override
    public void connectionClosed() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                exit();
                MainViewController.previous();
                new MessageBox("Connection closed", "Connection closed", "Ok").exec();
            }
        });

    }

    @Override
    public void initFramebuffer(PixelFormat format, Size size) {
        final org.javnce.rfb.types.PixelFormat newformat = format;
        final Size newsize = size;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                config(newformat, newsize);
            }
        });

    }

    @Override
    public void framebufferUpdate(Framebuffer[] buffers) {
        final Framebuffer[] newbuffers = buffers;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                update(newbuffers);
            }
        });

    }

    private org.javnce.rfb.types.PixelFormat getNative() {

        WritableImage temp = new WritableImage(1, 1);
        PixelWriter writer = temp.getPixelWriter();
        return VncImage.convertPixelFormat(writer.getPixelFormat());
    }

    private void initImageView() {
        resize();
        imageView.setImage(image.getImage());
    }

    private void initResizeObservers() {
        resize();
        final ChangeListener<Number> listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                resize();
            }
        };
        anchorPane.widthProperty().addListener(listener);
        anchorPane.heightProperty().addListener(listener);
    }

    private void config(org.javnce.rfb.types.PixelFormat format, Size size) {

        org.javnce.rfb.types.PixelFormat nativeFormat = getNative();

        if (null == nativeFormat) {
            //Cannot handle the format
            Platform.exit();
        } else if (!nativeFormat.equals(format)) {
            VncClientController.setFormat(nativeFormat);
        }

        rect = new Rect(0, 0, size.width(), size.height());
        image = new VncImage(nativeFormat, size);

        initImageView();
        initResizeObservers();
        VncClientController.requestFramebuffer(false, rect);
        mouseEventDispatcher = new VncPointerDispatcher(size.width(), size.height());
        mouseEventDispatcher.register(imageView);

        keyEventDispatcher = new VncKeyDispatcher();
        keyEventDispatcher.register(imageView);
        imageView.requestFocus();
    }

    private void update(Framebuffer[] buffers) {
        VncClientController.requestFramebuffer(true, rect);
        image.write(buffers);
        anchorPane.requestLayout();
    }

    private void resize() {
        imageView.setFitWidth(anchorPane.getWidth());
        imageView.setFitHeight(anchorPane.getHeight());
    }
}
