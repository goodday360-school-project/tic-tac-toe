package org.tictactoe.app.bot;

import javax.swing.*;
import java.util.*;

/* Custom Package Import */
import org.tictactoe.app.playground.Playground;

/* --- */

public class EasyBot {
    public EasyBot(Playground playground){
        int boardSize = playground.boardSize;
        JButton[][] board = playground.board;
        ArrayList<Integer[]> available_move = new ArrayList<>();
        for (int r = 0; r < boardSize; r++){
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c].getText().trim().equalsIgnoreCase("")){
                    available_move.add(new Integer[] {r,c});
                }
            }
        }
        if (!available_move.isEmpty()){
            String bot_turn = playground.getPlayerTurn().equalsIgnoreCase("x") ? "o" : "x";
            Collections.shuffle(available_move);
            playground.isWorking = false;
            playground.gameEvent.playMove(available_move.get(0)[0], available_move.get(0)[1], bot_turn);
        }
    }
}
