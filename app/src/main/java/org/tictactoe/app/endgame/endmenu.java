//package org.tictactoe.app.menu;
//import java.awt.*;
//import java.io.File;
//import java.net.URL;
//import java.util.Objects;
//
//import javax.swing.ImageIcon;
//
//
//import javax.swing.*;
//import javax.swing.JLabel;
//
//import org.tictactoe.app.utils.Utils;
//
//
//
//
//public class endmenu {
//
//    URL BUTTON_IMAGE_FILE = Objects.requireNonNull(getClass().getResource("/ui/button.png"));
//    URL bgfile = Objects.requireNonNull(getClass().getResource("/ui/button.png"));
//
//    String result="win";
//
//
//    public void main(String[] args) {
//
//
//        JPanel mainpane = new JPanel();
//        ImageIcon originalIcon = new ImageIcon(bgfile);
//        Image scaledImage = originalIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
//        ImageIcon bgIcon = new ImageIcon(scaledImage);
//
//        JLabel bgLabel = new JLabel(bgIcon);
//        mainpane.add(bgLabel);
//
//        JPanel OXpic = new JPanel();
//
//        JLabel OXJLabel = new JLabel("");
//
//        JLabel data=new JLabel(result);
//
//        mainpane.add(OXpic,insert(10, 0, 0, 0, GridBagConstraints.CENTER, 1, 1));
//        mainpane.add(data,insert(10, 0, 0, 0, GridBagConstraints.CENTER, 1, 2));
//        mainpane.add(createStyledButton("back",e -> "lol"),insert(10, 0, 0, 0, GridBagConstraints.CENTER, 1, 3));
//        mainpane.add(createStyledButton("restart",e -> "Framemain"),insert(10, 0, 0, 0, GridBagConstraints.CENTER, 1, 3));
//
//    }
//    public JButton createStyledButton(String text, java.awt.event.ActionListener action) {
//        JButton button = new JButton(text);
//        ImageIcon buttonOriginal = new ImageIcon(BUTTON_IMAGE_FILE);
//        Image buttonScaled = buttonOriginal.getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH);
//        ImageIcon icon = new ImageIcon(buttonScaled);
//
//        button.setIcon(icon);
//        button.setHorizontalTextPosition(JButton.CENTER);
//        button.setVerticalTextPosition(JButton.CENTER);
//        button.setContentAreaFilled(false);
//        button.setBorderPainted(false);
//        button.setFocusPainted(false);
//        button.setFont(Utils.getFont(25f));
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        if (action != null) button.addActionListener(action);
//        return button;
//    }
//
//    public static GridBagConstraints insert(int a, int b, int c, int d, int anchor, int weighty, int gridy) {
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.weighty = weighty;
//        gbc.anchor = anchor;
//        gbc.gridy = gridy;
//        gbc.insets = new Insets(a, b, c, d);
//        return gbc;
//    }
//
//
//}