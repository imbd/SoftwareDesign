package com.imbd.spbau.strategy;

import com.imbd.spbau.creature.Creature;
import com.imbd.spbau.Map;

import java.util.Random;

/**
 * Class with a strategy for random move
 */
public class RandomStrategy implements Strategy {

    private int[] directionX = {1, -1, 0, 0};
    private int[] directionY = {0, 0, 1, -1};
    private static final int TRYING_NUMBER = 10;
    public static final int RANDOM_IDENTIFIER = 123;
    private Random random = new Random();

    @Override
    public Map.Position getMove(Creature creature, Map worldMap) {

        Map.Position position = creature.getPosition();
        for (int i = 0; i < TRYING_NUMBER; i++) {
            int r = Math.abs(random.nextInt() % 4);
            int x = directionX[r];
            int y = directionY[r];
            if (worldMap.getMapCell((worldMap.getMapLength() + position.getX() + x) % worldMap.getMapLength(),
                    (worldMap.getMapHeight() + position.getY() + y) % worldMap.getMapHeight()) == Map.CellType.EMPTY) {
                return new Map.Position((worldMap.getMapLength() + position.getX() + x) % worldMap.getMapLength(),
                        (worldMap.getMapHeight() + position.getY() + y) % worldMap.getMapHeight());
            }
        }
        return position;
    }

    @Override
    public int getInfo() {
        return RANDOM_IDENTIFIER;
    }
}
