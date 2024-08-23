package com.taptap.breakout.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.taptap.breakout.BreakoutGame;
import com.taptap.breakout.Utilities;
import com.taptap.breakout.ecs.components.*;
import com.taptap.breakout.loader.BodyFactory;

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

    public int numOfBlocksLeft;

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
        renderPlayerAndBall();
//        renderPlayer();
//        renderBall();
        renderBlocks();
    }

    private void renderPlayerAndBall(){
        Entity playerEntity = en.createEntity();
        Entity ballEntity = en.createEntity();
        renderBall(ballEntity);
        renderPlayer(playerEntity, ballEntity);
    }

    private void renderPlayer(Entity playerEntity, Entity ballEntity){
        if(BreakoutGame.DEBUG_MODE) System.out.println("(LevelLoader) Rendering Player");
        PlayerComponent pc = en.createComponent(PlayerComponent.class);
        TextureComponent tc = en.createComponent(TextureComponent.class);
        TransformComponent tranC = en.createComponent(TransformComponent.class);
        CollisionComponent cc = en.createComponent(CollisionComponent.class);
        TypeComponent typeC = en.createComponent(TypeComponent.class);
        B2BodyComponent b2bodyC = en.createComponent(B2BodyComponent.class);
        AttachComponent attachC = en.createComponent(AttachComponent.class);

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
        attachC.setAttachedEntity(ballEntity);

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
        playerEntity.add(attachC);

        en.addEntity(playerEntity);

    }

    private void renderBall(Entity ballEntity){
        if(BreakoutGame.DEBUG_MODE) System.out.println("(LevelLoader) Rendering Ball");
        TextureComponent tc = en.createComponent(TextureComponent.class);
        TransformComponent tranC = en.createComponent(TransformComponent.class);
        CollisionComponent cc = en.createComponent(CollisionComponent.class);
        TypeComponent typeC = en.createComponent(TypeComponent.class);
        B2BodyComponent b2bodyC = en.createComponent(B2BodyComponent.class);
        BallComponent ballC = en.createComponent(BallComponent.class);
        SoundComponent soundComponent = en.createComponent(SoundComponent.class);

        // add sound fx
        soundComponent.soundEffects.put("ding1", (Sound) game.assetManager.manager.get(game.assetManager.ding1Sound));
        soundComponent.soundEffects.put("ding2", (Sound) game.assetManager.manager.get(game.assetManager.ding2Sound));


        // load texture
        tc.region = new TextureRegion(
                textures.findRegion("ball-sheet-removebg-preview"),
                8, 31, 25, 25
        );

        // create box2d body
        b2bodyC.body = bodyFactory.makeCirclePolyBody(
                Utilities.getPPMWidth() / 2,
                Utilities.convertToPPM(Utilities.PADDLE_HEIGHT + 25),
                Utilities.convertToPPM(tc.region.getRegionWidth()),
                null,
                BodyDef.BodyType.DynamicBody,
                true,
                false
        );

        b2bodyC.body.setUserData(ballEntity);
        ballC.speed = 5f;
        typeC.type = TypeComponent.BALL;

        // load transform
        tranC.position.set(b2bodyC.body.getPosition().x, b2bodyC.body.getPosition().y, 0);

        ballEntity.add(tc);
        ballEntity.add(tranC);
        ballEntity.add(cc);
        ballEntity.add(typeC);
        ballEntity.add(b2bodyC);
        ballEntity.add(ballC);
        ballEntity.add(soundComponent);
        en.addEntity(ballEntity);
    }

    private void renderBlocks(){
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Entity blockEntity = en.createEntity();
            B2BodyComponent b2Body = en.createComponent(B2BodyComponent.class);
            TextureComponent tc = en.createComponent(TextureComponent.class);
            CollisionComponent collision = en.createComponent(CollisionComponent.class);
            TypeComponent type = en.createComponent(TypeComponent.class);
            ScoreComponent scoreComponent = en.createComponent(ScoreComponent.class);

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
            blockEntity.add(scoreComponent);
            en.addEntity(blockEntity);

            numOfBlocksLeft++;
        }
    }
}
