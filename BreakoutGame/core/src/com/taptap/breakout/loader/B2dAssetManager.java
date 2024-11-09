package com.taptap.breakout.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class B2dAssetManager {
    private static volatile B2dAssetManager instance;
    public final AssetManager manager;

    // textures
    public final String gameImages = "images/breakout-game.atlas";

    // soundfx
    public final String ding1Sound = "soundfx/ding_1.wav";
    public final String ding2Sound = "soundfx/ding_2.wav";
    public final String explosionSound = "soundfx/explosion.wav";

    // music
    public final String backgroundMusic = "music/night night.ogg";

    // skin
    public final String skin = "skin/craftacular-ui.json";

    // fonts (LARGE, MEDIUM, SMALL)
    public final String gamerFont = "fonts/GAMER.otf";
    public final String fontLarge = "size72.otf";
    public final String fontMedium = "size36.otf";
    public final String fontSmall = "size18.otf";

    // Private constructor
    private B2dAssetManager() {
        manager = new AssetManager();
    }

    public static B2dAssetManager getInstance() {
        if (instance == null) {
            synchronized (B2dAssetManager.class) {
                if (instance == null) {
                    instance = new B2dAssetManager();
                }
            }
        }
        return instance;
    }

    public void queueAddImages() {
        manager.load(gameImages, TextureAtlas.class);
    }

    public void queueAddSounds() {
        manager.load(ding1Sound, Sound.class);
        manager.load(ding2Sound, Sound.class);
        manager.load(explosionSound, Sound.class);
    }

    public void queueAddMusic() {
        manager.load(backgroundMusic, Music.class);
    }

    public void queueAddSkin() {
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("skin/craftacular-ui.atlas");
        manager.load(skin, Skin.class, params);
    }

    public void queueAddFonts() {
        // Register the FreeTypeFontLoader with the AssetManager
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
        manager.setLoader(BitmapFont.class, ".otf", new FreetypeFontLoader(new InternalFileHandleResolver()));

        // small
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameterSmall = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameterSmall.fontFileName = gamerFont;
        fontParameterSmall.fontParameters.size = 18;
        manager.load(fontSmall, BitmapFont.class, fontParameterSmall);

        // medium
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameterMedium = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameterMedium.fontFileName = gamerFont;
        fontParameterMedium.fontParameters.size = 36;
        manager.load(fontMedium, BitmapFont.class, fontParameterMedium);

        // large
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameterLarge = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameterLarge.fontFileName = gamerFont;
        fontParameterLarge.fontParameters.size = 64;
        manager.load(fontLarge, BitmapFont.class, fontParameterLarge);
    }

    public void dispose() {
        if (manager != null) {
            manager.dispose();
        }
        instance = null;  // Allow for recreation if needed
    }
}

















