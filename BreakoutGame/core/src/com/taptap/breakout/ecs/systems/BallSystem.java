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
        B2BodyComponent b2bodyC = b2bodyMapper.get(entity);

        Vector2 ballPosition = b2bodyC.body.getPosition();
        float ballRadius = b2bodyC.body.getFixtureList().get(0).getShape().getRadius();
        System.out.println(ballRadius);
        b2bodyC.body.setLinearVelocity(ballC.xVel, ballC.yVel);

        // update position based on velocity
        // check top
//        if(ballPosition.y + ballRadius > Utilities.getPPMHeight()){
//            b2bodyC.body.setLinearVelocity(ballC.xVel, -ballC.yVel);
//        }else if(ballPosition.x + ballRadius > Utilities.getPPMWidth() || ballPosition.x - ballRadius < 0){
//            b2bodyC.body.setLinearVelocity(-ballC.xVel, ballC.yVel);
//        }else{
//            b2bodyC.body.setLinearVelocity(ballC.xVel, ballC.yVel);
//        }
    }
}











