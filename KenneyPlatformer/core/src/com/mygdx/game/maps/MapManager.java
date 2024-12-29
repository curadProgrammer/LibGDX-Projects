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
    private final MapRenderer mapRenderer;

    public MapManager(GameScreen gameScreen, MyGdxGame game){
        this.gameScreen = gameScreen;
        this.game = game;
        loadMaps();

        mapRenderer = new MapRenderer(game, gameScreen);
    }

    // method to start at the first level
    public void start(){
        logger.info("Start");
        mapRenderer.setCurrentMap(maps.get(0));
    }

    public void render(){
        mapRenderer.render();
    }

    // todo create a method to load a specific level


    // creates multiple map data objects based on the number of map files in the levels directory
    private void loadMaps(){
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
