package com.mygdx.game.ecs.systems.states;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.ecs.components.B2BodyComponent;
import com.mygdx.game.ecs.components.states.MovementStateComponent;

public class MovementStateSystem extends IteratingSystem {
    private final ComponentMapper<MovementStateComponent> movementStateComponentMapper = ComponentMapper.getFor(MovementStateComponent.class);
    private final ComponentMapper<B2BodyComponent> b2BodyComponentMapper = ComponentMapper.getFor(B2BodyComponent.class);

    public MovementStateSystem(){
        super(Family.all(MovementStateComponent.class, B2BodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        MovementStateComponent movementStateComponent = movementStateComponentMapper.get(entity);
        B2BodyComponent b2BodyComponent = b2BodyComponentMapper.get(entity);

        Body entityB2Body = b2BodyComponent.body;

        if(entityB2Body.getLinearVelocity().x != 0){
            movementStateComponent.currentState = MovementStateComponent.MovementState.WALKING;
        }else{
            movementStateComponent.currentState = MovementStateComponent.MovementState.IDLE;
        }

        if(entityB2Body.getLinearVelocity().y > 0){
            movementStateComponent.currentState = MovementStateComponent.MovementState.JUMPING;
            movementStateComponent.canJump = false;
        }else if(entityB2Body.getLinearVelocity().y < 0) {
            movementStateComponent.currentState = MovementStateComponent.MovementState.FALLING;
            movementStateComponent.canJump = false;
        }else{
            // they are not currently jumping or falling
            movementStateComponent.canJump = true;
        }

        System.out.println(movementStateComponent.currentState);
    }
}
