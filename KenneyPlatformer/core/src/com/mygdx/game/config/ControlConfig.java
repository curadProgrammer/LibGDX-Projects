package com.mygdx.game.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

/**
 *  This is so that the user can eventually change the controls if they wanted to
 */
public class ControlConfig {
    private static volatile ControlConfig instance;

    private static final String PREF_LEFT_KEY = "left";
    private static final String PREF_RIGHT_KEY = "right";
    private static final String PREF_UP_KEY = "up";
    private static final String PREF_JUMP_KEY = "jump";
    private static final String PREF_NAME = "kenplatcontrols";

    // Private constructor
    private ControlConfig() {
        // Prevent reflection instantiation
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static ControlConfig getInstance() {
        if (instance == null) {
            synchronized (ControlConfig.class) {
                if (instance == null) {
                    instance = new ControlConfig();
                }
            }
        }
        return instance;
    }

    protected Preferences getPrefs(){
        return Gdx.app.getPreferences(PREF_NAME);
    }

    // default key to go left will be A
    public Integer getPrefLeftKey(){
        return getPrefs().getInteger(PREF_LEFT_KEY, Input.Keys.A);
    }

    public void setPrefLeftKey(Integer leftKey){
        getPrefs().putInteger(PREF_LEFT_KEY, leftKey);
        getPrefs().flush();
    }

    // default key to go right will be D
    public Integer getPrefRightKey(){
        return getPrefs().getInteger(PREF_RIGHT_KEY, Input.Keys.D);
    }

    public void setPrefRightKey(Integer rightKey){
        getPrefs().putInteger(PREF_RIGHT_KEY, rightKey);
        getPrefs().flush();
    }

    // default key to go up will be W
    public Integer getPrefUpKey(){
        return getPrefs().getInteger(PREF_UP_KEY, Input.Keys.W);
    }

    public void setPrefUpKey(Integer upKey){
        getPrefs().putInteger(PREF_UP_KEY, upKey);
        getPrefs().flush();
    }

    // default key to jump will be space
    public Integer getPrefJumpKey(){
        return getPrefs().getInteger(PREF_JUMP_KEY, Input.Keys.SPACE);
    }

    public void setPrefJumpKey(Integer jumpKey){
        getPrefs().putInteger(PREF_JUMP_KEY, jumpKey);
        getPrefs().flush();
    }

}
