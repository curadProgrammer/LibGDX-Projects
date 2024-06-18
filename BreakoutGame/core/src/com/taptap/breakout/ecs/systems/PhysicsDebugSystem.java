package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/*
    System to debug the physics system
 */
public class PhysicsDebugSystem extends IteratingSystem {
    private Box2DDebugRenderer debugRenderer;
    private World world;
    private OrthographicCamera cam;

    public PhysicsDebugSystem(World world, OrthographicCamera cam){
        super(Family.all().get());
        this.world = world;
        this.cam = cam;
    }

    @Override
    protected void processEntity(Entity entity, float v) {

    }

}
