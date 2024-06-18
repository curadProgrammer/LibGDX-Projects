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

public class StartScreen extends FlappyBirdScreen {
    // heads up display
    float hudVerticalMargin, hudCentreX, hudRowY, hudSectionWidth;

    // textures
    TextureRegion startButton, exitButton;
    Rectangle startButtonBox, exitButtonBox;

    public StartScreen(SpriteBatch spriteBatch, Camera camera, Viewport viewport, ScreenManager screenManager,
                       TextureAtlas textureAtlas) {
        super(spriteBatch, camera, viewport, screenManager, textureAtlas);
        prepareHud();

        // buttons
        startButton = textureAtlas.findRegion("start");
        exitButton = textureAtlas.findRegion("exit");

        // create the configurations of the button
        startButtonBox = new Rectangle(FlappyBird.WORLD_WIDTH/2 - FlappyBird.WORLD_WIDTH / 6,
                FlappyBird.WORLD_HEIGHT/2.5f, FlappyBird.WORLD_WIDTH / 3,
                FlappyBird.WORLD_HEIGHT / 9);
        exitButtonBox = new Rectangle(FlappyBird.WORLD_WIDTH/2 - FlappyBird.WORLD_WIDTH / 6,
                FlappyBird.WORLD_HEIGHT/2.5f - FlappyBird.WORLD_HEIGHT/6, FlappyBird.WORLD_WIDTH / 3,
                FlappyBird.WORLD_HEIGHT / 9);
    }

    @Override
    public void render(float delta) {
        // render the scrolling background
        super.render(delta);

        spriteBatch.begin();

        detectInput();

        // render title and buttons
        renderHud();

        spriteBatch.end();
    }

    @Override
    public void prepareHud(){
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Gameplay.ttf"));

        // customize the font after creating it
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 32;
        fontParameter.borderWidth = 3.6f;
        font = fontGenerator.generateFont(fontParameter);

        font.getData().setScale(0.15f);

        hudVerticalMargin = font.getCapHeight();
        hudRowY = FlappyBird.WORLD_HEIGHT/1.25f - hudVerticalMargin;
        hudCentreX = FlappyBird.WORLD_WIDTH / 20;
        hudSectionWidth = FlappyBird.WORLD_WIDTH/2;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void renderHud(){
        // draw title
        font.draw(spriteBatch, "Flappy Bird", hudCentreX, hudRowY, hudSectionWidth, Align.left, false);

        // draw start button
        spriteBatch.draw(startButton, startButtonBox.x, startButtonBox.y, startButtonBox.width, startButtonBox.height);

        // draw exit button
        spriteBatch.draw(exitButton, exitButtonBox.x, exitButtonBox.y, exitButtonBox.width, exitButtonBox.height);
    }

    @Override
    public void detectInput(){
        if(Gdx.input.justTouched()){
            Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(worldCoordinates.x);

            // converts screen to world coordinates
            camera.unproject(worldCoordinates);
            System.out.println(worldCoordinates.x);

            // startbutton.x to startbutton.x + startbutton.width (x - limit)
            // startbutton.y to startbutton.y + startbutton.height (y - limit)
            float startButtonXLimit = startButtonBox.x + startButtonBox.width;
            float startButtonYLimit = startButtonBox.y + startButtonBox.height;
            if(worldCoordinates.x >= startButtonBox.x
                        && worldCoordinates.x <= startButtonXLimit
                        && worldCoordinates.y >= startButtonBox.y
                        && worldCoordinates.y <= startButtonYLimit){
                System.out.println("Touched Start Button");

                // update to the game screen
                screenManager.setScreen(new GameScreen(spriteBatch, camera, viewport, screenManager, textureAtlas));
            }

            // exitButton.x to exitButton.x + exitButton.width (x - limit)
            // exitButton.y to exitButton.y + exitButton.height (y - limit)
            float exitButtonXLimit = exitButtonBox.x + exitButtonBox.width;
            float exitButtonYLimit = exitButtonBox.y + exitButtonBox.height;
            if(worldCoordinates.x >= exitButtonBox.x
                    && worldCoordinates.x <= exitButtonXLimit
                    && worldCoordinates.y >= exitButtonBox.y
                    && worldCoordinates.y <= exitButtonYLimit){
//                System.out.println("Touched Exit Button");

                // end application
                Gdx.app.exit();
            }
        }
    }
}











