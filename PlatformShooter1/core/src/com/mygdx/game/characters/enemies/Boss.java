package com.mygdx.game.characters.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PlatformerShooter1;

public class Boss extends Enemy{
    public boolean dead;

    public Boss(World world, AssetManager assetManager, float xPos, float yPos) {
        super(world, 1f, 10);
        this.assetManager = assetManager;
        this.pos = new Vector2(xPos, yPos);

        // standing
        stand = new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 3, 95, 75, 105);

        // animations
        frames = new Array<>();

        // attack animation
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 80, 95, 75, 110));
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 301, 95, 75, 110));
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 379, 80, 85, 126));
        attackAnimation = new Animation<TextureRegion>(0.15f, frames);
        frames.clear();
        attackTimerDuration = attackAnimation.getAnimationDuration();

        // walking
        frames.add(new TextureRegion(((TextureAtlas) assetManager.get("platform-shooter.atlas"))
                .findRegion("player"), 222, 95, 77, 110));
        walkingAnimation = new Animation<TextureRegion>(0.15f, frames);
        frames.clear();

        damagedTimerDuration = 1f;
        killedTimerDuration = 3f;


        // instantiate states
        currentState = State.STAND;
        previousState = currentState;

        setRegion(stand);
        setScale(1.75f, 1.75f);
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

    public TextureRegion getFrame(float dt){
        TextureRegion region = null;

        switch(currentState){
            case STAND:
            case WAITING:
                region = stand; // no jumping animation at the moment
                b2body.setLinearVelocity(0, 0);
                break;
            case WALKING:
                b2body.setLinearDamping(0);
                region = walkingAnimation.getKeyFrame(stateTimer, false);
                break;
            case DAMAGED:
                region = stand;
                break;
            case ATTACK:
                System.out.println(stateTimer);
                region = attackAnimation.getKeyFrame(stateTimer, true);
                break;
            case KILLED:
                region = stand;
                break;
        }

        // update state timer
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    public void hit(){
        // add a bit of resistance so that the enemy doesn't fly off
        b2body.setLinearDamping(150f);

        // reset the damage timer
        damagedTimer = 0;

        // enemy goes to damaged state
        currentState = State.DAMAGED;

        attacking = false;

        // decrease health
        if(--health <= 0){
            // switch to killed state
            currentState = State.KILLED;

            // not collidable with bullets anymore
            b2body.getFixtureList().get(0).getFilterData().maskBits = PlatformerShooter1.GROUND_BIT;
        }

        // play getting hit sound
        Sound hurtSfx = assetManager.get("sfx/cyborg_hurt.ogg", Sound.class);
        hurtSfx.play(0.10f);

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
            setPosition(b2body.getPosition().x - getBoundingRectangle().getWidth() / 2,
                    b2body.getPosition().y - getBoundingRectangle().getHeight() / 2);
        else
            setPosition(b2body.getPosition().x - getBoundingRectangle().getWidth() / 2,
                    b2body.getPosition().y - getBoundingRectangle().getHeight() / 2);

    }

    public void update(float dt){
        System.out.println("Boss State: " + currentState);
//        System.out.println(stateTimer);
        setRegion(getFrame(dt));
        setFlip(facingRight == false, false);

        if(currentState == State.ATTACK){
            attackTimer += dt;
            attacking = true;
            if(attackTimer >= attackTimerDuration){
                attackTimer = 0;
                attacking = false;
                currentState = State.WAITING;
            }
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

    }
}
