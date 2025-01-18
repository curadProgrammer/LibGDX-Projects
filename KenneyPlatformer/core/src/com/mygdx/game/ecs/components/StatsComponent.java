package com.mygdx.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.utils.GameUtil;

public class StatsComponent implements Component, Pool.Poolable {

    // todo will also process the user's statcomponent so that we can control their max speed for now we will use a contant value
    // todo refactor this and put it in a component (StatsComponent)
    public float xVelocity = 100 / GameUtil.PPM;
    public float yVelocity = 100 / GameUtil.PPM;

    @Override
    public void reset() {
        xVelocity = 100 / GameUtil.PPM;
        yVelocity = 100 / GameUtil.PPM;
    }
}
