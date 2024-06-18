package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.screens.GameOverScreen;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.screens.ScreenManager;
import com.mygdx.game.screens.TitleScreen;

public class PlatformerShooter1 extends Game {
	// pixels per meter (Translate LibGDX coordinates to Box2D coordinates)
	// it takes 100 pixels per meter
	public static final float PPM = 100;

	// virtual width and height of our game (game world)
	public static final float V_WIDTH = 600;
	public static final float V_HEIGHT = 358;

	// category bits
	public static final short NOTHING_BIT = 0; // when we don't want the player to collide with anything
	public static final short GROUND_BIT = 2;
	public static final short PLAYER_BIT = 4;
	public static final short ENEMY_BIT = 8;
	public static final short BULLET_BIT = 16;

	// screen manager
	private ScreenManager screenManager;
	public ScreenManager getScreenManager(){ return screenManager;}

	// to draw sprites
	private SpriteBatch batch;
	public SpriteBatch getBatch(){return batch;}

	// asset manager
	private AssetManager assetManager;
	public AssetManager getAssetManager(){return assetManager;}

	@Override
	public void create () {
		// instantiate objects
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		screenManager = new ScreenManager(this);

		// load assets
		assetManager.load("sfx/shoot.wav", Sound.class);
		assetManager.load("platform-shooter.atlas", TextureAtlas.class);
		assetManager.load("music/title.mp3", Music.class);
		assetManager.load("music/level1.wav", Music.class);
		assetManager.load("sfx/Painsounds v2 - Track 2 - Hurgh.ogg", Sound.class);
		assetManager.load("sfx/enemy_hurt.ogg", Sound.class);
		assetManager.load("sfx/cyborg_hurt.ogg", Sound.class);
		assetManager.load("sfx/DeathFlash.ogg", Sound.class);
		assetManager.load("sfx/Win sound.wav", Sound.class);
		assetManager.finishLoading();

		// load title screen
		screenManager.setScreen(new TitleScreen(this));
//		screenManager.setScreen(new PlayScreen(this));
//		screenManager.setScreen(new GameOverScreen());

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}













