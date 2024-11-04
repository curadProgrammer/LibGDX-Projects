package com.taptap.breakout.utils;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.ecs.components.AttachComponent;
import com.taptap.breakout.ecs.components.B2BodyComponent;

public class PaddleAndBall {
    ComponentMapper<B2BodyComponent> b2bodyCM = ComponentMapper.getFor(B2BodyComponent.class);
    ComponentMapper<AttachComponent> attachCM = ComponentMapper.getFor(AttachComponent.class);

    private Entity paddle;
    private Entity ball;

    public PaddleAndBall(Entity paddle, Entity ball){
        this.paddle = paddle;
        this.ball = ball;
    }

    // this method will be in charge of resetting the ball to the paddle
    public void resetBallToPaddle(){
        // reset ball to paddle's position
        B2BodyComponent ballB2Body = b2bodyCM.get(ball);
        B2BodyComponent paddleB2Body = b2bodyCM.get(paddle);

        ballB2Body.body.setLinearVelocity(0, 0);
        ballB2Body.body.setTransform(
                paddleB2Body.body.getPosition().x,
                paddleB2Body.body.getPosition().y + Utilities.convertToPPM(Utilities.PADDLE_HEIGHT + 2),
                0
        );

        // reset attached component
        AttachComponent paddleAttachedComponent = attachCM.get(paddle);
        paddleAttachedComponent.setAttachedEntity(ball);
    }
}





