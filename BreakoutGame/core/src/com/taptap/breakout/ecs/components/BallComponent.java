package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;


public class BallComponent implements Pool.Poolable, Component {
    public float xVel = 0;
    public float yVel = 0;
    public boolean isDead = false;

    @Override
    public void reset() {
        System.out.println("Ball is reset");
        xVel = 0;
        yVel = 0;
        isDead = false;
    }
}
