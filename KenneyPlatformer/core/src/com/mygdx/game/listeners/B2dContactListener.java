package com.mygdx.game.listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.ecs.components.B2BodyComponent;


public class B2dContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        System.out.println(fa.getBody().getUserData());

        // check for player bottom edge and platform collision
        if(fa.getBody().getUserData() instanceof Entity){
            Entity entity = (Entity) fa.getBody().getUserData();

        }else if(fb.getBody().getUserData() instanceof Entity){
            Entity entity = (Entity) fb.getBody().getUserData();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
