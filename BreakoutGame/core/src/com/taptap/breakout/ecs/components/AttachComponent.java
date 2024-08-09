package com.taptap.breakout.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

// this component will be used to allow an entity to attach another entity
public class AttachComponent implements Component {
    public Entity attachedEntity;
    public void setAttachedEntity(Entity entity){
        System.out.println(entity);
        this.attachedEntity = entity;
    }
}
