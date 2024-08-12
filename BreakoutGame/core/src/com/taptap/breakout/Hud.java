package com.taptap.breakout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    private BreakoutGame game;
    private Stage stage;
    private Viewport viewport;

    private TextureRegion ballTexture;
    private Label scoreLabel, levelLabel, livesLabel;
    private int score, level, lives;

    public Hud(BreakoutGame game, TextureRegion ballTexture){
        this.game = game;
        this.ballTexture = ballTexture;

        viewport = new FitViewport(Utilities.VIRTUAL_WIDTH, Utilities.VIRTUAL_HEIGHT,
                new OrthographicCamera());
        stage = new Stage(viewport);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        scoreLabel = new Label("Score: 000000", new Label.LabelStyle(
                game.assetManager.manager.get(game.assetManager.fontSmall, BitmapFont.class), Color.WHITE));

        levelLabel = new Label("Level 1", new Label.LabelStyle(
                game.assetManager.manager.get(game.assetManager.fontSmall, BitmapFont.class), Color.WHITE));

        livesLabel = new Label("Lives: ", new Label.LabelStyle(
                game.assetManager.manager.get(game.assetManager.fontSmall, BitmapFont.class), Color.WHITE));

        table.top().left();
        table.add(levelLabel).left();
        table.row();
        table.add(scoreLabel).left();
        table.row();
        table.add(livesLabel).left();
        stage.addActor(table);
    }

    // call this method to update the score, lives, level
    public void update(){

    }

    public void render(){
        stage.act();
        stage.draw();
    }


    @Override
    public void dispose() {
        ballTexture.getTexture().dispose();
    }
}
