package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/*
    Entity has the ability to use textures
 */
public class TextureComponent implements Component, Pool.Poolable {
    public TextureRegion region = null;

    @Override
    public void reset() {
        region = null;
    }
}
