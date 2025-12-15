package org.tictactoe.app.playground;

/* Java Swing Import */
import javax.swing.*;

/* --- */

/* Java Utils Imports */
import java.util.ArrayList;
import java.util.Collections;
/* --- */

public class Bot {
    private final Playground playground;
    private final int difficulty;

    public Bot(int difficulty, Playground playground){
        this.playground = playground;
        this.difficulty = difficulty;
    }

    private void easy_bot(){
        JButton[][] board = playground.board;
        ArrayList<Integer[]> available_move = new ArrayList<>();
        for (int r = 0; r < 5; r++){
            for (int c = 0; c < 5; c++) {
                if (board[r][c].getText().trim().equalsIgnoreCase("")){
                    available_move.add(new Integer[] {r,c});
                }
            }
        }
        if (!available_move.isEmpty()){
            String bot_turn = this.playground.getPlayerTurn().equalsIgnoreCase("x") ? "o" : "x";
            Collections.shuffle(available_move);

            board[available_move.getFirst()[0]][available_move.getFirst()[1]].setText(bot_turn);
            this.playground.incrementPlayedMoveCount(bot_turn);
            this.playground.switchCurrentTurn();
        }

        this.playground.playgroundUtils.checkGameResult(available_move.getFirst()[0], available_move.getFirst()[1]);

    }

    public void play(){
        this.playground.isWorking = true;
        {
            if (this.playground.gameEnd) {
                return;
            }
            if (this.difficulty == 0) {
                easy_bot();
            }
        }
        this.playground.isWorking = false;
    }
}
