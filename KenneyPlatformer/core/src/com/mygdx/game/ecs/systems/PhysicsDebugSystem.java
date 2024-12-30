package com.mygdx.game.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.GameScreen;

/*
    System to debug the physics system
 */
public class PhysicsDebugSystem extends IteratingSystem {
    private final Box2DDebugRenderer debugRenderer;
    private final World world;
    private OrthographicCamera cam;

    public PhysicsDebugSystem(GameScreen gameScreen){
        super(Family.all().get());
        this.world = gameScreen.getWorld();
        this.cam = gameScreen.getCamera();
        debugRenderer = new Box2DDebugRenderer(
                true,true,true,true,true,true);
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