package com.mygdx.game.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PlatformerShooter1;
import com.mygdx.game.characters.Player;

public class Hud implements Disposable {
    private Texture healthTexture;
    private Player player;
    private OrthographicCamera cam;

    public Hud(Player player, OrthographicCamera cam){
        this.player = player;
        this.cam = cam;
        healthTexture = new Texture(Gdx.files.internal("raw-textures/health-icon.png"));
    }

    public void render(SpriteBatch batch){
        int horizontalSpacing = 45;

        System.out.println("HUD Position: " + (cam.viewportHeight));
        // render the amount based on player's health
        for(int i = 0; i < player.getHealth(); i++){
            batch.draw(healthTexture,
                    (cam.position.x - cam.viewportWidth/2) + (25 + (i * horizontalSpacing)) / PlatformerShooter1.PPM,
                    cam.position.y + cam.viewportHeight/3,
                    40 / PlatformerShooter1.PPM,
                    40/ PlatformerShooter1.PPM);
        }
    }

    @Override
    public void dispose() {
        healthTexture.dispose();
    }
}
