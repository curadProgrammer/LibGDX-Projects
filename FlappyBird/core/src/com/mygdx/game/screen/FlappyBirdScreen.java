package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.FlappyBird;

public abstract class FlappyBirdScreen implements Screen {
    protected SpriteBatch spriteBatch;
    protected Camera camera;
    protected Viewport viewport;
    protected ScreenManager screenManager;
    protected TextureAtlas textureAtlas;

    private TextureRegion background;
    private float backgroundOffset;
    private float backgroundScrollingSpeed;

    // font
    BitmapFont font;

    public FlappyBirdScreen(SpriteBatch spriteBatch, Camera camera, Viewport viewport, ScreenManager screenManager,
                            TextureAtlas textureAtlas){
        this.spriteBatch = spriteBatch;
        this.camera = camera;
        this.viewport = viewport;
        this.screenManager = screenManager;
        this.textureAtlas = textureAtlas;

        // texture background
        background = textureAtlas.findRegion("bg");
        backgroundScrollingSpeed = (float) FlappyBird.WORLD_WIDTH / 4;
    }

    public abstract void prepareHud();
    public abstract void renderHud();
    public abstract void detectInput();

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        renderBackground(delta);
        spriteBatch.end();
    }

    private void renderBackground(float deltaTime){
        backgroundOffset += deltaTime * backgroundScrollingSpeed;
        if(backgroundOffset > FlappyBird.WORLD_WIDTH){
            backgroundOffset = 0;
        }

        // first background texture
        spriteBatch.draw(background, -backgroundOffset, 0,
                FlappyBird.WORLD_WIDTH, FlappyBird.WORLD_HEIGHT);

        // second background texture
        spriteBatch.draw(background, -backgroundOffset + FlappyBird.WORLD_WIDTH, 0,
                FlappyBird.WORLD_WIDTH, FlappyBird.WORLD_HEIGHT);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        spriteBatch.setProjectionMatrix(camera.combined);
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
