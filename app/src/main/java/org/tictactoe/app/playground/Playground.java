package org.tictactoe.app.playground;

import java.net.URL;
import java.util.*;
import java.io.*;

/* awt Imports */
import java.awt.*;
import java.awt.event.*;
/* --- */

/* Javax Swing Imports */
import javax.swing.*;
import javax.swing.border.*;
/* --- */

/* JSON Import */

import com.fasterxml.jackson.databind.ObjectMapper;

/* --- */

/* Custom Package Imports */
import org.tictactoe.app.utils.Utils;
import org.tictactoe.app.bot.Bot;
import org.tictactoe.app.menu.Menu;
/* --- */


public class Playground {
    private final URL BUTTON_IMAGE_FILE = Objects.requireNonNull(getClass().getResource("/ui/button.png"));
    private final URL BORDER_BOX_FILE = Objects.requireNonNull(getClass().getResource("/ui/RectangleBox_96x96.png"));

    public GameEvent gameEvent = new GameEvent(this);
    public GameStatus gameStatus;

    public final static int width = 1000;
    public final static int height = 750;

    public final int maxMatchToWin = 5;
    public final static int boardSize = 7;

    public boolean isWorking = false;
    public boolean gameEnd = false;

    public Bot bot;

    private final JFrame mainFrame;
    private JLayeredPane layeredPane;
    private JPanel playgroundPanel;
    private JPanel endgamePanel;

    public JButton[][] board = new JButton[boardSize][boardSize];

    public int difficulty;
    public String current_turn = "x";
    private final JLabel current_turn_label = new JLabel("- Turn: " + this.current_turn.toUpperCase());
    public String player_turn;
    private final JLabel player_turn_label = new JLabel("- Player: ");

    private JLabel winner_label = new JLabel("");
    private JLabel endgame_feedback_label = new JLabel("");

    /* Played Move */
    public int x_played_move_count = 0;
    private final JLabel x_played_move_label = new JLabel("  X: 0");
    public int o_played_move_count = 0;
    private final JLabel o_played_move_label = new JLabel("  O: 0");
    /* --- */

    // For New Game
    public Playground(JFrame mainFrame, int difficulty) {

        this.mainFrame = mainFrame;
        this.difficulty = difficulty;
        this.setupPlaygroundUI();
        this.SetupEndGameContainer();

        /* Initialize Player & Bot */


        if (this.difficulty >= 0) {
            this.player_turn = Utils.shuffleArray(new String[]{"x","o"})[0];
            // ===> Initialize Game Status to JSON file
            this.gameStatus = new GameStatus(difficulty, this);
            this.gameStatus.saveGameStatus();
            // <===

            this.bot = new Bot(difficulty, this);

            if (!current_turn.equalsIgnoreCase(player_turn)) {
                this.bot.play();
            }
        }else if (this.difficulty == -1){
            this.player_turn = "x";
            // ===> Initialize Game Status to JSON file
            this.gameStatus = new GameStatus(difficulty, this);
            this.gameStatus.saveGameStatus();
            // <===
        }



        this.player_turn_label.setText("- Player: " + player_turn.toUpperCase());

        showPlayground();


        /* --- */
        
    }

    // For continue Game
    public Playground(JFrame mainFrame){
        this.mainFrame = mainFrame;
        this.setupPlaygroundUI();
        this.SetupEndGameContainer();

        this.gameStatus = new GameStatus(this);

        this.player_turn_label.setText("- Player: "+player_turn.toUpperCase());
        this.current_turn_label.setText("- Turn: " + this.current_turn.toUpperCase());


        this.x_played_move_label.setText("  X: "+x_played_move_count);
        this.o_played_move_label.setText("  O: "+o_played_move_count);


        showPlayground();

        if (this.difficulty >= 0){
            this.bot = new Bot(this.difficulty,this);
            if (!current_turn.equalsIgnoreCase(player_turn)) {
                this.bot.play();
            }
        }


    }



    public String getCurrentTurn(){
        return this.current_turn;
    }

    public String getPlayerTurn() {
        return this.player_turn;
    }



    public void switchCurrentTurn() {
        if (this.gameEnd){
            return;
        }
        System.out.println("Switching Turn From: " + this.current_turn);
        if (this.current_turn.trim().equalsIgnoreCase("x")){
            this.current_turn = "o";
        }else{
            this.current_turn = "x";
        }
        this.current_turn_label.setText("- Turn: " + this.current_turn.toUpperCase());

        if (difficulty == -1) {
            this.player_turn = current_turn;
            this.player_turn_label.setText("- Player: "+ this.player_turn.toUpperCase());
        }
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



    /* Setup Playground UI */
    public void setupPlaygroundUI(){
        /* Reset `mainFrame` */
        this.mainFrame.getContentPane().removeAll();
        this.mainFrame.revalidate();
        this.mainFrame.repaint();
        this.mainFrame.setSize(width, height);
        this.mainFrame.setResizable(false);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /* --- */
        /* Creating Layer Pane */
        this.layeredPane = new JLayeredPane();
        this.layeredPane.setLayout(null);
        /* --- */

        /* Creating Playground Panel */
        this.playgroundPanel = new JPanel();
        this.playgroundPanel.setSize(new Dimension(width, height));
        this.playgroundPanel.setLayout(new BoxLayout(this.playgroundPanel, BoxLayout.X_AXIS));
        this.playgroundPanel.setBackground(new Color(87,72,82));
        /* --- */

        /* Setup UI Container */
        SetupStatusContainer();
        SetupBoardContainer();
        /* --- */
        this.layeredPane.add(playgroundPanel, JLayeredPane.DEFAULT_LAYER);
        this.mainFrame.add(layeredPane);
    }


    public void showPlayground(){

        this.mainFrame.setVisible(true);
    }

    private void SetupStatusContainer() {
        JPanel statusContainer = new JPanel();
//        statusContainer.setOpaque(false);
//        statusContainer.setBackground(null);
        statusContainer.setBorder(new EmptyBorder(75, 25, 0, 0));
        statusContainer.setLayout(new BorderLayout());
        statusContainer.setPreferredSize(new Dimension(300,height));
        statusContainer.setMaximumSize(new Dimension(300,height));
        statusContainer.setBackground(null);


        /* Setup Game Status UI */
        // ==> Load Top Panel Image into Panel
        int panel_width = 225, panel_height = 300;
        ImageIcon icon = new ImageIcon(
                BORDER_BOX_FILE
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
        status_panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        statusContainer.add(status_panel, BorderLayout.NORTH);

        // <===

        {// ===> Add Text to Status Panel
            JLabel title = new JLabel("STATUS");
            title.setFont(Utils.getFont(28f));
            title.setMaximumSize(new Dimension(panel_width - 60, 50));
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

        }// < ===

        {// ===> Main Menu Button
            JPanel container = new JPanel();
            container.setLayout(new BorderLayout());
            container.setBackground(null);
            container.setOpaque(false);
            container.setBorder(new EmptyBorder(50, 0, 0, 50));
            JButton mm_btn = createStyledButton("Main Menu", 150, 40);
            mm_btn.addActionListener(e->{
                new Menu(this.mainFrame);
            });

            container.add(mm_btn, BorderLayout.NORTH);
            statusContainer.add(container, BorderLayout.CENTER);
        }

        this.playgroundPanel.add(statusContainer);
        /* --- */
    }

    private void SetupBoardContainer() {
        /* Setup Board Container & Board */
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setBackground(null);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setMaximumSize(new Dimension(600, height));
        JPanel boardContainer = new JPanel();
        boardContainer.setLayout(new BorderLayout());
        boardContainer.setOpaque(false);
        boardContainer.setBackground(null);
        boardContainer.setMaximumSize(new Dimension(650,650));

        container.add(boardContainer);
        this.playgroundPanel.add(container);

        // ==> Load Top Pattern Image
        ImageIcon icon = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/ui/PatternMiddleBottomBG_199x48.png"))
        );
        Image icon_to_img = icon.getImage();
        ImageIcon scaled_icon = new ImageIcon(icon_to_img.getScaledInstance(650, 100, Image.SCALE_SMOOTH));

        JLabel top_pattern_img_label = new JLabel(scaled_icon);
        top_pattern_img_label.setBorder(new EmptyBorder(-25, 0, 0, 0));
        top_pattern_img_label.setBackground(null);
        top_pattern_img_label.setOpaque(false);
        boardContainer.add(top_pattern_img_label, BorderLayout.NORTH);
        // <===

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(boardSize,boardSize));
        boardPanel.setBackground(null);
        boardPanel.setOpaque(false);
        boardContainer.add(boardPanel, BorderLayout.CENTER);



        for (int r = 0; r < boardSize; r++){
            for (int c = 0; c < boardSize; c++) {

                /* Styling `tile` */
                JButton tile = new JButton();
                tile.setFont(Utils.getFont(80));
                tile.setForeground(new Color(0,220,255));

                tile.setHorizontalAlignment(SwingConstants.CENTER);
                tile.setVerticalAlignment(SwingConstants.CENTER);

                tile.setFocusPainted(false);
                tile.setBackground(new Color(87,72,82));
                tile.setBorder(new LineBorder(new Color(186,145,88), 3));
                tile.setCursor(new Cursor(Cursor.HAND_CURSOR));

                tile.setHorizontalAlignment(SwingConstants.CENTER);
                tile.setVerticalAlignment(SwingConstants.CENTER);
                tile.setHorizontalTextPosition(SwingConstants.CENTER);
                tile.setVerticalTextPosition(SwingConstants.CENTER);

                // ===> Hover Effect And Mouse Click
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
                    public void mousePressed(MouseEvent e) {
                        gameEvent.playMove(finalR, finalC, player_turn);
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

    /* Setup EndGame Layer UI */
    public void SetupEndGameContainer(){
        this.endgamePanel = new JPanel();
        this.endgamePanel.setBackground(null);
        this.endgamePanel.setOpaque(false);
        this.endgamePanel.setSize(new Dimension(width,height));
        this.endgamePanel.setLayout(new GridBagLayout());


        // ==> Load Panel Image into Panel
        int panel_width = 450, panel_height = 500;
        ImageIcon icon = new ImageIcon(BORDER_BOX_FILE);
        Image scaled_img = icon.getImage().getScaledInstance(panel_width, panel_height, Image.SCALE_SMOOTH);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(scaled_img, 0, 0, panel_width, panel_height, this);
            }
        };
        panel.setBackground(null);
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(panel_width, panel_height));
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        this.endgamePanel.add(panel, new GridBagConstraints());

        // <===

        {// ===> Add to Panel
            winner_label.setText("x");
            winner_label.setFont(Utils.getFont(150));
            winner_label.setMaximumSize(new Dimension(panel_width - 60, 200));
            winner_label.setForeground(Color.WHITE);
            winner_label.setBackground(null);
            winner_label.setOpaque(false);
            winner_label.setHorizontalAlignment(SwingConstants.CENTER);
            winner_label.setVerticalAlignment(SwingConstants.CENTER);
            panel.add(winner_label);

            endgame_feedback_label.setForeground(Color.PINK);

            endgame_feedback_label.setFont(Utils.getFont(50));
            endgame_feedback_label.setMaximumSize(new Dimension(panel_width - 60, 20));
            endgame_feedback_label.setHorizontalAlignment(SwingConstants.CENTER);
            endgame_feedback_label.setVerticalAlignment(SwingConstants.CENTER);
            panel.add(endgame_feedback_label);

        }// < ===

        {// ===> Back Panel & Button
            JButton play_again_btn = createStyledButton("PLAY AGAIN", 150, 40);
            play_again_btn.addActionListener(e->{
                new Playground(this.mainFrame, this.difficulty);
            });
            play_again_btn.setBorder(new EmptyBorder(40,120,0,0));
            panel.add(play_again_btn);

            JButton close_btn = createStyledButton("CLOSE", 150, 40);
            close_btn.addActionListener(e->{
                this.endgamePanel.setVisible(false);
            });
            close_btn.setBorder(new EmptyBorder(40,120,0,0));
            panel.add(close_btn);
        }

        this.endgamePanel.setVisible(false);

        layeredPane.add(this.endgamePanel, JLayeredPane.PALETTE_LAYER);
    }

    public void setEndGame(String winner, int state){
        if (state == 1) {
            winner_label.setText(winner);
            if (winner.equalsIgnoreCase("x")){
                winner_label.setForeground(new Color(254,137,9));
            }else{
                winner_label.setForeground(new Color(0,220,255));
            }
            endgame_feedback_label.setMaximumSize(new Dimension(450, 25));
            endgame_feedback_label.setText("WIN THE GAME");
        }else{
            winner_label.setVisible(false);
            endgame_feedback_label.setMaximumSize(new Dimension(450, 150));
            endgame_feedback_label.setText("GAME DRAW");
        }

        this.endgamePanel.setVisible(true);
    }

    /* --- */

    private JButton createStyledButton(String text, int width, int height) {
        JButton button = new JButton(text);
        ImageIcon buttonOriginal = new ImageIcon(BUTTON_IMAGE_FILE);
        Image buttonScaled = buttonOriginal.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(buttonScaled);

        button.setIcon(icon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(Utils.getFont(25f));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }


}

