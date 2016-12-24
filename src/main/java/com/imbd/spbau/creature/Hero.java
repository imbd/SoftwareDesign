package com.imbd.spbau.creature;

import com.imbd.spbau.Map;
import com.imbd.spbau.strategy.Strategy;

/**
 * Class for your hero
 */
public class Hero implements Creature {

    private Map.Position position;
    private Strategy strategy;

    /**
     * Hero constructor
     *
     * @param position hero position
     * @param strategy hero strategy
     */
    public Hero(Map.Position position, Strategy strategy) {
        this.position = position;
        this.strategy = strategy;
    }

    @Override
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Strategy getStrategy() {
        return strategy;
    }

    @Override
    public Map.Position getPosition() {
        return position;
    }

    @Override
    public Map.Position makeMove(Map worldMap, Strategy strategy) {
        position = strategy.getMove(this, worldMap);
        return position;
    }
}
