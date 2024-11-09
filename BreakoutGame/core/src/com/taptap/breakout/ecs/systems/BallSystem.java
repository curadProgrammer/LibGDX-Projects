package com.taptap.breakout.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.taptap.breakout.Hud;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.ecs.components.B2BodyComponent;
import com.taptap.breakout.ecs.components.BallComponent;
import com.taptap.breakout.level.LevelManager;
import com.taptap.breakout.utils.SoundUtil;

import java.util.logging.Logger;

public class BallSystem extends IteratingSystem {
    private static final Logger logger = Logger.getLogger(BallSystem.class.getName());
    private ComponentMapper<BallComponent> ballMapper = ComponentMapper.getFor(BallComponent.class);
    private ComponentMapper<B2BodyComponent> b2bodyMapper = ComponentMapper.getFor(B2BodyComponent.class);

    private Hud hud;
    private LevelManager levelManager;
    public BallSystem(Hud hud, LevelManager levelManager){
        super(Family.all(BallComponent.class, B2BodyComponent.class).get());
        this.hud = hud;
        this.levelManager = levelManager;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        BallComponent ballC = ballMapper.get(entity);
        B2BodyComponent ballB2body = b2bodyMapper.get(entity);

        Vector2 ballPosition = ballB2body.body.getPosition();

        // N: this is used when the ball is removed when loading a level
        if(ballB2body.isDead) return;

        float ballRadius = ballB2body.body.getFixtureList().get(0).getShape().getRadius();

        // collision logic for when the ball hits the sides of the screens (top, down, left, right)
        if(ballPosition.y + ballRadius >= Utilities.getPPMHeight()){ // reverse y-direction
            // || ballPosition.y - ballRadius <= 0 conditional logic to bounce ball when it hits the bottom side
            ballC.reverseY(ballB2body.body);
            ballC.canBounce = true;
        }else if(ballPosition.y - ballRadius <= 0 && !ballC.isDead){ // ball fell through
            SoundUtil.getInstance().playExplosion();

            // decrease the lives count
            hud.setLives(hud.getLives() - 1);
            hud.updateLives();
            ballC.isDead = true;

            // reset ball position
            levelManager.currentLevel.paddleAndBall.resetBallToPaddle();

            // reset ball properties
            ballC.reset();
            ballC.speed = 5f;
        }

        if(ballPosition.x + ballRadius >= Utilities.getPPMWidth()
                || ballPosition.x - ballRadius <= 0){ // reverse x-direction
            ballC.reverseX(ballB2body.body);
            ballC.canBounce = true;
        }
    }
}



