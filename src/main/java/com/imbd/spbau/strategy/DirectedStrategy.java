package com.imbd.spbau.strategy;

import com.imbd.spbau.creature.Creature;
import com.imbd.spbau.Map;

/**
 * Strategy with walking on one direction(it changes to opposite after hitting a wall or other object)
 */
public class DirectedStrategy implements Strategy {

    private static final int[] directionX = {1, 0, 0, -1};
    private static final int[] directionY = {0, -1, 1, 0};
    private static final char[] directions = {'R', 'U', 'D', 'L'};
    private int DIRECTION = -1;

    /**
     * Strategy constructor
     *
     * @param c move direction
     */
    public DirectedStrategy(char c) {
        switch (c) {
            case ('R'):
                DIRECTION = 0;
                break;
            case ('L'):
                DIRECTION = 3;
                break;
            case ('D'):
                DIRECTION = 2;
                break;
            case ('U'):
                DIRECTION = 1;
                break;
            default:
                DIRECTION = -1;
                break;
        }
    }

    /**
     * Get direction's array
     *
     * @return direction's array
     */
    public static char[] getDirections() {
        return directions;
    }

    @Override
    public Map.Position getMove(Creature creature, Map worldMap) {

        Map.Position position = creature.getPosition();
        if (DIRECTION == -1) {
            return creature.getPosition();
        }
        int x = directionX[DIRECTION];
        int y = directionY[DIRECTION];
        int newX = (worldMap.getMapLength() + position.getX() + x) % worldMap.getMapLength();
        int newY = (worldMap.getMapHeight() + position.getY() + y) % worldMap.getMapHeight();

        if (worldMap.getMapCell(newX, newY) == Map.CellType.EMPTY || worldMap.getMapCell(newX, newY) == Map.CellType.HERO) {
            return new Map.Position(newX, newY);
        } else {
            DIRECTION = directions.length - DIRECTION - 1;
            return position;
        }

    }

    @Override
    public int getInfo() {
        return DIRECTION;
    }
}
