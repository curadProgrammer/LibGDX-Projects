package com.taptap.breakout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.taptap.breakout.loader.B2dAssetManager;
import com.taptap.breakout.screens.ScreenManager;

public class BreakoutGame extends Game {
	public ScreenManager screenManager;
	public B2dAssetManager assetManager;

	private AppPreferences appPreferences;

	public AppPreferences getAppPreferences() {
		return appPreferences;
	}

	// music
	private Music backgroundMusic;

	@Override
	public void create () {
		appPreferences = new AppPreferences();
		screenManager = new ScreenManager(this);
		assetManager = new B2dAssetManager();

		// change the screen to the loading screen
		screenManager.changeScreen(ScreenManager.LOADING);

		// music
		assetManager.queueAddMusic();
		assetManager.manager.finishLoading();
		backgroundMusic = assetManager.manager.get("music/night night.ogg");
		backgroundMusic.setLooping(true);
//		backgroundMusic.play();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		System.out.println("(BreakoutGame.java) Calling dispose()");
		backgroundMusic.dispose();
		assetManager.manager.dispose();
	}
}
