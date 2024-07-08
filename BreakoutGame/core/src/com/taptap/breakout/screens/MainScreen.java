package com.taptap.breakout.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taptap.breakout.BreakoutGame;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.controller.KeyboardController;
import com.taptap.breakout.ecs.systems.*;
import com.taptap.breakout.level.LevelManager;

public class MainScreen implements Screen {
    private BreakoutGame game;
    private OrthographicCamera cam;
    private Viewport viewport;

    private World world;
    private LevelManager levelManager;

    private SpriteBatch sb;
    private PooledEngine engine;

    private KeyboardController controller;


    public MainScreen(BreakoutGame game){
        this.game = game;
        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        viewport = new FitViewport(Utilities.getPPMWidth(), Utilities.getPPMHeight(),
                cam);
        cam.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        cam.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);
        controller = new KeyboardController();
        world = new World(new Vector2(0, 0), true);

        engine = new PooledEngine();
        levelManager = new LevelManager(game, world, engine, cam);

        // load first level
        levelManager.loadLevel(LevelManager.Level.TEST);


        sb.setProjectionMatrix(cam.combined);
        engine.addSystem(new RenderingSystem(sb, cam));
        engine.addSystem(new PhysicsSystem(world, engine));
        engine.addSystem(new PhysicsDebugSystem(world, cam));
        engine.addSystem(new BallSystem());
        engine.addSystem(new PlayerControlSystem(controller, levelManager));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //  clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // load level
        levelManager.renderLevel();

        // update engine
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
