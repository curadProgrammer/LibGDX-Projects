package com.mygdx.game.maps;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.GameUtil;

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
    private FixtureDef makeFixture(Shape shape, boolean isSensor, float restitution, short filterBit, short ...maskFilters){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = isSensor;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = filterBit;

        return fixtureDef;
    }

    // used by edges
    private FixtureDef makeFixture(Shape shape, boolean isSensor, short filterBit, short ...maskFilters){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = filterBit;

        // Combine all mask bits using bitwise OR
        short combinedMask = 0;
        for (short mask : maskFilters) {
            combinedMask |= mask;
        }
        fixtureDef.filter.maskBits = combinedMask;

        fixtureDef.isSensor = isSensor;
        return fixtureDef;
    }

    // make a box shape box2d body
    public Body makeBoxPolyBody(float posx, float posy, float width, float height,
                                BodyDef.BodyType bodyType, boolean fixedRotation, boolean isSensor, float restitution,
                                short filterBit, short ...maskFilters){
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
        boxBody.createFixture(makeFixture(poly, isSensor, restitution, filterBit, maskFilters));
        poly.dispose();

        return boxBody;
    }

    // method used to add edge shapes to body (default to sensor)
    public void addEdgeShape(Entity entity, Body body, Vector2 start, Vector2 end, short bitFilter, short ...maskFilters){
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(start, end);

        // todo might not need user data
        body.createFixture(makeFixture(edgeShape, true, bitFilter, maskFilters)).setUserData(entity);
        edgeShape.dispose();
    }
}
