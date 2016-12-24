package com.imbd.spbau.strategy;

import com.imbd.spbau.creature.Creature;
import com.imbd.spbau.Map;

/**
 * Interface for all strategies for creatures
 */
public interface Strategy {

    Map.Position getMove(Creature creature, Map worldMap);

    int getInfo();
}
