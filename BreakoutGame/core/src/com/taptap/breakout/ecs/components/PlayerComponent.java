package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool;

/*
    Entity has the ability to be a Player (describe properties of player here)
 */
public class PlayerComponent implements Component, Pool.Poolable {
    public OrthographicCamera cam;

    // used to indicate what level the paddle is in
    public enum Level{LEVEL1, LEVEL2, LEVEL3}
    public Level currentLevel;

    @Override
    public void reset() {
        currentLevel = Level.LEVEL1;
    }
}
