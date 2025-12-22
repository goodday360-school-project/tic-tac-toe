package org.tictactoe.app.menu;

import org.tictactoe.app.utils.Utils;
import org.tictactoe.app.playground.Playground;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.net.URL;


public class Menu {
    // Image paths (Consider using relative paths for portability)
    URL BUTTON_IMAGE_FILE = Objects.requireNonNull(getClass().getResource("/ui/button.png"));
    URL border_IMAGE_FILE = Objects.requireNonNull(getClass().getResource("/ui/RectangleBox_96x96.png"));
    URL home_IMAGE_FILE = Objects.requireNonNull(getClass().getResource("/ui/bg2.png"));

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final JFrame mainFrame;

    public JPanel createContentPane() {
        // Add screens to the card layout with unique names
//        cardPanel.add(createMenuPanel("SIGNIN"), "SIGNIN");
        cardPanel.add(createMenuPanel("START"), "START");
        cardPanel.add(createMenuPanel("DIFFICULTY"), "DIFFICULTY");

        // Background Wrapper
        JPanel mainWrapper = new JPanel(new BorderLayout());
        ImageIcon originalIcon = new ImageIcon(home_IMAGE_FILE); // load from path or URL
        Image scaledImage = originalIcon.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH);
        ImageIcon bgIcon = new ImageIcon(scaledImage);

        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setLayout(new GridBagLayout());


        cardPanel.setOpaque(false);
        bgLabel.add(cardPanel);
        mainWrapper.add(bgLabel);
        return mainWrapper;
    }

    private JPanel createMenuPanel(String type) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        ImageIcon borderOriginal = new ImageIcon(border_IMAGE_FILE);
        Image borderScaled = borderOriginal.getImage().getScaledInstance(300, 350, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(borderScaled);

        JLabel borderLabel = new JLabel(icon);
        borderLabel.setLayout(new GridBagLayout());

        String titleText = type.equals("DIFFICULTY") ? "Difficulty" : "Tic Tac Toe";
        JLabel title = new JLabel("<html><i style='font-size:30px'>" + titleText + "</i></html>");
        title.setFont(Utils.getFont(150f));
        title.setForeground(Color.WHITE);
        borderLabel.add(title, insert(50, 0, 0, 0, GridBagConstraints.NORTH, 1, 0));

        if (type.equals("SIGNIN")) {
            borderLabel.add(createStyledButton("Login", e -> cardLayout.show(cardPanel, "START")), insert(0, 0, -10, 0, GridBagConstraints.SOUTH, 1, 1));
            borderLabel.add(createStyledButton("Sign Up", null), insert(0, 0, -10, 0, GridBagConstraints.SOUTH, 1, 2));
            borderLabel.add(createStyledButton("Quit", e -> System.exit(0)),
                    insert(0, 0, 50, 0, GridBagConstraints.SOUTH, 1, 3));
        } else if (type.equals("START")) {
            borderLabel.add(createStyledButton("New Game", e -> cardLayout.show(cardPanel, "DIFFICULTY")), insert(0, 0, 0, 0, GridBagConstraints.SOUTH, 1, 1));
            borderLabel.add(createStyledButton("Continue", e -> cardLayout.show(cardPanel, "CONTINUE")), insert(0, 0, 125, 0, GridBagConstraints.SOUTH, 1, 2));
//            borderLabel.add(createStyledButton("Sign Out", e -> cardLayout.show(cardPanel, "SIGNIN")), insert(0, 0, 50, 0, GridBagConstraints.SOUTH, 1, 3));
        } else if (type.equals("DIFFICULTY")) {
            borderLabel.add(createStyledButton("Easy", e-> new Playground(mainFrame, 0)), insert(0, 0, -10, 0, GridBagConstraints.SOUTH, 1, 1));
            borderLabel.add(createStyledButton("Hard", e-> new Playground(mainFrame, 1)), insert(0, 0, -10, 0, GridBagConstraints.SOUTH, 1, 2));
            borderLabel.add(createStyledButton("Back", e -> cardLayout.show(cardPanel, "START")), insert(0, 0, 50, 0, GridBagConstraints.SOUTH, 1, 3));
        }

        panel.add(borderLabel);
        return panel;
    }

    public JButton createStyledButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        ImageIcon buttonOriginal = new ImageIcon(BUTTON_IMAGE_FILE);
        Image buttonScaled = buttonOriginal.getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(buttonScaled);

        button.setIcon(icon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(Utils.getFont(25f));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (action != null) button.addActionListener(action);
        return button;
    }

    public static GridBagConstraints insert(int a, int b, int c, int d, int anchor, int weighty, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = weighty;
        gbc.anchor = anchor;
        gbc.gridy = gridy;
        gbc.insets = new Insets(a, b, c, d);
        return gbc;
    }

    public Menu(JFrame frame) {
        this.mainFrame = frame;
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(this.createContentPane());
        frame.setResizable(false);
        frame.setVisible(true);
    }
}