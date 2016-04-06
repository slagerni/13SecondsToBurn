package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Assets;

/**
 * Created by Nick on 2/3/2015.
 */
public class MenuButton extends Actor {
    Image menuUp;
    Image menuDown;
    boolean isUp = true;

    public MenuButton(Assets assets) {
        menuDown = new Image(assets.getTexture(Assets.TEX_NAME.MENU_BUTTON_DOWN));
        menuUp = new Image(assets.getTexture(Assets.TEX_NAME.MENU_BUTTON));
        setSize(200, 150);

        addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isUp = true;
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isUp = false;
            }
        });

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Image background = menuDown;
        if(isUp) background = menuUp;

        background.setColor(getColor());
        background.setPosition(this.getX(), this.getY());
        background.draw(batch, parentAlpha);
    }
}
