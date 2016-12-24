package com.imbd.spbau;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.imbd.spbau.creature.Creature;
import com.imbd.spbau.strategy.DirectedStrategy;
import com.imbd.spbau.strategy.KeyboardStrategy;
import com.imbd.spbau.strategy.RandomStrategy;
import com.imbd.spbau.strategy.Strategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestGame {

    public Screen screen;
    GameController controller;

    @Before
    public void initialize() throws IOException {

        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        controller = new GameController(screen, new KeyboardStrategy(screen));
    }

    @Test
    public void testGame1() throws IOException {

        controller.setPlayerStrategy(new RandomStrategy());
        int livesNumber = 10;
        while (livesNumber > 0) {
            if (!controller.run()) {
                return;
            } else {
                livesNumber--;
                controller.setOnStart();
            }
        }
    }

    @Test
    public void testGame2() throws IOException {

        controller.setPlayerStrategy(new DirectedStrategy('U'));
        int livesNumber = 20;
        while (livesNumber > 0) {
            if (!controller.run()) {
                return;
            } else {
                livesNumber--;
                controller.setOnStart();
            }
        }
    }

    @Test
    public void testGame3() throws IOException {

        controller.setPlayerStrategy(new Strategy() {
            @Override
            public Map.Position getMove(Creature creature, Map worldMap) {
                return creature.getPosition();
            }

            @Override
            public int getInfo() {
                return -1;
            }
        });
        int livesNumber = 10;
        while (livesNumber > 0) {
            if (!controller.run()) {
                return;
            } else {
                livesNumber--;
                controller.setOnStart();
            }
        }
    }

    @After
    public void close() throws IOException {
        screen.stopScreen();
    }
}
