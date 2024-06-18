package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.screens.ScreenManager;
import com.mygdx.game.screens.TitleScreen;

import java.util.Random;

public class PongGame extends Game {
	// pixels per meter (Translate LibGDX coordinates to Box2D coordinates)
	// it takes 100 pixels per meter
	public static final float PPM = 100;

	// virtual width and height of our game (game world)
	public static final float V_WIDTH = 400;
	public static final float V_HEIGHT = 208;

	// category bits
	public static final short NOTHING_BIT = 0;
	public static final short PADDLE_BIT = 2;
	public static final short PADDLE_TOP_BIT = 4;
	public static final short PADDLE_MID_BIT = 8;
	public static final short PADDLE_BOTTOM_BIT = 16;
	public static final short BALL_BIT = 32;

	// will store assets to be used in the game (i.e music and sfx)
	private AssetManager assetManager;
	public AssetManager getAssetManager(){return assetManager;}

	// manages screen (use this to manage the screens throughout the game)
	private ScreenManager screenManager;
	public ScreenManager getScreenManager(){return screenManager;}

	// sprite batch used to render things on the screen
	private SpriteBatch batch;
	public SpriteBatch getBatch(){return batch;}

	// shape renderer
	private ShapeRenderer shapeRenderer;
	public ShapeRenderer getShapeRenderer(){return shapeRenderer;}

	private Random random;
	public Random getRandom(){return random;}

	@Override
	public void create () {
		// load assets
		assetManager = new AssetManager();
		assetManager.load("background.mp3", Music.class);
		assetManager.load("hit.wav", Sound.class);
		assetManager.load("score.wav", Sound.class);
		assetManager.finishLoading();

		// spritebatch
		batch = new SpriteBatch();

		// shape renderer
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		// play music
		Music music = assetManager.get("background.mp3", Music.class);
		music.setLooping(true);
		music.setVolume(0.4f);
		music.play();

		random = new Random();

		// load title screen
		screenManager = new ScreenManager(this);
		screenManager.setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {
		// for some odd reason we need to call this or else it won't render the screens properly
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
