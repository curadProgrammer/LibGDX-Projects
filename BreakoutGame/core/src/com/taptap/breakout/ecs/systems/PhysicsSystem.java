package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.taptap.breakout.ecs.components.B2BodyComponent;

/*
    Handles the physics of the world (i.e. calculates anything physics related such as gravity)
 */
public class PhysicsSystem extends IteratingSystem {
    private World world;
    private PooledEngine engine;
    private Array<Body> bodiesToRemove;
    private Array<Entity> entitiesToBeRemoved;

    // components that we will be accessing and updating
    private ComponentMapper<B2BodyComponent> bm = ComponentMapper.getFor(B2BodyComponent.class);

    public PhysicsSystem(World world, PooledEngine engine){
        super(Family.all(B2BodyComponent.class).get());
        this.world = world;
        this.engine = engine;
        bodiesToRemove = new Array<>();
        entitiesToBeRemoved = new Array<>();
    }

    private void removeEntitiesWhenPossible(){
        for(Entity entity : entitiesToBeRemoved){
            engine.removeEntity(entity);
        }

        entitiesToBeRemoved.clear();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        world.step(1/60f, 6, 2);

        // delete after calculating physics step
        for(Body body: bodiesToRemove){
            world.destroyBody(body);
        }

        // clear bodiesToRemove to prevent deleting non-existing bodies
        bodiesToRemove.clear();

        removeEntitiesWhenPossible();
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        B2BodyComponent b2BodyComponent = entity.getComponent(B2BodyComponent.class);

        // add to bodies to remove
        if(b2BodyComponent.setToDestroy && !b2BodyComponent.isDead){
            bodiesToRemove.add(b2BodyComponent.body);
            b2BodyComponent.isDead = true;

            entitiesToBeRemoved.add(entity);
        }
    }
}
