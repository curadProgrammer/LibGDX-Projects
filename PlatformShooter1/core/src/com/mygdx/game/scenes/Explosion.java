package com.mygdx.game.scenes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.PlatformerShooter1;

public class Explosion {
    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer;
    private Vector2 pos;
    private Texture texture;
    public Explosion(Texture texture, Vector2 pos, float totalAnimationTime, AssetManager assetManager){
        this.pos = pos;
        this.texture = texture;

        // split the texture
        TextureRegion[][] textureRegion2D = TextureRegion.split(texture, 62, 62);

        // convert to 1d array use for animation
        TextureRegion[] textureRegion1D = new TextureRegion[16];
        int index = 0;
        for(int i = 0; i < 4; i++){
            System.out.println(textureRegion2D.length);
            for(int j = 0; j < 4; j++){
                System.out.println(textureRegion2D[i].length);
                textureRegion1D[index++] = textureRegion2D[i][j];
            }
        }

        // play explosion animation
        Sound exploded = assetManager.get("sfx/DeathFlash.ogg", Sound.class);
        exploded.play(0.75f);

        explosionAnimation = new Animation<>(totalAnimationTime/40, textureRegion1D);
        explosionTimer = 0;
    }

    public void update(float deltaTime){
        explosionTimer += deltaTime;
    }


    public void draw(SpriteBatch batch){
        TextureRegion[][] textureRegion2D = TextureRegion.split(texture, 72, 70);

//        batch.draw(textureRegion2D[0][1],
//                pos.x - 200 / PlatformerShooter1.PPM / 2,
//                pos.y - 200 / PlatformerShooter1.PPM / 2,
//                200 / PlatformerShooter1.PPM, 200 / PlatformerShooter1.PPM);

        batch.draw(explosionAnimation.getKeyFrame(explosionTimer),
                pos.x - 200 / PlatformerShooter1.PPM / 2,
                pos.y - 200 / PlatformerShooter1.PPM / 2,
                200 / PlatformerShooter1.PPM, 200 / PlatformerShooter1.PPM);

    }

    // check to see if the animation is finished
    public boolean isFinished(){
        return explosionAnimation.isAnimationFinished(explosionTimer);
    }
}

























