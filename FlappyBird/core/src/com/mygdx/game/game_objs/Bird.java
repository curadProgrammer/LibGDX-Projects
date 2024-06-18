package com.mygdx.game.game_objs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.FlappyBird;
import com.mygdx.game.screen.FlappyBirdScreen;

public class Bird {
    // what makes the bird go down
    private static final int GRAVITY = -10;
    private static final int JUMP_ACCELERATION = 100;
    public static final int BIRD_WIDTH = (int) (FlappyBird.WORLD_WIDTH / 5);
    public static final int BIRD_HEIGHT = (int) (FlappyBird.WORLD_HEIGHT / 6);
    public final float totalAnimationTime;

    private float birdAnimationTimer;
    private Vector2 pos, velocity; // velocity refers to the value added to the bird's y position when screen is pressed
    private TextureRegion[] birdTextures;
    public Rectangle bounds;

    // sound
    private Sound flapSound;

    // bird has a flying animation
    private Animation <TextureRegion> birdAnimation;

    public Bird(Vector2 pos, TextureRegion[] birdTextures){
        totalAnimationTime = 0.25f;
        birdAnimationTimer = 0;

        velocity = new Vector2(0, 20);

        this.pos = pos;
        this.birdTextures = birdTextures;

        // create animation
        birdAnimation = new Animation<TextureRegion>(totalAnimationTime/birdTextures.length, birdTextures);

        // loop animation
        birdAnimation.setPlayMode(Animation.PlayMode.LOOP);

        bounds = new Rectangle(pos.x, pos.y, BIRD_WIDTH, BIRD_HEIGHT);

        flapSound = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));

    }

    public void update(float deltaTime){
        // update a bird animation timer
        birdAnimationTimer += deltaTime;

        if(pos.y > 0){
            velocity.add(0, GRAVITY);// x is 0 because the pipes are moving and not the bird
        }

        velocity.scl(deltaTime); // scale change by multiplying a delta time (small value) to the x and y position values
        pos.add(0, velocity.y);

        // we do this so that we can scale to delta time in the next frame
        velocity.scl(1/deltaTime);
        bounds.setPosition(pos.x, pos.y);
    }

    public void jump(){
        velocity.y = JUMP_ACCELERATION;
        flapSound.play(1);
    }

    public void draw(SpriteBatch batch){
        // draws at the current key frame
        batch.draw(birdAnimation.getKeyFrame(birdAnimationTimer),
                bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds(){
        return bounds;
    }
}
