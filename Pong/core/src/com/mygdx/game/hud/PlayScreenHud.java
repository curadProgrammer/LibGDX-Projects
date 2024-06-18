package com.mygdx.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PongGame;

public class PlayScreenHud implements Disposable {
    private Stage stage;
    private Viewport viewport;
    private Label playerOneScoreLabel, playerTwoScoreLabel;
    private int playerOneScore, playerTwoScore;

    public PlayScreenHud(){
        viewport = new StretchViewport(PongGame.V_WIDTH, PongGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // font
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Daily Note.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 24;

        BitmapFont font = fontGenerator.generateFont(fontParameter);

        playerOneScoreLabel = new Label(Integer.toString(playerOneScore), new Label.LabelStyle(font, Color.WHITE));
        playerTwoScoreLabel = new Label(Integer.toString(playerOneScore), new Label.LabelStyle(font, Color.WHITE));

        // add labels to table
        table.add(playerOneScoreLabel).expandX();
        table.add(playerTwoScoreLabel).expandX();

        stage.addActor(table);
        fontGenerator.dispose();
    }

    public void draw(){
        stage.draw();
    }

    public void incrementPlayerOneScore(){
        playerOneScoreLabel.setText(++playerOneScore);
    }

    public void incrementPlayerTwoScore(){
        playerTwoScoreLabel.setText(++playerTwoScore);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}





















