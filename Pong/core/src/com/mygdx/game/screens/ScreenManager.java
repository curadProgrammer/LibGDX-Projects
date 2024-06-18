package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenManager {
    private final Game game;
    private Screen currentScreen;

    public ScreenManager(Game game){
        this.game = game;
    }

    public void setScreen(Screen screen){
        if(currentScreen != null){
            currentScreen.dispose();
        }

        // update current screen;
        currentScreen = screen;

        // set the screen to new screen
        game.setScreen(currentScreen);
    }

    public Screen getCurrentScreen(){return currentScreen;}
}
