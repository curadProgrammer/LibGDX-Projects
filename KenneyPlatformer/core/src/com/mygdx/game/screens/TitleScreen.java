package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.assets.B2DAssetmanager;

public class TitleScreen implements Screen {
    private MyGdxGame game;
    private Texture bgTexture;

    public TitleScreen(MyGdxGame game){
        this.game = game;

        bgTexture = B2DAssetmanager.getInstance().assetManager.get(
                B2DAssetmanager.getInstance().titleBgTexturePath,
                Texture.class
        );
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //  clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.begin();
        game.spriteBatch.draw(bgTexture, 0, 0, 1920, 1080);
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

    }
}
