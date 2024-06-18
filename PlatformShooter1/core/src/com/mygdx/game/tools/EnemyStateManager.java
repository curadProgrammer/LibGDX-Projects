package com.mygdx.game.tools;

import com.mygdx.game.enums.EnemyState;

public class EnemyStateManager {
    // N: I'm thinking of handling the timers and durations here as well
    private EnemyState currentState;

    public EnemyStateManager(){
        // default state
        currentState = EnemyState.IDLE;
    }

    public void setState(EnemyState newState){

    }
}
