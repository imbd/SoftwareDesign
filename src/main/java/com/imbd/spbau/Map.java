package com.imbd.spbau;

import com.imbd.spbau.creature.Enemy;
import com.imbd.spbau.creature.Hero;
import com.imbd.spbau.strategy.DirectedStrategy;
import com.imbd.spbau.strategy.RandomStrategy;

import java.util.Random;

public class Map {

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

    public static class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Position && ((Position) o).x == this.x && ((Position) o).y == this.y;
        }
    }

    public int getFreezePower() {
        return FREEZE_POWER;
    }

    public Position getFreezePosition() {
        return freezePosition;
    }

    public Enemy[] getEnemyArray() {
        return enemyArray;
    }

    public Hero getHero() {
        return hero;
    }

    public int getMapLength() {
        return MAP_LENGTH;
    }

    public int getMapHeight() {
        return MAP_HEIGHT;
    }

    public Position getTreasurePosition() {
        return treasurePosition;
    }

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

    public CellType getMapCell(int x, int y) {
        return map[x][y];
    }

    public void setMapCell(Position position, CellType cellType) {
        map[position.getX()][position.getY()] = cellType;
    }
}
