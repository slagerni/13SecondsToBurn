package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Nick on 9/30/2015.
 */
public class RouletteNumberBoard extends Actor {
    private LinkedList<Integer> lastNumbers = new LinkedList<>();
    Assets assets;
    Image board;
    protected float textScale = 1.25f;
    ArrayList<Integer> redNumbers = new ArrayList<>(Arrays.asList(new Integer[]{1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36}));
    ArrayList<Integer> blackNumbers = new ArrayList<>(Arrays.asList(new Integer[]{2,4,6,8,10,11,13,15,17,20,22,24,26,28,29,31,33,35}));

        public RouletteNumberBoard(Assets assets) {
        this.assets = assets;
        board = new Image(assets.getTexture(Assets.TEX_NAME.ROULETTE_NUMBER_BOARD));
        board.setPosition(this.getX(), this.getY());

        setWidth(board.getImageWidth());
        setHeight(board.getImageHeight());
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        board.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        board.draw(batch, parentAlpha);

        int position = 0;
        for (int number : lastNumbers) {
            drawFont(batch, assets.getFont(), number, position);
            position++;
        }
    }

    protected void drawFont(Batch batch, BitmapFont font, int number, int position) {

        font.setScale(textScale);

        batch.setShader(assets.getDistanceFieldShader());
        boolean isRed = redNumbers.contains(number);
        boolean isBlack = blackNumbers.contains(number);

        String text = "" + number;
        if(text.equals("37")) text = "00";

        BitmapFont.TextBounds bounds = font.getBounds(text);
        int x = 20 + 65 * position;
        int y;
        if(isBlack) {
            font.setColor(Color.YELLOW);
            y = -100;
        } else if(isRed) {
            font.setColor(Color.RED);
            y = -200;
        } else {
            font.setColor(Color.GREEN);
            y = -150;
        }

        assets.getDistanceFieldShader().setSmoothing( 1f / 8f / textScale);
        font.draw(batch, text, getX() + x, getY() - y + getHeight() + getHeight()*.05f);
        batch.flush();
        batch.setShader(null);
    }


    public void push(int number) {
        if(lastNumbers.size() >= 15) {
            lastNumbers.pop();
        }
        lastNumbers.add(number);
    }
}
