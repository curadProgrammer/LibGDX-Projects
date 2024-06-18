package com.mygdx.game.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.characters.Player;
import com.mygdx.game.characters.enemies.Boss;
import com.mygdx.game.characters.enemies.Enemy;
import com.mygdx.game.characters.enemies.GruntEnemy;
import com.mygdx.game.projectiles.Bullet;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
//        System.out.println("Begin Contact");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() instanceof GruntEnemy){
            GruntEnemy gruntEnemy = (GruntEnemy) fixA.getUserData();

            // N: grunt enemy collides with a non-player object
            if(fixB.getUserData() == "Sensor" || fixB.getUserData() instanceof Enemy){
                // reverse direction
                gruntEnemy.reverse();
            }

            if(fixB.getUserData() instanceof Player){
                Player player = (Player) fixB.getUserData();
                player.hit();
            }else if(fixB.getUserData() instanceof Bullet){
                // bullet body gets destroyed
                Bullet bullet = (Bullet) fixB.getUserData();
                bullet.destroyBullet();
                gruntEnemy.hit();
            }
        }else if(fixB.getUserData() instanceof GruntEnemy){
            GruntEnemy gruntEnemy = (GruntEnemy) fixB.getUserData();

            // N: grunt enemy collides with a non-player object
            if(fixA.getUserData() == "Sensor" || fixA.getUserData() instanceof Enemy){
                // reverse direction
                gruntEnemy.reverse();
            }

            if(fixA.getUserData() instanceof Player){
                Player player = (Player) fixA.getUserData();
                player.hit();
            }else if(fixA.getUserData() instanceof Bullet){
                // bullet body gets destroyed
                Bullet bullet = (Bullet) fixB.getUserData();
                bullet.destroyBullet();
                gruntEnemy.hit();
            }
        }

        if(fixA.getUserData() instanceof Boss){
            Boss boss = (Boss) fixA.getUserData();

            // N: grunt enemy collides with a non-player object
            if(fixB.getUserData() == "Sensor" || fixB.getUserData() instanceof Enemy){
                // reverse direction
                boss.reverse();
            }

            if(fixB.getUserData() instanceof Player){
                Player player = (Player) fixB.getUserData();
                player.hit();
            }else if(fixB.getUserData() instanceof Bullet){
                // bullet body gets destroyed
                Bullet bullet = (Bullet) fixB.getUserData();
                bullet.destroyBullet();
                boss.hit();
            }
        }else if(fixB.getUserData() instanceof Boss){
            Boss boss = (Boss) fixB.getUserData();

            // N: grunt enemy collides with a non-player object
            if(fixA.getUserData() == "Sensor" || fixA.getUserData() instanceof Enemy){
                // reverse direction
                boss.reverse();
            }

            if(fixA.getUserData() instanceof Player){
                Player player = (Player) fixA.getUserData();
                player.hit();
            }else if(fixA.getUserData() instanceof Bullet){
                // bullet body gets destroyed
                Bullet bullet = (Bullet) fixB.getUserData();
                bullet.destroyBullet();
                boss.hit();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
