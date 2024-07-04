package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.taptap.breakout.ecs.components.B2BodyComponent;
import com.taptap.breakout.ecs.components.TransformComponent;

/*
    Handles the physics of the world (i.e. calculates anything physics related such as gravity)
 */
public class PhysicsSystem extends IteratingSystem {
    private World world;
    private PooledEngine engine;
    private Array<Entity> bodiesQueue;

    // components that we will be accessing and updating
    private ComponentMapper<B2BodyComponent> bm = ComponentMapper.getFor(B2BodyComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);

    public PhysicsSystem(World world, PooledEngine engine){
        super(Family.all(B2BodyComponent.class, TransformComponent.class).get());
        this.world = world;
        this.engine = engine;
        bodiesQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        world.step(1/60f, 6, 2);
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        B2BodyComponent b2body = bm.get(entity);
        TransformComponent transform = tm.get(entity);

        // update transform with body
        transform.position.set(b2body.body.getPosition().x, b2body.body.getPosition().y, 0);
    }
}
