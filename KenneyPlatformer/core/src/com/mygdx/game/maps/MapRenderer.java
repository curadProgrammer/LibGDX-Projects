package com.mygdx.game.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.GameUtil;

/**
 * This class will be in charge of rendering a single map file
 */
public class MapRenderer {
    private static final Logger logger = new Logger(MapRenderer.class.toString(), Logger.DEBUG);

    private MyGdxGame game;
    private final GameScreen gameScreen;
    private TiledMap map;
    private final TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer mapRenderer;

    public MapRenderer(MyGdxGame game, GameScreen gameScreen){
        this.game = game;
        this.gameScreen = gameScreen;

        mapLoader = new TmxMapLoader();
    }

    public void setCurrentMap(MapData currentMap) {
        logger.info("Setting Map Data");

        // Load map once when setting new map data
        map = mapLoader.load(currentMap.getMapPath());
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/GameUtil.PPM);
    }

    public void render(){
        if(map == null)
            throw new RuntimeException("No Map is Loaded!");

        mapRenderer.setView(gameScreen.getCamera());
        mapRenderer.render();
    }



    public TiledMap getMap() {
        return map;
    }

    public TmxMapLoader getMapLoader() {
        return mapLoader;
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }
}
