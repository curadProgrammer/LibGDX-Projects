package com.taptap.breakout.utils;

import com.badlogic.gdx.audio.Sound;
import com.taptap.breakout.loader.B2dAssetManager;

public class SoundUtil {
    private static SoundUtil instance;
    private B2dAssetManager assetManager;
    private SoundUtil(){
        assetManager = B2dAssetManager.getInstance();
    }

    public static SoundUtil getInstance(){
        if(instance == null){
            instance = new SoundUtil();
        }
        return instance;
    }

    public void playExplosion(){
        Sound explosionSound = assetManager.manager.get(assetManager.explosionSound);
//        explosionSound.play(getAppPreferences().getSoundVolume());
    }
}
