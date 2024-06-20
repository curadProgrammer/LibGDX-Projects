package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.taptap.breakout.ecs.components.TextureComponent;
import com.taptap.breakout.ecs.components.TransformComponent;

import java.util.Comparator;

/*
    System that is in charge of rendering the texture
 */
public class RenderingSystem extends IteratingSystem {
    private OrthographicCamera cam;
    private SpriteBatch batch;

    // an array used to allow sorting of images allowing us to draw images on top of each other
    private Array<Entity> renderQueue;

    // a comparator to sort images based on the z position of the transfromComponent
    private Comparator<Entity> comparator;

    // component mappers to get components from entities
    private ComponentMapper<TextureComponent> textureM = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);

    public RenderingSystem(SpriteBatch batch, OrthographicCamera cam){
        super(Family.all(TransformComponent.class, TextureComponent.class).get());
        this.batch = batch;
        this.cam = cam;
        renderQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float v) {

    }
}
