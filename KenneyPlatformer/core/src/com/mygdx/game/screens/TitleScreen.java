package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.assets.B2DAssetmanager;
import com.mygdx.game.utils.GameUtil;


public class TitleScreen implements Screen {
    private static final Logger logger = new Logger("Title Screen", Logger.DEBUG);

    private MyGdxGame game;
    private Texture bgTexture;

    private float bgTextureElapsedTime = 0;
    private float bgTextureXPos = 0;

    private OrthographicCamera camera;
    private Viewport viewport;

    public TitleScreen(MyGdxGame game){
        logger.info("Constructor");
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        viewport = new StretchViewport(GameUtil.VIRTUAL_WIDTH, GameUtil.VIRTUAL_HEIGHT, camera);
    }

    @Override
    public void show() {
        bgTexture = B2DAssetmanager.getInstance().assetManager.get(
                B2DAssetmanager.getInstance().titleBgTexturePath,
                Texture.class
        );
    }

    public void update(float delta){
        bgTextureElapsedTime += delta;

        // Use MathUtils for smooth back-and-forth movement
        bgTextureXPos = MathUtils.sin(bgTextureElapsedTime) * 25; // 25 is the range of movement
        camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //  clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);

        game.spriteBatch.setProjectionMatrix(camera.combined);
        game.spriteBatch.begin();
        game.spriteBatch.draw(
                bgTexture,
                bgTextureXPos - 100,
                -100,
                GameUtil.VIRTUAL_WIDTH * 1.5f,
                GameUtil.VIRTUAL_HEIGHT  * 1.5f
        );
        game.spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        bgTexture.dispose();
    }
}
