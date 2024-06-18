package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PongGame;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.game_objs.Ball;
import com.mygdx.game.game_objs.Paddle;
import com.mygdx.game.hud.PlayScreenHud;
import com.mygdx.game.tools.WorldContactListener;


public class PlayScreen implements Screen {
    private PongGame game;
    private PlayScreenHud hud;

    // box2d
    private World world;
    private Box2DDebugRenderer b2dr;

    // playscreen
    private OrthographicCamera gameCam;
    private Viewport viewport;

    // used to indicate whether the user chose the player v player or v computer option
    private boolean vsPlayer;

    // used to make sure that the computer's moves are delayed
    private float startComputerMoveTimer;

    // game objects
    private Paddle paddle1, paddle2;
    private Ball ball;

    public PlayScreen(Game game, boolean vsPlayer) {
        this.game = (PongGame) game;
        this.vsPlayer = vsPlayer;
        gameCam = new OrthographicCamera();
        viewport = new StretchViewport(PongGame.V_WIDTH / PongGame.PPM,
                PongGame.V_HEIGHT / PongGame.PPM, gameCam);

        // set the camera to use our new viewport (N: it seems we have to use world width and height as that is our viewports coordinates as well)
        gameCam.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());

        // center camera
//        gameCam.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);

        // hud
        hud = new PlayScreenHud();

        // box2d
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();

        // paddles
        paddle1 = new Paddle(world, 25, PongGame.V_HEIGHT / 2, true);

        if(vsPlayer)
            paddle2 = new Paddle(world, PongGame.V_WIDTH - 25, PongGame.V_HEIGHT / 2, true);
        else
            paddle2 = new Paddle(world, PongGame.V_WIDTH - 25, PongGame.V_HEIGHT / 2, false);

        // ball
        ball = new Ball(game, world, PongGame.V_WIDTH/2, PongGame.V_HEIGHT/2);

    }
    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            // reset to screen
            game.getScreenManager().setScreen(new TitleScreen(game));
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)
            && paddle1.getBounds().y + paddle1.getBounds().getHeight()/2 <= PongGame.V_HEIGHT/PongGame.PPM){
            paddle1.getB2Body().setTransform(paddle1.getB2Body().getPosition().x,
                    paddle1.getB2Body().getPosition().y + 0.05f, 0);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) &&
            paddle1.getBounds().y - paddle1.getBounds().getHeight()/2 >= 0){

            paddle1.getB2Body().setTransform(paddle1.getB2Body().getPosition().x,
                    paddle1.getB2Body().getPosition().y - 0.05f, 0);
        }

        if(vsPlayer){
            // allow player 2 to move other paddle
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)
                    && paddle2.getBounds().y + paddle2.getBounds().getHeight()/2 <= PongGame.V_HEIGHT/PongGame.PPM){

                paddle2.getB2Body().setTransform(paddle2.getB2Body().getPosition().x,
                        paddle2.getB2Body().getPosition().y + 0.05f, 0);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) &&
                    paddle2.getBounds().y - paddle2.getBounds().getHeight()/2 >= 0){
                paddle2.getB2Body().setTransform(paddle2.getB2Body().getPosition().x,
                        paddle2.getB2Body().getPosition().y - 0.05f, 0);
            }
        }

    }

    // logic to control the computer
    private void handleComputerInput(float delta) {
        startComputerMoveTimer += delta;

        // the timer is basically giving the cpu time to move to that position before switching
        if(startComputerMoveTimer >= 0.25f){
            // update new destined location
            paddle2.setDestinedLocation(ball.getB2Body().getPosition());

            // reset timer
            startComputerMoveTimer = 0;
        }else{
            // keep moving to the destined location if possible
            if(ball.getBounds().y + paddle2.getBounds().getHeight()/2 <= PongGame.V_HEIGHT/PongGame.PPM
                    && ball.getBounds().y - paddle2.getBounds().getHeight()/2 >= 0) {

                // the num being multiplied to delta and the position is the speed of the paddle
                paddle2.translate((paddle2.getDestinedLocation().y - paddle2.getB2Body().getPosition().y)
                        * Paddle.CPU_PADDLE_SPEED * delta);
            }
        }

        // try to make the paddle follow the position of the ball
//        if(ball.getBounds().y + paddle2.getBounds().getHeight()/2 <= PongGame.V_HEIGHT/PongGame.PPM
//                && ball.getBounds().y - paddle2.getBounds().getHeight()/2 >= 0){
//            // move the paddle
////            paddle2.getB2Body().setTransform(paddle2.getB2Body().getPosition().x,
////                    ball.getBounds().y, 0);
//
//
//
////            float yMove = ball.getB2Body().getPosition().y * 2f * delta;
////            paddle2.translate(yMove);
//        }
    }

    public void update(float delta){
        handleInput(delta);

        if(!vsPlayer){
            handleComputerInput(delta);
        }

        // world will perfrom calculations
        world.step(1/60f, 6, 2);

        // update paddles
        paddle1.update();
        paddle2.update();

        // update ball
        ball.update();

        // check to see if it is out of bounds
        if(ball.getB2Body().getPosition().x + ball.getBounds().radius <= 0 ||
                ball.getB2Body().getPosition().x - ball.getBounds().radius >= PongGame.V_WIDTH/PongGame.PPM){

            // increment score
            if(ball.getB2Body().getPosition().x + ball.getBounds().radius <= 0){
                // ball entered left side
                hud.incrementPlayerTwoScore();
            }else{
                // ball entered right side
                hud.incrementPlayerOneScore();
            }

            // play sound
            game.getAssetManager().get("score.wav", Sound.class).play(3f);

            // place ball back to the center
            ball.recenter();
        }

        // update gamecam
//        gameCam.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // box2d debug renderer
        b2dr.render(world, gameCam.combined);

        // draw shapes relative to our camera's position
        game.getShapeRenderer().setProjectionMatrix(gameCam.combined);
        game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);

        ball.draw(game.getShapeRenderer());
        paddle1.draw(game.getShapeRenderer());
        paddle2.draw(game.getShapeRenderer());

        game.getShapeRenderer().end();

        // draw hud at the end
        hud.draw();

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
        System.out.println("Calling Play Screen Dispose");
        hud.dispose();
    }

    @Override
    public void show() {

    }
}
