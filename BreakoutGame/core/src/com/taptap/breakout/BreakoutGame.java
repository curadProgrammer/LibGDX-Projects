package com.taptap.breakout;

import com.badlogic.gdx.Game;
import com.taptap.breakout.loader.B2dAssetManager;
import com.taptap.breakout.screens.ScreenManager;
import com.taptap.breakout.utils.SoundUtil;

public class BreakoutGame extends Game {
	// change this to false to turn of debug mode
	public static final boolean DEBUG_MODE = false;

	public ScreenManager screenManager;
	public B2dAssetManager assetManager;

	private AppPreferences appPreferences;

	public AppPreferences getAppPreferences() {
		return appPreferences;
	}

	@Override
	public void create () {
		appPreferences = AppPreferences.getInstance();
		screenManager = new ScreenManager(this);
		assetManager = B2dAssetManager.getInstance();

		// change the screen to the loading screen
		screenManager.changeScreen(ScreenManager.LOADING);

		// music
		assetManager.queueAddMusic();
		assetManager.manager.finishLoading();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		if(BreakoutGame.DEBUG_MODE) System.out.println("(BreakoutGame.java) Calling dispose()");
		assetManager.manager.dispose();
	}
}
