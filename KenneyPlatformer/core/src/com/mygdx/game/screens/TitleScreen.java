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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.assets.B2DAssetmanager;
import com.mygdx.game.utils.GameUtil;


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

    private NinePatch ninePatch;

    public TitleScreen(MyGdxGame game){
        logger.info("Constructor");
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new StretchViewport(GameUtil.VIRTUAL_WIDTH, GameUtil.VIRTUAL_HEIGHT, camera);

        stage = new Stage(viewport);
        stage.setDebugAll(true);
        skin = B2DAssetmanager.getInstance().assetManager.get(B2DAssetmanager.getInstance().skinPath);
        ninePatch = new NinePatch(new Texture(Gdx.files.internal("skin/nine-patch/button_rectangle_depth_flat.9.png")),
                                        12, 12, 12, 12);
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

        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(ninePatch);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font =  B2DAssetmanager.getInstance().assetManager.get(
                B2DAssetmanager.getInstance().fontLarge, BitmapFont.class
        );
        labelStyle.fontColor = Color.WHITE;
        labelStyle.background = ninePatchDrawable;

        title = new Label("Kenney Platformer", labelStyle);
        table.add(title).fillX().uniformX();
        stage.addActor(table);
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
