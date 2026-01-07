package org.tictactoe.app.playground;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tictactoe.app.utils.Utils;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class GameStatus {
    private final HashMap<String, Object> GAME_STATUS_DATA;
    private final Playground playground;

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

        this.saveGameStatus();
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

    private void saveGameStatus() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jarDir = Utils.getJarDir();
            mapper.writeValue(new File(jarDir, "game_status.json"), GAME_STATUS_DATA);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
