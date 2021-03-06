package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

/**
 * Created by Nick on 4/17/2015.
 */
public class ChipStackGroup extends Group {
    CasinoPracticeGame game;
    Assets assets;
    Image circle;
    ChipStack chipStack;
    MessagePopup popup;

    public ChipStackGroup(final CasinoPracticeGame game, Assets assets, float x, float y, float scale) {
        this(game, assets, null);
        setPosition(x, y);
        setScale(scale);
    }

    public ChipStackGroup(final CasinoPracticeGame game, Assets assets, float scale) {
        this(game, assets, null);
        setScale(scale);
    }

    public ChipStackGroup(final CasinoPracticeGame game, Assets assets, Assets.TEX_NAME circleTexture) {
        this.game = game;
        this.assets = assets;

        if(circleTexture != null) {
            circle = new Image(assets.getTexture(circleTexture));
        }

        chipStack = new ChipStack(game, 0);

        setSize(chipStack.getWidth(), chipStack.getHeight());

        popup = new MessagePopup(assets);

        if(circle != null) {
            addActor(circle);
        }
        addActor(chipStack);
        addActor(popup);
    }

    public void setTotal(int amount) {
        chipStack.setTotal(amount);
    }

    public int getTotal() {
        return chipStack.getTotal();
    }

    public void increaseTotal(int amount) {chipStack.increaseTotal(amount);}

    public void popStack(boolean won) {
        popup.pop(won ? MessagePopup.Message.WIN : MessagePopup.Message.LOSE, chipStack.getX() + 37, chipStack.getY() + 38);
    }

    public void popStack(MessagePopup.Message message) {
        popup.pop(message, chipStack.getX() + 37, chipStack.getY() + 38);
    }

    public void clear() {
        chipStack.clearTotal();
    }


}
