package com.taptap.breakout.level;

import com.badlogic.ashley.core.Entity;
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
import com.taptap.breakout.ecs.components.*;
import com.taptap.breakout.loader.BodyFactory;

import javax.rmi.CORBA.Util;
import java.lang.reflect.Type;

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
        renderPlayer();
        renderBall();
        renderBlocks();
    }

    private void renderPlayer(){
        Entity playerEntity = en.createEntity();
        PlayerComponent pc = en.createComponent(PlayerComponent.class);
        TextureComponent tc = en.createComponent(TextureComponent.class);
        TransformComponent tranC = en.createComponent(TransformComponent.class);
        CollisionComponent cc = en.createComponent(CollisionComponent.class);
        TypeComponent typeC = en.createComponent(TypeComponent.class);
        B2BodyComponent b2bodyC = en.createComponent(B2BodyComponent.class);

        // create box2d body
        b2bodyC.body = bodyFactory.makeBoxPolyBody(
                Utilities.getPPMWidth() / 2,
                Utilities.getPPMHeight(),
                Utilities.convertToPPM(PADDLE_WIDTH),
                Utilities.convertToPPM(PADDLE_HEIGHT),
                BodyDef.BodyType.StaticBody,
                true
        );

        playerEntity.add(pc);
        playerEntity.add(tc);
        playerEntity.add(tranC);
        playerEntity.add(cc);
        playerEntity.add(typeC);
        playerEntity.add(b2bodyC);
    }

    private void renderBall(){

    }

    private void renderBlocks(){
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Entity blockEntity = en.createEntity();
            B2BodyComponent b2Body = en.createComponent(B2BodyComponent.class);
            TextureComponent texture = en.createComponent(TextureComponent.class);
            CollisionComponent collision = en.createComponent(CollisionComponent.class);
            TypeComponent type = en.createComponent(TypeComponent.class);

            // get the rectangle object from the map
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // create body with bodyfactory
            b2Body.body = bodyFactory.makeBoxPolyBody(
                    Utilities.convertToPPM(rect.x),
                    Utilities.convertToPPM(rect.y),
                    Utilities.convertToPPM(rect.width),
                    Utilities.convertToPPM(rect.height),
                    BodyFactory.BlockType.ONE_HIT,
                    BodyDef.BodyType.StaticBody,
                    true);

            type.type = TypeComponent.BLOCK;
            b2Body.body.setUserData(blockEntity);
        }
    }
}
