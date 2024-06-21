package com.taptap.breakout.loader;

import com.badlogic.gdx.physics.box2d.*;

public class BodyFactory {
    // ONE_HIT - Takes one hit to get destroyed
    // TWO_HIT - Takes two hits to get destroyed
    // UNBREAKABLE - Block is unbreakable
    // PASSTHROUGH - Ball is able to break the block and pass through instead of bouncing back from it
    public enum BlockType {ONE_HIT, TWO_HIT, UNBREAKABLE, PASSTHROUGH}

    // singleton
    private static BodyFactory thisInstance;
    public static BodyFactory getInstance(World world){
        if(thisInstance == null) thisInstance = new BodyFactory(world);
        return thisInstance;
    }

    private World world;
    private BodyFactory(World world){
        this.world = world;
    }

    // material refers to how we want our fixture to behave
    private static FixtureDef makeFixture(BlockType blockType, Shape shape){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        switch(blockType){
            case ONE_HIT:
            case TWO_HIT:
            case UNBREAKABLE:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.3f;
                fixtureDef.restitution = 0.1f;
                break;
            case PASSTHROUGH:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0f;
                fixtureDef.restitution = 0.01f;
                break;
        }
        return fixtureDef;
    }

    // make a box box2d body
    public Body makeBoxPolyBody(float posx, float posy, float width, float height, BlockType blockType,
                                BodyDef.BodyType bodyType, boolean fixedRotation){
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
        boxBody.createFixture(makeFixture(blockType,poly));
        poly.dispose();

        return boxBody;
    }
}
