package org.tictactoe.app.playground;

/* Java Swing Import */
import javax.swing.*;

/* --- */

/* Java Utils Imports */
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
/* --- */

class TaskResult{
    int r;
    int c;
    int min_score;
    int max_score;
    boolean strong_min_score;
    boolean strong_max_score;
}

public class Bot {
    private final Playground playground;
    private final int difficulty;


    private static final ExecutorService bot_play_executor = Executors.newFixedThreadPool(1);
    private static final ExecutorService hard_bot_task_hard_bot_task_executor = Executors.newFixedThreadPool(7*7);

    public Bot(int difficulty, Playground playground){
        this.playground = playground;
        this.difficulty = difficulty;
    }

    public void play(){
        this.playground.isWorking = true;
        bot_play_executor.execute(()->{
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
            this.playground.isWorking = false;
        });
    }

    private void easy_bot(){
        int boardSize = this.playground.boardSize;
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
            String bot_turn = this.playground.getPlayerTurn().equalsIgnoreCase("x") ? "o" : "x";
            Collections.shuffle(available_move);

            this.playground.playMove(available_move.get(0)[0], available_move.get(0)[1], bot_turn);
        }
    }

    private Callable<TaskResult> hardBotTask(int r, int c) {
        return () -> {
            JButton[][] board = this.playground.board;
            if (!board[r][c].getText().trim().isEmpty()){
                return null;
            }
            String player_turn = this.playground.getPlayerTurn();
            String bot_turn = player_turn.equalsIgnoreCase("x") ? "o" : "x";
            int min_score = 0, max_score = 0;
            boolean strong_min_score = false;
            boolean strong_max_score = false;
            int min_outlier_predict_score = 0;
            int max_outlier_predict_score = 0;

            /* Check Matched In Pair Direction */
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
                int[] paired_direction_max_score = {0,0};
                int[] paired_direction_min_score = {0,0};
                int[][] last_paired_checking_position = {{r, c}, {r,c}};


                for (int direction_index=0; direction_index<=1; direction_index++) {
                    String direction = p_direction[direction_index];

                    while (true) {
                        int[] last_checking_position = last_paired_checking_position[direction_index];

                        if (Arrays.stream(paired_direction_max_score).sum() >= (this.playground.maxMatchToWin - 1)) {
                            break;
                        }

                        int[] next_position = this.playground.gameEvent.getNextPosition(last_checking_position[0], last_checking_position[1], direction);

                        if (next_position == null) {
                            break;
                        }

                        String current_checking_position_turn = board[last_checking_position[0]][last_checking_position[1]].getText().trim();
                        String next_position_turn = board[next_position[0]][next_position[1]].getText().trim();

                        // ===> Focus on Maximizer (Bot Matches)
                        if (
                                next_position_turn.equalsIgnoreCase(bot_turn) &&
                                (current_checking_position_turn.equalsIgnoreCase(bot_turn) || current_checking_position_turn.isEmpty())
                        ) {
                            paired_direction_max_score[direction_index]++;
                            System.out.println(paired_direction_max_score[direction_index]+ " "+ next_position[0] + " "+ next_position[1] + " #0001 here -> RC: " + r + " " + " " + c);

                        }else if (
                                next_position_turn.trim().isEmpty() &&
                                current_checking_position_turn.equalsIgnoreCase(bot_turn)
                        ){
                            System.out.println(next_position[0] + " "+ next_position[1] + " #0.5 here -> RC: " + r + " " + " " + c);
                            if (max_outlier_predict_score == 0) {
                                max_outlier_predict_score++;
                            }
                            break;
                        }

                        // <===

                        // ===> Focus on Minimizer (Player Matches)
                        else if (
                                next_position_turn.equalsIgnoreCase(player_turn) &&
                                (current_checking_position_turn.equalsIgnoreCase(player_turn) || current_checking_position_turn.isEmpty())
                        ){
                            paired_direction_min_score[direction_index]--;
                            System.out.println(next_position[0] + " "+ next_position[1] + " #1 here -> RC: " + r + " " + " " + c);
                        }else if (
                                next_position_turn.trim().isEmpty() &&
                                current_checking_position_turn.equalsIgnoreCase(player_turn)
                        ){
                            System.out.println(next_position[0] + " "+ next_position[1] + " #2 here -> RC: " + r + " " + " " + c);
                            if (min_outlier_predict_score == 0) {
                                min_outlier_predict_score--;
                            }
                            break;
                        }
                        // <===

                        else{
                            break;
                        }
                        last_paired_checking_position[direction_index] = next_position;
                    }

                }

                int sum_min_score = Arrays.stream(paired_direction_min_score).sum();
                int sum_max_score = Arrays.stream(paired_direction_max_score).sum();

                if (sum_min_score < min_score) min_score = sum_min_score;
                if (sum_max_score > max_score) max_score = sum_max_score;

                if (sum_min_score <= -(this.playground.maxMatchToWin - 1)){
                    strong_min_score = true;
                }

                if (sum_max_score >= (this.playground.maxMatchToWin - 1)){
                    strong_max_score = true;
                }



                if (!strong_min_score && !strong_max_score){ // <== Find more outlier score if no wining position yet.
                    for (int direction_index=0; direction_index<=1; direction_index++){
                        String opposite_direction = p_direction[direction_index == 0 ? 1 : 0];
                        int[] opposite_position = this.playground.gameEvent.getNextPosition(r, c, opposite_direction);

                        // ==> Calculate for additional max outlier predict score
                        if (paired_direction_max_score[direction_index] >= (this.playground.maxMatchToWin-2)){
                            if (opposite_position != null){
                                String opposite_position_turn = board[opposite_position[0]][opposite_position[1]].getText().trim();
                                if (opposite_position_turn.isEmpty()){
                                    max_outlier_predict_score++;
                                }
                            }else{
                                max_outlier_predict_score--;
                            }
                        }
                        // <===

                        // ==> Calculate for additional min outlier predict score
                        if (paired_direction_min_score[direction_index] <= -(this.playground.maxMatchToWin-2)){
                            System.out.printf("#1 RC: %d %d has xxx\n", r, c);
                            if (opposite_position != null){
                                String opposite_position_turn = board[opposite_position[0]][opposite_position[1]].getText().trim();
                                if (opposite_position_turn.isEmpty()){
                                    min_outlier_predict_score--;
                                }
                            }else{
                                min_outlier_predict_score++;
                            }
                        }
                        // <===
                    }
                }
            }


            // ===> Current Position Score Adjustment
            if (min_score < 0){
                min_score--;
            }

            if (max_score > 0){
                max_score++;
            }
            // <===


            // Apply outlier Score

            min_score+=min_outlier_predict_score;
            max_score+=max_outlier_predict_score;

            // <===




            TaskResult result = new TaskResult();
            result.r = r;
            result.c = c;
            result.min_score = min_score;
            result.max_score = max_score;
            result.strong_min_score = strong_min_score;
            result.strong_max_score = strong_max_score;
            return result;
        };
    }

    private void hard_bot(){
        int boardSize = this.playground.boardSize;
        int[] play_position = null;
        JButton[][] board = this.playground.board;
        String bot_turn = this.playground.getPlayerTurn().trim().equalsIgnoreCase("x") ? "o" : "x";


        if ((this.playground.x_played_move_count+this.playground.o_played_move_count) == 0){
            int center_position = (int) Math.floor((double) boardSize/2);
            play_position = new int[]{center_position, center_position};
        }else{
            ArrayList<Callable<TaskResult>> task_list = new ArrayList<>();

            for (int r = 0; r < boardSize; r++) {
                for (int c = 0; c < boardSize; c++) {
                    Callable<TaskResult> new_task = this.hardBotTask(r, c);
                    task_list.add(new_task);
                }
            }


            try {
                // Run all tasks and wait for them to finish
                List<Future<TaskResult>> results = hard_bot_task_hard_bot_task_executor.invokeAll(task_list);
                int[] min_position_to_play = new int[2];
                int[] max_position_to_play = new int[2];
                int max_score = Integer.MIN_VALUE;
                int min_score = Integer.MAX_VALUE;

                for (Future<TaskResult> future : results) {
                    TaskResult result = future.get();
                    if (result == null) {
                        continue;
                    }
                    // ===> Pick Lowest Min and Highest Max Score Position
                    if ((result.min_score < min_score) || (result.strong_min_score)) {
                        min_score = result.min_score;
                        min_position_to_play = new int[]{result.r, result.c};
                    }

                    if ((result.max_score > max_score) || (result.strong_max_score)) {
                        max_score = result.max_score;
                        max_position_to_play = new int[]{result.r, result.c};
                    }
                    // <===
                    System.out.println("Min: " + result.min_score + " Max: " + result.max_score + " Pos: " + result.r + " " + result.c);
                }
                System.out.println("Picked-> Min: " + min_score + " Max: " + max_score);
                System.out.println("Picked-> Min Pos: " + min_position_to_play[0] + " " + min_position_to_play[1]);
                System.out.println("Picked-> Max Pos: " + max_position_to_play[0] + " " + max_position_to_play[1]);

                // ===> Calculate to choose position between Min or Max to play.
                if (max_score >= this.playground.maxMatchToWin) {
                    play_position = max_position_to_play;
                } else if (min_score <= -this.playground.maxMatchToWin) {
                    play_position = min_position_to_play;
                } else if (max_score > 0) {
                    play_position = max_position_to_play;
                } else {
                    // ===> Play Any Random Move if no score
                    ArrayList<Integer[]> available_move = new ArrayList<>();
                    for (int r = 0; r < boardSize; r++) {
                        for (int c = 0; c < boardSize; c++) {
                            if (board[r][c].getText().trim().equalsIgnoreCase("")) {
                                available_move.add(new Integer[]{r, c});
                            }
                        }
                    }
                    if (!available_move.isEmpty()) {
                        Collections.shuffle(available_move);
                        play_position = new int[]{available_move.get(0)[0], available_move.get(0)[1]};
                    }
                    // <===
                }
                // <====

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }


        if (play_position != null){
            this.playground.playMove(play_position[0], play_position[1], bot_turn);
        }else{
            throw new RuntimeException("Play Position should not be null!");
        }
    }


}
