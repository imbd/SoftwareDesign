package com.imbd.spbau;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.imbd.spbau.creature.Enemy;
import com.imbd.spbau.creature.Hero;
import com.imbd.spbau.strategy.KeyboardStrategy;
import com.imbd.spbau.strategy.Strategy;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Class which provides all game logic
 */
public class GameController {

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private static final int MAX_MOVES_NUMBER = 1000;

    private int level = 1;
    private int freezeMoves = 0;
    private Screen screen;
    private Strategy playerStrategy;


    GameController(Screen screen, Strategy playerStrategy) {
        this.playerStrategy = playerStrategy;
        this.screen = screen;
    }

    /**
     * Set player strategy
     *
     * @param playerStrategy new player strategy
     */
    public void setPlayerStrategy(Strategy playerStrategy) {
        this.playerStrategy = playerStrategy;
    }

    /**
     * Handle completing a level
     */
    public void levelUp() {

        try {

            LOGGER.info("Win, starting next level");
            level += 1;
            run();
        } catch (IOException e) {
            LOGGER.info("Couldn't start or stop screen");
        }
    }

    /**
     * Handle lose
     */
    public void setOnStart() {
        level = 1;
        freezeMoves = 0;
    }

    /**
     * Running a game
     *
     * @return false if EOF was found else true
     */
    public boolean run() throws IOException {

        int movesNumber = 0;
        TerminalSize terminalSize = screen.getTerminalSize();
        screen.setCursorPosition(new TerminalPosition(terminalSize.getColumns(), terminalSize.getRows()));
        if (level == 1) {
            screen.startScreen();
        }
        screen.clear();
        Map worldMap = new Map(level);
        worldMap.getHero().setStrategy(playerStrategy);
        Printer printer = new Printer(screen, worldMap);
        printer.print(level, movesNumber);

        while (true) {

            Hero hero = worldMap.getHero();
            Map.Position position = hero.getPosition();
            worldMap.setMapCell(position, Map.CellType.EMPTY);

            Map.Position newHeroPosition = hero.makeMove(worldMap, hero.getStrategy());
            if (hero.getStrategy().getInfo() == KeyboardStrategy.KEYBOARD_IDENTIFIER && newHeroPosition.getX() == KeyboardStrategy.getKeyRepeat()) {
                LOGGER.info("No move or just long key repeat");
                return true;
            }
            if (hero.getStrategy().getInfo() == KeyboardStrategy.KEYBOARD_IDENTIFIER && newHeroPosition.getX() == KeyboardStrategy.getEofFound()) {
                LOGGER.info("EOF found");
                return false;
            }

            worldMap.setMapCell(hero.getPosition(), Map.CellType.HERO);
            movesNumber++;
            printer.print(level, movesNumber);
            if (hero.getPosition().equals(worldMap.getTreasurePosition())) {
                levelUp();
                return true;
            }

            if (hero.getPosition().equals(worldMap.getFreezePosition())) {
                freezeMoves = worldMap.getFreezePower();
            }
            if (freezeMoves > 0) {
                freezeMoves--;
                continue;
            }

            Enemy[] enemies = worldMap.getEnemyArray();
            boolean lost = false;
            for (Enemy enemy : enemies) {
                Map.Position pos = enemy.getPosition();
                if (worldMap.getHero().getPosition().equals(enemy.getPosition())) {
                    lost = true;
                }
                worldMap.setMapCell(pos, Map.CellType.EMPTY);
                enemy.makeMove(worldMap, enemy.getStrategy());
                worldMap.setMapCell(enemy.getPosition(), Map.CellType.ENEMY);
                if (worldMap.getHero().getPosition().equals(enemy.getPosition())) {
                    lost = true;
                }
            }
            printer.print(level, movesNumber);
            if (lost || movesNumber > MAX_MOVES_NUMBER) {
                LOGGER.info("Lose, starting new game");
                return true;
            }

        }
    }

}
