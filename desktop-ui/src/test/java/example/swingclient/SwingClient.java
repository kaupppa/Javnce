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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.javnce.eventing.EventLoop;
import org.javnce.upnp.RemoteServerInfo;

/**
 * The ugly but well performing Javnce client with swing.
 *
 * Just to compare JavaFX performance.
 */
public class SwingClient {

    /**
     * The frame.
     */
    private JFrame f;

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SwingClient().createAndShowGUI();
            }
        });
    }

    /**
     * Creates the and show gui.
     */
    private void createAndShowGUI() {
        f = new JFrame("JaVNCe - The Swing client");

        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                EventLoop.shutdownAll();
                System.exit(0);
            }
        });

        RemoteServerView panel = new RemoteServerView(this);
        f.add(panel);
        f.pack();
        f.setVisible(true);

    }

    /**
     * Creates the vnc view.
     *
     * @param o the o
     * @param info the info
     */
    public void createVncView(Component o, RemoteServerInfo info) {
        f.remove(o);
        VncPanel panel = new VncPanel(info);
        f.add(panel);
        f.pack();
    }
}
