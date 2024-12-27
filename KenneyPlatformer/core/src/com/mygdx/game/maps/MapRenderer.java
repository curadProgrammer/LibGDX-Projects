package com.mygdx.game.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.GameScreen;

/**
 * This class will be in charge of rendering a single map file
 */
public class MapRenderer {
    private MyGdxGame game;
    private GameScreen gameScreen;
    private TiledMap map;
    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer mapRenderer;
    private MapData mapData;

    public MapRenderer(MyGdxGame game, GameScreen gameScreen, MapData mapData){
        this.game = game;
        this.gameScreen = gameScreen;
        this.mapData = mapData;

        mapLoader = new TmxMapLoader();
        map = mapLoader.load(mapData.ge)
    }
}
