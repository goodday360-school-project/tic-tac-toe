package org.tictactoe.app.playground;

import java.util.Objects;
import java.io.*;

/* awt Imports */
import java.awt.*;
import java.awt.event.*;
/* --- */

/* Javax Swing Imports */
import javax.swing.*;
import javax.swing.border.*;
/* --- */

/* Custom Package Imports */
import org.tictactoe.app.utils.Utils;
/* --- */


public class Playground {
    int width = 1000;
    int height = 650;

    JFrame frame = new JFrame();
    JButton[][] board = new JButton[5][5];

    private String current_turn = "x";
    private final JLabel current_turn_label = new JLabel("- Turn: "+current_turn.toUpperCase());
    private String player_turn = "";
    private final JLabel player_turn_label = new JLabel("- Player: "+player_turn.toUpperCase());

    /* Played Move */
    private int x_played_move_count = 0;
    private final JLabel x_played_move_label = new JLabel("  X: 0");
    private int o_played_move_count = 0;
    private final JLabel o_played_move_label = new JLabel("  O: 0");
    /* --- */

    public Playground() {
        this.player_turn = "x"; // Utils.shuffleArray(new String[] {"x", "o"})[0];

        /* Styling `frame` */
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
        frame.getContentPane().setBackground(new Color(87,72,82));
        /* --- */
        
        /* Setup UI Container */
        SetupStatusContainer();
        SetupBoardContainer();
        /* --- */
        
        frame.pack();
        frame.setVisible(true);
    }

    public String getCurrentTurn() {
        return this.current_turn;
    }

    public void setCurrentTurn(String turn) {
        this.current_turn = turn;
        this.current_turn_label.setText("- Turn: " + turn.toUpperCase());
    }


    public void incrementPlayedMoveCount(String turn) {
        if (turn.trim().equalsIgnoreCase("x")){
            x_played_move_count+=1;
            x_played_move_label.setText("  X: "+x_played_move_count);
        }else{
            o_played_move_count+=1;
            o_played_move_label.setText("  O: "+o_played_move_count);
        }
    }

    private void SetupStatusContainer() {
        JPanel statusContainer = new JPanel();
        statusContainer.setOpaque(false);
        statusContainer.setBackground(null);
        statusContainer.setBorder(new EmptyBorder(75, 25, 0, 25));
        statusContainer.setLayout(new BorderLayout());
        frame.add(statusContainer);

        /* Setup Game Status UI */
        // ==> Load Top Panel Image into Panel
        int panel_width = 200, panel_height = 300;
        ImageIcon icon = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/ui/RectangleBox_96x96.png"))
        );
        Image scaled_img = icon.getImage().getScaledInstance(panel_width, panel_height, Image.SCALE_SMOOTH);

        JPanel status_panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(scaled_img, 0, 0, panel_width, panel_height, this);
            }
        };
        status_panel.setBackground(null);
        status_panel.setPreferredSize(new Dimension(panel_width, panel_height));
        status_panel.setLayout(new BoxLayout(status_panel, BoxLayout.Y_AXIS));
        status_panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        statusContainer.add(status_panel, BorderLayout.CENTER);

        // <===
        // ===> Add Text to Status Panel

        JLabel title = new JLabel("STATUS");
        title.setFont(Utils.getFont(28f));
        title.setMaximumSize(new Dimension(panel_width, 50));;
        title.setForeground(Color.WHITE);
        title.setBackground(null);
        title.setOpaque(false);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        status_panel.add(title);

        current_turn_label.setMaximumSize(new Dimension(panel_width, 25));;
        current_turn_label.setForeground(Color.WHITE);
        current_turn_label.setFont(Utils.getFont(20f));
        status_panel.add(current_turn_label);

        player_turn_label.setMaximumSize(new Dimension(panel_width, 25));;
        player_turn_label.setForeground(Color.WHITE);
        player_turn_label.setFont(Utils.getFont(20f));
        status_panel.add(player_turn_label);

        JLabel label2 = new JLabel("- Played Move");
        label2.setMaximumSize(new Dimension(panel_width, 25));;
        label2.setForeground(Color.WHITE);
        label2.setFont(Utils.getFont(20f));
        status_panel.add(label2);


        x_played_move_label.setMaximumSize(new Dimension(panel_width, 25));;
        x_played_move_label.setForeground(Color.WHITE);
        x_played_move_label.setFont(Utils.getFont(20f));
        status_panel.add(x_played_move_label);

        o_played_move_label.setMaximumSize(new Dimension(panel_width, 25));;
        o_played_move_label.setForeground(Color.WHITE);
        o_played_move_label.setFont(Utils.getFont(20f));
        status_panel.add(o_played_move_label);

        // < ===

        /* --- */
    }

    private void SetupBoardContainer() {


        /* Setup Board Container & Board */
        JPanel boardContainer = new JPanel();
        boardContainer.setLayout(new BorderLayout());
        boardContainer.setOpaque(false);
        boardContainer.setBackground(null);
        frame.add(boardContainer);

        // ==> Load Top Pattern Image
        ImageIcon icon = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/ui/PatternMiddleBottomBG_199x48.png"))
        );
        Image icon_to_img = icon.getImage();
        ImageIcon scaled_icon = new ImageIcon(icon_to_img.getScaledInstance(600, 100, Image.SCALE_SMOOTH));

        JLabel top_pattern_img_label = new JLabel(scaled_icon);
        top_pattern_img_label.setBorder(new EmptyBorder(-25, 0, 0, 0));
        top_pattern_img_label.setBackground(null);
        top_pattern_img_label.setOpaque(false);
        boardContainer.add(top_pattern_img_label, BorderLayout.NORTH);
        // <===

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(5,5));
        boardPanel.setBackground(Color.DARK_GRAY);
        boardPanel.setOpaque(true);
        boardPanel.setPreferredSize(new Dimension(600, 600));
        boardContainer.add(boardPanel, BorderLayout.CENTER);


        for (int r = 0; r < 5; r++){
            for (int c = 0; c < 5; c++) {

                /* Styling `tile` */
                JButton tile = new JButton();
                tile.setFont(Utils.getFont(150f));
                tile.setForeground(new Color(0,220,255));

                tile.setHorizontalAlignment(SwingConstants.CENTER);
                tile.setVerticalAlignment(SwingConstants.CENTER);

                tile.setFocusPainted(false);
                tile.setBackground(new Color(87,72,82));
                tile.setBorder(new LineBorder(new Color(186,145,88), 5));
                tile.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // ===> Hover Effect
                tile.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        tile.setBackground(new Color(171,155,142));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        tile.setBackground(new Color(87,72,82));
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println(current_turn +" "+ player_turn);
                        if (!tile.getText().trim().isEmpty()) {
                            System.out.println("NOT EMPTY");
                            return;
                        }

                        if (current_turn.equals(player_turn)){
                            tile.setText(player_turn);
                            setCurrentTurn(player_turn.equals("x") ? "o" : "x");
                        }
                        // Testing need to remove after added bot
                        else{
                            tile.setText(player_turn.equals("x") ? "o" : "x");
                            setCurrentTurn(player_turn);
                        }
                        // !-----

                        // ===> Apply New Text Color After Player Played Move
                        if (tile.getText().trim().equalsIgnoreCase("x")){
                            tile.setForeground(new Color(254,137,9));
                        }else{
                            tile.setForeground(new Color(0,220,255));
                        }
                        // <===

                        // ===> Increment Turn Played Move Count
                        incrementPlayedMoveCount(tile.getText());
                        // <===

                        System.out.println(tile.getText());
                    }
                });
                // <===
                /* ---- */

                board[r][c] = tile;
                boardPanel.add(tile);
            }
        }

    }


}
