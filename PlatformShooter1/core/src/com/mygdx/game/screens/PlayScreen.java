package com.mygdx.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PlatformerShooter1;
import com.mygdx.game.characters.Player;
import com.mygdx.game.characters.enemies.Boss;
import com.mygdx.game.characters.enemies.Enemy;
import com.mygdx.game.characters.enemies.GruntEnemy;
import com.mygdx.game.projectiles.Bullet;
import com.mygdx.game.scenes.Explosion;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.tools.B2WorldCreator;
import com.mygdx.game.tools.WorldContactListener;

import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class PlayScreen extends InputAdapter implements Screen {
    private PlatformerShooter1 game;
    private OrthographicCamera cam;
    private Viewport gamePort;
    private Hud hud;

    // load map stuff
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer render;
    private B2WorldCreator b2WorldCreator;

    // box2d vars
    private World world;
    private Box2DDebugRenderer b2dr;

    // music
    private Music backgroundMusic;

    private ShapeRenderer shapeRenderer;

    // flags
    private boolean canTranslateCamera;

    // entities
    private Player player;
    private Texture explosionTexture;
    private LinkedList<Explosion> explosionList;

    public PlayScreen(Game game){
        this.game = (PlatformerShooter1) game;
        cam = new OrthographicCamera();
        gamePort = new StretchViewport(
                PlatformerShooter1.V_WIDTH / PlatformerShooter1.PPM,
                PlatformerShooter1.V_HEIGHT / PlatformerShooter1.PPM,
                cam
        );


        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        cam.setToOrtho(false, gamePort.getWorldWidth(), gamePort.getWorldHeight());

        // center camera
        // N: not sure if we need to do this as I think it might be centered already
        cam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        mapLoader = new TmxMapLoader();
//        map = mapLoader.load("demo_level.tmx");
        map = mapLoader.load("level1.tmx");
        render = new OrthogonalTiledMapRenderer(map, 1/PlatformerShooter1.PPM);

        // no gravity and sleep if possible (saves resources)
        world = new World(new Vector2(0, -20), true);
        b2dr = new Box2DDebugRenderer();
        b2WorldCreator = new B2WorldCreator(world, map, ((PlatformerShooter1) game).getAssetManager());
        world.setContactListener(new WorldContactListener());

        // load characters
        player = new Player(world, (PlatformerShooter1) game);
        hud = new Hud(player, cam);

        // play background music
        backgroundMusic = ((PlatformerShooter1) game).getAssetManager().get("music/level1.wav");
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.05f);
        backgroundMusic.play();

        canTranslateCamera = true;

//        explosionTexture = new Texture(Gdx.files.internal("raw-textures/explosion-animation.png"));
        explosionTexture = new Texture(Gdx.files.internal("raw-textures/explosion.png"));
        explosionList = new LinkedList<>();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {

    }

    private void handleInput(float dt){
        if(Gdx.input.justTouched()){
            System.out.println(Gdx.input.getX() + "," + Gdx.input.getY());
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            // go right
            player.move("Right");
        }else if(Gdx.input.isKeyPressed(Input.Keys.A)){
            // go left
            player.move("Left");
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            // jump
            player.move("Jump");
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            // shoot
            player.shoot();
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        // this means player stopped walking so go back to walking state
        player.move("Stand");
        return super.keyUp(keycode);
    }

    public void update(float delta){
        handleInput(delta);

        // update world
        world.step(1/60f, 6, 2);

        // player
        player.update(delta);

//        for(GruntEnemy gruntEnemy : b2WorldCreator.getGruntEnemies()){
//            gruntEnemy.update(delta);
//            makeEnemyWalk(gruntEnemy);
//        }

        for(Enemy enemy : b2WorldCreator.getEnemies()){
            if(enemy instanceof GruntEnemy){
                ((GruntEnemy)enemy).update(delta);
            }else if(enemy instanceof Boss){
                // check to see if boss is destroyed if so then change to congratulations screen
                if(enemy.isDestroyed()){
                    game.getScreenManager().setScreen(new WinScreen(game));
                }else{
                    ((Boss) enemy).update(delta);
                }
            }
            makeEnemyWalk(enemy);
        }

        // translate camera after reaching a point
        if(player.getB2body().getPosition().x >= gamePort.getWorldWidth() && canTranslateCamera){
            cam.translate(gamePort.getWorldWidth(), 0);
            canTranslateCamera = false;
        }

        // then make camera follow player
        if(!canTranslateCamera){
            cam.position.x = player.getB2body().getPosition().x;
            cam.position.y = player.getB2body().getPosition().y;
        }

        // update camera on every iteration of the render cycle
        cam.update();

        // render what can be seen relative to the gameCam's current position
        render.setView(cam);
    }
    private void makeEnemyWalk(Enemy enemy){
        if(enemy.getCurrentState() == Enemy.State.DAMAGED
                || enemy.getCurrentState() == Enemy.State.KILLED) return;

        // check to see if enemy is close to player
        // positive - player is in the left
        // negative - player is in the right
        float xDiffDistance = enemy.getB2body().getPosition().x - player.getB2body().getPosition().x;
        float yDiffDistance = Math.abs(enemy.getB2body().getPosition().y - player.getB2body().getPosition().y);
        System.out.println(enemy.isAttacking());
        System.out.println(xDiffDistance);
        if(Math.abs(xDiffDistance) < 35 / PlatformerShooter1.PPM){
            if(enemy instanceof Boss){
                if(yDiffDistance > 100 / PlatformerShooter1.PPM) return;
            }else{
                if(yDiffDistance > 15 / PlatformerShooter1.PPM) return;
            }
//            System.out.println("Enemy attacks");
            // switch to attacking state
            enemy.setCurrentState(Enemy.State.ATTACK);
            player.hit();
            if(xDiffDistance >= 0) // face left
                enemy.setFacingRight(false);
            else
                enemy.setFacingRight(true);
        }else if((Math.abs(xDiffDistance) > 35 / PlatformerShooter1.PPM
                && Math.abs(xDiffDistance) <= 300 / PlatformerShooter1.PPM) && !enemy.isAttacking()) {
            // walk towards player
            System.out.println("Setting Enemy State to Walking");
            enemy.setCurrentState(Enemy.State.WALKING);

            if(xDiffDistance >= 0) { // walk left
                enemy.setFacingRight(false);
                enemy.getB2body().setLinearVelocity(-enemy.getxVelocity(), 0);
            }else{ // walk right
                enemy.setFacingRight(true);
                enemy.getB2body().setLinearVelocity(enemy.getxVelocity(), 0);
            }
        }else if(Math.abs(xDiffDistance) >= 300 / PlatformerShooter1.PPM && !enemy.isAttacking()){
            enemy.setCurrentState(Enemy.State.WAITING);
        }
    }

//    private void makeEnemyWalk(GruntEnemy gruntEnemy){
//        if(gruntEnemy.getCurrentState() == Enemy.State.DAMAGED
//            || gruntEnemy.getCurrentState() == Enemy.State.KILLED) return;
//
//        // check to see if enemy is close to player
//        // positive - player is in the left
//        // negative - player is in the right
//        float xDiffDistance = gruntEnemy.getB2body().getPosition().x - player.getB2body().getPosition().x;
//        float yDiffDistance = Math.abs(gruntEnemy.getB2body().getPosition().y - player.getB2body().getPosition().y);
//        if(Math.abs(xDiffDistance) < 35 / PlatformerShooter1.PPM){
//            if(yDiffDistance > 15 / PlatformerShooter1.PPM) return;
//
//            // switch to attacking state
//            gruntEnemy.setCurrentState(Enemy.State.ATTACK);
//            player.hit();
//            if(xDiffDistance >= 0) // face left
//                gruntEnemy.setFacingRight(false);
//            else
//                gruntEnemy.setFacingRight(true);
//        }else if(Math.abs(xDiffDistance) > 35 / PlatformerShooter1.PPM
//                && Math.abs(xDiffDistance) <= 100 / PlatformerShooter1.PPM) {
//            // walk towards player
//            gruntEnemy.setCurrentState(Enemy.State.WALKING);
//
//            if(xDiffDistance >= 0) { // walk left
//                gruntEnemy.setFacingRight(false);
//                gruntEnemy.getB2body().setLinearVelocity(-gruntEnemy.getxVelocity(), 0);
//            }else{ // walk right
//                gruntEnemy.setFacingRight(true);
//                gruntEnemy.getB2body().setLinearVelocity(gruntEnemy.getxVelocity(), 0);
//            }
//        }else if(Math.abs(xDiffDistance) >= 100 / PlatformerShooter1.PPM){
//            gruntEnemy.setCurrentState(Enemy.State.WAITING);
//        }
//    }


    @Override
    public void render(float delta) {
        update(delta);

        // need this to reset background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render map
        render.render();
//        b2dr.render(world, cam.combined);

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for(Bullet bullet : player.getBullets()){
            bullet.draw(shapeRenderer);
        }

        shapeRenderer.end();

        game.getBatch().setProjectionMatrix(cam.combined);
        game.getBatch().begin();

        // enemies
//        for(GruntEnemy gruntEnemy : b2WorldCreator.getGruntEnemies()){
//            gruntEnemy.draw(game.getBatch());
//        }

        for(Enemy enemy : b2WorldCreator.getEnemies()){
            enemy.draw(game.getBatch());
            if(enemy instanceof Boss && enemy.getHealth() <= 0 && !((Boss) enemy).dead){
                explosionList.add(new Explosion(explosionTexture,
                        enemy.getB2body().getPosition(), 1f,
                        game.getAssetManager()));

                ((Boss) enemy).dead = true;
            }

//            if(enemy instanceof GruntEnemy){
//                ((GruntEnemy) enemy).draw(game.getBatch());
//            }
        }

        // player
        player.draw(game.getBatch());

        ListIterator<Explosion> explosionListIterator = explosionList.listIterator();
        while(explosionListIterator.hasNext()){
            Explosion explosion = explosionListIterator.next();
            explosion.update(delta);
            if(explosion.isFinished()){
                // remove the animation once it is finished
                explosionListIterator.remove();
            }else{
                // call on the explosion draw method to draw the animation
                explosion.draw(game.getBatch());
            }
        }


        // draw hud at the end
        hud.render(game.getBatch());
        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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

    }
}
