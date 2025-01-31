package com.mygdx.game.maps;

import com.badlogic.gdx.math.Vector2;
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
    private FixtureDef makeFixture(Shape shape, boolean isSensor, float restitution){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = isSensor;
        fixtureDef.restitution = restitution;

        return fixtureDef;
    }

    // used by edges
    private FixtureDef makeFixture(Shape shape, boolean isSensor, short filterBit){
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = filterBit;
        fdef.isSensor = isSensor;

        return fdef;
    }

    // make a box shape box2d body
    public Body makeBoxPolyBody(float posx, float posy, float width, float height,
                                BodyDef.BodyType bodyType, boolean fixedRotation, boolean isSensor, float restitution){
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
        boxBody.createFixture(makeFixture(poly, isSensor, restitution));
        poly.dispose();

        return boxBody;
    }

    public Body makeCirclePolyBody(float posx, float posy, float diameter,
                                   BodyDef.BodyType bodyType, boolean fixedRotation, boolean isSensor){
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        Body boxBody = world.createBody(boxBodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(diameter /2);
        boxBody.createFixture(makeFixture(circleShape, isSensor, 0));
        circleShape.dispose();
        return boxBody;
    }

    // method used to add edge shapes to body (default to sensor)
    public void addEdgeShape(Body body, Vector2 start, Vector2 end, short bitFilter){
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(start, end);

        // todo might need to mask bits (so they don't collide with everything and only the ones we want)

        body.createFixture(makeFixture(edgeShape, true, bitFilter)).setUserData(this);
        edgeShape.dispose();
    }
}
