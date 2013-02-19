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
import org.junit.runners.model.Statement;

public class FXStatement extends Statement {

    final static Thread fxThread = createRunningFxThread();
    private final Statement statement;
    private boolean evaluated;
    private Throwable exception;

    static private Thread createRunningFxThread() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Application.launch(TestApplication.class);
            }
        });
        t.start();

        return t;
    }

    public FXStatement(Statement statement) {
        this.statement = statement;
        evaluated = false;
    }

    synchronized private void doEvaluate() {
        try {
            statement.evaluate();
        } catch (Throwable e) {
            exception = e;
        }
        evaluated = true;
        notifyAll();
    }

    synchronized private void waitEvaluate() {
        if (!evaluated) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(FXStatement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void evaluate() throws Throwable {
        TestApplication.waitForApplication();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                doEvaluate();
            }
        });
        waitEvaluate();
        if (null != exception) {
            //Forward to callers thread
            throw new Throwable(exception);
        }
    }
}
