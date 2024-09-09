package com.taptap.breakout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taptap.breakout.level.LevelManager;
import com.taptap.breakout.screens.MainScreen;
import com.taptap.breakout.screens.ScreenManager;
import javafx.scene.control.Button;

import java.util.Locale;
import java.util.logging.Logger;

public class Hud implements Disposable {
    private static final Logger logger = Logger.getLogger(Hud.class.getName());
    private static final boolean DEBUG_MODE = true;

    private BreakoutGame game;
    private Stage stage;
    public Stage getStage(){return stage;}
    private Skin skin;
    private TextureAtlas textures;
    private Viewport viewport;
    private LevelManager levelManager;

    // dialog table
    private Table dialog; // N: this will be used to show a dialog to the user
    private Label messageLabel;
    private TextButton nextLevelBtn, tryAgainBtn, goBackToMenuScreenBtn;

    // hud table
    private Table table;
    private TextureRegion ballTexture;
    private Label scoreLabel, levelLabel, livesLabel;
    private Image ballImage;
    private int score, level, lives;

    public int getScore(){return score;}
    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel(){return level;}
    public void setLevel(int level) {
        this.level = level;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public Hud(BreakoutGame game, LevelManager levelManager){
        if(DEBUG_MODE) logger.info("Constructor");

        this.game = game;
        this.levelManager = levelManager;

        textures = game.assetManager.manager.get(game.assetManager.gameImages);
        skin = game.assetManager.manager.get("skin/craftacular-ui.json", Skin.class);

        ballTexture = new TextureRegion(
                textures.findRegion("ball-sheet-removebg-preview"),
                8, 31, 25, 25
        );

        score = 0;
        level = 1;
        lives = 3;

        viewport = new FitViewport(Utilities.VIRTUAL_WIDTH, Utilities.VIRTUAL_HEIGHT,
                new OrthographicCamera());
        stage = new Stage(viewport);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        scoreLabel = new Label(String.format(Locale.getDefault(), "Score: %06d", score), new Label.LabelStyle(
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

        createDialog();

        Gdx.input.setInputProcessor(stage);
    }

    private void createDialog(){
        dialog = new Table();
        dialog.setFillParent(true);
        dialog.setDebug(true);
        dialog.center();
        dialog.setColor(Color.BLACK);

        messageLabel = new Label("", new Label.LabelStyle(
                game.assetManager.manager.get(game.assetManager.fontMedium, BitmapFont.class), Color.WHITE));
        nextLevelBtn = new TextButton("Next Level", skin);
        nextLevelBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Going to Next Level");
                levelManager.loadLevel(++level);
                System.out.println("Level: " + level);

                // hide dialog
                closeDialog();
            }
        });

        tryAgainBtn = new TextButton("Try Again", skin);
        tryAgainBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Resetting Level");

                // hide dialog
                closeDialog();
            }
        });

        goBackToMenuScreenBtn = new TextButton("Go back to Menu Screen", skin);
        goBackToMenuScreenBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // reset configurations
                level = 1;
                lives = 3;
                score = 0;

                game.screenManager.changeScreen(ScreenManager.MENU);

                // hide dialog
                closeDialog();
            }
        });

        dialog.setVisible(false);
        stage.addActor(dialog);
    }

    // call this method to update the score, lives, level
    public void update(){
        levelLabel.setText("Level " + level);
        scoreLabel.setText(String.format(Locale.getDefault(), "Score: %06d", score));
    }

    public void render(){
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void openDialog(boolean hasWon){
        // needed to remove current actors from dialog
        dialog.clearChildren();
        dialog.add(messageLabel);
        dialog.row();

        if(hasWon) {
            messageLabel.setText("Congratulations!");
            if(level >= LevelManager.MAX_LEVELS){
                dialog.add(goBackToMenuScreenBtn);
            }else{
                dialog.add(nextLevelBtn);
            }
        }else{
            messageLabel.setText("Game Over =[");
            dialog.add(tryAgainBtn);
        }
        dialog.setVisible(true);
    }

    public void closeDialog(){
        dialog.setVisible(false);
    }


    @Override
    public void dispose() {
        ballTexture.getTexture().dispose();
    }
}
