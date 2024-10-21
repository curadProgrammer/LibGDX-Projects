package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;


public class BallComponent implements Pool.Poolable, Component {
    public enum Direction {LEFT, CENTER, RIGHT}

    public float speed = 0;
    public boolean isDead = false;
    public boolean canBounce = false;
    public boolean isAttached = true;
    public Vector2 prevBallLinearVelocity;


    // this bounce method is mainly for the paddle
    public void bouncePaddle(Direction bounceDirection, Body ballB2body){
        // don't perform bounce logic if it can not bounce
        if(!canBounce) return;

        System.out.println("Ball Direction: " + bounceDirection);

        float angle;
        if(bounceDirection == Direction.LEFT){
            angle = MathUtils.random(120, 150);
        }else if(bounceDirection == Direction.CENTER){
            angle = MathUtils.random(60, 120);
        }else{
            angle = MathUtils.random(30, 60);
        }

        // reset the velocity to prevent reaching crazy values
        ballB2body.setLinearVelocity(0, 0);

        // create force to bounce ball in specific direction
        Vector2 velocity = new Vector2(1, 1);
        velocity.setAngleDeg(angle);
        velocity = velocity.nor().scl(speed);

        // apply force
        ballB2body.applyLinearImpulse(velocity, ballB2body.getWorldCenter(), true);
        System.out.println("Bounce: " + ballB2body.getLinearVelocity());
        canBounce = false;
    }

    public void reverseX(Body ballB2body) {
        Vector2 velocity = ballB2body.getLinearVelocity();

        // calculate neccessary impulse needed to reverse its direction considering the mass of the body
        float impulse = -2 * velocity.x * ballB2body.getMass();
        ballB2body.applyLinearImpulse(new Vector2(impulse, 0), ballB2body.getWorldCenter(), true);
        System.out.println("ReverseX: " + ballB2body.getLinearVelocity());
    }

    public void reverseY(Body ballB2body) {
        Vector2 velocity = ballB2body.getLinearVelocity();
        float impulse = -2 * velocity.y * ballB2body.getMass();
        ballB2body.applyLinearImpulse(new Vector2(0, impulse), ballB2body.getWorldCenter(), true);
        System.out.println("ReverseY: " + ballB2body.getLinearVelocity());
    }

    @Override
    public void reset() {
        System.out.println("Ball is reset");
        speed = 0;
        isDead = false;
        canBounce = true;
        isAttached = true;
        prevBallLinearVelocity = null;
    }
}
