package com.mygdx.game.utils;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

// this class will have actions that I can use to add to actors
public class ActionsUtil {

    // add action by making it go up and down
    public static void addMovingUpDownAction(Actor actor){
        MoveToAction moveUpAction = new MoveToAction();
        moveUpAction.setPosition(GameUtil.VIRTUAL_WIDTH / 2 - actor.getWidth() + 15,
                GameUtil.VIRTUAL_HEIGHT - actor.getHeight() - 150);
        moveUpAction.setDuration(1);
        moveUpAction.setInterpolation(Interpolation.smooth);

        MoveToAction moveDownAction = new MoveToAction();
        moveDownAction.setPosition(GameUtil.VIRTUAL_WIDTH / 2 - actor.getWidth() + 15,
                GameUtil.VIRTUAL_HEIGHT - actor.getHeight() - 175);
        moveDownAction.setDuration(1);
        moveDownAction.setInterpolation(Interpolation.smooth);

        SequenceAction overallSequence = new SequenceAction();
        overallSequence.addAction(moveUpAction);
        overallSequence.addAction(moveDownAction);

        RepeatAction infiniteLoop = new RepeatAction();
        infiniteLoop.setCount(RepeatAction.FOREVER);
        infiniteLoop.setAction(overallSequence);

        actor.addAction(infiniteLoop);
    }

    // this only works for non-label actors
    public static void addScaleAction(Actor actor){
        ScaleToAction scaleUpAction = new ScaleToAction();
        scaleUpAction.setScale(1.10f);
        scaleUpAction.setDuration(1);

        ScaleToAction scaleDownAction = new ScaleToAction();
        scaleDownAction.setScale(1f);
        scaleDownAction.setDuration(1);

        SequenceAction overallSequence = new SequenceAction();
        overallSequence.addAction(scaleUpAction);
        overallSequence.addAction(scaleDownAction);

        RepeatAction infiniteLoop = new RepeatAction();
        infiniteLoop.setCount(RepeatAction.FOREVER);
        infiniteLoop.setAction(overallSequence);

        actor.addAction(infiniteLoop);
        actor.setOrigin(actor.getWidth()/2, actor.getHeight()/2);
    }
}
