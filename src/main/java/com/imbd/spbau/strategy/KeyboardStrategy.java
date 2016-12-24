package com.imbd.spbau.strategy;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.imbd.spbau.creature.Creature;
import com.imbd.spbau.Map;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Class with a strategy for keyboard control
 */
public class KeyboardStrategy implements Strategy {

    private Screen screen;
    private static final int TRYING_NUMBER = 20;
    private static final int EOF_FOUND = -1;
    private static final int KEY_REPEAT = -2;
    public static final int KEYBOARD_IDENTIFIER = 8;
    private static final Logger LOGGER = Logger.getLogger(KeyboardStrategy.class.getName());

    /**
     * Getting key repeat identifier
     *
     * @return key repeat identifier
     */
    public static int getKeyRepeat() {
        return KEY_REPEAT;
    }

    /**
     * Getting eof identifier
     *
     * @return eof identifier
     */
    public static int getEofFound() {
        return EOF_FOUND;
    }

    /**
     * Keyboard strategy constructor
     *
     * @param screen screen to use
     */
    public KeyboardStrategy(Screen screen) {
        this.screen = screen;
    }

    @Override
    public Map.Position getMove(Creature creature, Map worldMap) {

        KeyStroke key = null;
        Map.Position position = creature.getPosition();
        int x = position.getX();
        int y = position.getY();
        for (int i = 0; i < TRYING_NUMBER; i++) {
            try {
                key = screen.readInput();
            } catch (IOException e) {
                LOGGER.info("Couldn't read key from input");
            }

            KeyType keyType = KeyType.EOF;
            if (key != null) {
                keyType = key.getKeyType();
            }
            int newX = -1;
            int newY = -1;
            switch (keyType) {
                case ArrowDown:
                    newX = (worldMap.getMapLength() + x) % worldMap.getMapLength();
                    newY = (worldMap.getMapHeight() + y + 1) % worldMap.getMapHeight();
                    break;

                case ArrowUp:
                    newX = (worldMap.getMapLength() + x) % worldMap.getMapLength();
                    newY = (worldMap.getMapHeight() + y - 1) % worldMap.getMapHeight();
                    break;

                case ArrowLeft:
                    newX = (worldMap.getMapLength() + x - 1) % worldMap.getMapLength();
                    newY = (worldMap.getMapHeight() + y) % worldMap.getMapHeight();
                    break;

                case ArrowRight:
                    newX = (worldMap.getMapLength() + x + 1) % worldMap.getMapLength();
                    newY = (worldMap.getMapHeight() + y) % worldMap.getMapHeight();
                    break;
                case EOF:
                    return new Map.Position(EOF_FOUND, EOF_FOUND);
                default:
                    break;
            }
            if (newX != -1 && (worldMap.getMapCell(newX, newY) != Map.CellType.WALL)) {
                return new Map.Position(newX, newY);
            }
        }
        return new Map.Position(KEY_REPEAT, KEY_REPEAT);
    }

    @Override
    public int getInfo() {
        return KEYBOARD_IDENTIFIER;
    }
}
