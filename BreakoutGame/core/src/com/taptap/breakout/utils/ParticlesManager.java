package com.taptap.breakout.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.taptap.breakout.Utilities;

public class ParticlesManager {
    private ParticleEffect prototype;
    private ParticleEffectPool effectPool;
    private Array<ParticleEffectPool.PooledEffect> activeEffects;

    public ParticlesManager(String particlesFile, String imagesDir){
        // load particle effect
        prototype = new ParticleEffect();
        prototype.load(Gdx.files.internal(particlesFile), Gdx.files.internal(imagesDir));

        // create a pool for better performance
        effectPool = new ParticleEffectPool(prototype, 1, 20);
        activeEffects = new Array<>();
    }

    // spawn a new effect at the specified position
    public void spawn(float x, float y){
        ParticleEffectPool.PooledEffect effect = effectPool.obtain();
        effect.setPosition(x, y);
        activeEffects.add(effect);
    }

    // update all active effects
    public void update(float delta){
        for(int i = activeEffects.size - 1; i >= 0; i--){
            ParticleEffectPool.PooledEffect effect = activeEffects.get(i);
            effect.update(delta);

            if(effect.isComplete()){
                effect.free();
                activeEffects.removeIndex(i);
            }
        }
    }

    // render particle effect
    public void render(SpriteBatch spriteBatch){
        spriteBatch.begin();
        for(ParticleEffectPool.PooledEffect effect : activeEffects){
            effect.draw(spriteBatch);
        }
        spriteBatch.end();
    }

    // clean up resources
    public void dispose() {
        // free all active effects
        for (ParticleEffectPool.PooledEffect effect : activeEffects) {
            effect.free();
        }
        activeEffects.clear();
        prototype.dispose();
    }

    // stop all active effects
    public void stopAll() {
        for (ParticleEffectPool.PooledEffect effect : activeEffects) {
            effect.allowCompletion();
        }
    }

    // Optional: Scale all effects
    public void setScale(float scale) {
        prototype.scaleEffect(Utilities.convertToPPM(scale));
    }
}













