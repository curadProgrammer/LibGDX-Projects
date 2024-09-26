package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.ecs.components.B2BodyComponent;
import com.taptap.breakout.ecs.components.TextureComponent;

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
    private ComponentMapper<B2BodyComponent> b2bodyM = ComponentMapper.getFor(B2BodyComponent.class);

    public RenderingSystem(SpriteBatch batch, OrthographicCamera cam){
        super(Family.all(B2BodyComponent.class, TextureComponent.class).get());
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
        TextureComponent texture = textureM.get(entity);
        B2BodyComponent b2body = b2bodyM.get(entity);

        // don't process if the entity is dead
        if(b2body.isDead) return;

        float width = Utilities.convertToPPM(texture.region.getRegionWidth());
        float height = Utilities.convertToPPM(texture.region.getRegionHeight());
        float originX = width * 0.5f;
        float originY = height * 0.5f;

        // draw texture at box2d body
        batch.begin();
        batch.draw(texture.region,
                b2body.body.getPosition().x - originX,
                b2body.body.getPosition().y - originY,
                width, height);
        batch.end();
    }
}
