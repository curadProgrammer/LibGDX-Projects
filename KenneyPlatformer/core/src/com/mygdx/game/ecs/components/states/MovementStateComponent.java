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

    public boolean isFacingLeft = false;

    public MovementState currentState = MovementState.IDLE;
    public boolean isGrounded = true;

    public final float coyoteTime = 0.1f; // 100 milliseconds grace period
    public float coyoteTimer = 0;

    public final float jumpBufferTime = 0.05f;
    public float jumpBufferCounter = 0;

    @Override
    public void reset() {
        currentState = MovementState.IDLE;
        coyoteTimer = 0;
        isGrounded = true;
        jumpBufferCounter = 0;
        isFacingLeft = false;
    }
}
