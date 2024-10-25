package com.taptap.breakout.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taptap.breakout.BreakoutGame;
import com.taptap.breakout.Hud;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.controller.KeyboardController;
import com.taptap.breakout.ecs.systems.*;
import com.taptap.breakout.level.B2dContactListener;
import com.taptap.breakout.level.LevelManager;
import com.taptap.breakout.listeners.ScoreChangeListener;
import com.taptap.breakout.utils.PaddleAndBall;

import java.util.logging.Logger;

public class MainScreen implements Screen, ScoreChangeListener {
    private static final Logger logger = Logger.getLogger(MainScreen.class.getName());
    private static final boolean DEBUG_MODE = true;

    private BreakoutGame game;
    private OrthographicCamera cam;
    private Viewport viewport;
    private Hud hud;

    private World world;
    private LevelManager levelManager;

    private SpriteBatch sb;
    private PooledEngine engine;
    private CollisionSystem collisionSystem;

    private InputMultiplexer inputMultiplexer;
    private KeyboardController controller;

    public MainScreen(BreakoutGame game){
        if(DEBUG_MODE) logger.info("Constructor");

        this.game = game;
        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        viewport = new FitViewport(Utilities.getPPMWidth(), Utilities.getPPMHeight(),
                cam);
        cam.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        cam.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);
        controller = new KeyboardController();
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new B2dContactListener());
        inputMultiplexer = new InputMultiplexer();

        engine = new PooledEngine();
        levelManager = new LevelManager(game, world, engine, cam);
        hud = new Hud(game, levelManager);
        sb.setProjectionMatrix(cam.combined);
    }

    @Override
    public void show() {
        logger.info("Show");

        // load first level
        levelManager.loadLevel(1);

        engine.addSystem(new RenderingSystem(sb, cam));
        engine.addSystem(new PhysicsSystem(world, engine));
        engine.addSystem(new PhysicsDebugSystem(world, cam));
        engine.addSystem(new BallSystem(hud, levelManager));
        engine.addSystem(new AttachSystem());
        collisionSystem = new CollisionSystem(hud, levelManager, this);
        engine.addSystem(collisionSystem);
        engine.addSystem(new SoundSystem(game.getAppPreferences()));
        engine.addSystem(new PlayerControlSystem(controller, hud, levelManager));

        inputMultiplexer.addProcessor(hud.getStage()); // Add stage first for UI priority
        inputMultiplexer.addProcessor(controller);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void update(){
        hud.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //  clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();

        // load level
        levelManager.renderLevel();

        // update engine
        engine.update(delta);

        // render hud at the end so it overlays on top of everything else
        hud.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hud.resize(width, height);
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
        if(DEBUG_MODE) logger.info("Dispose()");
        levelManager.dispose();
    }

    @Override
    public void onScoreChange(int appendScore) {
        hud.setScore(hud.getScore() + appendScore);
    }
}
