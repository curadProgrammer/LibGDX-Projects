package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.ecs.components.B2BodyComponent;
import com.taptap.breakout.ecs.components.BallComponent;
import com.taptap.breakout.ecs.components.CollisionComponent;
import com.taptap.breakout.ecs.components.TypeComponent;

/*
    System to handle the ball's collisions
 */
public class CollisionSystem extends IteratingSystem {
    private ComponentMapper<CollisionComponent> collisionC = ComponentMapper.getFor(CollisionComponent.class);
    private ComponentMapper<B2BodyComponent> b2BodyC = ComponentMapper.getFor(B2BodyComponent.class);
    private ComponentMapper<BallComponent> ballC = ComponentMapper.getFor(BallComponent.class);

    public CollisionSystem(){
       super(Family.all(CollisionComponent.class, B2BodyComponent.class, BallComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {

        CollisionComponent collision = collisionC.get(entity);
        B2BodyComponent b2body = b2BodyC.get(entity);
        BallComponent ball = ballC.get(entity);

        Entity otherEntity = collision.collisionEntity;
        if(otherEntity == null) return;

        TypeComponent otherEntityType = otherEntity.getComponent(TypeComponent.class);

        if(otherEntityType.type == TypeComponent.PLAYER){
            B2BodyComponent paddleB2body = otherEntity.getComponent(B2BodyComponent.class);
            Vector2 ballPosition = b2body.body.getPosition();
            Vector2 paddlePosition = paddleB2body.body.getPosition();

//            System.out.println("Ball Position(x) : " + ballPosition.x);

            if(ballPosition.x >= paddlePosition.x - Utilities.getPPMWidth()/2 &&
                    ballPosition.x < paddlePosition.x - Utilities.convertToPPM(3)){ // LEFT
                ball.bounce(BallComponent.Direction.LEFT, b2body.body);
            }else if(ballPosition.x <= paddlePosition.x + Utilities.getPPMWidth()/2 &&
                    ballPosition.x > paddlePosition.x + Utilities.convertToPPM(3)){ // RIGHT
                ball.bounce(BallComponent.Direction.RIGHT, b2body.body);
            }else{ // CENTER
                ball.bounce(BallComponent.Direction.CENTER, b2body.body);
            }

        }else if(otherEntityType.type == TypeComponent.BLOCK){
            // todo choose a random angle for the ball to bounce from the block

            // todo update canBounce flag when it collides with the block too
        }
    }
}











