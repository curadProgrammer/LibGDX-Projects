package com.taptap.breakout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    private Table table;
    private Viewport viewport;

    private TextureAtlas textures;
    private TextureRegion ballTexture;
    private Label scoreLabel, levelLabel, livesLabel;
    private Image ballImage;
    private int score, level, lives;

    public void setScore(int score) {
        this.score = score;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public Hud(BreakoutGame game){
        this.game = game;

        textures = game.assetManager.manager.get(game.assetManager.gameImages);

        ballTexture = new TextureRegion(
                textures.findRegion("ball-sheet-removebg-preview"),
                8, 31, 25, 25
        );


        score = 0;
        lives = 3;

        viewport = new FitViewport(Utilities.VIRTUAL_WIDTH, Utilities.VIRTUAL_HEIGHT,
                new OrthographicCamera());
        stage = new Stage(viewport);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        scoreLabel = new Label("Score: 000000", new Label.LabelStyle(
                game.assetManager.manager.get(game.assetManager.fontSmall, BitmapFont.class), Color.WHITE));

        levelLabel = new Label("Level 1", new Label.LabelStyle(
                game.assetManager.manager.get(game.assetManager.fontSmall, BitmapFont.class), Color.WHITE));

        livesLabel = new Label("Lives: ", new Label.LabelStyle(
                game.assetManager.manager.get(game.assetManager.fontSmall, BitmapFont.class), Color.WHITE));

        table.top();

        // add lives
        Table livesTabel = new Table();
        livesTabel.setDebug(false);
        livesTabel.add(livesLabel);
        for(int i = 0; i < lives; i++){
            ballImage = new Image(ballTexture);
            ballImage.setScale(0.85f);
            livesTabel.add(ballImage);
        }

        table.add(livesTabel).left().expandX();
        table.add(scoreLabel).center().padRight(50).expandX();
        table.add(levelLabel).right().expandX();
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
