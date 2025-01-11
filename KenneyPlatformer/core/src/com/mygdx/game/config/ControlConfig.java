package com.mygdx.game.config;

import com.badlogic.gdx.Gdx;
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
    public String getPrefLeftKey(){
        return getPrefs().getString(PREF_LEFT_KEY, "A");
    }

    public void setPrefLeftKey(String leftKey){
        getPrefs().putString(PREF_LEFT_KEY, leftKey);
        getPrefs().flush();
    }

    // default key to go right will be D
    public String getPrefRightKey(){
        return getPrefs().getString(PREF_RIGHT_KEY, "D");
    }

    public void setPrefRightKey(String rightKey){
        getPrefs().putString(PREF_RIGHT_KEY, rightKey);
        getPrefs().flush();
    }

    // default key to go up will be W
    public String getPrefUpKey(){
        return getPrefs().getString(PREF_UP_KEY, "W");
    }

    public void setPrefUpKey(String upKey){
        getPrefs().putString(PREF_UP_KEY, upKey);
        getPrefs().flush();
    }

    // default key to jump will be space
    public String getPrefJumpKey(){
        return getPrefs().getString(PREF_UP_KEY, "SPACE");
    }

    public void setPrefJumpKey(String jumpKey){
        getPrefs().putString(PREF_UP_KEY, jumpKey);
        getPrefs().flush();
    }

}
