package com.taptap.breakout.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.taptap.breakout.BreakoutGame;

public class KeyboardController implements InputProcessor{
    // paddle can only move left and right
    public boolean left, right;

    public boolean space;

    // just in case the user wants to control with mouse
    public Vector2 mouseLocation;

    public KeyboardController(){
        mouseLocation = new Vector2();
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean keyProcessed = false;
        switch(keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                if(BreakoutGame.DEBUG_MODE) System.out.println("(KeyboardController) Pressed A/Left");
                left = true;
                keyProcessed = true;
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                if(BreakoutGame.DEBUG_MODE) System.out.println("(KeyboardController) Pressed D/Right");
                right = true;
                keyProcessed = true;
                break;
            case Input.Keys.SPACE:
                space = true;
                keyProcessed = true;
                break;
        }
        return keyProcessed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean keyProcessed = false;
        switch(keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                if(BreakoutGame.DEBUG_MODE) System.out.println("(KeyboardController) Released A/Left");
                left = false;
                keyProcessed = true;
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                if(BreakoutGame.DEBUG_MODE) System.out.println("(KeyboardController) Released D/Right");
                right = false;
                keyProcessed = true;
                break;
            case Input.Keys.SPACE:
                space = false;
                keyProcessed = true;
                break;
        }
        return keyProcessed;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
