package com.mygdx.game.ecs.systems.states;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.ecs.components.B2BodyComponent;
import com.mygdx.game.ecs.components.states.MovementStateComponent;
import com.mygdx.game.ecs.systems.ControllerSystem;

public class MovementStateSystem extends IteratingSystem {
    private static final Logger logger = new Logger(MovementStateSystem.class.toString(), Logger.DEBUG);

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

        // update isFacingLeft flag depending on the entities velocity direction
        if (Math.abs(entityB2Body.getLinearVelocity().x) > 0.1f) {
            movementStateComponent.isFacingLeft = entityB2Body.getLinearVelocity().x < 0;
        }

        if (entityB2Body.getLinearVelocity().y > 0) {
            movementStateComponent.currentState = MovementStateComponent.MovementState.JUMPING;
        } else if (entityB2Body.getLinearVelocity().y < 0) {
            movementStateComponent.currentState = MovementStateComponent.MovementState.FALLING;
        } else if (Math.abs(entityB2Body.getLinearVelocity().x) > 0.1f) {
            movementStateComponent.currentState = MovementStateComponent.MovementState.WALKING;
        } else {
            movementStateComponent.currentState = MovementStateComponent.MovementState.IDLE;
        }

//        logger.info("IsGrounded: " + movementStateComponent.isGrounded);
//        logger.info(String.valueOf(movementStateComponent.currentState));
//        logger.info(String.valueOf(movementStateComponent.isFacingLeft));
    }
}
