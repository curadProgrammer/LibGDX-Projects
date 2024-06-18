package com.taptap.breakout;

public class Utilities {
    public static final float PPM = 100;
    public static final float VIRTUAL_WIDTH = 800;
    public static final float VIRTUAL_HEIGHT = 450;

    public static float getPPMWidth(){
        return VIRTUAL_WIDTH / PPM;
    }

    public static float getPPMHeight(){
        return VIRTUAL_HEIGHT / PPM;
    }
}
