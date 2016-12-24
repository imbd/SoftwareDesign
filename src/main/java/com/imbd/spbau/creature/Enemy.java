package com.imbd.spbau.creature;

import com.imbd.spbau.Map;
import com.imbd.spbau.strategy.Strategy;

/**
 * Class for creatures who are your enemies
 */
public class Enemy implements Creature {

    private Map.Position position;
    private Strategy strategy;

    /**
     * Enemy constructor
     *
     * @param position enemy position
     * @param strategy enemy strategy
     */
    public Enemy(Map.Position position, Strategy strategy) {
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
    public Map.Position makeMove(Map worldMap, Strategy strategy) {
        position = strategy.getMove(this, worldMap);
        return position;
    }

    @Override
    public Map.Position getPosition() {
        return position;
    }

}
