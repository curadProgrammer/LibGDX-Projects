package com.taptap.breakout.level;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.taptap.breakout.BreakoutGame;
import com.taptap.breakout.screens.MainScreen;

import java.util.HashMap;

public class LevelManager {
    public enum Level{TEST, LEVEL1, LEVEL2}

    private MainScreen screen;
    private OrthographicCamera cam;
    private HashMap<Level, String> levels;
    private LevelLoader currentLevel;

    public LevelManager(MainScreen screen, OrthographicCamera cam){
        this.screen = screen;
        this.cam = cam;

        // store level paths
        levels = new HashMap<>();
        levels.put(Level.TEST, "levels/test.tmx");
    }

    public void loadLevel(Level level){
        currentLevel = new LevelLoader(levels.get(level));
        currentLevel.getMapRenderer().setView(cam);
    }

    public void renderLevel(){
        currentLevel.getMapRenderer().render();
    }
}
