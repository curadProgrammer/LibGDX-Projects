package com.mygdx.game.characters;

import com.badlogic.gdx.assets.AssetManager;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PlatformerShooter1;
import com.mygdx.game.projectiles.Bullet;
import com.mygdx.game.screens.GameOverScreen;
import com.mygdx.game.screens.ScreenManager;

import java.util.ArrayList;

public class Player extends Sprite {
    // states
    public enum State{
        STAND, DAMAGED, WALKING, JUMPING
    }

    private State currentState, previousState;
    public void setCurrentState(State newState){currentState = newState;}
    public State getCurrentState(){return currentState;}

    private float stateTimer; // used with animation to know which part of animation to render
    private ScreenManager screenManager;
    private PlatformerShooter1 game;

    // box2d
    private World world;
    private Body b2body;
    public Body getB2body(){ return b2body;}

    private int health;
    public int getHealth(){return health;}

    private float xVelocity; // for walking
    private float yVelocity; // for jumping
    private boolean facingRight;
    public boolean isFacingRight(){return facingRight;}
    private boolean canJump;
    private boolean isInvincible;
    private float invincibilityTimer;
    private float invincibilityDuration = 3f; // 3 seconds of i frames

    private float damageStateTimer;
    private float damageStateDuration = 0.65f;

    private float killedTimer;
    private float killedTimerDuration = 0.75f;
    private boolean isKilled;

    // textures
    private TextureRegion stand, damaged;
    private Animation<TextureRegion> walkingAnimation;
    private Array<TextureRegion> frames; // used for storing frames of an animation

    // bullets
    private ArrayList<Bullet> bullets;
    private Sound shootSFX;

    // used to render the bullets from play screen
    public ArrayList<Bullet> getBullets(){
        return bullets;
    }

    // N: I don't want to pass the game as it is pointless so we can just the pass
    // reference to the assetmanager
    public Player(World world, PlatformerShooter1 game){
        // box2d instantiate
        this.world = world;
        this.game = game;
        this.screenManager = game.getScreenManager();
        AssetManager assetManager = game.getAssetManager();
        health = 3; // takes 3 health
        xVelocity = 150 / PlatformerShooter1.PPM;
        yVelocity = 800 / PlatformerShooter1.PPM;

        // instantiate textures
        stand = new TextureRegion(
                ((TextureAtlas) assetManager.get("platform-shooter.atlas")).findRegion("player"),
                29, 0, 32, 34
        );

        damaged = new TextureRegion(
                ((TextureAtlas) assetManager.get("platform-shooter.atlas")).findRegion("player"),
                0, 0, 28, 34
        );

        // instantiate animation
        frames = new Array<>();
        frames.add(new TextureRegion(
                ((TextureAtlas) assetManager.get("platform-shooter.atlas")).findRegion("player"),
                66, 2, 32, 34
        ));

        frames.add(new TextureRegion(
                ((TextureAtlas) assetManager.get("platform-shooter.atlas")).findRegion("player"),
                104, 2, 32, 34
        ));

        walkingAnimation = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // flags
        facingRight = true;
        canJump = true;

        currentState = State.STAND;

        bullets = new ArrayList<>();
        shootSFX = assetManager.get("sfx/shoot.wav", Sound.class);

        definePlayer();

        // texture init
        setRegion(stand);
        setBounds(getB2body().getPosition().x,
                getB2body().getPosition().y,
                getRegionWidth()/ PlatformerShooter1.PPM,
                getRegionHeight()/PlatformerShooter1.PPM);
        setScale(2);

    }

    private void definePlayer(){
        // define body
        BodyDef bdef = new BodyDef();
        bdef.position.set(64/ PlatformerShooter1.PPM, 32/PlatformerShooter1.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        // define fixture
        FixtureDef fdef = new FixtureDef();

        // head
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(15 / PlatformerShooter1.PPM);
        circleShape.setPosition(new Vector2(-0.015f, 25f / PlatformerShooter1.PPM));
        fdef.shape = circleShape;
        b2body.createFixture(fdef).setUserData(this);

        // body
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(20 / PlatformerShooter1.PPM, 20 / PlatformerShooter1.PPM);
        fdef.shape = bodyShape;
        fdef.filter.categoryBits = PlatformerShooter1.PLAYER_BIT;
        fdef.filter.maskBits = PlatformerShooter1.GROUND_BIT;
//                | PlatformerShooter1.ENEMY_BIT;
        b2body.createFixture(fdef).setUserData(this);

        b2body.setLinearDamping(5f);

        // arm
//        PolygonShape armShape = new PolygonShape();
//        armShape.setAsBox(12 / PlatformerShooter1.PPM, 8 / PlatformerShooter1.PPM,
//                new Vector2(0.40f, 0.15f), 0);
//        fdef.shape = armShape;
//        b2body.createFixture(fdef).setUserData("player-arm");
    }

    public void move(String direction){
        if(currentState == State.DAMAGED) return;

        switch(direction){
            case "Right":
                facingRight = true;
                currentState = State.WALKING;

                // N: setting the limit and the start velocity to be the same keeps the speed constant
                if(b2body.getLinearVelocity().x <= xVelocity){
                    b2body.applyLinearImpulse(new Vector2(xVelocity, 0), b2body.getWorldCenter(), true);
                }

                break;
            case "Left":
                facingRight = false;
                currentState = State.WALKING;

                if(b2body.getLinearVelocity().x >= -xVelocity){
                    b2body.applyLinearImpulse(new Vector2(-xVelocity, 0), b2body.getWorldCenter(), true);
                }
                break;
            case "Jump":
                if(canJump && b2body.getLinearVelocity().y >= -yVelocity && currentState != State.JUMPING){
                    b2body.applyLinearImpulse(new Vector2(0, yVelocity), b2body.getWorldCenter(),
                            true);
                    canJump = false;
                }

                currentState = State.JUMPING;
                break;

            case "Stand":
                b2body.setLinearVelocity(new Vector2(0, 0));
                currentState = State.STAND;
                break;
        }
    }

    // depending on state, we will return the corresponding frame
    public TextureRegion getFrame(float dt){
        TextureRegion region = null;

        switch(currentState){
            case STAND:
            case JUMPING:
                region = stand; // no jumping animation at the moment
                break;
            case WALKING:
                region = walkingAnimation.getKeyFrame(stateTimer, true);
                break;
            case DAMAGED:
                region = damaged;
                break;
        }

        // update state timer
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    // call this method if the player gets hit by an enemy
    public void hit(){
        if(isInvincible) return;

//        b2body.getFixtureList().get(1).getFilterData().maskBits = PlatformerShooter1.GROUND_BIT;

        System.out.println("Player gets hit");

        // change the state to damaged
        currentState = State.DAMAGED;

        // set to invincible if not already
        isInvincible = true;
        invincibilityTimer = 0;


        // add a small knockback
        getB2body().applyLinearImpulse(new Vector2(-4f, 6f), getB2body().getWorldCenter(),
                true);

        if(--health <= 0){
            isKilled = true;

            // fall through map
            b2body.getFixtureList().get(0).getFilterData().maskBits = PlatformerShooter1.NOTHING_BIT;
            b2body.getFixtureList().get(1).getFilterData().maskBits = PlatformerShooter1.NOTHING_BIT;
        }

        System.out.println("Player Health: " + health);
    }

    public void shoot(){
        bullets.add(new Bullet(this, world));
        shootSFX.play(0.35f);
    }

    public void update(float dt){
//        System.out.println("Player State: " + currentState);

        setRegion(getFrame(dt));
        setFlip(facingRight == false, false);

        // means that player is not jumping or falling therefore can jump again
        if(b2body.getLinearVelocity().y == 0){
            canJump = true;
        }

        updateSprite();

        if(isKilled){
            killedTimer += dt;

            if(killedTimer >= killedTimerDuration){
                screenManager.setScreen(new GameOverScreen(game));
            }
            return;
        }

        handleDamagedState(dt);
        handleInvincibleState(dt);


    }

    private void updateSprite(){
        setBounds(getB2body().getPosition().x, getB2body().getPosition().y,
                getRegionWidth() / PlatformerShooter1.PPM,
                getRegionHeight() / PlatformerShooter1.PPM);

        // adjust the sprite to the hitbox depending on where they are facing
        if(facingRight){
            if(currentState == State.WALKING){
                setPosition(b2body.getPosition().x - getWidth() / 1.25f
                        , b2body.getPosition().y - getHeight()/1.25f);
            }else if(currentState == State.STAND || currentState == State.JUMPING)
                setPosition(b2body.getPosition().x - getWidth()/ 1.5f
                        , b2body.getPosition().y - getHeight()/ 1.5f);
            else
                setPosition(b2body.getPosition().x - getWidth()
                        , b2body.getPosition().y - getHeight()/ 1.5f);
        }else {
            if (currentState == State.WALKING) {
                setPosition(b2body.getPosition().x - getWidth() / 0.75f
                        , b2body.getPosition().y - getHeight() / 1.25f);
            } else if (currentState == State.STAND || currentState == State.JUMPING) {
                setPosition(b2body.getPosition().x - getWidth() / 0.75f
                        , b2body.getPosition().y - getHeight() / 1.5f);
            } else {
                setPosition(b2body.getPosition().x - getWidth()
                        , b2body.getPosition().y - getHeight() / 1.5f);
            }
        }
    }

    private void handleDamagedState(float dt){
        if(currentState == State.DAMAGED){
            damageStateTimer += dt;
            if(damageStateTimer >= damageStateDuration){
                // switch back to stand
                currentState = State.STAND;
                damageStateTimer = 0;
            }
        }
    }
    private void handleInvincibleState(float dt){
        if(isInvincible){
            invincibilityTimer += dt;

            // make sprite blink to indicate invincibility
            float blink = Math.sin(invincibilityTimer * 10) > 0 ? 1.0f : 0.0f;
            setAlpha(blink);

            // make user not invincible anymore
            if(invincibilityTimer >= invincibilityDuration){
                isInvincible = false;
                invincibilityTimer = 0;

                // allow user to collide with enemy again
//                b2body.getFixtureList().get(1).getFilterData().maskBits = PlatformerShooter1.GROUND_BIT
//                        | PlatformerShooter1.ENEMY_BIT;

                setAlpha(1);
            }
        }
    }
}














