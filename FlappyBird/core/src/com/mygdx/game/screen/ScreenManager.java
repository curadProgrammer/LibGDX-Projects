package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenManager {
    private final Game game; // reference to the game instance
    private Screen currentScreen;

    public ScreenManager(Game game){
        this.game = game;
    }

    public void setScreen(Screen screen){
        // releases the resources being used by the current screen
        // (don't need to use them right now)
        if(currentScreen != null){
            currentScreen.dispose();
        }

        // update the current screen to the new screen
        currentScreen = screen;

        // set the screen to the new screen
        game.setScreen(screen);
    }

    public Screen getCurrentScreen(){
        return currentScreen;
    }
}
