package org.tictactoe.app.utils;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class Utils {
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    public static Font getFont(float size) {
        try {
            // Load font from resources (classpath)
            InputStream is = Objects.requireNonNull(
                    Utils.class.getResourceAsStream("/fonts/ThaleahFat.ttf")
            );

            Font thaleah = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);

            // Register font once with the graphics environment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(thaleah);

            return thaleah;
        } catch (Exception e) {
            LOGGER.severe("Failed to load Thaleah font: " + e.getMessage());
            // Fallback to default font if loading fails
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }

    public static <T> T[] shuffleArray(T[] array) {
        List<T> list = Arrays.asList(array);
        Collections.shuffle(list);
        return array;
    }
}

