package org.tictactoe.app;

import javax.swing.*;

/* Custom Package Imports */
import org.tictactoe.app.playground.Playground;
import org.tictactoe.app.menu.Menu;

/* --- */


public class App {

    private static final JFrame mainFrame = new JFrame("Tic Tac Toe");

    public static void main(String[] args) {
//        Playground playground = new Playground(mainFrame, 0);
            Menu menu = new Menu(mainFrame);

    }
}
