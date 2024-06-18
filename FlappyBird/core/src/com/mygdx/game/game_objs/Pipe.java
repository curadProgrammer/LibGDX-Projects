package com.mygdx.game.game_objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.FlappyBird;

public class Pipe {
    public static final int SPACING = (int) (FlappyBird.WORLD_WIDTH / 1.5f); // spacing between the pairs of pipes (x-axis)
    public static final int PIPE_WIDTH = (int) (FlappyBird.WORLD_WIDTH/5);
    public static final int FLUCTUATION = (int) (FlappyBird.WORLD_HEIGHT/5); // height of pipes varied
    public static final int PIPE_GAP = 30; // gap between the pipes (y-axis) (will need to mess with this when I start adding in the bird)

    private final TextureRegion topPipe, bottomPipe;
    public final Vector2 posTopPipe, posBottomPipe;
    public final Rectangle boundsTopPipe, boundsBottomPipe;

    private float movementSpeed;

    public Pipe(float xPos, TextureRegion topPipe, TextureRegion bottomPipe){
        // init texture pipes
        this.topPipe = topPipe;
        this.bottomPipe = bottomPipe;

        // init positions
        posTopPipe = new Vector2(xPos, FlappyBird.random.nextInt(FLUCTUATION) + PIPE_GAP + FlappyBird.random.nextInt(20));
        posBottomPipe = new Vector2(xPos, posTopPipe.y - PIPE_GAP - bottomPipe.getRegionHeight());

        // init bounds
        boundsTopPipe = new Rectangle(posTopPipe.x, posTopPipe.y, PIPE_WIDTH, topPipe.getRegionHeight());
        boundsBottomPipe = new Rectangle(posBottomPipe.x, posBottomPipe.y, PIPE_WIDTH, bottomPipe.getRegionHeight());

        // movement
        movementSpeed = 15;
    }

    public void draw(SpriteBatch batch){
        batch.draw(topPipe, posTopPipe.x, posTopPipe.y, boundsTopPipe.width, boundsTopPipe.height);
        batch.draw(bottomPipe, posBottomPipe.x, posBottomPipe.y, boundsBottomPipe.width, boundsBottomPipe.height);
    }

    // updates the position and size of the pipes
    public void updatePipes(float deltaTime){
        // make movement speed relative to time
        float xChange = movementSpeed * deltaTime;

        // update pos
        posTopPipe.x -= xChange;
        posBottomPipe.x -= xChange;

        // update bounds
        boundsTopPipe.setPosition(posTopPipe.x, posTopPipe.y);
        boundsBottomPipe.setPosition(posBottomPipe.x, posBottomPipe.y);
    }

    public void repositionPipe(float x){
        // change the height and pos
        posTopPipe.set(x, FlappyBird.random.nextInt(FLUCTUATION) + PIPE_GAP + FlappyBird.random.nextInt(20));
        posBottomPipe.set(x, posTopPipe.y - PIPE_GAP - bottomPipe.getRegionHeight());

        // update bounds
        boundsTopPipe.setPosition(posTopPipe.x, posTopPipe.y);
        boundsBottomPipe.setPosition(posBottomPipe.x, posBottomPipe.y);
    }

    public boolean checkCollision(Bird bird){
        // checks the collision of the top and bottom pipe if it makes contact with the bird
        return boundsTopPipe.overlaps(bird.bounds) || boundsBottomPipe.overlaps(bird.bounds);
    }
}
