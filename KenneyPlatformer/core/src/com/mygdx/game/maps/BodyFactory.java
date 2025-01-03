package com.mygdx.game.maps;

import com.badlogic.gdx.physics.box2d.*;

/**
 * This class will be used to create Box2D bodies for different entities
 */
public class BodyFactory {
    private World world;
    private BodyFactory(World world){
        this.world = world;
    }

    private static BodyFactory thisInstance;
    public static BodyFactory getInstance(World world){
        if(thisInstance == null) thisInstance = new BodyFactory(world);
        return thisInstance;
    }

    // this method will be used by other methods to actually create the fixture for the Box2D world
    private static FixtureDef makeFixture(Shape shape, boolean isSensor){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        // make fixture a sensor
        if(isSensor) fixtureDef.isSensor = true;

        return fixtureDef;
    }

    // make a box shape box2d body
    public Body makeBoxPolyBody(float posx, float posy, float width, float height,
                                BodyDef.BodyType bodyType, boolean fixedRotation, boolean isSensor){
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx + (width / 2);
        boxBodyDef.position.y = posy + (height / 2);
        boxBodyDef.fixedRotation = fixedRotation;

        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width / 2, height / 2);
        boxBody.createFixture(makeFixture(poly, isSensor));
        poly.dispose();

        return boxBody;
    }
}
