package com.mygdx.game.utils;

public class GameUtil {
    public static final float PPM = 100;
    public static final float VIRTUAL_WIDTH = 1920;
    public static final float VIRTUAL_HEIGHT = 1080;

    public static float getPPMWidth(){
        return VIRTUAL_WIDTH / PPM;
    }

    public static float getPPMHeight(){
        return VIRTUAL_HEIGHT / PPM;
    }

    public static float convertToPPM(float num){return num / PPM;}
}
