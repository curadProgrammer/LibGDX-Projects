package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.taptap.breakout.ecs.components.AttachComponent;
import com.taptap.breakout.ecs.components.B2BodyComponent;

public class AttachSystem extends IteratingSystem {
    private ComponentMapper<AttachComponent> attachComponentComponentMapper = ComponentMapper.getFor(AttachComponent.class);
    private ComponentMapper<B2BodyComponent> b2BodyComponentComponentMapper = ComponentMapper.getFor(B2BodyComponent.class);

    public AttachSystem(){
        super(Family.all(AttachComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        AttachComponent attachComponent = attachComponentComponentMapper.get(entity);
        if(attachComponent.attachedEntity == null) return;

        // update the attached entity to match that of the entity
        B2BodyComponent entityB2Body = b2BodyComponentComponentMapper.get(entity);
        B2BodyComponent attachedB2Body = b2BodyComponentComponentMapper.get(attachComponent.attachedEntity);
        attachedB2Body.body.setTransform(entityB2Body.body.getPosition().x,
                attachedB2Body.body.getPosition().y, attachedB2Body.body.getAngle());
    }
}










