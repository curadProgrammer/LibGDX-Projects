package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

// describes the entity to have a box 2d body
public class B2BodyComponent implements Component, Pool.Poolable {
    public Body body;
    public boolean isDead = false;
    public boolean setToDestroy = false;

    @Override
    public void reset() {
        body = null;
        isDead = false;
        setToDestroy = false;
    }
}
