package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.assets.B2DAssetmanager;
import com.mygdx.game.listeners.HoverListener;
import com.mygdx.game.utils.ActionsUtil;
import com.mygdx.game.utils.GameUtil;

public class TitleScreen implements Screen {
    private static final Logger logger = new Logger(TitleScreen.class.toString(), Logger.DEBUG);

    private OrthographicCamera camera;
    private Viewport viewport;
    private MyGdxGame game;

    private Texture bgTexture;
    private float bgTextureElapsedTime;
    private float bgTextureXPos;

    // ui
    private Stage stage;
    private Table table;
    private Skin skin;
    private Label title;
    private TextButton startGameBtn, settingsBtn, exitBtn;

    private final float TITLE_VIRTUAL_WIDTH = 1920;
    private final float TITLE_VIRTUAL_HEIGHT = 1080;


    public TitleScreen(MyGdxGame game){
        logger.info("Constructor");
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new StretchViewport(TITLE_VIRTUAL_WIDTH, TITLE_VIRTUAL_HEIGHT, camera);

        stage = new Stage(viewport);
        stage.setDebugAll(false);
        skin = B2DAssetmanager.getInstance().assetManager.get(B2DAssetmanager.getInstance().skinPath);

    }

    @Override
    public void show() {
        bgTexture = B2DAssetmanager.getInstance().assetManager.get(
                B2DAssetmanager.getInstance().titleBgTexturePath,
                Texture.class
        );

        stage.clear();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(false);

        // todo clean this up
        NinePatch labelNinePatch = new NinePatch(new Texture(Gdx.files.internal("skin/nine-patch/button_rectangle_depth_flat.9.png")),
                12, 12, 12, 12);

        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(labelNinePatch);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font =  B2DAssetmanager.getInstance().assetManager.get(
                B2DAssetmanager.getInstance().fontLarge, BitmapFont.class
        );
        labelStyle.fontColor = Color.WHITE;
        labelStyle.background = ninePatchDrawable;
        title = new Label("Kenney Platformer", labelStyle);
        title.setFontScale(2f);

        labelStyle = new Label.LabelStyle();
        labelStyle.font =  B2DAssetmanager.getInstance().assetManager.get(
                B2DAssetmanager.getInstance().fontMedium, BitmapFont.class
        );

        startGameBtn = new TextButton("", skin);
        startGameBtn.setLabel(new Label("Start", labelStyle));
        startGameBtn.getLabel().setAlignment(Align.center);
        startGameBtn.setTransform(true);
        startGameBtn.addListener(new HoverListener(startGameBtn));
        startGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.info("Main Screen");

                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME_SCREEN);
            }
        });

        settingsBtn = new TextButton("", skin);
        settingsBtn.setLabel(new Label("Settings", labelStyle));
        settingsBtn.getLabel().setAlignment(Align.center);
        settingsBtn.setTransform(true);
        settingsBtn.addListener(new HoverListener(settingsBtn));
        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.info("Setting Screen");

                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.SETTING_SCREEN);
            }
        });

        exitBtn = new TextButton("", skin);
        exitBtn.setLabel(new Label("Exit", labelStyle));
        exitBtn.getLabel().setAlignment(Align.center);
        exitBtn.setTransform(true);
        exitBtn.addListener(new HoverListener(exitBtn));
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.info("Exit");

                // exit
                Gdx.app.exit();
            }
        });

        table.top().padTop(100);
        table.add(title).fillX().uniformX();
        table.row().padTop(100);
        table.add(startGameBtn).width(500);
        table.row().padTop(50);
        table.add(settingsBtn).width(500);
        table.row().padTop(50);
        table.add(exitBtn).width(500);
        stage.addActor(table);

        ActionsUtil.addMovingUpDownAction(title);
    }

    public void update(float delta){
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

        // Note: we adjust the initial position to move the texture to the left and a bit down
        // We also scale the image bigger to prevent seeing the black background as we moved the image
        game.spriteBatch.draw(
                bgTexture,
                bgTextureXPos - 100,
                -100,
                TITLE_VIRTUAL_WIDTH * 1.5f,
                TITLE_VIRTUAL_HEIGHT  * 1.5f
        );

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
        // todo figure out a proper way to dispose
        // bgTexture.dispose();
    }
}
