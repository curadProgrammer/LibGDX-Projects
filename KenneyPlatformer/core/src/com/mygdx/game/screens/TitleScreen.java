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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.assets.B2DAssetmanager;
import com.mygdx.game.utils.GameUtil;

import javax.swing.*;


public class TitleScreen implements Screen {
    private static final Logger logger = new Logger("Title Screen", Logger.DEBUG);

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


    public TitleScreen(MyGdxGame game){
        logger.info("Constructor");
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new StretchViewport(GameUtil.VIRTUAL_WIDTH, GameUtil.VIRTUAL_HEIGHT, camera);

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

        settingsBtn = new TextButton("", skin);
        settingsBtn.setLabel(new Label("Settings", labelStyle));
        settingsBtn.getLabel().setAlignment(Align.center);

        exitBtn = new TextButton("", skin);
        exitBtn.setLabel(new Label("Exit", labelStyle));
        exitBtn.getLabel().setAlignment(Align.center);

        table.top().padTop(100);
        table.add(title).fillX().uniformX();
        table.row().padTop(100);
        table.add(startGameBtn).width(500);
        table.row().padTop(50);
        table.add(settingsBtn).width(500);
        table.row().padTop(50);
        table.add(exitBtn).width(500);
        stage.addActor(table);

        addTitleAction();
    }

    private void addTitleAction(){
        // add action to title by making it go up and down
        MoveToAction moveUpAction = new MoveToAction();
        moveUpAction.setPosition(GameUtil.VIRTUAL_WIDTH / 2 - title.getWidth() + 15,
                                    GameUtil.VIRTUAL_HEIGHT - title.getHeight() - 150);
        moveUpAction.setDuration(1);
        moveUpAction.setInterpolation(Interpolation.smooth);

        MoveToAction moveDownAction = new MoveToAction();
        moveDownAction.setPosition(GameUtil.VIRTUAL_WIDTH / 2 - title.getWidth() + 15,
                GameUtil.VIRTUAL_HEIGHT - title.getHeight() - 175);
        moveDownAction.setDuration(1);
        moveDownAction.setInterpolation(Interpolation.smooth);

        SequenceAction overallSequence = new SequenceAction();
        overallSequence.addAction(moveUpAction);
        overallSequence.addAction(moveDownAction);

        RepeatAction infiniteLoop = new RepeatAction();
        infiniteLoop.setCount(RepeatAction.FOREVER);
        infiniteLoop.setAction(overallSequence);

        title.addAction(infiniteLoop);
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
                GameUtil.VIRTUAL_WIDTH * 1.5f,
                GameUtil.VIRTUAL_HEIGHT  * 1.5f
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
        bgTexture.dispose();
    }
}
