package org.tictactoe.app.bot;

/* Java Swing Import */
import org.tictactoe.app.playground.Playground;

import javax.swing.*;

/* --- */

/* Java Utils Imports */
import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
/* --- */



public class Bot {
    private final Playground playground;
    private final int difficulty;

    private static final ExecutorService bot_play_executor = Executors.newFixedThreadPool(1);

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
                    new EasyBot(this.playground);
                    break;
                }
                case 1: {
                    new NormalBot(this.playground);
                    break;
                }
                case 2: {
                    new HardBot(this.playground);
                    break;
                }
                default:{
                    throw new IllegalArgumentException("Invalid difficulty level: " + this.difficulty);

                }
            }
            this.playground.isWorking = false;
        });
    }


}


