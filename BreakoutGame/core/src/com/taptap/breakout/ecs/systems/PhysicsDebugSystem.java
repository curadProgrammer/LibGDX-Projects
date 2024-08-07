package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.taptap.breakout.ecs.components.B2BodyComponent;

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
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        debugRenderer.render(world, cam.combined);
    }

    @Override
    protected void processEntity(Entity entity, float v) {
    }

}
