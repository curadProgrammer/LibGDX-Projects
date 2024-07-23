package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;


public class BallComponent implements Pool.Poolable, Component {
    public enum Direction {LEFT, CENTER, RIGHT}

    public float xVel = 0;
    public float yVel = 0;
    public float speed = 0;
    public boolean isDead = false;
    public boolean canBounce = true;

    public void bounce(Direction bounceDirection, Body ballB2body){
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

        Vector2 velocity = ballB2body.getLinearVelocity();
        velocity = velocity.nor().scl(speed);
        velocity.setAngleDeg(angle);

        xVel = velocity.x;
        yVel = velocity.y;

        // apply force
        ballB2body.applyLinearImpulse(velocity, ballB2body.getWorldCenter(), true);
        System.out.println("Bounce: " + xVel + "," + yVel);
        canBounce = false;
    }

    @Override
    public void reset() {
        System.out.println("Ball is reset");
        xVel = 0;
        yVel = 0;
        speed = 0;
        isDead = false;
        canBounce = true;
    }
}
