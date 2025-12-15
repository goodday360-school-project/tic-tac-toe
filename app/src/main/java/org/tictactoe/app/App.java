package org.tictactoe.app;



/* Custom Package Imports */
import org.tictactoe.app.playground.Playground;

import javax.swing.*;
/* --- */


public class App {

    private static final JFrame mainFrame = new JFrame();

    public static void main(String[] args) {
        Playground playground = new Playground(mainFrame);

    }
}
