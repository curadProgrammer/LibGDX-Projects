package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.List;

public class GameUtil {
    public static final float PPM = 100;
    public static final float VIRTUAL_WIDTH = 600;
    public static final float VIRTUAL_HEIGHT = 300;

    // Game Bits (used for filtering box2d bodies)
    public static final short PLATFORM = 1;
    public static final short PLAYER = 2;
    public static final short PLAYER_TOP = 4;
    public static final short PLAYER_BOTTOM = 8;
    public static final short ENEMY = 16;

    public static List<String> getMapPaths(String folderPath){
        FileHandle folder = Gdx.files.internal(folderPath);

        List<String> paths = new ArrayList<>();

        for(FileHandle file : folder.list(".tmx")) {
            paths.add(file.path());
        }

        return paths;
    }

    public static float convertToPPM(float num){return num / PPM;}
    public static float getPPMWidth(){
        return VIRTUAL_WIDTH / PPM;
    }
    public static float getPPMHeight(){
        return VIRTUAL_HEIGHT / PPM;
    }
}
