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
import com.mygdx.game.maps.MapManager;
import com.mygdx.game.config.ControlConfig;
import com.mygdx.game.utils.GameUtil;

/**
 *  This class will be in charge of updating the box2d position for those that have the controller component
 *  and also process input based on the user's input
 */
public class ControllerSystem extends IteratingSystem implements InputProcessor {
    private static final Logger logger = new Logger(ControllerSystem.class.toString(), Logger.DEBUG);

    private ComponentMapper<ControllerComponent> controllerComponentMapper = ComponentMapper.getFor(ControllerComponent.class);
    private ComponentMapper<B2BodyComponent> b2BodyComponentMapper = ComponentMapper.getFor(B2BodyComponent.class);

    // todo will also process the user's statcomponent so that we can control their max speed for now we will use a contant value
    // todo refactor this and put it in a component (StatsComponent)
    private final float xVelocity = 100 / GameUtil.PPM;
    private final float yVelocity = 100 / GameUtil.PPM;

    // todo will also need to get animation component so that we can face the texture the right way
    public ControllerSystem(MapManager mapManager){
        super(Family.all(ControllerComponent.class, B2BodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        ControllerComponent controllerComponent = controllerComponentMapper.get(entity);
        B2BodyComponent b2BodyComponent = b2BodyComponentMapper.get(entity);

        // todo refactor (this is used to prevent sliding)
        b2BodyComponent.body.setLinearDamping(10f);

        // Note: using separate if statements (not else-if) to handle multiple simultaneous inputs
        if(controllerComponent.left){
            if(b2BodyComponent.body.getLinearVelocity().x >= -xVelocity){
                b2BodyComponent.body.applyLinearImpulse(new Vector2(-xVelocity, 0), b2BodyComponent.body.getWorldCenter(), true);
            }
        }

        if(controllerComponent.right){
            if(b2BodyComponent.body.getLinearVelocity().x <= xVelocity){
                b2BodyComponent.body.applyLinearImpulse(new Vector2(xVelocity, 0), b2BodyComponent.body.getWorldCenter(), true);
            }
        }

        if(controllerComponent.up || controllerComponent.space){
            // todo will need to figure out how access the canJump so that the user can't jump all the time
            b2BodyComponent.body.applyLinearImpulse(new Vector2(0
                    , yVelocity), b2BodyComponent.body.getWorldCenter(), true);
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
