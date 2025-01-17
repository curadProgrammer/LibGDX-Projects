package com.mygdx.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

import java.util.Map;

public class AnimationComponent implements Component, Pool.Poolable {
    // we will be storing the state and then the animation
    public Map<String, Animation<TextureRegion>> animationMap;
    public TextureRegion currentFrame;
    public boolean isFlip = true;

    @Override
    public void reset() {
        animationMap = null;
        currentFrame = null;
        isFlip = false;
    }
}
