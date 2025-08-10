package com.mygdx.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/*
    Entity has the ability to collide
 */
public class CollisionComponent implements Component, Pool.Poolable {
    // other entity
    public Entity collisionEntity;
    public boolean canCollide = true; // N: I'm thinking of using this for when the user gets injured and are invincible for a certain period of time

    @Override
    public void reset() {
        collisionEntity = null;
        canCollide = true;
    }
}