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
import org.javnce.vnc.client.RemoteVncServerObserver;
import org.javnce.vnc.client.VncClientController;

/**
 * The Class ClientView is the VNC client view.
 *
 * The ClientView shows the remote frame buffer and dispatches mouse and key
 * events to server.
 */
public class ClientView extends View implements Initializable, RemoteVncServerObserver {

    /**
     * The VNC client.
     */
    final private VncClientController vncClient;
    /**
     * The remote frame buffer .
     */
    private VncImage image;
    /**
     * The mouse event dispatcher.
     */
    private VncPointerDispatcher mouseEventDispatcher;
    /**
     * The key event dispatcher.
     */
    private VncKeyDispatcher keyEventDispatcher;
    /**
     * The frame buffer full area.
     */
    private Rect rect;
    /**
     * The image view.
     */
    @FXML
    ImageView imageView;
    /**
     * The anchor pane.
     */
    @FXML
    AnchorPane anchorPane;

    /**
     * Instantiates a new client view.
     *
     * @param controller the controller
     */
    public ClientView(Controller controller) {
        super(controller);
        vncClient = new VncClientController();
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#createNode()
     */
    @Override
    public Node createNode() throws IOException {
        URL fxmlUrl = View.class.getResource("ClientView.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        loader.setController(this);
        return (Node) loader.load();
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#onExit()
     */
    @Override
    public void onExit() {
        vncClient.setObserver(null);
        vncClient.shutdown();
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.View#createFactory(org.javnce.ui.Controller)
     */
    @Override
    public ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public View viewFactory(Controller controller) {
                return null;
            }
        };
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vncClient.setObserver(this);
        vncClient.launch(getController().getConfig().getServerInfo().getAddress());
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.client.RemoteVncServerObserver#connectionClosed()
     */
    @Override
    public void connectionClosed() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getController().previousView();
                new MessageBox("Connection closed", "Connection closed", "Ok").exec();
            }
        });
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.client.RemoteVncServerObserver#initFramebuffer(org.javnce.rfb.types.PixelFormat, org.javnce.rfb.types.Size)
     */
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

    /* (non-Javadoc)
     * @see org.javnce.vnc.client.RemoteVncServerObserver#framebufferUpdate(org.javnce.rfb.types.Framebuffer[])
     */
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

    /**
     * Gets the pixel format of WritableImage.
     *
     * @return the native
     */
    private org.javnce.rfb.types.PixelFormat getNative() {

        WritableImage temp = new WritableImage(1, 1);
        PixelWriter writer = temp.getPixelWriter();
        return VncImage.convertPixelFormat(writer.getPixelFormat());
    }

    /**
     * Inits the image view.
     */
    private void initImageView() {
        resize();
        imageView.setImage(image.getImage());
    }

    /**
     * Inits the resize observers.
     */
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

    /**
     * Configures the image and dispathers.
     *
     * @param format the format
     * @param size the size
     */
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

    /**
     * Update image.
     *
     * @param buffers the buffers
     */
    private void update(Framebuffer[] buffers) {
        VncClientController.requestFramebuffer(true, rect);
        image.write(buffers);
        anchorPane.requestLayout();
    }

    /**
     * Resize image.
     */
    private void resize() {
        imageView.setFitWidth(anchorPane.getWidth());
        imageView.setFitHeight(anchorPane.getHeight());
    }
}
