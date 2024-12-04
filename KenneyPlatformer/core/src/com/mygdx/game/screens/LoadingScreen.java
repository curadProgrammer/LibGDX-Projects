package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.assets.B2DAssetmanager;

public class LoadingScreen implements Screen {
    private MyGdxGame game;

    public LoadingScreen(MyGdxGame game){
        this.game = game;

    }

    @Override
    public void show() {
        B2DAssetmanager.getInstance().queueAddImages();
        B2DAssetmanager.getInstance().queueAddFonts();
        B2DAssetmanager.getInstance().assetManager.finishLoading();
    }

    public void update(float delta){

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //  clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        if(B2DAssetmanager.getInstance().assetManager.isFinished()){
            ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.TITLE_SCREEN);
        }
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
