package com.mygdx.game.characters.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.PlatformerShooter1;

public class GruntEnemy extends Enemy implements Disposable {
    public GruntEnemy(World world, AssetManager assetManager, float xPos, float yPos){
        super(world, 1.5f, 3);
        this.assetManager = assetManager;
        pos = new Vector2(xPos, yPos);

        // define textures
        stand = new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 2, 38, 20, 32);

        damaged = new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 200, 38, 20, 32);

        // animations
        frames = new Array<>();

        // walking animation
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 32, 38, 20, 32));
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 56, 38, 20, 32));
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 79, 38, 20, 32));
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 101, 38, 20, 32));

        walkingAnimation = new Animation<TextureRegion>(0.25f, frames);
        frames.clear();

        // attack animation
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 137, 38, 20, 32));
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 167, 38, 30, 32));
        attackAnimation = new Animation<TextureRegion>(0.2f, frames);
        attackTimerDuration = attackAnimation.getAnimationDuration();
        frames.clear();

        // killed animation
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 256, 45, 27, 29));
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 289, 52, 33, 24));
        killedAnimation = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        // health
        health = 3;

        // instantiate states
        currentState = State.STAND;
        previousState = currentState;

        damagedTimerDuration = 1f;
        killedTimerDuration = 1.5f;


        walkingTimer = 1.5f;
        waitingTimer = 1f;

        facingRight = true;

        setRegion(stand);
        setScale(2, 2);
        setBounds(0,
                0,
                getRegionWidth()/ PlatformerShooter1.PPM,
                getRegionHeight()/PlatformerShooter1.PPM);
        setPosition(xPos, yPos);

        defineBody();
    }

    private void defineBody(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(getBoundingRectangle().getWidth() / 2.5f,
                getBoundingRectangle().getHeight() / 2);
        fdef.shape = bodyShape;
        fdef.filter.categoryBits = PlatformerShooter1.ENEMY_BIT;
        fdef.filter.maskBits = PlatformerShooter1.GROUND_BIT | PlatformerShooter1.BULLET_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    private void redefineBodyToKilled(){
        Vector2 currentPos = b2body.getPosition();

        // destroy body
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(getBoundingRectangle().getWidth() / 2.5f,
                getBoundingRectangle().getHeight() / 4);
        fdef.shape = bodyShape;
        fdef.filter.categoryBits = PlatformerShooter1.ENEMY_BIT;
        fdef.filter.maskBits = PlatformerShooter1.GROUND_BIT;
        b2body.createFixture(fdef).setUserData(this);

        // apply a small force to the body
        b2body.applyLinearImpulse(new Vector2(-2, -2), b2body.getWorldCenter(), true);

        timeToRedefineEnemy = false;
    }

    public void hit(){
        // add a bit of resistance so that the enemy doesn't fly off
        b2body.setLinearDamping(150f);

        // reset the damage timer
        damagedTimer = 0;

        // enemy goes to damaged state
        currentState = State.DAMAGED;

        // decrease health
        if(--health <= 0){
            // switch to killed state
            currentState = State.KILLED;

            // play dying sound
            Sound dyingSfx = assetManager.get("sfx/Painsounds v2 - Track 2 - Hurgh.ogg", Sound.class);
            dyingSfx.play(0.15f);


            timeToRedefineEnemy = true;
        }else{
            // play getting hit sound
            Sound hurtSfx = assetManager.get("sfx/enemy_hurt.ogg", Sound.class);
            hurtSfx.play(0.10f);
        }

        System.out.println("Current Health: " + health);
    }

    public void reverse() {
        // reverse direction
        if(currentState == State.WALKING){
            xVelocity *= -1;
        }

        // flip sprite
        facingRight = !facingRight;
    }

    @Override
    public void draw(Batch batch) {
        // don't render sprite if it is destroyed
        if(destroyed) return;

        super.draw(batch);
        setBounds(0, 0,
                getRegionWidth() / PlatformerShooter1.PPM,
                getRegionHeight() / PlatformerShooter1.PPM);

        // adjust the sprite to the hitbox depending on where they are facing
        if(facingRight)
            setPosition(b2body.getPosition().x - getBoundingRectangle().getWidth() / 4,
                b2body.getPosition().y - getBoundingRectangle().getHeight() / 2);
        else
            setPosition(b2body.getPosition().x - getBoundingRectangle().getWidth() / 2,
                    b2body.getPosition().y - getBoundingRectangle().getHeight() / 2);


//        if(!world.isLocked() && !destroyed){
//            world.destroyBody(b2body);
//            destroyed = true;
//        }
    }

    public TextureRegion getFrame(float dt){
        TextureRegion region = null;

        switch(currentState){
            case STAND:
            case WAITING:
                region = stand; // no jumping animation at the moment

                b2body.setLinearVelocity(0, 0);

                // Check if it's time to start walking again
//                if (stateTimer >= waitingTimer) {
//                    currentState = State.WALKING;
//                    stateTimer = 0;
//                }

                break;
            case WALKING:
                b2body.setLinearDamping(0);
                region = walkingAnimation.getKeyFrame(stateTimer, true);
//                b2body.setLinearVelocity(xVelocity, 0);

                // Check if it's time to switch to waiting
//                if (stateTimer >= walkingTimer) {
//                    currentState = State.WAITING;
//                    stateTimer = 0;
//                }

                break;
            case DAMAGED:
                region = damaged;
                break;

            case ATTACK:
                region = attackAnimation.getKeyFrame(stateTimer, true);
                break;
            case KILLED:
                region = killedAnimation.getKeyFrame(stateTimer, false);
                break;
        }

        // update state timer
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    public void update(float dt){
//        System.out.println("Enemy Current State: " + currentState);
        setRegion(getFrame(dt));
        setFlip(facingRight == false, false);

        if(timeToRedefineEnemy){
            redefineBodyToKilled();
        }

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }

        if(currentState == State.DAMAGED){
            damagedTimer += dt;

            if(damagedTimer >= damagedTimerDuration){
                // reset timer
                damagedTimer = 0;

                // change state to waiting
                currentState = State.WAITING;
            }
        }else if(currentState == State.KILLED){
            killedTimer += dt;

            // flash to show it is going to disappear
            // make sprite blink to indicate invincibility
            float blink = Math.sin(killedTimer * 10) > 0 ? 1.0f : 0.0f;
            setAlpha(blink);

            if(killedTimer >= killedTimerDuration){
                setToDestroy = true;
            }
        }

        if(currentState == State.ATTACK){
            attackTimer += dt;
            attacking = true;
            if(attackTimer >= attackTimerDuration){
                attackTimer = 0;
                attacking = false;
                currentState = State.WAITING;
            }
        }
    }

    @Override
    public void dispose() {

    }
}






