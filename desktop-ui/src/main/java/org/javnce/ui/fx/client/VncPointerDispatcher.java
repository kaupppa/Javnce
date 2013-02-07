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

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.javnce.vnc.client.VncClientController;

/**
 * The class for dispatch pointer events to server.
 */
public class VncPointerDispatcher {

    /**
     * The VNC image width.
     */
    final private double width;
    /**
     * The VNC image height.
     */
    final private double height;
    /**
     * The node.
     */
    private Node node;
    /**
     * The pointer mask.
     */
    private int mask;

    /**
     * Instantiates a new pointer dispatcher.
     *
     * @param width the width
     * @param height the height
     */
    public VncPointerDispatcher(int width, int height) {
        this.width = width;
        this.height = height;
        mask = 0;
    }

    /**
     * Register view for listening mouse events.
     *
     * @param nodeToBeWatched the node to be watched
     */
    public void register(Node nodeToBeWatched) {
        node = nodeToBeWatched;
        final EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mouseEvent(t);
            }
        };
        node.setOnMouseMoved(handler);
        node.setOnMousePressed(handler);
        node.setOnMouseReleased(handler);
        node.setOnMouseDragged(handler);

        node.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent t) {
                        scrollEvent(t);
                    }
                });
    }

    /**
     * Mouse event handler.
     *
     * @param event the event
     */
    private void mouseEvent(MouseEvent event) {

        updateMask(1, event.isPrimaryButtonDown());
        updateMask(3, event.isSecondaryButtonDown());
        updateMask(2, event.isMiddleButtonDown());
        dispatch(mask, event.getX(), event.getY());
    }

    /**
     * Pointer mask handler.
     *
     * @param button the button
     * @param down the down
     */
    private void updateMask(int button, boolean down) {

        if (down) {
            mask |= 1 << (button - 1);
        } else {
            mask &= ~(1 << (button - 1));
        }
    }

    /**
     * Scroll event handler.
     *
     * @param event the event
     */
    private void scrollEvent(ScrollEvent event) {
        int newmask = mask;
        double x = event.getDeltaX();
        double y = event.getDeltaY();

        if (x > 0 || y > 0) {
            newmask |= 1 << 3;
        } else if (x < 0 || y < 0) {
            newmask |= 1 << 4;
        }

        if (newmask != mask) {
            dispatch(newmask, event.getX(), event.getY());
            dispatch(mask, event.getX(), event.getY());
        }
    }

    /**
     * Dispatch pointer event.
     *
     * @param mouseMask the mouse mask
     * @param node_x the node_x
     * @param node_y the node_y
     */
    private void dispatch(int mouseMask, double node_x, double node_y) {
        Bounds bounds = node.getBoundsInLocal();
        int x = getX(node_x, bounds.getWidth());
        int y = getY(node_y, bounds.getHeight());


        if (0 > x || x > width) {
            return; //Out of area, nothing is dispatched
        }

        if (0 > y || y > height) {
            return; //Out of area, nothing is dispatched
        }
        //Logger.getLogger(MouseEventDispatcher.class.getName()).info("mouse mask=" + mouseMask + " x="+x+" y="+y);
        VncClientController.pointerEvent(mouseMask, x, y);
    }

    /**
     * Scale x to VNC image coordinate.
     *
     * @param x the x
     * @param nodeWidth the node width
     * @return the x
     */
    private int getX(double x, double nodeWidth) {
        double scale = width / nodeWidth;

        double scaled_x = scale * x;
        return (int) scaled_x;
    }

    /**
     * Scale y to VNC image coordinate.
     *
     * @param y the y
     * @param nodeHeight the node height
     * @return the y
     */
    private int getY(double y, double nodeHeight) {
        double scale = height / nodeHeight;

        double scaled_y = scale * y;
        return (int) scaled_y;
    }
}
