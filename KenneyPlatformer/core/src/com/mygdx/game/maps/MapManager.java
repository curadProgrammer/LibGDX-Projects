package com.mygdx.game.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.screens.GameScreen;

/**
 * This class will manage the different levels that we have
 */
public class MapManager {
    private static final Logger logger = new Logger(MapManager.class.toString(), Logger.DEBUG);

    private GameScreen gameScreen;
    private final int levelCount;

    public MapManager(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        levelCount = countLevels(Gdx.files.getLocalStoragePath() + "/levels");
        System.out.println(levelCount);
    }


    // count the number of tmx files in the levels folder
    public int countLevels(String folderPath) {
        FileHandle folder = Gdx.files.internal(folderPath);
        return folder.list(".tmx").length;
    }
}
