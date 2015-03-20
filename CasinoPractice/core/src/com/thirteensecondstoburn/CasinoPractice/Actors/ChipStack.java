package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nick on 1/16/2015.
 */
public class ChipStack extends Actor {
    public static int CHIP_WIDTH = 100;
    public static int CHIP_HEIGHT = 71;
    public static int CHIP_BASE_HEIGHT = 10;
    int stackTotal = 0;

    ArrayList<Integer> chipStack = new ArrayList<Integer>();

    HashMap<Integer, TextureRegion> regions = new HashMap<Integer, TextureRegion>();

    CasinoPracticeGame game;
    Assets assets;

    public ChipStack(CasinoPracticeGame game, int stackTotal) {
        this.stackTotal = stackTotal;
        calculateChips();
        this.assets = game.getAssets();
        regions.put(1, assets.getChipTexture("1"));
        regions.put(5, assets.getChipTexture("5"));
        regions.put(25, assets.getChipTexture("25"));
        regions.put(100, assets.getChipTexture("100"));
        regions.put(500, assets.getChipTexture("500"));
        regions.put(1000, assets.getChipTexture("1000"));
        regions.put(2000, assets.getChipTexture("2000"));
        regions.put(5000, assets.getChipTexture("5000"));

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int offset = 0;
        for(Integer index : chipStack) {
            Image chip = new Image(regions.get(index));
            chip.setPosition(this.getX() + 25, this.getY() + 25 + offset);
            chip.draw(batch, parentAlpha);
            offset += CHIP_BASE_HEIGHT;
        }
    }

    public void increaseTotal(int amount) {
        stackTotal += amount;
        if (stackTotal > game.getTableMaximum()) stackTotal = game.getTableMaximum();
        calculateChips();
    }

    public void clearTotal() {
        stackTotal = 0;
        calculateChips();
    }

    public int getTotal() {
        return stackTotal;
    }
    public void setTotal(int total) {
        stackTotal = total;
        calculateChips();
    }

    private void calculateChips() {
        chipStack.clear();
        int current = stackTotal;
        while (current > 0) {
            if (current >= 5000) {
                chipStack.add(5000);
                current -= 5000;
            } else if (current >= 2000) {
                chipStack.add(2000);
                current -= 2000;
            } else if (current >= 1000) {
                chipStack.add(1000);
                current -= 1000;
            } else if (current >= 500) {
                chipStack.add(500);
                current -= 500;
            } else if (current >= 100) {
                chipStack.add(100);
                current -= 100;
            } else if (current >= 25) {
                chipStack.add(25);
                current -= 25;
            } else if (current >= 5) {
                chipStack.add(5);
                current -= 5;
            } else if (current >= 1) {
                chipStack.add(1);
                current -= 1;
            }
        }

        setWidth(CHIP_WIDTH + 50);
        int height = CHIP_HEIGHT + chipStack.size() * CHIP_BASE_HEIGHT;
        if(height < 150 ) height = 150;
        setHeight(150);
    }

    public boolean isInside(float x, float y) {
        if(x > 0 && x < getWidth()
                && y > 0 && y < getHeight()) {
            return true;
        }
        return false;
    }
}
