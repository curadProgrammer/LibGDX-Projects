package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/*
    Entity has the ability to transform its coordinates (i.e. move)
 */
public class TransformComponent implements Component, Pool.Poolable {
    public final Vector3 position = new Vector3();
    public final Vector2 scale = new Vector2(1, 1);
    public float rotation = 0.0f;
    public boolean isHidden = false;

    @Override
    public void reset() {

    }
}
