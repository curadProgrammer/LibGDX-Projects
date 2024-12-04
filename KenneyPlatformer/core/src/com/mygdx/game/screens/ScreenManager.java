package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGdxGame;

import java.util.logging.Logger;


public class ScreenManager {
    private static final Logger logger = Logger.getLogger(ScreenManager.class.getName());

    private static ScreenManager instance;
    private MyGdxGame game;

    // Define your screen types
    public enum ScreenType {
        TITLE_SCREEN,
        GAME_SCREEN,
        OPTIONS_SCREEN,
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
//            case OPTIONS_SCREEN:
//                return new OptionsScreen(game);
            default:
                throw new IllegalArgumentException("Unknown screen type: " + screenType);
        }
    }
}
