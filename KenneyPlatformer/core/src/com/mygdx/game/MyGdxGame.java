package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.ScreenManager;

public class MyGdxGame extends Game {
	public SpriteBatch spriteBatch;

	@Override
	public void create() {
		spriteBatch = new SpriteBatch();

		ScreenManager screenManager = ScreenManager.getInstance();
		screenManager.initialize(this);

		// start loading screen
		screenManager.setScreen(ScreenManager.ScreenType.LOADING_SCREEN);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
