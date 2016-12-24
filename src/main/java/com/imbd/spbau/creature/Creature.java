package com.imbd.spbau.creature;

import com.imbd.spbau.Map;
import com.imbd.spbau.strategy.Strategy;

/**
 * Interface for all moving creatures
 */
public interface Creature {

    /**
     * @param worldMap map
     * @param strategy moving strategy to use
     * @return position after this move
     */
    Map.Position makeMove(Map worldMap, Strategy strategy);

    /**
     * Return current position
     *
     * @return position
     */
    Map.Position getPosition();

    /**
     * Change moving strategy
     *
     * @param strategy strategy to set
     */
    void setStrategy(Strategy strategy);

    /**
     * Get current strategy
     *
     * @return creature's strategy
     */
    Strategy getStrategy();
}
