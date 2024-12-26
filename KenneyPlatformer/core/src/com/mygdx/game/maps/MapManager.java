package com.mygdx.game.maps;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will manage the different levels that we have
 */
public class MapManager {
    private static final Logger logger = new Logger(MapManager.class.toString(), Logger.DEBUG);

    private GameScreen gameScreen;
    private final int levelCount; // used to find the number of levels
    private List<MapData> maps;

    public MapManager(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        List<String> mapPaths = GameUtil.getMapPaths(Gdx.files.getLocalStoragePath() + "/levels");
        levelCount = mapPaths.size();
        maps = new ArrayList<>(levelCount);
    }

    // method to start at the first level
    public void start(){

    }

    // todo create a method to load a specific level



}
