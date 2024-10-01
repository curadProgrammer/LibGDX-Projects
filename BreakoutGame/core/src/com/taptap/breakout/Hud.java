package com.taptap.breakout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taptap.breakout.level.LevelManager;
import com.taptap.breakout.screens.ScreenManager;

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
        table.setDebug(false);

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

        table.add(levelLabel).left().padLeft(5).padTop(5).expandX();
        table.add(scoreLabel).right().padRight(5).padTop(5).expandX();
        table.row();
        table.add(livesTabel).left().padLeft(5);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
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

    // opens a generic dialog which will have a confirm, cancel type button
    private void openDialog(String message, String positiveButtonText, String negativeButtonText,
                            ClickListener positiveListener, ClickListener negativeListener){
        Dialog dialog = new Dialog("", skin);
        dialog.text(message);

        if(positiveButtonText != null){
            TextButton positiveButton = new TextButton(positiveButtonText, skin);
            positiveButton.addListener(positiveListener);
            dialog.button(positiveButton);
        }

        if(negativeButtonText != null){
            TextButton negativeButton = new TextButton(negativeButtonText, skin);
            negativeButton.addListener(negativeListener);
            dialog.button(negativeButton);
        }

        dialog.show(stage);
    }

    // Example method to show the level completion dialog
    public void showLevelCompleteDialog() {
        openDialog(
                "Congratulations! You've completed Level " + level + ".",
                level < LevelManager.MAX_LEVELS ? "Next Level" : null,
                null,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (level < LevelManager.MAX_LEVELS) {
                            levelManager.loadLevel(++level);
                        }
                    }
                },
                null
        );
    }

    public void showGameOverDialog(){
        openDialog(
                "Game Over",
                "Retry",
                "Menu",
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // reset the current level
                        logger.info("clicked on retry");
                        levelManager.loadLevel(level);
                    }
                },

                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        logger.info("clicked on menu");

                        // reset game state
                        level = 1;
                        lives = 3;
                        score = 0;

                        // take user back to the menu screen
                        game.screenManager.changeScreen(ScreenManager.MENU);

                    }
                }
        );
    }

    @Override
    public void dispose() {
        ballTexture.getTexture().dispose();
    }
}
