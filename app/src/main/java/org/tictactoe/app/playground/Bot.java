package org.tictactoe.app.playground;

/* Java Swing Import */
import javax.swing.*;

/* --- */

/* Java Utils Imports */
import java.util.*;
import java.util.concurrent.*;
/* --- */

public class Bot {
    private final Playground playground;
    private final int difficulty;

    private static final ExecutorService executor = Executors.newFixedThreadPool(25);

    public Bot(int difficulty, Playground playground){
        this.playground = playground;
        this.difficulty = difficulty;
    }

    public void play(){
        this.playground.isWorking = true;
        {
            if (this.playground.gameEnd) {
                return;
            }
            switch (this.difficulty) {
                case 0: {
                    easy_bot();
                    break;
                }
                case 1: {
                    hard_bot();
                    break;
                }
                default:{
                    throw new IllegalArgumentException("Invalid difficulty level: " + this.difficulty);

                }
            }
        }
        this.playground.isWorking = false;
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

    private Callable<Integer[]> hardBotTask(int current_r, int current_c) {
        return () -> {

            return new Integer[] {1, 1}; // {Min, Max}
        };
    };

    private void hard_bot(){
        ArrayList<Callable<Integer[]>> task_list = new ArrayList<>();

        for (int r = 0; r < 5; r++){
            for (int c = 0; c < 5; c++) {
                Callable<Integer[]> new_task = this.hardBotTask(0,0);
                task_list.add(new_task);
            }
        }

        try {
            // Run all tasks and wait for them to finish
            List<Future<Integer[]>> results = executor.invokeAll(task_list);

            for (Future<Integer[]> future : results) {
                Integer[] score = future.get();
                System.out.println(score[0]+ score[1]);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


}
