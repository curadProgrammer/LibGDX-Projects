package com.taptap.breakout.utils;

import com.badlogic.gdx.audio.Sound;
import com.taptap.breakout.AppPreferences;
import com.taptap.breakout.loader.B2dAssetManager;

public class SoundUtil {
    private static volatile SoundUtil instance;
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
        explosionSound.play(AppPreferences.getInstance().getSoundVolume());
    }

    public void playDingSound1(){
        Sound dingSound1 = assetManager.manager.get(assetManager.ding1Sound);
        dingSound1.play(AppPreferences.getInstance().getSoundVolume());
    }

    public void playDingSound2(){
        Sound dingSound2 = assetManager.manager.get(assetManager.ding2Sound);
        dingSound2.play(AppPreferences.getInstance().getSoundVolume());
    }
}
