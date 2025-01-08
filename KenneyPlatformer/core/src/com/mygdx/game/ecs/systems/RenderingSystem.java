package com.mygdx.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ecs.components.AnimationComponent;
import com.mygdx.game.ecs.components.B2BodyComponent;
import com.mygdx.game.utils.GameUtil;

public class RenderingSystem extends IteratingSystem {
    private static final Logger logger = new Logger(RenderingSystem.class.toString(), Logger.DEBUG);

    private final ComponentMapper<B2BodyComponent> b2BodyComponentMapper = ComponentMapper.getFor(B2BodyComponent.class);
    private final ComponentMapper<AnimationComponent> animationComponentMapper = ComponentMapper.getFor(AnimationComponent.class);

    private MyGdxGame game;
    public RenderingSystem(MyGdxGame game){
        super(Family.all(B2BodyComponent.class, AnimationComponent.class).get());
        this.game = game;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        B2BodyComponent b2BodyComponent = b2BodyComponentMapper.get(entity);
        AnimationComponent animationComponent = animationComponentMapper.get(entity);

        render(b2BodyComponent, animationComponent, v);
    }

    private void render(B2BodyComponent b2BodyComponent, AnimationComponent animationComponent, float delta){
        // have to use texture region instead of texture (Not fully sure why though)
        // todo might put this in a separate method
        TextureRegion currentFrame = animationComponent.currentFrame;
        float width = GameUtil.convertToPPM(currentFrame.getRegionWidth());
        float height = GameUtil.convertToPPM(currentFrame.getRegionHeight());
        float originX = width * 0.5f;
        float originY = height * 0.5f;

        // update body
        game.spriteBatch.begin();
        game.spriteBatch.draw(
                currentFrame,
                b2BodyComponent.body.getPosition().x - originX,
                b2BodyComponent.body.getPosition().y - originY,
                width,
                height
        );
        game.spriteBatch.end();
    }
}






