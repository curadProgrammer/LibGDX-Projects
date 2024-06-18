package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PongGame;

public class TitleScreen extends InputListener implements Screen {
    private PongGame game;
    private Stage stage;
    private BitmapFont font;

    private Label pongTitleLabel, playerVsPlayerLabel, playerVsComputerLabel, exitLabel;
    private Viewport viewport;

    public TitleScreen(Game game){
        this.game = (PongGame) game;
        this.viewport = new StretchViewport(PongGame.V_WIDTH, PongGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);

        // prepare font
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Daily Note.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter pongTitleFont = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pongTitleFont.size = 48;
        pongTitleFont.borderWidth = 3.6f;

        FreeTypeFontGenerator.FreeTypeFontParameter optionsFont = new FreeTypeFontGenerator.FreeTypeFontParameter();
        optionsFont.size = 16;
        optionsFont.borderWidth = 3.6f;

        // tabel
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // labels
        font = fontGenerator.generateFont(pongTitleFont);
        pongTitleLabel = new Label("PONG", new Label.LabelStyle(font, Color.WHITE));

        font = fontGenerator.generateFont(optionsFont);
        playerVsPlayerLabel = new Label("Player vs. Player", new Label.LabelStyle(font, Color.WHITE));
        playerVsPlayerLabel.addListener(this);

        playerVsComputerLabel = new Label("Player vs. Computer", new Label.LabelStyle(font, Color.WHITE));
        playerVsComputerLabel.addListener(this);

        exitLabel = new Label("Exit", new Label.LabelStyle(font, Color.WHITE));
        exitLabel.addListener(this);


        // expandX() takes up the whole row
        table.add(pongTitleLabel).expandX().padTop(10);
        table.row(); // adds a new row
        table.add(playerVsPlayerLabel).expandX().padTop(20);
        table.row();
        table.add(playerVsComputerLabel).expandX().padTop(10);
        table.row();
        table.add(exitLabel).expandX().padTop(10);
        
        stage.addActor(table);

        // this is required for us to read input from the user
        Gdx.input.setInputProcessor(stage);
        fontGenerator.dispose();

    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        // used to know which label was clicked on
        Label activeLabel = (Label) event.getListenerActor();
        if(activeLabel == playerVsPlayerLabel){
            game.getScreenManager().setScreen(new PlayScreen(game, true));
        }else if(activeLabel == playerVsComputerLabel){
            game.getScreenManager().setScreen(new PlayScreen(game, false));
        }else{
            // end app process
            Gdx.app.exit();
        }

        return super.touchDown(event, x, y, pointer, button);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
    }
}
