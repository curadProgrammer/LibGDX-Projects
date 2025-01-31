package com.mygdx.game.maps;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.assets.B2DAssetmanager;
import com.mygdx.game.ecs.components.AnimationComponent;
import com.mygdx.game.ecs.components.B2BodyComponent;
import com.mygdx.game.ecs.components.ControllerComponent;
import com.mygdx.game.ecs.components.StatsComponent;
import com.mygdx.game.ecs.components.states.MovementStateComponent;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.GameUtil;

import java.util.HashMap;

/**
 * This class will be in charge of rendering a single map file
 */
public class MapRenderer {
    private static final Logger logger = new Logger(MapRenderer.class.toString(), Logger.DEBUG);

    private MyGdxGame game;
    private final GameScreen gameScreen;
    private TiledMap map;
    private final TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer mapRenderer;

    private World world;
    private PooledEngine engine;
    private BodyFactory bodyFactory;

    public MapRenderer(MyGdxGame game, GameScreen gameScreen){
        this.game = game;
        this.gameScreen = gameScreen;
        world = gameScreen.getWorld();
        engine = gameScreen.getEngine();
        bodyFactory = BodyFactory.getInstance(world);
        mapLoader = new TmxMapLoader();
    }

    public void setCurrentMap(MapData currentMap) {
        logger.info("Setting Map Data");

        // Load map once when setting new map data
        map = mapLoader.load(currentMap.getMapPath());
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/GameUtil.PPM);

        // only when the map has been loaded should we render the platforms and other objs
        renderPlatforms();
        renderEntities();
    }

    public void render(){
        if(map == null)
            throw new RuntimeException("No Map is Loaded!");

        mapRenderer.setView(gameScreen.getCamera());
        mapRenderer.render();
    }

    private void renderPlatforms(){
        logger.info("Rendering Platform Objects");

        // todo might place the names as config strings
        MapLayer platformLayer = map.getLayers().get("Platforms");

        // don't render platform if the map file doesn't have a platform layer
        if(platformLayer == null) return;


        for(RectangleMapObject object : platformLayer.getObjects().getByType(RectangleMapObject.class)) {
            Entity platformBlockEntity = engine.createEntity();
            B2BodyComponent b2BodyComponent = engine.createComponent(B2BodyComponent.class);

            Rectangle rect = object.getRectangle();

            // create body with bodyfactory
            b2BodyComponent.body = bodyFactory.makeBoxPolyBody(
                    GameUtil.convertToPPM(rect.x),
                    GameUtil.convertToPPM(rect.y),
                    GameUtil.convertToPPM(rect.width),
                    GameUtil.convertToPPM(rect.height),
                    BodyDef.BodyType.StaticBody,
                    true,
                    false,
                    0
            );

            platformBlockEntity.add(b2BodyComponent);
            engine.addEntity(platformBlockEntity);
        }
    }

    private void renderEntities(){
        logger.info("Rendering Entities");

        // todo might place the names as config strings
        MapLayer entitiesLayer = map.getLayers().get("Entities");

        // don't render platform if the map file doesn't have a platform layer
        if(entitiesLayer == null) return;

        for(EllipseMapObject object : entitiesLayer.getObjects().getByType(EllipseMapObject.class)) {
            MapProperties properties = object.getProperties();
            Entity entity = engine.createEntity();
            Ellipse ellipse = object.getEllipse();

            // check to see if the entity is the player entity
            String isPlayerProperty = (String) properties.get("isPlayer");
            if(isPlayerProperty != null){
                renderPlayer(entity, ellipse);
            }else{
                // render others
                renderOthers(entity);
            }
        }

    }

    private void renderPlayer(Entity playerEntity, Ellipse mapObject){
        B2BodyComponent b2BodyComponent = engine.createComponent(B2BodyComponent.class);
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        ControllerComponent controllerComponent = engine.createComponent(ControllerComponent.class);
        StatsComponent statsComponent = engine.createComponent(StatsComponent.class);

        // states
        MovementStateComponent movementStateComponent = new MovementStateComponent();

        // add animations to player
        Array<TextureRegion> frames = new Array<>();
        animationComponent.animationMap = new HashMap<>();

        // standing animation
        frames.add(
                new TextureRegion(
                        ((TextureAtlas) B2DAssetmanager.getInstance()
                                .assetManager.get(B2DAssetmanager.getInstance().charactersAtlasPath))
                                .findRegion("tile", 1)
                )
        );

        Animation<TextureRegion> standingAnimation = new Animation<>(0, frames);
        standingAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        frames.clear();

        // starting texture is standing for the player
        animationComponent.currentFrame = standingAnimation.getKeyFrame(0);
        animationComponent.animationMap.put("STAND", standingAnimation);

        // create body with bodyfactory
        b2BodyComponent.body = bodyFactory.makeBoxPolyBody(
                GameUtil.convertToPPM(mapObject.x),
                GameUtil.convertToPPM(mapObject.y),
                GameUtil.convertToPPM(animationComponent.currentFrame.getRegionWidth()/1.5f),
                GameUtil.convertToPPM(animationComponent.currentFrame.getRegionHeight()/1.5f),
                BodyDef.BodyType.DynamicBody,
                true,
                false,
                0
        );


        // add top edge (play with values)
        bodyFactory.addEdgeShape(
                b2BodyComponent.body,
                new Vector2(-8/GameUtil.PPM, 12/GameUtil.PPM),
                new Vector2(8/GameUtil.PPM, 12/GameUtil.PPM),
                GameUtil.PLAYER_TOP
        );

        // add bottom edge
        bodyFactory.addEdgeShape(
                b2BodyComponent.body,
                new Vector2(-8/GameUtil.PPM, -12/GameUtil.PPM),
                new Vector2(8/GameUtil.PPM, -12/GameUtil.PPM),
                GameUtil.PLAYER_BOTTOM
        );

        // states
        playerEntity.add(movementStateComponent);

        playerEntity.add(statsComponent);
        playerEntity.add(controllerComponent);
//        playerEntity.add(animationComponent);
        playerEntity.add(b2BodyComponent);
        engine.addEntity(playerEntity);
    }

    private void renderOthers(Entity otherEntities){

    }

    public TiledMap getMap() {
        return map;
    }

    public TmxMapLoader getMapLoader() {
        return mapLoader;
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }
}
