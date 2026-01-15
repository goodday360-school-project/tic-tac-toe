package org.tictactoe.app.menu;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tictactoe.app.playground.GameStatus;
import org.tictactoe.app.playground.Playground;
import org.tictactoe.app.utils.Utils;


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
        cardPanel.add(createMenuPanel("START"), "START");
        cardPanel.add(createMenuPanel("DIFFICULTY"), "DIFFICULTY");
        cardPanel.add(createMenuPanel("STATS"), "STATS");

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

        String titleText = type;
        if(type.equals("START")){
            titleText="tic tac toe";
        }
        JLabel title = new JLabel(titleText);
        title.setFont(Utils.getFont(40f));
        title.setForeground(Color.WHITE);
        borderLabel.add(title, insert(50, 0, 0, 0, GridBagConstraints.NORTH, 1, 0));

        if (type.equals("START")) {

            borderLabel.add(createStyledButton("New Game", e -> cardLayout.show(cardPanel, "DIFFICULTY")), insert(0, 0, -20, 0, GridBagConstraints.SOUTH, 1, 1));

            if (GameStatus.is_can_continue()){
                borderLabel.add(createStyledButton("Continue", e -> new Playground(mainFrame)), insert(0, 0, -20, 0, GridBagConstraints.SOUTH, 1, 2));
            }

            borderLabel.add(createStyledButton("stats", e -> cardLayout.show(cardPanel, "STATS")), insert(0, 0, 100, 0, GridBagConstraints.SOUTH, 1, 3));
        } else if (type.equals("DIFFICULTY")) {
            borderLabel.add(createStyledButton("Easy", e-> new Playground(mainFrame, 0)), insert(0, 0, -10, 0, GridBagConstraints.SOUTH, 1, 1));
            borderLabel.add(createStyledButton("Normal", e-> new Playground(mainFrame, 1)), insert(0, 0, -10, 0, GridBagConstraints.SOUTH, 1, 2));
            borderLabel.add(createStyledButton("Hard", e-> new Playground(mainFrame, 2)), insert(0, 0, -10, 0, GridBagConstraints.SOUTH, 1, 3));
            borderLabel.add(createStyledButton("Back", e -> cardLayout.show(cardPanel, "START")), insert(0, 0, 50, 0, GridBagConstraints.SOUTH, 1, 4));
        } else if (type.equals("STATS")) {

            JLabel winLabel = new JLabel("Wins: 0");
            winLabel.setFont(Utils.getFont(20f));
            winLabel.setForeground(Color.WHITE);
            borderLabel.add(winLabel, insert(0, 20, 0, 0, GridBagConstraints.WEST, 1, 1));

            JLabel lossLabel = new JLabel("Losses: 0");
            lossLabel.setFont(Utils.getFont(20f));
            lossLabel.setForeground(Color.WHITE);
            borderLabel.add(lossLabel, insert(0, 20, 0, 0, GridBagConstraints.WEST, 1, 2));

            JLabel drawlabal = new JLabel("draw: 0");
            drawlabal.setFont(Utils.getFont(20f));
            drawlabal.setForeground(Color.WHITE);
            borderLabel.add(drawlabal, insert(0, 20, 0, 0, GridBagConstraints.WEST, 1, 3));

            borderLabel.add(createStyledButton("Back", e -> cardLayout.show(cardPanel, "START")), insert(0, 0, 50, 0, GridBagConstraints.SOUTH, 1, 4));
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