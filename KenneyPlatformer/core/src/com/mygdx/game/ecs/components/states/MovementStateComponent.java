package com.mygdx.game.ecs.components.states;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

// a component used to be able to make the player able to move
public class MovementStateComponent implements Component, Pool.Poolable {
    public enum MovementState{
        IDLE,
        WALKING,
        JUMPING,
        FALLING
    }

    public MovementState currentState = MovementState.IDLE;
    public float jumpTimer = 0;
    public boolean canJump = true;

    @Override
    public void reset() {
        currentState = MovementState.IDLE;
        jumpTimer = 0;
        canJump = true;
    }
}
