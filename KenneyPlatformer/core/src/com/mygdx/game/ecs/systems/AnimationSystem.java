package com.mygdx.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.ecs.components.AnimationComponent;

/**
 *  This system will be managing updating the frames of the corresponding animation
 */
public class AnimationSystem extends IteratingSystem {
    private ComponentMapper<AnimationComponent> animationComponentMapper = ComponentMapper.getFor(AnimationComponent.class);


    public AnimationSystem(){
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        AnimationComponent animationComponent = animationComponentMapper.get(entity);
    }
}
