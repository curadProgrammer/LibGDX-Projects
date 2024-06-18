package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.FlappyBird;
import com.mygdx.game.game_objs.Bird;
import com.mygdx.game.game_objs.Pipe;

public class GameScreen extends FlappyBirdScreen{
    private final int PIPE_COUNT = 3;
    // heads up display
    private float hudVerticalMargin, hudCentreX, hudRow1Y, hudRow2Y, hudSectionWidth;
    private int score;

    private Bird bird;
    private Pipe pipes[];
    private boolean passedPipe[]; // used to for updating the score
    private TextureRegion[] birdTextures;
    private TextureRegion topPipeTexture, bottomPipeTexture;
    private boolean isGameOver;

    // flash animation (death) will be used to dictate when the flash animation is over
    private ShapeRenderer shapeRenderer;
    private float flashTimer;
    private float maxFlashTimer;

    // sound
    private Sound pipeSound, crashSound;

    public GameScreen(SpriteBatch spriteBatch, Camera camera, Viewport viewport, ScreenManager screenManager,
                       TextureAtlas textureAtlas) {
        super(spriteBatch, camera, viewport, screenManager, textureAtlas);
        // N: for the time being because we are going to want to setup an animation
        // size 2 because we only have to frames for the bird animation
        birdTextures = new TextureRegion[2];
        birdTextures[0] = textureAtlas.findRegion("bird1");
        birdTextures[1] = textureAtlas.findRegion("bird2");

        topPipeTexture = textureAtlas.findRegion("toptube");
        bottomPipeTexture = textureAtlas.findRegion("bottomtube");

        // init bird obj
        bird = new Bird(new Vector2(10, FlappyBird.WORLD_HEIGHT/2), birdTextures);

        // generates a pair of pipes
        pipes = new Pipe[PIPE_COUNT];
        for(int i = 0; i < pipes.length; i++){
            if(i == 0){
                pipes[i] = new Pipe(FlappyBird.WORLD_WIDTH, topPipeTexture, bottomPipeTexture);
            }else{
                // we place the pipe SPACING away from the previous pipe
                pipes[i] = new Pipe(pipes[i - 1].posBottomPipe.x + Pipe.SPACING, topPipeTexture, bottomPipeTexture);
            }
        }

        passedPipe = new boolean[PIPE_COUNT];

        // sound
        pipeSound = Gdx.audio.newSound(Gdx.files.internal("pipe-sound.ogg"));
        crashSound = Gdx.audio.newSound(Gdx.files.internal("crash.ogg"));

        // flash animation
        shapeRenderer = new ShapeRenderer();
        flashTimer = 0;
        maxFlashTimer = 10f;

        prepareHud();
    }

    @Override
    public void render(float delta) {
        // renders the scrolling background
        super.render(delta);

        spriteBatch.begin();
        checkGameOver();

        if(isGameOver && Float.compare(flashTimer, maxFlashTimer) < 0){
            // remove the hitbox of the bird
            renderFlash(delta);
        }

        if(!isGameOver){
            // only read input until game over
            detectInput();
        }

        // render pipes
        renderPipes(spriteBatch, delta);

        // render bird
        renderAndUpdateBird(spriteBatch, delta);

        // check if bird passed pipe
        updateScore();

        // render title and buttons
        // N: this has to be at the end so that it can overlay on top of any other sprites
        renderHud();

        // check to see if game is over and that the bird's position is below 0
        if(isGameOver && bird.getBounds().y + bird.getBounds().height < 0){
            screenManager.setScreen(new GameOverScreen(
                    spriteBatch, camera, viewport, screenManager, textureAtlas, score
            ));
        }

        spriteBatch.end();
    }

    private void updateScore(){
        for(int i = 0; i < pipes.length; i++){
            Pipe pipe = pipes[i];

            // checks so see if bird passed pipe and if it hasn't been passed before
            if(Float.compare(bird.getBounds().x, pipe.boundsTopPipe.x + Pipe.PIPE_WIDTH) > 0 &&
                    !passedPipe[i]){
                // this means that the bird passed the pipe
                score++;

                // update passedPipe
                passedPipe[i] = true;

                // play sound
                pipeSound.play();
            }
        }
    }

    // conditions for game over
    private void checkGameOver(){
        // bird flies below ground
        if(bird.getBounds().y + bird.getBounds().height < 0){
            isGameOver = true;
            crashSound.play(0.35f);
            return;
        }

        if(!isGameOver){
            for(Pipe pipe: pipes){
                if(pipe.checkCollision(bird)){
                    System.out.println("Bird hit a pipe");
                    isGameOver = true;
                    crashSound.play(0.35f);
                    // no need to keep iterating
                    return;
                }
            }
        }
    }

    private void renderFlash(float delta){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1); // White color
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        while(Float.compare(flashTimer, maxFlashTimer) < 0) {
            flashTimer += delta;
        }

        shapeRenderer.end();
    }

    private void renderAndUpdateBird(SpriteBatch batch, float delta){
        bird.update(delta);
        bird.draw(batch);
    }

    private void renderPipes(SpriteBatch batch, float delta){
        for(int i = 0; i < pipes.length; i++){
            Pipe pipe = pipes[i];
            pipe.draw(batch);

            // 5 is padding to make it a little bit more accurate
            if(pipe.posTopPipe.x + Pipe.PIPE_WIDTH + 5 <= 0){
                // reposition pipes based on their index
                switch(i){
                    case 0:
                        // pipe #1 goes behind pipe #3
                        pipe.repositionPipe(pipes[2].posTopPipe.x + Pipe.SPACING);
                        break;
                    case 1:
                        // pipe #2 goes behind pipe #1
                        pipe.repositionPipe(pipes[0].posTopPipe.x + Pipe.SPACING);
                        break;
                    case 2:
                        // pipe #3 goes behind pipe #2
                        pipe.repositionPipe(pipes[1].posTopPipe.x + Pipe.SPACING);
                        break;
                }

                // reset passedPipe
                passedPipe[i] = false;
            }

            if(!isGameOver){
                pipe.updatePipes(delta);
            }
        }
    }

    @Override
    public void prepareHud() {
        score = 0;

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Gameplay.ttf"));

        // customize the font after creating it
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 30;
        fontParameter.borderWidth = 3.6f;
        font = fontGenerator.generateFont(fontParameter);

        font.getData().setScale(0.15f);

        // location
        hudVerticalMargin = font.getCapHeight();
        hudRow1Y = FlappyBird.WORLD_HEIGHT - hudVerticalMargin;
        hudRow2Y = FlappyBird.WORLD_HEIGHT/1.10f - hudVerticalMargin;
        hudCentreX = FlappyBird.WORLD_WIDTH / 3.75f;
        hudSectionWidth = FlappyBird.WORLD_WIDTH/2;
    }

    @Override
    public void renderHud(){
        // draw title
        font.draw(spriteBatch, "Score", hudCentreX, hudRow1Y, hudSectionWidth, Align.left, false);
        font.draw(spriteBatch, Integer.toString(score), hudCentreX, hudRow2Y, hudSectionWidth, Align.center, false);

        // load the retry and menu button when game is over
        if(isGameOver){
//            spriteBatch.draw(restartButton, )
        }
    }

    @Override
    public void detectInput(){
        if(Gdx.input.justTouched()){
            bird.jump();
        }
    }
}
