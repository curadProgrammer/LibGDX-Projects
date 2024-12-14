package com.mygdx.game.assets;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class B2DAssetmanager {
    private static B2DAssetmanager instance;
    public final AssetManager assetManager;

    // images
    public String titleBgTexturePath = "ui/title-bg.png";

    // fonts (LARGE, MEDIUM, SMALL)
    public final String gamerFont = "fonts/Kenney Future.ttf";
    public final String fontLarge = "sizeLg.ttf";
    public final String fontMedium = "sizeMd.ttf";
    public final String fontSmall = "sizeSm.ttf";
    private final int fontLargeValue = 64;
    private final int fontMediumValue = 48;
    private final int fontSmallValue = 16;

    // skin
    public final String skinPath = "skin/kenney-ui-green.json";
    private final String skinAtlasPath = "skin/kenney-ui-green.atlas";

    private B2DAssetmanager() {
        assetManager = new AssetManager();
    }

    public static B2DAssetmanager getInstance() {
        if (instance == null) {
            instance = new B2DAssetmanager();
        }
        return instance;
    }

    public void queueAddImages() {
        assetManager.load(titleBgTexturePath, Texture.class);
    }

    public void queueAddSkin() {
        System.out.println(skinPath);
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter(skinAtlasPath);
        assetManager.load(skinPath, Skin.class, params);
    }

    public void queueAddFonts() {
        // Register the FreeTypeFontLoader with the AssetManager
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(new InternalFileHandleResolver()));

        // small
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameterSmall = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameterSmall.fontFileName = gamerFont;
        fontParameterSmall.fontParameters.size = fontSmallValue;
        assetManager.load(fontSmall, BitmapFont.class, fontParameterSmall);

        // medium
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameterMedium = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameterMedium.fontFileName = gamerFont;
        fontParameterMedium.fontParameters.size = fontMediumValue;
        assetManager.load(fontMedium, BitmapFont.class, fontParameterMedium);

        // large
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameterLarge = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameterLarge.fontFileName = gamerFont;
        fontParameterLarge.fontParameters.size = fontLargeValue;
        assetManager.load(fontLarge, BitmapFont.class, fontParameterLarge);
    }
}
