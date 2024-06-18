package com.mygdx.game.characters.enemies;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Enemy extends Sprite {
    public enum State{
        STAND, DAMAGED, WALKING, KILLED, ATTACK, WAITING
    }
    protected State currentState, previousState;
    public void setCurrentState(State newState){
        currentState = newState;
    }
    public State getCurrentState(){return currentState;}
    protected float stateTimer; // used with animation to know which part of animation to render
    protected float damagedTimer;
    protected float damagedTimerDuration;
    protected float walkingTimer, waitingTimer;
    protected float killedTimer, killedTimerDuration;
    protected float attackTimer, attackTimerDuration;

    protected TextureRegion stand, damaged;
    protected Animation<TextureRegion> walkingAnimation, killedAnimation, attackAnimation;
    protected Array<TextureRegion> frames; // used for storing frames of an animation
    protected boolean timeToRedefineEnemy;
    protected Vector2 pos;

    protected AssetManager assetManager;
    protected World world;

    // must be instantiated in child class
    protected Body b2body;
    public Body getB2body(){ return b2body;}

    protected float xVelocity;
    public float getxVelocity(){return xVelocity;}

    protected boolean facingRight;
    public boolean isFacingRight(){return facingRight;}
    protected boolean setToDestroy;
    protected boolean destroyed;
    protected boolean attacking;
    public boolean isAttacking(){return attacking;}
    public void setIsAttacking(boolean val){attacking = val;}
    public boolean isDestroyed(){return destroyed;}
    public void setFacingRight(boolean isFacingRight){this.facingRight = isFacingRight;}

    protected int health;
    public int getHealth(){return health;}

    public Enemy(World world, float xVelocity, int health){
        this.world = world;
        this.xVelocity = xVelocity;
        this.health = health;
    }
}

