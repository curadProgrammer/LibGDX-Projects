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
    private int level;
    private String mapPath;
    private TiledMap tiledMap;
    private Map<String, MapLayer> mapLayers;
    private List<MapObject> mapObjects;

    public MapData(int level, String mapPath) {
        this.level = level;
        this.mapPath = mapPath;
        this.mapLayers = new HashMap<>();
        this.mapObjects = new ArrayList<>();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMapPath() {
        return mapPath;
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    public Map<String, MapLayer> getMapLayers() {
        return mapLayers;
    }

    public void setMapLayers(Map<String, MapLayer> mapLayers) {
        this.mapLayers = mapLayers;
    }

    public List<MapObject> getMapObjects() {
        return mapObjects;
    }

    public void setMapObjects(List<MapObject> mapObjects) {
        this.mapObjects = mapObjects;
    }
}
