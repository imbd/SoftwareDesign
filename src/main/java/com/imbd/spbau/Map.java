package com.imbd.spbau;

import com.imbd.spbau.creature.Enemy;
import com.imbd.spbau.creature.Hero;
import com.imbd.spbau.strategy.DirectedStrategy;
import com.imbd.spbau.strategy.RandomStrategy;

import java.util.Random;

/**
 * Class for creating new level and keeping all necessary information about it
 */
public class Map {

    /**
     * Enumeration of cell's types
     */
    public enum CellType {
        EMPTY,
        WALL,
        HERO,
        ENEMY,
        TREASURE,
        FREEZE
    }

    private static final char[] directions = {'U', 'D', 'L', 'R'};
    private static final int MAP_LENGTH = 48;
    private static final int MAP_HEIGHT = 24;
    private static final int ENEMY_NUMBER = 20;
    private static final int WALL_NUMBER = 40;
    private static final int FREEZE_POWER = 5;
    private static final Random random = new Random();

    private CellType[][] map = new CellType[MAP_LENGTH][MAP_HEIGHT];
    private Enemy[] enemyArray;
    private Hero hero;
    private Position treasurePosition;
    private Position freezePosition;

    /**
     * Class for comfortable work with map's position
     */
    public static class Position {
        private int x;
        private int y;

        /**
         * Constructor
         *
         * @param x x-coordinate
         * @param y y-coordinate
         */
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Getting x-coordinate value
         *
         * @return x-coordinate value
         */
        public int getX() {
            return x;
        }

        /**
         * Getting y-coordinate value
         *
         * @return y-coordinate value
         */
        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Position && ((Position) o).x == this.x && ((Position) o).y == this.y;
        }
    }

    /**
     * Getting number of moves which freeze will work
     *
     * @return freeze moves number
     */
    public int getFreezePower() {
        return FREEZE_POWER;
    }

    /**
     * Getting position of freeze bonus
     *
     * @return freeze position
     */
    public Position getFreezePosition() {
        return freezePosition;
    }

    /**
     * Getting all enemies of the map
     *
     * @return array of enemies
     */
    public Enemy[] getEnemyArray() {
        return enemyArray;
    }

    /**
     * Getting hero of the map
     *
     * @return hero
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Get map's length
     *
     * @return map's length
     */
    public int getMapLength() {
        return MAP_LENGTH;
    }

    /**
     * Get map's height
     *
     * @return map's height
     */
    public int getMapHeight() {
        return MAP_HEIGHT;
    }

    /**
     * Getting position of treasure
     *
     * @return position of treasure
     */
    public Position getTreasurePosition() {
        return treasurePosition;
    }

    /**
     * Creating object with a given type
     *
     * @param cellType type of object to create
     * @return position of created object
     */
    private Position createItem(CellType cellType) {
        while (true) {
            int x = Math.abs(random.nextInt()) % MAP_LENGTH;
            int y = Math.abs(random.nextInt()) % MAP_HEIGHT;
            if (map[x][y] == CellType.EMPTY) {
                map[x][y] = cellType;
                return new Position(x, y);
            }
        }
    }

    /**
     * Creating of map and all objects on it
     *
     * @param level game level
     */
    public Map(int level) {
        for (int i = 0; i < MAP_LENGTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                map[i][j] = CellType.EMPTY;
            }
        }
        for (int i = 0; i < WALL_NUMBER; i++) {
            createItem(CellType.WALL);
        }
        int level_enemy_number = ENEMY_NUMBER + 4 * level;
        enemyArray = new Enemy[level_enemy_number];
        for (int i = 0; i < (level_enemy_number); i++) {
            enemyArray[i] = new Enemy(createItem(CellType.ENEMY), new DirectedStrategy(directions[Math.abs(random.nextInt()) % directions.length]));
        }
        hero = new Hero(createItem(CellType.HERO), new RandomStrategy());
        treasurePosition = createItem(CellType.TREASURE);
        freezePosition = createItem(CellType.FREEZE);
    }

    /**
     * Getting type of cell
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return type of cell with given coordinates
     */
    public CellType getMapCell(int x, int y) {
        return map[x][y];
    }

    /**
     * Changing type of given position
     *
     * @param position position which will be changed
     * @param cellType type to set
     */
    public void setMapCell(Position position, CellType cellType) {
        map[position.getX()][position.getY()] = cellType;
    }
}
