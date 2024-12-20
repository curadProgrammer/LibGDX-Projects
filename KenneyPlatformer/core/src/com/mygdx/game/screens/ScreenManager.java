package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.MyGdxGame;



public class ScreenManager {
    private static final Logger logger = new Logger(ScreenManager.class.toString(), Logger.DEBUG);

    private static ScreenManager instance;
    private MyGdxGame game;

    // Define your screen types
    public enum ScreenType {
        TITLE_SCREEN,
        GAME_SCREEN,
        SETTING_SCREEN,
        LOADING_SCREEN
    }

    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }

        return instance;
    }

    public void initialize(MyGdxGame game) {
        this.game = game;
    }

    public void setScreen(ScreenType screenType) {
        // Clear the previous screen
        if (game.getScreen() != null) {
            game.getScreen().dispose();
        }

        // Set the new screen based on type
        Screen newScreen = createScreen(screenType);
        game.setScreen(newScreen);
    }

    private Screen createScreen(ScreenType screenType) {
        switch (screenType) {
            case LOADING_SCREEN:
                logger.info("Creating and Switching to LOADING_SCREEN");
                return new LoadingScreen(game);
            case TITLE_SCREEN:
                logger.info("Creating and Switching to TITLE_SCREEN");
                return new TitleScreen(game);
//            case GAME_SCREEN:
//                return new GamePlayScreen(game);
            case SETTING_SCREEN:
                return new SettingScreen(game);
            default:
                throw new IllegalArgumentException("Unknown screen type: " + screenType);
        }
    }
}
