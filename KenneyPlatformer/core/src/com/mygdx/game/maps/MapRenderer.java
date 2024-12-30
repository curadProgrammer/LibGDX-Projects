package com.mygdx.game.maps;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ecs.components.B2BodyComponent;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.GameUtil;

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
    }

    public void render(){
        if(map == null)
            throw new RuntimeException("No Map is Loaded!");

        mapRenderer.setView(gameScreen.getCamera());
        mapRenderer.render();
    }

    public void renderPlatforms(){
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
                    false);

            platformBlockEntity.add(b2BodyComponent);
            engine.addEntity(platformBlockEntity);
        }
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
