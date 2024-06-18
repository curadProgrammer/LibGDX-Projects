package com.mygdx.game.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.PlatformerShooter1;
import com.mygdx.game.characters.Player;

// N: only the player will be able to shoot which is why we pass in the player obj
public class Bullet implements Disposable {
    private Player player;

    private World world;
    private Body body;
    public Body getBody(){return body;}

    private float xVelocity;
    private boolean setToDestroy;
    private boolean destroyed;
    public boolean isSetToDestroy(){return setToDestroy;}
    public void setxVelocity(float newXVelocity){
        xVelocity = newXVelocity;
    }

    public Bullet(Player player, World world){
        this.player = player;
        this.world = world;

        defineBullet();

        // set linear speed
        // N: might need to try with applylinearimpulse if this doesn't work
        xVelocity = 400 / PlatformerShooter1.PPM;

        if(player.isFacingRight())
            body.setLinearVelocity(xVelocity, 0);
        else
            body.setLinearVelocity(-xVelocity, 0);

    }

    // define box2d properties (i.e. fixture and body)
    private void defineBullet(){
        BodyDef bdef = new BodyDef();
        if(player.isFacingRight()){
            bdef.position.set(player.getB2body().getPosition().x + player.getWidth(),
                    player.getB2body().getPosition().y + player.getHeight()/2f);
        }else{
            bdef.position.set(player.getB2body().getPosition().x - player.getWidth(),
                    player.getB2body().getPosition().y  + player.getHeight()/2f);
        }
        bdef.type = BodyDef.BodyType.KinematicBody; // I'm assuming this means that it can move but not get affected by gravity
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape cShape = new CircleShape();
        cShape.setRadius(3 / PlatformerShooter1.PPM);
        fdef.shape = cShape;
        fdef.filter.categoryBits = PlatformerShooter1.BULLET_BIT;
        fdef.filter.maskBits = PlatformerShooter1.ENEMY_BIT;
        body.createFixture(fdef).setUserData(this);
    }

    public void destroyBullet(){
        setToDestroy = true;
    }

    // N: call this on the render method
    // only when this bullet goes past a certain point we can stop drawing it
    // need to review how to destroy bodies in box2d
    public void draw(ShapeRenderer shapeRenderer){
        if(setToDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }else if(!destroyed){
            // draw bullet on body
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.circle(body.getPosition().x,
                    body.getPosition().y,
                    body.getFixtureList().get(0).getShape().getRadius(),
                    25);
//            shapeRenderer.circle(body.getPosition().x * PlatformerShooter1.PPM,
//                    body.getPosition().y * PlatformerShooter1.PPM,
//                    body.getFixtureList().get(0).getShape().getRadius() * PlatformerShooter1.PPM,
//                    25);
        }
    }

    @Override
    public void dispose() {

    }
}
