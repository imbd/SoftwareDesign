package com.imbd.spbau;


import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.Screen;
import com.imbd.spbau.creature.Enemy;
import com.imbd.spbau.strategy.DirectedStrategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Class for printing current state to given screen
 */
public class Printer {

    private static java.util.Map<Map.CellType, TextCharacter> characterMap;
    private static final TextColor RED = new TextColor.RGB(255, 0, 0);
    private static final TextColor GREEN = new TextColor.RGB(0, 255, 0);
    private static final TextColor BLACK = new TextColor.RGB(0, 0, 0);
    private static final TextColor WHITE = new TextColor.RGB(255, 255, 255);
    private static final int TAB = 2;
    private static final Logger LOGGER = Logger.getLogger(Printer.class.getName());

    private static final String MOVES_NUMBER = "MOVES NUMBER: ";
    private static final String LEVEL = "LEVEL: ";
    private static final String INTRO = "HINT";
    private static final String EMPTY = "EMPTY CELL";
    private static final String WALL = "WALL";
    private static final String HERO = "YOUR HERO";
    private static final String TREASURE = "TREASURE(GO TO IT)";
    private static final String ENEMY = "ENEMY(WITH MOVE DIRECTION)";
    private static final String SEPARATOR = " - ";

    private Screen screen;
    private Map worldMap;

    static {
        characterMap = new HashMap<>();
        characterMap.put(Map.CellType.EMPTY, new TextCharacter('.', BLACK, WHITE, SGR.BORDERED));
        characterMap.put(Map.CellType.WALL, new TextCharacter('W', WHITE, new TextColor.RGB(160, 160, 160), SGR.BORDERED));
        characterMap.put(Map.CellType.HERO, new TextCharacter('I', RED, GREEN, SGR.BLINK));
        characterMap.put(Map.CellType.ENEMY, new TextCharacter('@', WHITE, RED, SGR.BORDERED));
        characterMap.put(Map.CellType.TREASURE, new TextCharacter('T', WHITE, new TextColor.RGB(255, 180, 100), SGR.BLINK));
        characterMap.put(Map.CellType.FREEZE, new TextCharacter('*', WHITE, new TextColor.RGB(0, 255, 255), SGR.BLINK));
    }

    Printer(Screen screen, Map worldMap) {
        this.screen = screen;
        this.worldMap = worldMap;
    }

    /**
     * Print all information to given screen
     *
     * @param level       current level
     * @param movesNumber current moves number
     */
    public void print(int level, int movesNumber) {
        Enemy[] enemies = worldMap.getEnemyArray();
        for (int x = 0; x < worldMap.getMapLength(); x++) {
            for (int y = 0; y < worldMap.getMapHeight(); y++) {
                screen.setCharacter(x, y, characterMap.get(worldMap.getMapCell(x, y)));
            }
        }
        for (Enemy enemy : enemies) {
            if (enemy.getStrategy().getInfo() != -1) {
                int x = enemy.getPosition().getX();
                int y = enemy.getPosition().getY();
                TextCharacter textCharacter = characterMap.get(worldMap.getMapCell(x, y)).withCharacter(DirectedStrategy.getDirections()[enemy.getStrategy().getInfo()]);
                screen.setCharacter(x, y, textCharacter);
            }
        }
        printString(worldMap.getMapLength() + TAB, TAB, LEVEL + String.valueOf(level));
        printString(worldMap.getMapLength() + TAB, 2 * TAB, MOVES_NUMBER + String.valueOf(movesNumber));
        printHelp(worldMap.getMapLength() + TAB, 4 * TAB);
        try {
            screen.refresh();
        } catch (IOException e) {
            LOGGER.info("Exception caught during refresh");
        }
    }

    private void printString(int x, int row, String stringValue) {
        for (int i = 0; i < stringValue.length(); i++) {
            screen.setCharacter(x + i, row, new TextCharacter(stringValue.charAt(i), WHITE, BLACK, SGR.BOLD));
        }
    }

    private void printHelp(int x, int row) {
        printString(x, row++, INTRO);
        row++;
        screen.setCharacter(x, row, characterMap.get(Map.CellType.EMPTY));
        printString(x + 1, row++, SEPARATOR + EMPTY);
        row++;
        screen.setCharacter(x, row, characterMap.get(Map.CellType.WALL));
        printString(x + 1, row++, SEPARATOR + WALL);
        row++;
        screen.setCharacter(x, row, characterMap.get(Map.CellType.HERO));
        printString(x + 1, row++, SEPARATOR + HERO);
        row++;
        screen.setCharacter(x, row, characterMap.get(Map.CellType.ENEMY));
        printString(x + 1, row++, SEPARATOR + ENEMY);
        row++;
        screen.setCharacter(x, row, characterMap.get(Map.CellType.TREASURE));
        printString(x + 1, row++, SEPARATOR + TREASURE);
        row++;
        screen.setCharacter(x, row, characterMap.get(Map.CellType.FREEZE));
        String freeze = "FREEZE";
        printString(x + 1, row, SEPARATOR + freeze + "(FOR " + worldMap.getFreezePower() + " MOVES)");
    }
}
