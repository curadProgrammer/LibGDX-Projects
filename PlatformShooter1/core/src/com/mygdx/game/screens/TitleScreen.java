package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PlatformerShooter1;

public class TitleScreen extends InputListener implements Screen {
    // temp
    private Texture background;

    private PlatformerShooter1 game;
    private Stage stage;
    private BitmapFont font;
    private Music backgroundMusic;
    private OrthographicCamera cam;

    private Label titleLabel, startLabel, exitLabel;
    private Viewport viewport;

    public TitleScreen(Game game){
        this.game = (PlatformerShooter1) game;
        cam = new OrthographicCamera();
        this.viewport = new StretchViewport(PlatformerShooter1.V_WIDTH,
                PlatformerShooter1.V_HEIGHT,
                cam);
        stage = new Stage(viewport);

        // background
        background = new Texture(Gdx.files.internal("title-bg.png"));

        // prepare font
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("font/Mega-Man-Battle-Network.ttf")
        );

        // title font params
        FreeTypeFontGenerator.FreeTypeFontParameter titleFont =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleFont.size = 28;
        titleFont.borderWidth = 3.6f;

        // option font params
        FreeTypeFontGenerator.FreeTypeFontParameter optionFont =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        optionFont.size = 16;
        optionFont.borderWidth = 3.6f;

        // labels
        font = fontGenerator.generateFont(titleFont);
        titleLabel = new Label("Great Guy", new Label.LabelStyle(font, Color.WHITE));

        font = fontGenerator.generateFont(optionFont);
        startLabel = new Label("Start Game", new Label.LabelStyle(font, Color.WHITE));
        startLabel.addListener(this);

        exitLabel = new  Label("Exit Game", new Label.LabelStyle(font, Color.WHITE));
        exitLabel.addListener(this);

        // outer table
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        Table titleTable = new Table();
        titleTable.add(titleLabel).expandX();
        table.add(titleTable).expandX().center().padTop(30);

        table.row();

        Table optionsTable = new Table();
        optionsTable.add(startLabel).expandX();
        optionsTable.add(exitLabel).expandX();
        table.add(optionsTable).expandX().fillX().padTop(175);

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        fontGenerator.dispose();

        // load music
        backgroundMusic = ((PlatformerShooter1) game).getAssetManager().get("music/title.mp3");
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.05f);
        backgroundMusic.play();
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Label clickedLabel = (Label) event.getListenerActor();
        if(clickedLabel == startLabel){
            game.getScreenManager().setScreen(new PlayScreen(game));
        }else{
            // exit
            Gdx.app.exit();
        }

        return super.touchDown(event, x, y, pointer, button);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // need this to reset background when switching to a new screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().setProjectionMatrix(cam.combined);
        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0, PlatformerShooter1.V_WIDTH,
                PlatformerShooter1.V_HEIGHT);
        game.getBatch().end();

        stage.act();
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
        stage.dispose();
        background.dispose();
        backgroundMusic.stop();
        backgroundMusic.dispose();
    }
}
