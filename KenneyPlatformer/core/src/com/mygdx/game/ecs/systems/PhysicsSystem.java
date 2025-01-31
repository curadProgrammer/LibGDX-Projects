package com.mygdx.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ecs.components.B2BodyComponent;

public class PhysicsSystem extends IteratingSystem {
    private final ComponentMapper<B2BodyComponent> b2BodyComponentMapper = ComponentMapper.getFor(B2BodyComponent.class);
    private World world;
    private PooledEngine pooledEngine;
    private Array<Body> bodiesToRemove;
    private Array<Entity> entitiesToBeRemoved;

    public PhysicsSystem(World world, PooledEngine pooledEngine){
        super(Family.all(B2BodyComponent.class).get());
        this.world = world;
        this.pooledEngine = pooledEngine;
        bodiesToRemove = new Array<>();
        entitiesToBeRemoved = new Array<>();
    }

    private void removeEntitiesWhenPossible(){
        for(Entity entity : entitiesToBeRemoved){
            pooledEngine.removeEntity(entity);
        }

        entitiesToBeRemoved.clear();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        world.step(1/60f, 5, 8);

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
