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
    public boolean canJump = true;
    public boolean isGrounded = true;

    public final float coyoteTime = 0.1f; // 200 milliseconds grace period
    public float coyoteTimer = 0;

    public final float jumpBufferTime = 0.1f;
    public float jumpBufferCounter = 0;

    @Override
    public void reset() {
        currentState = MovementState.IDLE;
        coyoteTimer = 0;
        isGrounded = true;
        jumpBufferCounter = 0;
        canJump = true;
    }
}
