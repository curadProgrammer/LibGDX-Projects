package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.assets.B2DAssetmanager;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.utils.GameUtil;

public class SettingScreen implements Screen {
    private static final Logger logger = new com.badlogic.gdx.utils.Logger(Screen.class.toString(), Logger.DEBUG);

    private MyGdxGame game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture bgTexture;
    private float bgTextureElapsedTime;
    private float bgTextureXPos;

    // ui
    private Stage stage;
    private Table table;
    private Skin skin;

    private Label settingTitleLabel, volumeMusicLabel, volumeSoundLabel, musicOnOffLabel, soundOnOffLabel;
    private Slider volumeMusicSlider, volumeSoundSlider;
    private CheckBox musicOnOffCheck, soundOnOffCheck;
    private TextButton backBtn;

    public SettingScreen(MyGdxGame game){
        logger.info("Constructor");

        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new StretchViewport(GameUtil.VIRTUAL_WIDTH, GameUtil.VIRTUAL_HEIGHT, camera);
        stage = new Stage(viewport);
        stage.setDebugAll(false);
//        stage.setDebugTableUnderMouse(true);
        skin = B2DAssetmanager.getInstance().assetManager.get(B2DAssetmanager.getInstance().skinPath, Skin.class);
    }

    @Override
    public void show() {
        logger.info("Show");

        bgTexture = B2DAssetmanager.getInstance().assetManager.get(
                B2DAssetmanager.getInstance().titleBgTexturePath,
                Texture.class
        );

        stage.clear();
        Gdx.input.setInputProcessor(stage);

        // todo clean this up
        NinePatch labelNinePatch = new NinePatch(new Texture(Gdx.files.internal("skin/nine-patch/button_rectangle_depth_flat.9.png")),
                12, 12, 12, 12);

        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(labelNinePatch);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font =  B2DAssetmanager.getInstance().assetManager.get(
                B2DAssetmanager.getInstance().fontLarge, BitmapFont.class
        );
        labelStyle.background = ninePatchDrawable;

        // labels
        settingTitleLabel = new Label("Settings", labelStyle);
        settingTitleLabel.setFontScale(1.5f);
        settingTitleLabel.setAlignment(Align.center);

        volumeMusicLabel = new Label("Music Volume:", labelStyle);
        volumeSoundLabel = new Label("Sound Volume:", labelStyle);

        // sliders
        volumeMusicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        volumeMusicSlider.setValue(GameConfig.getInstance().getMusicVolume());
        volumeMusicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                GameConfig.getInstance().setMusicVolume(volumeMusicSlider.getValue());
                return false;
            }
        });

        volumeSoundSlider = new Slider(0f, 1f, 0.1f, false, skin);
        volumeSoundSlider.setValue(GameConfig.getInstance().getSoundVolume());
        volumeSoundSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                GameConfig.getInstance().setSoundVolume(volumeSoundSlider.getValue());
                return false;
            }
        });

        // checkboxes
        labelStyle = new Label.LabelStyle();
        labelStyle.font =  B2DAssetmanager.getInstance().assetManager.get(
                B2DAssetmanager.getInstance().fontLarge, BitmapFont.class
        );

        musicOnOffCheck = new CheckBox("Enable Music", skin);
        musicOnOffCheck.setChecked(GameConfig.getInstance().isMusicEnabled());
        musicOnOffCheck.getLabel().setStyle(labelStyle);
        musicOnOffCheck.getLabelCell().padLeft(10);
        musicOnOffCheck.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                GameConfig.getInstance().setMusicEnabled(musicOnOffCheck.isChecked());
                return false;
            }
        });

        soundOnOffCheck = new CheckBox("Enable Sound", skin);
        soundOnOffCheck.setChecked(GameConfig.getInstance().isSoundEnabled());
        soundOnOffCheck.getLabel().setStyle(labelStyle);
        soundOnOffCheck.getLabelCell().padLeft(10);
        soundOnOffCheck.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                GameConfig.getInstance().setSoundEnabled(soundOnOffCheck.isChecked());
                return false;
            }
        });

        // buttons
        backBtn = new TextButton("Back", skin);
        backBtn.getLabel().setStyle(labelStyle);
        backBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // go back to menu screen
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.TITLE_SCREEN);
            }
        });

        table = new Table();
        table.setFillParent(true);

        table.setDebug(true);
        table.top().padTop(50);
        table.add(settingTitleLabel).fillX().uniformX().colspan(4);

        table.row().pad(60, 0, 0, 0);
        table.add(volumeMusicLabel).left().fillX();
        table.add().width(0);
        table.add(volumeMusicSlider).fill().colspan(2);

        table.row().pad(60, 0, 0, 0);
        table.add(volumeSoundLabel).left().fillX();
        table.add().width(0);
        table.add(volumeSoundSlider).fill().colspan(2);

        table.row().pad(60, 0, 0, 0);
        table.add(musicOnOffCheck).left().colspan(2);
        table.add(soundOnOffCheck).center().colspan(2);

        table.row().pad(150, 0, 0, 0);
        table.add(backBtn).fillY().center().colspan(4);

        stage.addActor(table);
    }

    private void update(float delta){
        bgTextureElapsedTime += delta;

        // use MathUtils for smooth back-and-forth movement
        bgTextureXPos = MathUtils.cos(bgTextureElapsedTime) * 25; // 25 is the range of movement

        stage.act();
        camera.update();
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //  clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        game.spriteBatch.setProjectionMatrix(camera.combined);
        game.spriteBatch.begin();
        game.spriteBatch.setColor(0.5f, 0.5f, 0.5f, 1f); // darken background texture
        game.spriteBatch.draw(
                bgTexture,
                bgTextureXPos - 100,
                -100,
                GameUtil.VIRTUAL_WIDTH * 1.5f,
                GameUtil.VIRTUAL_HEIGHT  * 1.5f
        );
        game.spriteBatch.setColor(1, 1, 1, 1f); // reset so that other textures are colored properly
        game.spriteBatch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
