package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Assets;

/**
 * Created by Nick on 4/6/2016.
 */
public class HelpButton extends Actor {
    Image helpIcon;

    public HelpButton(Assets assets) {
        setSize(120, 120);

        helpIcon = new Image(assets.getTexture(Assets.TEX_NAME.HELP_ICON));
        setColor(Color.YELLOW);

        addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("http://casino.13secondstoburn.com");
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        helpIcon.setColor(getColor());
        helpIcon.setPosition(this.getX(), this.getY());
        helpIcon.draw(batch, parentAlpha);
    }
}
