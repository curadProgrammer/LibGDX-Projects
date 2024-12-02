package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.ScreenManager;

public class MyGdxGame extends Game {
	private SpriteBatch spriteBatch;

	@Override
	public void create() {
		ScreenManager screenManager = ScreenManager.getInstance();
		screenManager.initialize(this);

		screenManager.setScreen(ScreenManager.ScreenType.TITLE_SCREEN);
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
