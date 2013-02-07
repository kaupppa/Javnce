/*
 * Copyright (C) 2012 Pauli Kauppinen
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
package example.swingclient;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.event.MouseInputListener;
import org.javnce.vnc.client.VncClientController;

/**
 * The class for dispatching mouse events to server.
 */
public class VncPointerDispatcher implements MouseInputListener, MouseWheelListener, MouseMotionListener {

    /**
     * The component.
     */
    private Component component;
    /**
     * The width of VNC image.
     */
    final private double width;
    /**
     * The height of VNC image.
     */
    final private double height;
    /**
     * The pointer mask.
     */
    private int mask;

    /**
     * Instantiates a new vnc pointer dispatcher.
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
     * Register swing component.
     *
     * @param componentToBeWatched the component to be watched
     */
    public void register(Component componentToBeWatched) {
        component = componentToBeWatched;

        component.addMouseMotionListener(this);
        component.addMouseListener(this);
        component.addMouseWheelListener(this);
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent event) {
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent event) {
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent event) {
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent event) {
        updateMask(event);
        dispatch(mask, event.getX(), event.getY());
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent event) {
        updateMask(event);
        dispatch(mask, event.getX(), event.getY());
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        updateMask(event);
        dispatch(mask, event.getX(), event.getY());
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(MouseEvent event) {
        updateMask(event);
        dispatch(mask, event.getX(), event.getY());
    }

    /**
     * Update mask.
     *
     * @param event the event
     */
    private void updateMask(MouseEvent event) {
        int modifier = event.getModifiersEx();

        updateMask(1, (0 != (InputEvent.BUTTON1_DOWN_MASK & modifier)));
        updateMask(2, (0 != (InputEvent.BUTTON2_DOWN_MASK & modifier)));
        updateMask(3, (0 != (InputEvent.BUTTON3_DOWN_MASK & modifier)));
    }

    /**
     * Update mask.
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
     * Dispatch.
     *
     * @param mouseMask the mouse mask
     * @param mouse_x the mouse_x
     * @param mouse_y the mouse_y
     */
    private void dispatch(int mouseMask, double mouse_x, double mouse_y) {
        int x = getX(mouse_x);
        int y = getY(mouse_y);

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
     * Gets the x in VNC coordinates.
     *
     * @param x the x
     * @return the x
     */
    private int getX(double x) {
        double scale = width / component.getWidth();

        double scaled_x = scale * x;
        return (int) scaled_x;
    }

    /**
     * Gets the y in VNC coordinates.
     *
     * @param y the y
     * @return the y
     */
    private int getY(double y) {
        double scale = height / component.getHeight();

        double scaled_y = scale * y;
        return (int) scaled_y;
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        int value = event.getWheelRotation();

        int newmask = mask;

        if (value < 0) {
            newmask |= 1 << 3;
        } else if (value > 0) {
            newmask |= 1 << 4;
        }

        int x = event.getX();
        int y = event.getY();
        if (newmask != mask) {
            dispatch(newmask, x, y);
            dispatch(mask, x, y);
        }
    }
}
