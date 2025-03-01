package com.mygdx.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Pool;

public class B2BodyComponent implements Component, Pool.Poolable {
    public Body body;
    public boolean isDead = false;
    public boolean setToDestroy = false;

    public boolean hasCategoryBit(short categoryBit){
        for (Fixture fixture : body.getFixtureList()) {
            if (fixture.getFilterData().categoryBits == categoryBit) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void reset() {
        if (body != null) {
            body.getWorld().destroyBody(body);
            body = null;
        }

        isDead = false;
        setToDestroy = false;
    }
}