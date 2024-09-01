package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.ecs.components.B2BodyComponent;
import com.taptap.breakout.ecs.components.BallComponent;

public class BallSystem extends IteratingSystem {
    private ComponentMapper<BallComponent> ballMapper = ComponentMapper.getFor(BallComponent.class);
    private ComponentMapper<B2BodyComponent> b2bodyMapper = ComponentMapper.getFor(B2BodyComponent.class);

    public BallSystem(){
        super(Family.all(BallComponent.class, B2BodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        BallComponent ballC = ballMapper.get(entity);
        B2BodyComponent ballB2body = b2bodyMapper.get(entity);

        Vector2 ballPosition = ballB2body.body.getPosition();

        // N: this is used when the ball is removed when loading a level
        if(ballB2body.isDead) return;

        float ballRadius = ballB2body.body.getFixtureList().get(0).getShape().getRadius();

        // collision logic for when the ball hits the sides of the screens (top, down, left, right)
        if(ballPosition.y + ballRadius >= Utilities.getPPMHeight()
                || ballPosition.y - ballRadius <= 0){ // reverse y-direction
            ballC.reverseY(ballB2body.body);
            ballC.canBounce = true;
        }

        if(ballPosition.x + ballRadius >= Utilities.getPPMWidth()
                || ballPosition.x - ballRadius <= 0){ // reverse x-direction
            ballC.reverseX(ballB2body.body);
            ballC.canBounce = true;
        }
    }
}



