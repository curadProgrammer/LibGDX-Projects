package com.mygdx.game.maps;

/**
 * This class will contain information about the map such as name, path, and layers
 */
public class MapData {
    private int level;
    private String mapPath;

    public MapData(int level, String mapPath) {
        this.level = level;
        this.mapPath = mapPath;
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
}
