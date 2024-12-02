package com.mygdx.game.assets;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;


public class B2DAssetmanager {
    private static B2DAssetmanager instance;
    private final AssetManager assetManager;

    // images
    public final String titleBgImagePath = "ui/title-bg";

    private B2DAssetmanager() {
        assetManager = new AssetManager();
    }

    public static B2DAssetmanager getInstance() {
        if (instance == null) {
            instance = new B2DAssetmanager();
        }
        return instance;
    }

    public void queueAddImages(){
        assetManager.load(titleBgImagePath, Texture.class);
    }

    public void queueAddSkin() {

    }

    public void queueAddFonts() {

    }
