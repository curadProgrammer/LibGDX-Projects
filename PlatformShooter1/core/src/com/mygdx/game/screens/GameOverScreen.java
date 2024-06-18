package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PlatformerShooter1;

public class GameOverScreen implements Screen {
    private PlatformerShooter1 game;
    private OrthographicCamera cam;
    private Viewport gamePort;

    private Stage stage;
    private BitmapFont font;
    private Label gameOverLabel, tryAgainLabel;

    public GameOverScreen(Game game){
        this.game = (PlatformerShooter1) game;
        cam = new OrthographicCamera();
        gamePort = new StretchViewport(
                PlatformerShooter1.V_WIDTH,
                PlatformerShooter1.V_HEIGHT,
                cam
        );
        stage = new Stage(gamePort);

        // prepare font
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("font/Mega-Man-Battle-Network.ttf")
        );

        // game over font params
        FreeTypeFontGenerator.FreeTypeFontParameter gameOverFont =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        gameOverFont.size = 22;
        gameOverFont.borderWidth = 3.6f;

        // try again font
        FreeTypeFontGenerator.FreeTypeFontParameter tryAgainFont =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        tryAgainFont.size = 12;
        tryAgainFont.borderWidth = 3.6f;

        // labels
        font = fontGenerator.generateFont(gameOverFont);
        gameOverLabel = new Label("You Died", new Label.LabelStyle(font, Color.WHITE));

        font = fontGenerator.generateFont(tryAgainFont);
        tryAgainLabel = new Label("Click Anywhere to Try Again", new Label.LabelStyle(font, Color.WHITE));

        // Table
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        table.add(gameOverLabel);
        table.row();
        table.add(tryAgainLabel).padTop(40);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    public void update(){
        if(Gdx.input.justTouched()){
            game.getScreenManager().setScreen(new PlayScreen(game));
        }
    }

    @Override
    public void render(float v) {
        update();
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

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
