package com.mygdx.game.listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.ecs.components.states.MovementStateComponent;
import com.mygdx.game.utils.GameUtil;


public class B2dContactListener implements ContactListener {
    private static final Logger logger = new Logger(ContactListener.class.toString(), Logger.DEBUG);

    @Override
    public void beginContact(Contact contact) {
        logger.info("Begin Contact");

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa.getFilterData().categoryBits == GameUtil.PLAYER_BOTTOM && fb.getFilterData().categoryBits == GameUtil.PLATFORM){
            logger.info("Player Bottom -> Platform");
            if(fa.getBody().getUserData() instanceof Entity){
                Entity entity = (Entity) fa.getBody().getUserData();
                MovementStateComponent movementStateComponent = entity.getComponent(MovementStateComponent.class);
                movementStateComponent.isGrounded = true;
            }
        }else if(fb.getFilterData().categoryBits == GameUtil.PLAYER_BOTTOM && fa.getFilterData().categoryBits == GameUtil.PLATFORM){
            logger.info("Platform -> Player Bottom");
            if(fb.getBody().getUserData() instanceof Entity){
                Entity entity = (Entity) fa.getBody().getUserData();
                MovementStateComponent movementStateComponent = entity.getComponent(MovementStateComponent.class);
                movementStateComponent.isGrounded = true;
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        logger.info("End Contact");

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa.getFilterData().categoryBits == GameUtil.PLAYER_BOTTOM && fb.getFilterData().categoryBits == GameUtil.PLATFORM){
            if(fa.getBody().getUserData() instanceof Entity){
                Entity entity = (Entity) fa.getBody().getUserData();
                MovementStateComponent movementStateComponent = entity.getComponent(MovementStateComponent.class);
                movementStateComponent.isGrounded = false;
            }
        }else if(fb.getFilterData().categoryBits == GameUtil.PLAYER_BOTTOM && fa.getFilterData().categoryBits == GameUtil.PLATFORM){
            if(fb.getBody().getUserData() instanceof Entity){
                Entity entity = (Entity) fa.getBody().getUserData();
                MovementStateComponent movementStateComponent = entity.getComponent(MovementStateComponent.class);
                movementStateComponent.isGrounded = false;
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
//        logger.info("PreSolve Contact");

        // todo refactor due to repeating code
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa.getFilterData().categoryBits == GameUtil.PLAYER_BOTTOM && fb.getFilterData().categoryBits == GameUtil.PLATFORM){
            if(fa.getBody().getUserData() instanceof Entity){
                Entity entity = (Entity) fa.getBody().getUserData();
                MovementStateComponent movementStateComponent = entity.getComponent(MovementStateComponent.class);
                movementStateComponent.isGrounded = true;
            }
        }else if(fb.getFilterData().categoryBits == GameUtil.PLAYER_BOTTOM && fa.getFilterData().categoryBits == GameUtil.PLATFORM){
            if(fb.getBody().getUserData() instanceof Entity){
                Entity entity = (Entity) fa.getBody().getUserData();
                MovementStateComponent movementStateComponent = entity.getComponent(MovementStateComponent.class);
                movementStateComponent.isGrounded = true;
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
//        logger.info("PostSolve Contact");

    }
}
