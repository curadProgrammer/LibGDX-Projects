package com.taptap.breakout.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.taptap.breakout.BreakoutGame;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.ecs.components.*;
import com.taptap.breakout.loader.BodyFactory;

import javax.rmi.CORBA.Util;

public class LevelLoader {
    private BodyFactory bodyFactory;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private World world;
    private BreakoutGame game;
    private PooledEngine en;
    private OrthogonalTiledMapRenderer mapRenderer;
    public OrthogonalTiledMapRenderer getMapRenderer(){return mapRenderer;}

    private TextureAtlas textures;

    // mapFilePath - pass in the path to the map file to be loaded
    public LevelLoader(BreakoutGame game, World world, PooledEngine en, String mapFilePath){
        this.world = world;
        this.en = en;
        this.game = game;
        bodyFactory = BodyFactory.getInstance(world);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(mapFilePath);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/ Utilities.PPM);
        textures = game.assetManager.manager.get(game.assetManager.gameImages);
        loadWorld();
    }


    public void loadWorld(){
        renderPlayer();
        renderBall();
        renderBlocks();
    }

    private void renderPlayer(){
        if(BreakoutGame.DEBUG_MODE) System.out.println("(LevelLoader) Rendering Player");
        Entity playerEntity = en.createEntity();
        PlayerComponent pc = en.createComponent(PlayerComponent.class);
        TextureComponent tc = en.createComponent(TextureComponent.class);
        TransformComponent tranC = en.createComponent(TransformComponent.class);
        CollisionComponent cc = en.createComponent(CollisionComponent.class);
        TypeComponent typeC = en.createComponent(TypeComponent.class);
        B2BodyComponent b2bodyC = en.createComponent(B2BodyComponent.class);

        // create box2d body
        b2bodyC.body = bodyFactory.makeBoxPolyBody(
                Utilities.getPPMWidth() / 2 - (Utilities.convertToPPM(Utilities.PADDLE_WIDTH) / 2),
                Utilities.convertToPPM(10),
                Utilities.convertToPPM(Utilities.PADDLE_WIDTH),
                Utilities.convertToPPM(Utilities.PADDLE_HEIGHT),
                null,
                BodyDef.BodyType.KinematicBody,
                true,
                false
        );

        b2bodyC.body.setUserData(playerEntity);
        typeC.type = TypeComponent.PLAYER;

        // load texture
        tc.region = new TextureRegion(
                textures.findRegion("paddle-sheet-removebg-preview(1)"),
                0, 0, 100, 30
        );

        // load transform
        tranC.position.set(b2bodyC.body.getPosition().x, b2bodyC.body.getPosition().y, 0);

        playerEntity.add(pc);
        playerEntity.add(tc);
        playerEntity.add(tranC);
        playerEntity.add(cc);
        playerEntity.add(typeC);
        playerEntity.add(b2bodyC);
        b2bodyC.body.setUserData(playerEntity);

        en.addEntity(playerEntity);

    }

    private void renderBall(){
        if(BreakoutGame.DEBUG_MODE) System.out.println("(LevelLoader) Rendering Ball");
        Entity ballEntity = en.createEntity();
        TextureComponent tc = en.createComponent(TextureComponent.class);
        TransformComponent tranC = en.createComponent(TransformComponent.class);
        CollisionComponent cc = en.createComponent(CollisionComponent.class);
        TypeComponent typeC = en.createComponent(TypeComponent.class);
        B2BodyComponent b2bodyC = en.createComponent(B2BodyComponent.class);
        BallComponent ballC = en.createComponent(BallComponent.class);

        // load texture
        tc.region = new TextureRegion(
                textures.findRegion("ball-sheet-removebg-preview"),
                8, 31, 25, 25
        );

        // create box2d body
        b2bodyC.body = bodyFactory.makeCirclePolyBody(
                Utilities.getPPMWidth() / 2,
                Utilities.convertToPPM(Utilities.PADDLE_HEIGHT + 10),
                Utilities.convertToPPM(tc.region.getRegionWidth()),
                null,
                BodyDef.BodyType.DynamicBody,
                true,
                false
        );

        b2bodyC.body.setUserData(ballEntity);


        // give ball an initial velocity
        // todo will change this such that the ball will be on the paddle until the player presses spacebar to let go
        ballC.speed = 5f;
//        b2bodyC.body.setLinearVelocity(new Vector2(1f, 1f).nor().scl(ballC.speed));

        typeC.type = TypeComponent.BALL;

        // load transform
        tranC.position.set(b2bodyC.body.getPosition().x, b2bodyC.body.getPosition().y, 0);

        ballEntity.add(tc);
        ballEntity.add(tranC);
        ballEntity.add(cc);
        ballEntity.add(typeC);
        ballEntity.add(b2bodyC);
        ballEntity.add(ballC);
        b2bodyC.body.setUserData(ballEntity);

        en.addEntity(ballEntity);
    }

    private void renderBlocks(){
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Entity blockEntity = en.createEntity();
            B2BodyComponent b2Body = en.createComponent(B2BodyComponent.class);
            TextureComponent tc = en.createComponent(TextureComponent.class);
            CollisionComponent collision = en.createComponent(CollisionComponent.class);
            TypeComponent type = en.createComponent(TypeComponent.class);

            // get the rectangle object from the map
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            MapProperties properties = object.getProperties();
            String color = (String) properties.get("color");

            switch(color){
                case "red":
                    // load red block texture
                    tc.region = new TextureRegion(
                            textures.findRegion("blocks-sheet-removebg-preview"),
                            1, 2, 34, 32
                    );
                    break;
                case "purple":
                    // load purple block texture
                    tc.region = new TextureRegion(
                            textures.findRegion("blocks-sheet-removebg-preview"),
                            41, 2, 34, 32
                    );
                    break;
                case "yellow":
                    // load yellow block texture
                    tc.region = new TextureRegion(
                            textures.findRegion("blocks-sheet-removebg-preview"),
                            81, 2, 34, 32
                    );
                    break;
            }


            // create body with bodyfactory
            b2Body.body = bodyFactory.makeBoxPolyBody(
                    Utilities.convertToPPM(rect.x),
                    Utilities.convertToPPM(rect.y),
                    Utilities.convertToPPM(rect.width),
                    Utilities.convertToPPM(rect.height),
                    BodyFactory.Material.PLASTIC,
                    BodyDef.BodyType.StaticBody,
                    true,
                    false);


            type.type = TypeComponent.BLOCK;
            b2Body.body.setUserData(blockEntity);

            blockEntity.add(b2Body);
            blockEntity.add(tc);
            blockEntity.add(collision);
            blockEntity.add(type);
            en.addEntity(blockEntity);
        }
    }
}
