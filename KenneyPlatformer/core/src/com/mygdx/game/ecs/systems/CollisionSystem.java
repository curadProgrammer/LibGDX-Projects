package com.mygdx.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.ecs.components.B2BodyComponent;
import com.mygdx.game.ecs.components.CollisionComponent;
import com.mygdx.game.ecs.components.states.MovementStateComponent;
import com.mygdx.game.utils.GameUtil;

public class CollisionSystem extends IteratingSystem {
    private static final Logger logger = new Logger(CollisionSystem.class.toString(), Logger.DEBUG);

    private final ComponentMapper<CollisionComponent> collisionComponentMapper = ComponentMapper.getFor(CollisionComponent.class);
    private final ComponentMapper<B2BodyComponent> b2BodyComponentMapper = ComponentMapper.getFor(B2BodyComponent.class);
    private final ComponentMapper<MovementStateComponent> movementMapper = ComponentMapper.getFor(MovementStateComponent.class);

    public CollisionSystem(){
        super(Family.all(CollisionComponent.class, B2BodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        CollisionComponent collisionComponent = collisionComponentMapper.get(entity);
        B2BodyComponent b2BodyComponent = b2BodyComponentMapper.get(entity);

        // check to see if player is colliding with platform
        if(b2BodyComponent.hasCategoryBit(GameUtil.PLAYER_BOTTOM)){
            MovementStateComponent playerMovementStateComponent = movementMapper.get(entity);
            // player bottom edge shape currently colliding with another entity
            if(collisionComponent.collisionEntity != null){
                // check the category bits of the other entity
                B2BodyComponent otherB2BodyComponent = b2BodyComponentMapper.get(collisionComponent.collisionEntity);
                if(otherB2BodyComponent.hasCategoryBit(GameUtil.PLATFORM))
                    playerMovementStateComponent.isGrounded = true;
            }else{
                // player bottom edge shape not colliding with anything so it isn't grounded
                playerMovementStateComponent.isGrounded = false;
            }
        }
    }
}



















