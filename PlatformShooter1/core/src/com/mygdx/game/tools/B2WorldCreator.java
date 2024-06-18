package com.mygdx.game.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PlatformerShooter1;
import com.mygdx.game.characters.enemies.Boss;
import com.mygdx.game.characters.enemies.Enemy;
import com.mygdx.game.characters.enemies.GruntEnemy;

// this class will be used to render the game objects stored in
// the tiled map software
public class B2WorldCreator {
//    private Array<GruntEnemy> gruntEnemies;
//    public Array<GruntEnemy> getGruntEnemies(){
//        return gruntEnemies;
//    }
    private Array<Enemy> enemies;
    public Array<Enemy> getEnemies(){return enemies;}

    public B2WorldCreator(World world, TiledMap map, AssetManager assetManager){
        BodyDef bdef = new BodyDef();
        PolygonShape pShape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // create ground fixture
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;

            // centers to the ground position
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ PlatformerShooter1.PPM, (rect.getY() + rect.getHeight()/2)/PlatformerShooter1.PPM);
            body = world.createBody(bdef);
            pShape.setAsBox(rect.getWidth()/2/ PlatformerShooter1.PPM, rect.getHeight()/2/PlatformerShooter1.PPM);
            fdef.shape = pShape;
            fdef.filter.categoryBits = PlatformerShooter1.GROUND_BIT;
            fdef.filter.maskBits = PlatformerShooter1.ENEMY_BIT | PlatformerShooter1.PLAYER_BIT;
            body.createFixture(fdef).setUserData("Ground");
        }

        // create grunt enemies
//        gruntEnemies = new Array<>();
        // create enemies
        enemies = new Array<>();

        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
//            gruntEnemies.add(new GruntEnemy(world, assetManager, rect.x/PlatformerShooter1.PPM,
//                    rect.y/PlatformerShooter1.PPM));
            enemies.add(new GruntEnemy(world, assetManager, rect.x/PlatformerShooter1.PPM,
                    rect.y/PlatformerShooter1.PPM));
        }

        // create boss
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            enemies.add(new Boss(world, assetManager, rect.x/PlatformerShooter1.PPM,
                    rect.y/PlatformerShooter1.PPM));
        }


        // create sensors
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;

            // centers to the ground position
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ PlatformerShooter1.PPM, (rect.getY() + rect.getHeight()/2)/PlatformerShooter1.PPM);
            body = world.createBody(bdef);
            pShape.setAsBox(rect.getWidth()/2/ PlatformerShooter1.PPM, rect.getHeight()/2/PlatformerShooter1.PPM);
            fdef.shape = pShape;
            fdef.isSensor = true;
            body.createFixture(fdef).setUserData("Sensor");
        }
    }
}
