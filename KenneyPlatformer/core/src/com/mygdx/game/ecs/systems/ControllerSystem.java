package com.mygdx.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.ecs.components.B2BodyComponent;
import com.mygdx.game.ecs.components.ControllerComponent;
import com.mygdx.game.ecs.components.StatsComponent;
import com.mygdx.game.ecs.components.states.MovementStateComponent;
import com.mygdx.game.maps.MapManager;
import com.mygdx.game.config.ControlConfig;

/**
 *  This class will be in charge of updating the box2d position for those that have the controller component
 *  and also process input based on the user's input
 */
public class ControllerSystem extends IteratingSystem implements InputProcessor {
    private static final Logger logger = new Logger(ControllerSystem.class.toString(), Logger.DEBUG);

    private ComponentMapper<ControllerComponent> controllerComponentMapper = ComponentMapper.getFor(ControllerComponent.class);
    private ComponentMapper<B2BodyComponent> b2BodyComponentMapper = ComponentMapper.getFor(B2BodyComponent.class);
    private ComponentMapper<StatsComponent> statsComponentMapper = ComponentMapper.getFor(StatsComponent.class);
    private ComponentMapper<MovementStateComponent> movementStateComponentMapper = ComponentMapper.getFor(MovementStateComponent.class);

    // todo will also need to get animation component so that we can face the texture the right way
    public ControllerSystem(MapManager mapManager){
        super(Family.all(ControllerComponent.class, B2BodyComponent.class, StatsComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        ControllerComponent controllerComponent = controllerComponentMapper.get(entity);
        B2BodyComponent b2BodyComponent = b2BodyComponentMapper.get(entity);
        StatsComponent statsComponent = statsComponentMapper.get(entity);
        MovementStateComponent movementStateComponent = movementStateComponentMapper.get(entity);

        // todo refactor (this is used to prevent sliding)
        b2BodyComponent.body.setLinearDamping(10f);

        // Note: using separate if statements (not else-if) to handle multiple simultaneous inputs
        if(controllerComponent.left){
            if(b2BodyComponent.body.getLinearVelocity().x >= -statsComponent.xSpeed){
                b2BodyComponent.body.applyLinearImpulse(new Vector2(-statsComponent.xSpeed, 0), b2BodyComponent.body.getWorldCenter(), true);
            }
        }

        if(controllerComponent.right){
            if(b2BodyComponent.body.getLinearVelocity().x <= statsComponent.xSpeed){
                b2BodyComponent.body.applyLinearImpulse(new Vector2(statsComponent.xSpeed, 0), b2BodyComponent.body.getWorldCenter(), true);
            }
        }

        if(controllerComponent.up || controllerComponent.space){
            if(!movementStateComponent.canJump) return;

            b2BodyComponent.body.applyLinearImpulse(new Vector2(0
                    , statsComponent.ySpeed), b2BodyComponent.body.getWorldCenter(), true);
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        ImmutableArray<Entity> entities = getEntities();

        for (Entity entity : entities) {
            ControllerComponent controller = controllerComponentMapper.get(entity);
            if(keycode == ControlConfig.getInstance().getPrefLeftKey()){ // pressed left
                logger.info("Pressed Left");
                controller.left = true;
            }else if(keycode == ControlConfig.getInstance().getPrefRightKey()){
                logger.info("Pressed Right");
                controller.right = true;
            }else if(keycode == ControlConfig.getInstance().getPrefUpKey()){
                logger.info("Pressed Up");
                controller.up = true;
            }else if(keycode == ControlConfig.getInstance().getPrefJumpKey()){
                logger.info("Pressed Jump");
                controller.space = true;
            }
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        ImmutableArray<Entity> entities = getEntities();

        for (Entity entity : entities) {
            ControllerComponent controller = controllerComponentMapper.get(entity);
            if(keycode == ControlConfig.getInstance().getPrefLeftKey()){ // pressed left
                logger.info("Released Left");
                controller.left = false;
            }else if(keycode == ControlConfig.getInstance().getPrefRightKey()){
                logger.info("Released Right");
                controller.right = false;
            }else if(keycode == ControlConfig.getInstance().getPrefUpKey()){
                logger.info("Released Up");
                controller.up = false;
            }else if(keycode == ControlConfig.getInstance().getPrefJumpKey()){
                logger.info("Released Jump");
                controller.space = false;
            }
        }

        return true;    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
