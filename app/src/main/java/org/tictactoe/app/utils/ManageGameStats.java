package org.tictactoe.app.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;


public class ManageGameStats {
    public static File FILE = new File(Utils.getJarDir(), "game_stats.json");
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static class GameStats {
        public int wins;
        public int losses;
        public int draws;
    }

    public static GameStats GAME_STATS = new GameStats();

    public static void saveGameStats(GameStats stats) {
        try {
            mapper.writeValue(FILE, stats);
        } catch (IOException e) {
            System.err.println("Error writing: " + e.getMessage());
        }
    }

    public static GameStats getGameStats() {
        try {
            if (FILE.exists()) {
                return mapper.readValue(FILE, GameStats.class);
            }else{
                return GAME_STATS;
            }
        } catch (IOException e) {
            System.err.println("Error reading: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
