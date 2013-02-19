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
import java.util.Stack;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import org.javnce.eventing.EventLoop;

/**
 * The Class ViewController handles View in MainView.
 *
 * The ViewController has stack of ViewFactory to support back.
 */
public class ViewController implements Controller {

    /**
     * The view satck.
     */
    final private Stack<ViewFactory> viewSatck;
    /**
     * The current view.
     */
    private View currentView;
    /**
     * The main view.
     */
    final private MainView mainView;
    /**
     * The config.
     */
    final private Config config;

    /**
     * Instantiates a new view controller.
     */
    public ViewController() {
        viewSatck = new Stack<>();
        config = new Config();
        mainView = new MainView(config);
        init();
    }

    /**
     * Inits the.
     */
    private void init() {
        final ViewController controller = this;
        mainView.getProperties().getInitDone().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    setView(new ModeView(controller));
                }
            }
        });
    }

    /**
     * Sets the view.
     *
     * @param view the new view
     */
    private void setView(View view) {
        if (null != view) {
            try {
                currentView = view;
                mainView.setView(view);
            } catch (IOException ex) {
                EventLoop.fatalError(this, ex);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.Controller#showView(org.javnce.ui.View)
     */
    @Override
    public void showView(View view) {

        if (null != currentView) {
            viewSatck.push(currentView.createFactory());
        }
        setView(view);
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.Controller#previousView()
     */
    @Override
    public void previousView() {
        if (!viewSatck.isEmpty()) {
            ViewFactory factory = viewSatck.pop();
            View view = factory.viewFactory(this);
            if (null != view) {
                setView(view);
            } else {
                previousView();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.Controller#exit()
     */
    @Override
    public void exit() {
        mainView.exit();
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.Controller#getConfig()
     */
    @Override
    public Config getConfig() {
        return config;
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.Controller#getParent()
     */
    @Override
    public Parent getParent() throws IOException {
        Parent parent = null;

        if (null != mainView) {
            parent = (Parent) mainView.getNode();
        }
        return parent;
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.Controller#showHelp()
     */
    @Override
    public void showHelp() {
        showView(new AboutView(this));
    }

    /* (non-Javadoc)
     * @see org.javnce.ui.Controller#getProperties()
     */
    @Override
    public ViewProperties getProperties() {
        return mainView.getProperties();
    }
}
