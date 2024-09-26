package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.taptap.breakout.AppPreferences;
import com.taptap.breakout.BreakoutGame;
import com.taptap.breakout.ecs.components.CollisionComponent;
import com.taptap.breakout.ecs.components.SoundComponent;
import com.taptap.breakout.ecs.components.TypeComponent;

public class SoundSystem extends IteratingSystem{
    private AppPreferences appPreferences;

    public SoundSystem(AppPreferences appPreferences){
        super(Family.all(SoundComponent.class, CollisionComponent.class, TypeComponent.class).get());
        this.appPreferences = appPreferences;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        // don't play sound if the sound is disabled in settings
        if(!appPreferences.isSoundEnabled()) return;

        SoundComponent soundComponent = entity.getComponent(SoundComponent.class);
        CollisionComponent collisionComponent = entity.getComponent(CollisionComponent.class);
        TypeComponent typeComponent = entity.getComponent(TypeComponent.class);

        // don't do anything if there is no collision
        if(collisionComponent.collisionEntity == null) return;

        if(typeComponent.type == TypeComponent.BALL){
            Entity otherEntity = collisionComponent.collisionEntity;
            TypeComponent otherEntityType = otherEntity.getComponent(TypeComponent.class);
            if(otherEntityType.type == TypeComponent.BLOCK){
                soundComponent.soundEffects.get("ding1").play();
            }else if(otherEntityType.type == TypeComponent.PLAYER){
                soundComponent.soundEffects.get("ding2").play();
            }
        }

    }
}

















