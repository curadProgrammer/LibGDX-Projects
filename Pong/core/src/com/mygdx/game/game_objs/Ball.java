package com.mygdx.game.game_objs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.PongGame;

public class Ball {
    private static final float BALL_RADIUS = 4;
    private static final float BALL_XSPEED = 2.75f;
    private static final float BALL_YSPEED = 1f;

    // where the paddle will live in
    private PongGame game;
    private World world;

    public boolean canBounce;

    // box2d body
    private Body b2Body;
    public Body getB2Body(){return b2Body;}

    // bounds
    private Circle bounds;
    public Circle getBounds(){return bounds;}

    public Ball(Game game, World world, float x, float y){
        this.game = (PongGame) game;
        this.world = world;
        bounds = new Circle(x/PongGame.PPM, y/ PongGame.PPM, BALL_RADIUS/PongGame.PPM);
        canBounce = true;
        defineBall();
    }

    private void defineBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(bounds.x, bounds.y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape cShape = new CircleShape();
        cShape.setRadius(bounds.radius);
        fdef.shape = cShape;
//        fdef.filter.categoryBits = PongGame.BALL_BIT;
//        fdef.filter.maskBits = PongGame.PADDLE_TOP_BIT | PongGame.PADDLE_MID_BIT | PongGame.PADDLE_BOTTOM_BIT;
        b2Body.createFixture(fdef).setUserData(this);
        cShape.dispose();

        // set linear velocity
        b2Body.setLinearVelocity(new Vector2(-BALL_XSPEED, BALL_YSPEED));
    }

    public void bounce(String direction){
        System.out.println("Direction: " + direction);

        // creates a copy of the linear velocity
        Vector2 currentVelocity = b2Body.getLinearVelocity().cpy();

        // needed to make it to 0 deg as well so that i can go straight
        float angle;

        if(direction.equals("top")){
            angle = MathUtils.random(35, 75);
//            angle = 90;
        }else if(direction.equals("mid")){
            angle = MathUtils.random(-30, 30);
        }else{
            // bottom
            angle = MathUtils.random(-75, -35);
        }

        // calculate new velocity
        float newVelocityX = -currentVelocity.x; // reverses direction
        float newVelocityY = MathUtils.sinDeg(angle);

        b2Body.setLinearVelocity(newVelocityX, newVelocityY);

        // play hit sound
        game.getAssetManager().get("hit.wav", Sound.class).play(0.35f);
    }

    public void update(){
        // check collision for top and bottom
        if(b2Body.getPosition().y + bounds.radius/2 >= PongGame.V_HEIGHT/PongGame.PPM ||
                b2Body.getPosition().y - bounds.radius/2 <= 0){
            // reverse the velocity
            b2Body.setLinearVelocity(new Vector2(
                    b2Body.getLinearVelocity().x,
                    b2Body.getLinearVelocity().y * -1
            ));
        }

        // update position
        bounds.setPosition(b2Body.getPosition().x, b2Body.getPosition().y);

    }

    public void recenter(){
        // reset position to the center
        b2Body.setTransform(PongGame.V_WIDTH/2/PongGame.PPM, PongGame.V_HEIGHT/2/PongGame.PPM, 0);

        // choose random angle
        float randomAngle = MathUtils.random(-90, 90);
        System.out.println(MathUtils.sinDeg(randomAngle));

        // change direction
        b2Body.setLinearVelocity(b2Body.getLinearVelocity().x * -1,
                MathUtils.sinDeg(randomAngle));
    }

    public void draw(ShapeRenderer shape){
        // draw circle
        shape.circle(bounds.x, bounds.y, bounds.radius, 25);
    }
}
