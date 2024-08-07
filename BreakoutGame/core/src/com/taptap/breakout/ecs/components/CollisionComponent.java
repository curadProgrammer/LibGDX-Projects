package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/*
    Entity has the ability to collide
 */
public class CollisionComponent implements Component, Pool.Poolable {
    // other entity
    public Entity collisionEntity;
    public boolean canCollide = true;

    @Override
    public void reset() {
        collisionEntity = null;
        canCollide = true;
    }
}
