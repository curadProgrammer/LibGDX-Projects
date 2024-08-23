package com.taptap.breakout.level;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.taptap.breakout.BreakoutGame;
import com.taptap.breakout.screens.MainScreen;

import java.util.HashMap;

public class LevelManager {
    public enum Level{TEST, LEVEL1, LEVEL2}

    private World world;
    private OrthographicCamera cam;
    private HashMap<Level, String> levels;
    private PooledEngine en;
    private BreakoutGame game;
    public LevelLoader currentLevel;

    public LevelManager(BreakoutGame game, World world, PooledEngine en, OrthographicCamera cam){
        this.world = world;
        this.game = game;
        this.cam = cam;
        this.en = en;

        // store level paths
        levels = new HashMap<>();
        levels.put(Level.TEST, "levels/test.tmx");
    }

    public void loadLevel(Level level){
        currentLevel = new LevelLoader(game, world, en, levels.get(level));
        currentLevel.getMapRenderer().setView(cam);
    }

    public void renderLevel(){
        currentLevel.getMapRenderer().render();
    }
}
