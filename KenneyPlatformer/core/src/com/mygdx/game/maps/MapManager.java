package com.mygdx.game.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will manage the different levels that we have
 */
public class MapManager {
    private static final Logger logger = new Logger(MapManager.class.toString(), Logger.DEBUG);

    // Note: I have this reference so that I can reference the world and pooledengine from the game screen
    private MyGdxGame game;
    private GameScreen gameScreen;
    private List<MapData> maps;
    private MapData currentMap;
    private MapRenderer mapRenderer;

    public MapManager(GameScreen gameScreen, MyGdxGame game){
        this.gameScreen = gameScreen;
        this.game = game;
        loadMaps();
    }

    // method to start at the first level
    public void start(){
        currentMap = maps.get(0);
    }

    // todo create a method to load a specific level


    // Note: to calculate the number of levels we will use the size of the mapPaths list
    private void loadMaps(){
        // load map
        List<String> mapPaths = GameUtil.getMapPaths(Gdx.files.getLocalStoragePath() + "/levels");
        int levelCount = mapPaths.size();
        maps = new ArrayList<>(levelCount);
        for(int i = 0; i < levelCount; i++){
            String mapPath = mapPaths.get(i);
            MapData mapData = new MapData(i, mapPath);
            maps.add(mapData);
        }
    }
}
