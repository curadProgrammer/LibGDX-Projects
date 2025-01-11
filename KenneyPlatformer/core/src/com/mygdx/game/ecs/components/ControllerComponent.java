package com.mygdx.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ControllerComponent implements Component, Pool.Poolable {
    public boolean left, right, up;

    @Override
    public void reset() {
        left = false;
        right = false;
        up = false;
    }
}
