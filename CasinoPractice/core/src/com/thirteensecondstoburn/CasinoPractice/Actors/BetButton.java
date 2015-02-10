package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;

/**
 * Created by Nick on 1/17/2015.
 */
public class BetButton extends Actor {
    int minAmount = 5;
    int currentAmount = 5;
    
    Assets assets;

    public BetButton(Assets assets) {
        this.assets = assets;
        this.setWidth(256);
        this.setHeight(256);
    }

    public boolean isInside(float x, float y) {
        return x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Image chip = new Image(assets.getChipTexture("" + currentAmount));
        chip.setScale(1.5f);
        chip.setPosition(this.getX() + (getWidth() - ChipStack.CHIP_WIDTH * chip.getScaleX()) / 2f, this.getY() + (getHeight() - ChipStack.CHIP_HEIGHT * chip.getScaleY()) / 2f);
        chip.draw(batch, parentAlpha);
    }

    public int resetToMin() {
        currentAmount = minAmount;
        return minAmount;
    }

    public int increaseAmount(){
        if(currentAmount == 1) {
            currentAmount = 5;
        }
        else if(currentAmount == 5) {
            currentAmount = 25;
        }
        else if(currentAmount == 25) {
            currentAmount = 100;
        }
        else if(currentAmount == 100) {
            currentAmount = 500;
        }
        else if(currentAmount == 500) {
            currentAmount = 1000;
        }
        else if(currentAmount == 1000) {
            currentAmount = 2000;
        }
        else if(currentAmount == 2000) {
            currentAmount = 5000;
        }

        return currentAmount;
    }

    public int decreaseAmount(){
        if(currentAmount == 5) {
            if(currentAmount != minAmount) currentAmount = 1;
        }
        else if(currentAmount == 25) {
            if(currentAmount != minAmount) currentAmount = 5;
        }
        else if(currentAmount == 100) {
            if(currentAmount != minAmount) currentAmount = 25;
        }
        else if(currentAmount == 500) {
            if(currentAmount != minAmount) currentAmount = 100;
        }
        else if(currentAmount == 1000) {
            if(currentAmount != minAmount) currentAmount = 500;
        }
        else if(currentAmount == 2000) {
            if(currentAmount != minAmount) currentAmount = 1000;
        }
        else if(currentAmount == 5000) {
            if(currentAmount != minAmount) currentAmount = 2000;
        }

        return currentAmount;
    }

    public int getAmount() {
        return currentAmount;
    }

    public void setMinAmount(int min) {
        minAmount = min;
    }
}
