package com.taptap.breakout.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.taptap.breakout.BreakoutGame;
import com.taptap.breakout.ecs.components.B2BodyComponent;
import com.taptap.breakout.utils.PaddleAndBall;

public class LevelManager implements Disposable {
    public static int MAX_LEVELS = 3;

    public enum Level{TEST, LEVEL1, LEVEL2, LEVEL3}

    private World world;
    private OrthographicCamera cam;
    private PooledEngine en;
    private BreakoutGame game;
    public LevelLoader currentLevel;

    public LevelManager(BreakoutGame game, World world, PooledEngine en, OrthographicCamera cam){
        this.world = world;
        this.game = game;
        this.cam = cam;
        this.en = en;
    }

    public void loadLevel(int level){
        cleanupCurrentLevel();

        switch (level){
            case 1:
                currentLevel = new LevelLoader(game, world, en, getLevelMapPath(Level.LEVEL1));
                break;
            case 2:
                currentLevel = new LevelLoader(game, world, en, getLevelMapPath(Level.LEVEL2));
                break;
            case 3:
                currentLevel = new LevelLoader(game, world, en, getLevelMapPath(Level.LEVEL3));
                break;
        }
        currentLevel.getMapRenderer().setView(cam);
    }

    public void renderLevel(){
        currentLevel.getMapRenderer().render();
    }

    // this is where you will pass in the level's path
    public String getLevelMapPath(Level level){
        String path = "";

        switch(level){
            case TEST:
                path = "levels/test.tmx";
                break;
            case LEVEL1:
                path = "levels/level1.tmx";
                break;
            case LEVEL2:
                path = "levels/level2.tmx";
                break;
            case LEVEL3:
                path = "levels/level3.tmx";
                break;
        }

        return path;
    }

    private void cleanupCurrentLevel(){
        if (currentLevel == null) return;

        // Dispose the current level
        currentLevel.dispose();

        // Remove all bodies from the current level
        ImmutableArray<Entity> matchingEntities = en.getEntitiesFor(Family.all(B2BodyComponent.class).get());
        for (Entity entity : matchingEntities) {
            B2BodyComponent b2Body = entity.getComponent(B2BodyComponent.class);
            b2Body.setToDestroy = true;
        }

        // Clear the current level
        currentLevel = null;
    }

    @Override
    public void dispose() {
        System.out.println("Calling Level Manager dispose");
        cleanupCurrentLevel();
    }
}
