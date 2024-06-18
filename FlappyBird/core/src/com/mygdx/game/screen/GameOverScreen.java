package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.FlappyBird;

public class GameOverScreen extends FlappyBirdScreen{
    // hud
    private float hudVerticalMargin, hudCentreX, hudRow1Y, hudSectionWidth;
    private int score;

    // texture
    private TextureRegion gameOverLabel, restartButton;
    private Rectangle restartButtonBounds;

    public GameOverScreen(SpriteBatch spriteBatch, Camera camera, Viewport viewport, ScreenManager screenManager,
                          TextureAtlas textureAtlas, int score){
        super(spriteBatch, camera, viewport, screenManager, textureAtlas);
        gameOverLabel = textureAtlas.findRegion("gameover");
        restartButton = textureAtlas.findRegion("restart");
        restartButtonBounds = new Rectangle(
                FlappyBird.WORLD_WIDTH/2 - FlappyBird.WORLD_WIDTH / 6,
                FlappyBird.WORLD_HEIGHT/2.5f, FlappyBird.WORLD_WIDTH / 3,
                FlappyBird.WORLD_HEIGHT / 9
        );
        this.score = score;

        // restart button
        restartButtonBounds = new Rectangle(FlappyBird.WORLD_WIDTH/2 - FlappyBird.WORLD_WIDTH / 6,
                FlappyBird.WORLD_HEIGHT/5, FlappyBird.WORLD_WIDTH / 3,
                FlappyBird.WORLD_HEIGHT / 9);

        prepareHud();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        spriteBatch.begin();

        detectInput();

        renderHud();
        spriteBatch.end();
    }

    @Override
    public void prepareHud() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Gameplay.ttf"));

        // customize the font after creating it
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 20;
        fontParameter.borderWidth = 5f;
        font = fontGenerator.generateFont(fontParameter);

        font.getData().setScale(0.15f);

        // location
        hudVerticalMargin = font.getCapHeight();
        hudRow1Y = FlappyBird.WORLD_HEIGHT/1.75f - hudVerticalMargin;
        hudCentreX = FlappyBird.WORLD_WIDTH / 6;
        hudSectionWidth = FlappyBird.WORLD_WIDTH;
    }

    @Override
    public void renderHud() {
        // game over
        spriteBatch.draw(gameOverLabel, FlappyBird.WORLD_WIDTH/4, FlappyBird.WORLD_HEIGHT/1.5f,
                FlappyBird.WORLD_WIDTH/2, FlappyBird.WORLD_HEIGHT/10);

        // score label
        font.draw(spriteBatch, "Final Score:\n\n" + score, hudCentreX, hudRow1Y, hudSectionWidth, Align.left, false);

        // restart button
        spriteBatch.draw(restartButton, restartButtonBounds.x, restartButtonBounds.y, restartButtonBounds.getWidth(),
                restartButtonBounds.getHeight());
    }

    @Override
    public void detectInput() {
        if(Gdx.input.justTouched()){
            Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(worldCoordinates.x);

            // converts screen to world coordinates
            camera.unproject(worldCoordinates);
            System.out.println(worldCoordinates.x);

            // startbutton.x to startbutton.x + startbutton.width (x - limit)
            // startbutton.y to startbutton.y + startbutton.height (y - limit)
            float restartButtonXLimit = restartButtonBounds.x + restartButtonBounds.width;
            float startButtonYLimit = restartButtonBounds.y + restartButtonBounds.height;
            if(worldCoordinates.x >= restartButtonBounds.x
                    && worldCoordinates.x <= restartButtonXLimit
                    && worldCoordinates.y >= restartButtonBounds.y
                    && worldCoordinates.y <= startButtonYLimit){
                System.out.println("Touched Restart Button");

                // update to the game screen
                screenManager.setScreen(new GameScreen(spriteBatch, camera, viewport, screenManager, textureAtlas));
            }
        }
    }
}
