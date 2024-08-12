package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.taptap.breakout.BreakoutGame;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.controller.KeyboardController;
import com.taptap.breakout.ecs.components.*;
import com.taptap.breakout.level.LevelManager;
/*
    System to handle player entity controls
 */
public class PlayerControlSystem extends IteratingSystem {
    private ComponentMapper<PlayerComponent> pc = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<B2BodyComponent> b2dc = ComponentMapper.getFor(B2BodyComponent.class);
    private ComponentMapper<BallComponent> ballComponentComponentMapper = ComponentMapper.getFor(BallComponent.class);
    private ComponentMapper<AttachComponent> attachComponentComponentMapper = ComponentMapper.getFor(AttachComponent.class);

    private KeyboardController keyCon;

    // N: figure out what to do with this
    private LevelManager lvlManager;

    public PlayerControlSystem(KeyboardController keyCon, LevelManager lvlManager){
        // only gets entities that have the player component (in this case the paddle only)
        super(Family.all(PlayerComponent.class).get());
        this.keyCon = keyCon;
        this.lvlManager = lvlManager;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        B2BodyComponent b2body = b2dc.get(entity);
        PlayerComponent player = pc.get(entity);
        AttachComponent attachComponent = attachComponentComponentMapper.get(entity);

        // get current width of paddle (note: we use 0 because there is only one fixture)
        // TODO fix this later
//        float currentWidth = b2body.body.getFixtureList().get(0).getShape().getRadius() ;
        float width = Utilities.convertToPPM(Utilities.PADDLE_WIDTH);

        // can't move to one side if they reached the end of the screen
        if(keyCon.left && b2body.body.getPosition().x - width/2 - Utilities.PADDLE_PADDING > 0){
            if(BreakoutGame.DEBUG_MODE) System.out.println("Going Left");
            b2body.body.setLinearVelocity(
                    MathUtils.lerp(b2body.body.getLinearVelocity().x, -5f, 0.2f),
                    b2body.body.getLinearVelocity().y
            );
        }else if(keyCon.right && b2body.body.getPosition().x + width/2 + Utilities.PADDLE_PADDING < Utilities.getPPMWidth()){
            if(BreakoutGame.DEBUG_MODE) System.out.println("Going Right");
            b2body.body.setLinearVelocity(
                    MathUtils.lerp(b2body.body.getLinearVelocity().x, 5f, 0.2f),
                    b2body.body.getLinearVelocity().y
            );
        }else if(keyCon.space){
            Entity ballEntity = attachComponent.attachedEntity;

            // don't do anything if there is no attached entity
            if(ballEntity == null) return;

            B2BodyComponent ballB2Body = b2dc.get(ballEntity);
            BallComponent ballComponent = ballComponentComponentMapper.get(ballEntity);

            // supply initial velocity
            ballB2Body.body.setLinearVelocity(new Vector2(0f, 1f).nor().scl(ballComponent.speed));

            // remove attached entity
            attachComponent.setAttachedEntity(null);

        }else{
            if(BreakoutGame.DEBUG_MODE) System.out.println("Stop momentum");
            b2body.body.setLinearVelocity(0, 0);
        }
    }
}
