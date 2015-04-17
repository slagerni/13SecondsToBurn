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
    WinLosePopup popup;

    public ChipStackGroup(final CasinoPracticeGame game, Assets assets) {
        this(game, assets, null);
    }

    public ChipStackGroup(final CasinoPracticeGame game, Assets assets, Assets.TEX_NAME circleTexture) {
        this.game = game;
        this.assets = assets;

        if(circleTexture != null) {
            //Assets.TEX_NAME.BLANK_CIRCLE
            circle = new Image(assets.getTexture(circleTexture));
        }

        chipStack = new ChipStack(game, 0);

        setSize(chipStack.getWidth(), chipStack.getHeight());

        popup = new WinLosePopup(assets);

        addActor(circle);
        addActor(chipStack);
        addActor(popup);
    }

//    public ChipStack getChipStack() {
//        return chipStack;
//    }

    public void setTotal(int amount) {
        chipStack.setTotal(amount);
    }

    public int getTotal() {
        return chipStack.getTotal();
    }

    public void increaseTotal(int amount) {chipStack.increaseTotal(amount);}

    public void popStack(boolean won) {
        popup.pop(won, chipStack.getX() + 37, chipStack.getY() + 38);
    }

    public void clear() {
        chipStack.clearTotal();
    }


}
