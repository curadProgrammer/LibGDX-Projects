package com.taptap.breakout.level;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.loader.BodyFactory;

import javax.rmi.CORBA.Util;

public class LevelLoader {
    private BodyFactory bodyFactory;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private World world;
    private PooledEngine en;
    private OrthogonalTiledMapRenderer mapRenderer;
    public OrthogonalTiledMapRenderer getMapRenderer(){return mapRenderer;}

    // mapFilePath - pass in the path to the map file to be loaded
    public LevelLoader(World world, PooledEngine en, String mapFilePath){
        this.world = world;
        this.en = en;
        bodyFactory = BodyFactory.getInstance(world);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(mapFilePath);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/ Utilities.PPM);
        loadWorld();
    }

    public void loadWorld(){

        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            // get the rectangle object from the map
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // create body with bodyfactory
            bodyFactory.makeBoxPolyBody(
                    Utilities.convertToPPM(rect.x),
                    Utilities.convertToPPM(rect.y),
                    Utilities.convertToPPM(rect.width),
                    Utilities.convertToPPM(rect.height),
                    BodyFactory.BlockType.ONE_HIT,
                    BodyDef.BodyType.StaticBody,
                    true);
        }
    }
}
