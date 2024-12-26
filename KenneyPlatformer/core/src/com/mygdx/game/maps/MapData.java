package com.mygdx.game.maps;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will contain information about the map such as name, path, and layers
 */
public class MapData {
    private String mapName;
    private String mapPath;
    private TiledMap tiledMap;
    private Map<String, MapLayer> mapLayers;
    private List<MapObject> mapObjects;

    public MapData(String mapName, String mapPath) {
        this.mapName = mapName;
        this.mapPath = mapPath;
        this.mapLayers = new HashMap<>();
        this.mapObjects = new ArrayList<>();
    }
}
