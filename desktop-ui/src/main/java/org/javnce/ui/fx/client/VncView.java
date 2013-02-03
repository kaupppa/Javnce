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
package org.javnce.ui.fx.client;

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
import javafx.util.Callback;
import org.javnce.rfb.types.Framebuffer;
import org.javnce.rfb.types.Rect;
import org.javnce.rfb.types.Size;
import org.javnce.ui.fx.MainFrame;
import org.javnce.ui.fx.MainFrameObserver;
import org.javnce.ui.fx.MessageBox;
import org.javnce.ui.model.ClientConfiguration;
import org.javnce.upnp.client.RemoteServerInfo;
import org.javnce.vnc.client.RemoteVncServerObserver;
import org.javnce.vnc.client.VncClientController;

/**
 * The VNC client view.
 */
public class VncView extends AnchorPane implements Initializable, RemoteVncServerObserver, MainFrameObserver {

    /**
     * The fxml url.
     */
    private final static URL fxmlUrl = VncView.class.getResource("VncView.fxml");
    /**
     * The server info.
     */
    final private RemoteServerInfo serverInfo;
    /**
     * The controller.
     */
    final private VncClientController controller;
    /**
     * The image.
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
     * The VNC image full area.
     */
    private Rect rect;
    /**
     * The running.
     */
    volatile private boolean running;
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
     * Instantiates a new view.
     *
     * @param serverInfo the server info
     */
    public VncView(RemoteServerInfo serverInfo) {
        controller = ClientConfiguration.instance().getVncController();
        this.serverInfo = serverInfo;
        running = true;
    }

    /**
     * Creates the VncView.
     *
     * @param server the server
     * @return the node
     * @throws Exception the exception
     */
    public static Node create(final RemoteServerInfo server) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> paramClass) {
                return new VncView(server);
            }
        });
        return (AnchorPane) loader.load();
    }

    /**
     * Initialize.
     *
     * @param location the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainFrame.getMainFrame().setTitle("Connecting to " + serverInfo.getName());
        MainFrame.getMainFrame().addObserver(this);
        controller.start(serverInfo.getAddress(), this);
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.client.VncClientObserver#connectionClosed()
     */
    @Override
    public void connectionClosed() {
        if (running) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    shutdown();
                    new MessageBox("Connection closed", "Connection closed", "Ok").exec();
                    MainFrame.getMainFrame().backAction();
                }
            });
        }
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.client.VncClientObserver#initFramebuffer(org.javnce.rfb.types.PixelFormat, org.javnce.rfb.types.Size)
     */
    @Override
    public void initFramebuffer(org.javnce.rfb.types.PixelFormat format, Size size) {
        final org.javnce.rfb.types.PixelFormat newformat = format;
        final Size newsize = size;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                config(newformat, newsize);
            }
        });
    }

    /**
     * Gets the native image format.
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
     * Config.
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
            controller.setFormat(nativeFormat);
        }

        rect = new Rect(0, 0, size.width(), size.height());
        image = new VncImage(nativeFormat, size);

        initImageView();
        initResizeObservers();
        controller.requestFramebuffer(false, rect);
        mouseEventDispatcher = new VncPointerDispatcher(size.width(), size.height());
        mouseEventDispatcher.register(imageView);

        keyEventDispatcher = new VncKeyDispatcher();
        keyEventDispatcher.register(imageView);
        imageView.requestFocus();
    }

    /* (non-Javadoc)
     * @see org.javnce.vnc.client.VncClientObserver#framebufferUpdate(org.javnce.rfb.types.Framebuffer[])
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
     * Update image.
     *
     * @param buffers the buffers
     */
    private void update(Framebuffer[] buffers) {
        image.write(buffers);
        controller.requestFramebuffer(true, rect);
        anchorPane.requestLayout();
    }

    /**
     * Resize image view.
     */
    private void resize() {
        //FIXME Javafx v.2.2 has major issues with scaling quality

        imageView.setFitWidth(anchorPane.getWidth());
        imageView.setFitHeight(anchorPane.getHeight());
    }

    /**
     * Shutdown the view.
     */
    private void shutdown() {
        running = false;
        controller.shutdown();
        MainFrame.getMainFrame().removeObserver(this);
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.fx.MainFrameObserver#previousView()
     */
    @Override
    public void previousView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        });
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.fx.MainFrameObserver#nextView()
     */
    @Override
    public void nextView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        });
    }
}
