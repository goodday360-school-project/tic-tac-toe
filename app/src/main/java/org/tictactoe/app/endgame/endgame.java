package org.tictactoe.app.endgame;

import java.io.*;
import java.util.*;
public class endgame {

    private static final String FILE_NAME = "stats.txt";

    public static void addResult(String result) {
        int wins = 0, losses = 0, draws = 0;

        // read old stats (if exist)
        try (Scanner sc = new Scanner(new File(FILE_NAME))) {
            wins = Integer.parseInt(sc.nextLine().split("=")[1]);
            losses = Integer.parseInt(sc.nextLine().split("=")[1]);
            draws = Integer.parseInt(sc.nextLine().split("=")[1]);
        } catch (Exception ignored) {}

        // update the result after play
        if (result.equals("win")) wins++;
        else if (result.equals("lose")) losses++;
        else draws++;

        // save back the
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            pw.println("wins=" + wins);
            pw.println("losses=" + losses);
            pw.println("draws=" + draws);
        } catch (Exception e) {
            System.out.println("Error saving stats");
        }
    }

    public static void showStats() {
        try (Scanner sc = new Scanner(new File(FILE_NAME))) {
            System.out.println("\n=== Player Stats ===");
            System.out.println(sc.nextLine());
            System.out.println(sc.nextLine());
            System.out.println(sc.nextLine());
        } catch (Exception e) {
            System.out.println("No stats yet.");
        }
    }
}

