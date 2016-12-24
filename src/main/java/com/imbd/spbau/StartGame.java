package com.imbd.spbau;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.imbd.spbau.strategy.KeyboardStrategy;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Main-class
 */
public class StartGame {

    private static final Logger LOGGER = Logger.getLogger(StartGame.class.getName());

    /**
     * Initializing of screen and starting a game
     */
    public static void main(String[] args) {

        Terminal terminal;
        Screen screen = null;
        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameController controller = new GameController(screen, new KeyboardStrategy(screen));
        try {
            while (true) {
                if (!controller.run()) {
                    if (screen != null) {
                        screen.stopScreen();
                    }
                    return;
                } else {
                    controller.setOnStart();
                }
            }
        } catch (IOException e) {
            LOGGER.info("Couldn't stop screen");
        }
    }
}
