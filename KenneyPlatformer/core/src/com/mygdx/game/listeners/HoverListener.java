package com.mygdx.game.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;
import com.mygdx.game.utils.ActionsUtil;

// this listener is used to add hover actions
public class HoverListener extends ClickListener {
    private Actor actor;

    public HoverListener(Actor actor){
        this.actor = actor;
    }

    @Override
    public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
        // pointer = -1 when hovering else other actions like clicking
        if(pointer == -1 && actor instanceof Button){
            Button button = (Button) actor;
            ActionsUtil.addScaleAction(button);
        }
    }

    @Override
    public void exit (InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
        if(pointer == -1 && actor instanceof Button){
            Button button = (Button) actor;
            button.setScale(1f);
            button.clearActions();
        }
    }
}
