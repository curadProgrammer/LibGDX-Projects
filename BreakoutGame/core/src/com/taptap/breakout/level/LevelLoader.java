package com.taptap.breakout.level;

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

public class LevelLoader {
    private BodyFactory bodyFactory;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private World world;
    private OrthogonalTiledMapRenderer mapRenderer;
    public OrthogonalTiledMapRenderer getMapRenderer(){return mapRenderer;}

    // mapFilePath - pass in the path to the map file to be loaded
    public LevelLoader(World world, String mapFilePath){
        this.world = world;
        bodyFactory = BodyFactory.getInstance(world);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(mapFilePath);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/ Utilities.PPM);
        loadWorld();
    }

    public void loadWorld(){
        // why is this returning 0?
        System.out.println(map.getLayers().get(1).getObjects().getCount());
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            System.out.println("Hi");
            // get the rectangle object from the map
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // create body with bodyfactory
            bodyFactory.makeBoxPolyBody(rect.x, rect.y, rect.width, rect.height, BodyFactory.BlockType.ONE_HIT,
                    BodyDef.BodyType.StaticBody, true);
        }
    }
}
