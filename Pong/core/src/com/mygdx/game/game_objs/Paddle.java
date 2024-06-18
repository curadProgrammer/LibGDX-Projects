package com.mygdx.game.game_objs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.PongGame;

public class Paddle {
    public static final float CPU_PADDLE_SPEED = 4;
    private static final float PADDLE_WIDTH = 3;
    private static final float PADDLE_HEIGHT = 45;

    // where the paddle will live in
    private World world;

    // box2d body
    private Body b2Body;
    public Body getB2Body(){return b2Body;}

    // bounds
    private Rectangle bounds;
    public Rectangle getBounds(){return bounds;}

    private Vector2 destinedLocation;
    public Vector2 getDestinedLocation(){return destinedLocation;}

    // used to indicate where to put sensors (right - playerOne, left - playerTwo)
    private boolean isPlayer;

    public Paddle(World world, float x, float y, boolean isPlayer){
        this.world = world;
        this.isPlayer = isPlayer;

        if(!isPlayer){
            destinedLocation = new Vector2(x/PongGame.PPM, y/PongGame.PPM);
        }

        bounds = new Rectangle(x / PongGame.PPM , y / PongGame.PPM ,
                PADDLE_WIDTH / PongGame.PPM , PADDLE_HEIGHT / PongGame.PPM ); // N: we don't scale these
        definePaddle();
    }

    public void definePaddle(){
        // body def is used for setting the type of body it will have in the box2d world
        BodyDef bdef = new BodyDef();
        bdef.position.set(bounds.x, bounds.y);
        b2Body = world.createBody(bdef);

        // fixture (used to indicate the shape of the object in the world)
        FixtureDef fdef = new FixtureDef();
        PolygonShape rectangleShape =  new PolygonShape();

        // N: not exactly sure why we need to divide by 2
//        rectangleShape.setAsBox(bounds.getWidth() / PongGame.PPM / 2, bounds.getHeight() / PongGame.PPM / 2);
        rectangleShape.setAsBox(bounds.getWidth()/2, bounds.getHeight()/2);
        fdef.shape = rectangleShape;
//        fdef.filter.categoryBits = PongGame.PADDLE_BIT;
//        fdef.filter.maskBits = PongGame.NOTHING_BIT; // does not collide with anything
        b2Body.createFixture(fdef).setUserData(this);
        rectangleShape.dispose();

        // create sensors on the paddle
//        if(isPlayerOne){
//            // top part of the paddle
//            EdgeShape topPart = new EdgeShape();
//            topPart.set(
//                    new Vector2(2 / PongGame.PPM, bounds.getHeight()/2),
//                    new Vector2(2 / PongGame.PPM, 8/PongGame.PPM)
//            );
//            fdef.shape = topPart;
//            fdef.isSensor = true;
//            fdef.filter.categoryBits = PongGame.PADDLE_TOP_BIT;
//            fdef.filter.maskBits = PongGame.BALL_BIT;
//            b2Body.createFixture(fdef).setUserData("top");
//
//            // middle part
//            EdgeShape midPart = new EdgeShape();
//            midPart.set(
//                    new Vector2(2 / PongGame.PPM, 8/PongGame.PPM),
//                    new Vector2(2 / PongGame.PPM, -8/PongGame.PPM)
//            );
//            fdef.shape = midPart;
//            fdef.isSensor = true;
//            fdef.filter.categoryBits = PongGame.PADDLE_MID_BIT;
//            fdef.filter.maskBits = PongGame.BALL_BIT;
//            b2Body.createFixture(fdef).setUserData("mid");
//
//            // bottom
//            EdgeShape bottomPart = new EdgeShape();
//            bottomPart.set(
//                    new Vector2(2 / PongGame.PPM, -8/PongGame.PPM),
//                    new Vector2(2 / PongGame.PPM, -bounds.getHeight() / 2)
//            );
//            fdef.shape = bottomPart;
//            fdef.isSensor = true;
//            fdef.filter.categoryBits = PongGame.PADDLE_BOTTOM_BIT;
//            fdef.filter.maskBits = PongGame.BALL_BIT;
//            b2Body.createFixture(fdef).setUserData("bottom");
//        }
//        else{
//            // top part of the paddle
//            EdgeShape topPart = new EdgeShape();
//            topPart.set(
//                    new Vector2(-2 / PongGame.PPM, bounds.getHeight()/2),
//                    new Vector2(-2 / PongGame.PPM, 8/PongGame.PPM)
//            );
//            fdef.shape = topPart;
//            fdef.isSensor = true;
//            fdef.filter.categoryBits = PongGame.PADDLE_TOP_BIT;
//            fdef.filter.maskBits = PongGame.BALL_BIT;
//            b2Body.createFixture(fdef).setUserData("top");
//
//            // middle part
//            EdgeShape midPart = new EdgeShape();
//            midPart.set(
//                    new Vector2(-2 / PongGame.PPM, 10/PongGame.PPM),
//                    new Vector2(-2 / PongGame.PPM, -10/PongGame.PPM)
//            );
//            fdef.shape = midPart;
//            fdef.isSensor = true;
//            fdef.filter.categoryBits = PongGame.PADDLE_MID_BIT;
//            fdef.filter.maskBits = PongGame.BALL_BIT;
//            b2Body.createFixture(fdef).setUserData("mid");
//
//            // bottom
//            EdgeShape bottomPart = new EdgeShape();
//            bottomPart.set(
//                    new Vector2(-2 / PongGame.PPM, -10/PongGame.PPM),
//                    new Vector2(-2 / PongGame.PPM, -bounds.getHeight() / 2)
//            );
//            fdef.shape = bottomPart;
//            fdef.isSensor = true;
//            fdef.filter.categoryBits = PongGame.PADDLE_BOTTOM_BIT;
//            fdef.filter.maskBits = PongGame.BALL_BIT;
//            b2Body.createFixture(fdef).setUserData("bottom");
//        }
    }

    // used only for when battling against computer
    public void translate(float yChange){
        b2Body.setTransform(b2Body.getPosition().x, b2Body.getPosition().y + yChange, 0);
    }

    public void setDestinedLocation(Vector2 location){
        destinedLocation = location;
    }

    public void update(){
        // update rectangle at b2body position
        bounds.setPosition(b2Body.getPosition().x, b2Body.getPosition().y);

    }

    public void draw(ShapeRenderer shape){
        // draw rectangle at b2body position
        shape.rect(b2Body.getPosition().x - bounds.getWidth()/2,
                b2Body.getPosition().y - bounds.getHeight()/2, bounds.getWidth(), bounds.getHeight());

    }

}





































