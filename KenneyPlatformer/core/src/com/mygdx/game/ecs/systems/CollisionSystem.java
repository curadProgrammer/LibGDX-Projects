package com.mygdx.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.ecs.components.B2BodyComponent;
import com.mygdx.game.ecs.components.CollisionComponent;
import com.mygdx.game.ecs.components.states.MovementStateComponent;
import com.mygdx.game.utils.GameUtil;

import java.util.Arrays;

public class CollisionSystem extends IteratingSystem {
    private static final Logger logger = new Logger(CollisionSystem.class.toString(), Logger.DEBUG);

    private ComponentMapper<CollisionComponent> collisionComponentMapper = ComponentMapper.getFor(CollisionComponent.class);
    private ComponentMapper<B2BodyComponent> b2BodyComponentMapper = ComponentMapper.getFor(B2BodyComponent.class);

    public CollisionSystem(){
        super(Family.all(CollisionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        CollisionComponent collisionComponent = collisionComponentMapper.get(entity);

        // only process collisions that have a collision entity
        // N: don't want this as we also want to process when the collision has ended or when it becomes null
//        if(collisionComponent.collisionEntity == null) return;

        // check to see if player is colliding with platform
        B2BodyComponent b2BodyComponent = b2BodyComponentMapper.get(entity);

        for(int i = 0; i < b2BodyComponent.body.getFixtureList().size; i++){
            short entityBit = b2BodyComponent.body.getFixtureList().get(i).getFilterData().categoryBits;

            if(entityBit == GameUtil.PLAYER_BOTTOM){
                MovementStateComponent playerMovementStateComponent = entity.getComponent(MovementStateComponent.class);

                if(collisionComponent.collisionEntity != null){
                    // time to check fo the category bits of the other entity
                    B2BodyComponent otherB2BodyComponent = b2BodyComponentMapper.get(collisionComponent.collisionEntity);

                    for(int j = 0; j < otherB2BodyComponent.body.getFixtureList().size; j++){
                        short otherEntityBit = otherB2BodyComponent.body.getFixtureList().get(j).getFilterData().categoryBits;
                        if(otherEntityBit == GameUtil.PLATFORM){
                            playerMovementStateComponent.isGrounded = true;
                        }
                    }
                }else{
                    // N: if collision entity is null that means the player is not colliding
                    // with anything so it should not be grounded
                    playerMovementStateComponent.isGrounded = false;
                }
            }

//            if(playerBit == GameUtil.PLAYER_BOTTOM){
//                // time to check fo the category bits of the other entity
//                B2BodyComponent otherB2BodyComponent = b2BodyComponentMapper.get(collisionComponent.collisionEntity);
//
//                // check for platform
//                for(int j = 0; j < otherB2BodyComponent.body.getFixtureList().size; j++){
//                    short platformBit =  otherB2BodyComponent.body.getFixtureList().get(j).getFilterData().categoryBits;
//                    MovementStateComponent playerMovementStateComponent = entity.getComponent(MovementStateComponent.class);
//                    if(platformBit == GameUtil.PLATFORM){
//                        playerMovementStateComponent.isGrounded = true;
//                    }else{
//                        playerMovementStateComponent.isGrounded = false;
//                    }
//                }
//            }
        }
    }
}



















