package org.tictactoe.app.playground;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameEvent {
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

        // Boundary check (Base on grid size)
        int boardSize = Playground.boardSize;
        if (new_r >= boardSize || new_c >= boardSize || new_r < 0 || new_c < 0) {
            return null;
        }

        return new int[]{new_r, new_c};
    }


    public void playMove(int r, int c, String turn){

        JButton tile = this.playground.board[r][c];
        if (!tile.getText().trim().isEmpty() || this.playground.isWorking || this.playground.gameEnd) {
            return;
        }

        if (!this.playground.getCurrentTurn().equalsIgnoreCase(turn)){
            return;
        }

        // ===> Apply New Text Color After Played Move
        tile.setBackground(new Color(87,72,82));
        tile.setText(turn.toUpperCase());
        if (tile.getText().trim().equalsIgnoreCase("x")){
            tile.setForeground(new Color(254,137,9));
        }else{
            tile.setForeground(new Color(0,220,255));
        }
        // <===

        // ===> Increment Turn Played Move Count
        this.playground.incrementPlayedMoveCount(tile.getText());
        // <===

        this.checkGameResult(r, c);
        this.playground.switchCurrentTurn();
        System.out.println("Turn: " + this.playground.getCurrentTurn());

        this.playground.gameStatus.updateGameStatus(r, c, turn);

        if (!this.playground.getCurrentTurn().equals(this.playground.getPlayerTurn())){
            this.playground.isWorking = true;

            // ===> Give Turn To Bot
            this.playground.bot.play();
            // <===
        }
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
