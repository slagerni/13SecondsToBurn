package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;

import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.addAction;

/**
 * Created by Nick on 1/22/2015.
 */
public class WinLosePopup extends Actor {
    Assets assets;
    boolean isWinner = true;
    Image winnerImage;
    Image loserImage;
    Random random = new Random();

    class HideRunnable implements Runnable {
        @Override
        public void run() {
            setVisible(false);
        }
    }

    public WinLosePopup(Assets assets) {
        this.assets = assets;

        setVisible(false);

        winnerImage = new Image(assets.getTexture(Assets.TEX_NAME.WIN_POPUP));
        winnerImage.setOrigin(winnerImage.getWidth()/2, winnerImage.getHeight()/2);
        winnerImage.setColor(Color.YELLOW);

        loserImage = new Image(assets.getTexture(Assets.TEX_NAME.LOSE_POPUP));
        loserImage.setOrigin(loserImage.getWidth()/2, loserImage.getHeight()/2);
        loserImage.setColor(Color.RED);
    }

    public void pop(boolean isWinner, float startX, float startY) {
        setColor(Color.WHITE); // so I reset the alpha of the actor
        winnerImage.setColor(Color.YELLOW);
        winnerImage.setPosition(0, 0);
        loserImage.setColor(Color.RED);
        loserImage.setPosition(0, 0);

        this.isWinner = isWinner;
        setVisible(true);
        Image image;
        if(isWinner) {
            image = winnerImage;
        }
        else {
            image = loserImage;
        }

        setScale(1);
        setPosition(startX, startY);

        int xOffset = random.nextInt(200) - 100;
        int yOffset = random.nextInt(100) + 50;

        addAction(sequence(
                parallel(moveTo(startX + xOffset, startY + yOffset, 2f), scaleTo(2f, 2f, 2f), fadeOut(2f)),
                run(new HideRunnable())));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Image toDraw;
        if(isWinner) {
            toDraw = winnerImage;
        }
        else {
            toDraw = loserImage;
        }

        toDraw.setPosition(this.getX(), this.getY());
        toDraw.setRotation(this.getRotation());
        toDraw.setScale(this.getScaleX(), this.getScaleY());
        toDraw.setColor(toDraw.getColor().r, toDraw.getColor().g, toDraw.getColor().b, getColor().a);
        toDraw.draw(batch, parentAlpha);
    }

}
