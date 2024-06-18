package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.screen.ScreenManager;
import com.mygdx.game.screen.StartScreen;

import java.util.Random;

public class FlappyBird extends Game {
	SpriteBatch spriteBatch;
	TextureAtlas textureAtlas;
	ScreenManager screenManager;
	Music music;

	// created a main camera that all screens can use (prevent re-creating)
	Camera camera;
	Viewport viewport;

	public static final float WORLD_WIDTH = 45;
	public static final float WORLD_HEIGHT = 80;

	public static final Random random = new Random();

	@Override
	public void create () {
		spriteBatch = new SpriteBatch();

		// music
		music = Gdx.audio.newMusic(Gdx.files.internal("bg-music.wav"));
		music.setLooping(true);
		music.setVolume(0.05f);
		music.play();

		// holds all the textures that we are going to use in the game
		textureAtlas = new TextureAtlas("flappybird.atlas");
		screenManager = new ScreenManager(this);

		// orthographic camera is good for 2d games
		camera = new OrthographicCamera();

		// viewport
		viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

		screenManager.setScreen(new StartScreen(spriteBatch,camera, viewport, screenManager, textureAtlas));
	}

	@Override
	public void render(){
		super.render();
	}
	
	@Override
	public void dispose () {
		// removes the resource on the current scene once the game terminates
		screenManager.getCurrentScreen().dispose();
	}

	@Override
	public void resize(int width, int height) {
		screenManager.getCurrentScreen().resize(width, height);
	}
}
