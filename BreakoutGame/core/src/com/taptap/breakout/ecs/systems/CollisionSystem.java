package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.ecs.components.*;

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
        B2BodyComponent ballB2body = b2BodyC.get(entity);

        BallComponent ball = ballC.get(entity);

        Entity otherEntity = collision.collisionEntity;
        if(otherEntity == null) return;

        TypeComponent otherEntityType = otherEntity.getComponent(TypeComponent.class);
        if(otherEntityType.type == TypeComponent.PLAYER){ // ball collides with paddle
            B2BodyComponent paddleB2body = otherEntity.getComponent(B2BodyComponent.class);
            Vector2 ballPosition = ballB2body.body.getPosition();
            Vector2 paddlePosition = paddleB2body.body.getPosition();

            if(ballPosition.x >= paddlePosition.x - Utilities.getPPMWidth()/2 &&
                    ballPosition.x < paddlePosition.x - Utilities.convertToPPM(3)){ // LEFT
                ball.bouncePaddle(BallComponent.Direction.LEFT, ballB2body.body);
            }else if(ballPosition.x <= paddlePosition.x + Utilities.getPPMWidth()/2 &&
                    ballPosition.x > paddlePosition.x + Utilities.convertToPPM(3)){ // RIGHT
                ball.bouncePaddle(BallComponent.Direction.RIGHT, ballB2body.body);
            }else{ // CENTER
                ball.bouncePaddle(BallComponent.Direction.CENTER, ballB2body.body);
            }

        }else if(otherEntityType.type == TypeComponent.BLOCK){ // ball collides with block
            // todo might make this outside because it is similar to lines 38 - 40
            B2BodyComponent blockB2Body = otherEntity.getComponent(B2BodyComponent.class);

            if(blockB2Body.isDead) return;

            CollisionComponent otherCollision = otherEntity.getComponent(CollisionComponent.class);

            // prevents multiple collision detections in an instance
            if(!otherCollision.canCollide) return;

            // used to get the width of the block
            TextureComponent blockTexture = otherEntity.getComponent(TextureComponent.class);

            Vector2 ballPosition = ballB2body.body.getPosition();
            Vector2 blockPosition = blockB2Body.body.getPosition();
            ballB2body.body.setLinearVelocity(0, 0);
            Vector2 force = new Vector2(0, 0);
            float angle = 0;

            if(ballPosition.x <=
                    blockPosition.x - Utilities.convertToPPM((float) blockTexture.region.getRegionWidth() /2)){ // LEFT
                force = new Vector2(-1, 0);
                angle = MathUtils.random(-150, 150);
            }else if(ballPosition.x >=
                    blockPosition.x + Utilities.convertToPPM((float) blockTexture.region.getRegionWidth() /2)){ // RIGHT
                force = new Vector2(1, 0);
                angle = MathUtils.random(-30, 30);
            }

            if(ballPosition.y >=
                    blockPosition.y - Utilities.convertToPPM((float) blockTexture.region.getRegionWidth() /2)){ // TOP
                force = new Vector2(0, 1);
                angle = MathUtils.random(60, 120);
            }else if(ballPosition.y <=
                    blockPosition.y + Utilities.convertToPPM((float) blockTexture.region.getRegionWidth() /2)){ // DOWN
                force = new Vector2(0, -1);
                angle = MathUtils.random(-120, -60);
            }

            force.nor().scl(ball.speed);
            force.setAngleDeg(angle);
            ballB2body.body.applyLinearImpulse(force, ballB2body.body.getWorldCenter(), true);

            // update ball flag to allow it bounce from the paddle again
            ball.canBounce = true;
            otherCollision.canCollide = false;

            // block is destroyed
            System.out.println("Block is destroyed");
            blockB2Body.setToDestroy = true;
        }
    }
}











