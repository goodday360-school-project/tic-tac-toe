package org.tictactoe.app.playground;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tictactoe.app.utils.Utils;

import java.awt.*;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

/* Javax Swing Imports */
import javax.swing.*;
/* --- */


public class GameStatus {
    private HashMap<String, Object> GAME_STATUS_DATA;
    private Playground playground;

    // ==> For New Game
    public GameStatus(int difficulty, Playground playground) {
        this.playground = playground;

        GAME_STATUS_DATA = new HashMap<>(Map.of(
                "difficulty", difficulty,
                "player_turn", this.playground.getPlayerTurn(),
                "current_turn", this.playground.getCurrentTurn(),
                "x_played_move_count", this.playground.x_played_move_count,
                "o_played_move_count", this.playground.o_played_move_count,
                "game_end", this.playground.gameEnd,
                "board", new HashMap<String, String>()
        ));
    }
    // <===


    // ===> For continue Game
    public GameStatus(Playground playground) {
        this.playground = playground;
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jarDir = Utils.getJarDir();
            File file = new File(jarDir, "game_status.json");

            if (file.exists()) {

                @SuppressWarnings("unchecked")
                HashMap<String, Object> loadedData = mapper.readValue(file, HashMap.class);

                this.GAME_STATUS_DATA = loadedData;

                this.playground.difficulty = (int) GAME_STATUS_DATA.get("difficulty");
                this.playground.player_turn = (String) GAME_STATUS_DATA.get("player_turn");

                this.playground.current_turn = (String) GAME_STATUS_DATA.get("current_turn");
                this.playground.x_played_move_count = (int) GAME_STATUS_DATA.get("x_played_move_count");
                this.playground.o_played_move_count = (int) GAME_STATUS_DATA.get("o_played_move_count");
                this.playground.gameEnd = (boolean) GAME_STATUS_DATA.get("game_end");

                @SuppressWarnings("unchecked")
                HashMap<String, String> board = (HashMap<String, String>) GAME_STATUS_DATA.get("board");

                for (Map.Entry<String, String> entry : board.entrySet()) {
                    String[] key = entry.getKey().split("-");
                    int r = Integer.parseInt(key[0]);
                    int c = Integer.parseInt(key[1]);
                    JButton tile = this.playground.board[r][c];
                    tile.setText(entry.getValue().toUpperCase());
                    if (tile.getText().trim().equalsIgnoreCase("x")){
                        tile.setForeground(new Color(254,137,9));
                    }else{
                        tile.setForeground(new Color(0,220,255));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // <===

    public static boolean is_can_continue() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jarDir = Utils.getJarDir();
            File file = new File(jarDir, "game_status.json");

            if (file.exists()) {

                @SuppressWarnings("unchecked")
                HashMap<String, Object> data = mapper.readValue(file, HashMap.class);

                return !((boolean) data.get("game_end"));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false;
    }


    public void updateGameStatus(int r, int c, String turn) {

        @SuppressWarnings("unchecked") // ===> ignore checking Type if it's a Map
        Map<String, String> board = (Map<String, String>) GAME_STATUS_DATA.get("board");
        board.put(String.format("%d-%d", r, c), turn);

        this.GAME_STATUS_DATA.put("current_turn", this.playground.getCurrentTurn());
        this.GAME_STATUS_DATA.put("x_played_move_count", this.playground.x_played_move_count);
        this.GAME_STATUS_DATA.put("o_played_move_count", this.playground.o_played_move_count);
        this.GAME_STATUS_DATA.put("game_end", this.playground.gameEnd);

        this.saveGameStatus();
    }



    public void saveGameStatus() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jarDir = Utils.getJarDir();
            mapper.writeValue(new File(jarDir, "game_status.json"), GAME_STATUS_DATA);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
