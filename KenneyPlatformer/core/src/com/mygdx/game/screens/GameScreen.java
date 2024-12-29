package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.maps.MapManager;
import com.mygdx.game.utils.GameUtil;

public class GameScreen implements Screen {
    private static final Logger logger = new Logger(GameScreen.class.toString(), Logger.DEBUG);

    private MyGdxGame game;
    private MapManager mapManager;
    private OrthographicCamera camera;
    private Viewport viewport;

    private World world;

    public GameScreen(MyGdxGame game){
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameUtil.getPPMWidth(), GameUtil.getPPMHeight(), camera);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
        camera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);

        // todo change gravity later
        world = new World(new Vector2(0, 0), true);

        mapManager = new MapManager(this, game);
        mapManager.start();

    }

    @Override
    public void show() {

    }

    private void update(float delta){
        camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //  clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);

        mapManager.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }
}
