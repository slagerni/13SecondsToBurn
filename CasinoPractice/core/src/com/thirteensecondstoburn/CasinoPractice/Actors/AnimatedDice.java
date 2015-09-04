package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Nick on 9/3/2015.
 */
public class AnimatedDice extends Image
{
    protected Animation animation = null;
    private float stateTime = 0;
    List<ActionCompletedListener> listeners = new ArrayList<ActionCompletedListener>();

    public AnimatedDice(Animation animation) {
        super(animation.getKeyFrame(0));
        this.animation = animation;
    }

    class NotifyRunnable implements Runnable {
        @Override
        public void run() {
            notifyListeners();
        }
    }

    public void roll(float startX, float startY, float endX, float endY) {
//        setColor(Color.WHITE); // so I reset the alpha of the actor

        setVisible(true);

        setScale(1);
        setPosition(startX, startY);

        addAction(sequence(
                moveTo(endX, endY, 2f),
                run(new NotifyRunnable())));
    }

    @Override
    public void act(float delta)
    {
        ((TextureRegionDrawable)getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta, true));
        super.act(delta);
    }

    public void addActionListener(ActionCompletedListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for(ActionCompletedListener l : listeners) {
            l.actionCompleted(this);
        }
    }

}
