package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/*
    Entity has the ability to have a type
 */
public class TypeComponent implements Component, Pool.Poolable {
    public static final int PLAYER = 0;
    public static final int BLOCK = 1;
    public static final int OTHER = 2;

    public int type = OTHER;

    @Override
    public void reset() {
        type = OTHER;
    }
}
