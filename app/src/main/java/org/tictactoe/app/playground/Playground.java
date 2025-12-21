package org.tictactoe.app.playground;


import java.util.*;

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
    GameEvent gameEvent = new GameEvent(this);

    int width = 1000;
    int height = 650;

    public final int maxMatchToWin = 4;

    public boolean isWorking = false;
    public boolean gameEnd = false;

    public final Bot bot;

    private final JFrame mainFrame;

    JButton[][] board = new JButton[5][5];

    private String current_turn = "x";
    private final JLabel current_turn_label = new JLabel("- Turn: " + this.current_turn.toUpperCase());
    private final String player_turn;
    private final JLabel player_turn_label = new JLabel("- Player: ");

    /* Played Move */
    public int x_played_move_count = 0;
    private final JLabel x_played_move_label = new JLabel("  X: 0");
    public int o_played_move_count = 0;
    private final JLabel o_played_move_label = new JLabel("  O: 0");
    /* --- */

    public Playground(JFrame mainFrame, int difficulty) {

        /* Styling `mainFrame` */
        this.mainFrame = mainFrame;
        this.mainFrame.setSize(width, height);
        this.mainFrame.setLocationRelativeTo(null);
        this.mainFrame.setResizable(false);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.X_AXIS));
        this.mainFrame.getContentPane().setBackground(new Color(87,72,82));
        /* --- */

        /* Setup UI Container */
        SetupStatusContainer();
        SetupBoardContainer();
        /* --- */

        mainFrame.pack();
        mainFrame.setVisible(true);

        /* Initialize Bot */
        this.player_turn =  "o"; //Utils.shuffleArray(new String[]{"x","o"})[0];
        this.player_turn_label.setText("- Player: "+player_turn.toUpperCase());
        this.bot = new Bot(difficulty,this);
        if (!current_turn.equalsIgnoreCase(player_turn)) {
            this.bot.play();
        }
        /* --- */
        
    }

    public String getCurrentTurn(){
        return this.current_turn;
    }

    public String getPlayerTurn() {
        return this.player_turn;
    }


    public void switchCurrentTurn() {
        System.out.println("Switching Turn From: " + this.current_turn);
        if (this.current_turn.trim().equalsIgnoreCase("x")){
            this.current_turn = "o";
        }else{
            this.current_turn = "x";
        }
        this.current_turn_label.setText("- Turn: " + this.current_turn.toUpperCase());
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

    public void playMove(int r, int c, String turn){
        // ===> Apply New Text Color After Played Move
        JButton tile = board[r][c];
        tile.setBackground(new Color(87,72,82));
        tile.setText(turn.toUpperCase());
        if (tile.getText().trim().equalsIgnoreCase("x")){
            tile.setForeground(new Color(254,137,9));
        }else{
            tile.setForeground(new Color(0,220,255));
        }
        // <===

        // ===> Increment Turn Played Move Count
        this.incrementPlayedMoveCount(tile.getText());
        // <===

        this.gameEvent.checkGameResult(r, c);
        this.switchCurrentTurn();
        System.out.println("Turn: " + this.current_turn);
    }

    /* Setup UI */
    private void SetupStatusContainer() {
        JPanel statusContainer = new JPanel();
        statusContainer.setOpaque(false);
        statusContainer.setBackground(null);
        statusContainer.setBorder(new EmptyBorder(75, 25, 0, 25));
        statusContainer.setLayout(new BorderLayout());
        this.mainFrame.add(statusContainer);

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
        title.setMaximumSize(new Dimension(panel_width, 50));
        title.setForeground(Color.WHITE);
        title.setBackground(null);
        title.setOpaque(false);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        status_panel.add(title);

        current_turn_label.setMaximumSize(new Dimension(panel_width, 25));
        current_turn_label.setForeground(Color.WHITE);
        current_turn_label.setFont(Utils.getFont(20f));
        status_panel.add(current_turn_label);

        player_turn_label.setMaximumSize(new Dimension(panel_width, 25));
        player_turn_label.setForeground(Color.WHITE);
        player_turn_label.setFont(Utils.getFont(20f));
        status_panel.add(player_turn_label);

        JLabel label2 = new JLabel("- Played Move");
        label2.setMaximumSize(new Dimension(panel_width, 25));
        label2.setForeground(Color.WHITE);
        label2.setFont(Utils.getFont(20f));
        status_panel.add(label2);


        x_played_move_label.setMaximumSize(new Dimension(panel_width, 25));
        x_played_move_label.setForeground(Color.WHITE);
        x_played_move_label.setFont(Utils.getFont(20f));
        status_panel.add(x_played_move_label);

        o_played_move_label.setMaximumSize(new Dimension(panel_width, 25));
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
        mainFrame.add(boardContainer);

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
                final int finalR = r;
                final int finalC = c;
                tile.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!tile.getText().trim().isEmpty() || gameEnd || isWorking){
                            return;
                        }
                        tile.setBackground(new Color(171,155,142));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!tile.getText().trim().isEmpty()  || gameEnd || isWorking){
                            return;
                        }
                        tile.setBackground(new Color(87,72,82));
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gameEvent.playerPlay(finalR, finalC, tile);
                    }
                });
                // <===
                /* ---- */

                board[r][c] = tile;
                boardPanel.add(tile);
            }
        }

    }

    /* --- */
}

class GameEvent {
    private final Playground playground;
    public GameEvent(Playground playground){
        this.playground = playground;
    }

    public int[] getNextPosition(int current_r, int current_c, String direction) {
        int new_r = current_r;
        int new_c = current_c;

        switch (direction.trim().toLowerCase()) {
            case "n":   // North
                new_r -= 1;
                break;
            case "s":   // South
                new_r += 1;
                break;
            case "e":   // East
                new_c += 1;
                break;
            case "w":   // West
                new_c -= 1;
                break;
            case "ne":  // North-East
                new_r -= 1;
                new_c += 1;
                break;
            case "nw":  // North-West
                new_r -= 1;
                new_c -= 1;
                break;
            case "se":  // South-East
                new_r += 1;
                new_c += 1;
                break;
            case "sw":  // South-West
                new_r += 1;
                new_c -= 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction +
                        ". Must be one of: n, s, e, w, ne, nw, se, sw");
        }

        // Boundary check (assuming 5x5 grid)
        if (new_r >= 5 || new_c >= 5 || new_r < 0 || new_c < 0) {
            return null;
        }

        return new int[]{new_r, new_c};
    }

    public void playerPlay(int current_r, int current_c, JButton tile){
        if (!tile.getText().trim().isEmpty() || this.playground.isWorking || this.playground.gameEnd) {
            return;
        }

        if (this.playground.getCurrentTurn().equals(this.playground.getPlayerTurn())){
            this.playground.playMove(current_r, current_c, this.playground.getPlayerTurn());
        }else{
            return;
        }

        this.playground.isWorking = true;

        // ===> Give Turn To Bot
        this.playground.bot.play();
        // <===
    }

    public void checkGameResult(int r, int c) {
        System.out.println("It run with RC: "+r+c);
        JButton[][] board = playground.board;
        String current_position_turn = board[r][c].getText().trim();


        /* Check Matched Direction */
        //      NW  N  NE
        //      W - | - E
        //      SW  S  SE

        String[][] paired_directions = {
                {"n", "s"},
                {"e", "w"},
                {"ne", "sw"},
                {"nw", "se"},

        };

        for (String[] p_direction: paired_directions) {
            ArrayList<Integer[]> checked_direction_positions = new ArrayList<>();
            int matched_score = 1; //-> score start with 1 because current pos already matched.

            for (String direction: p_direction) {
                int[] current_checking_position = {r, c};

                while (true) {
                    System.out.println("Score: " + matched_score);

                    if (matched_score == this.playground.maxMatchToWin) {
                        break;
                    }

                    int[] next_position = this.getNextPosition(current_checking_position[0], current_checking_position[1], direction);

                    if (next_position == null) {
                        break;
                    }
                    System.out.println("Next position:"+ next_position[0]+ next_position[1]);

                    String next_position_turn = board[next_position[0]][next_position[1]].getText().trim();

                    if (next_position_turn.equalsIgnoreCase(current_position_turn)) {
                        matched_score++;
                        checked_direction_positions.add(new Integer[]{next_position[0], next_position[1]});
                        current_checking_position = next_position;
                    } else {
                        break;
                    }
                }
            }

            if (matched_score == this.playground.maxMatchToWin){
                this.playground.gameEnd = true;
                System.out.println("Win Matched Direction: " + p_direction[0]+ " " + p_direction[1]);
                for (Integer[] position: checked_direction_positions){
                    board[r][c].setBackground(new Color(255,0,0));
                    board[position[0]][position[1]].setBackground(new Color(255,0,0));
                }

                break;
            }

        }
        System.out.println("====");

        /* --- */


    }
}

