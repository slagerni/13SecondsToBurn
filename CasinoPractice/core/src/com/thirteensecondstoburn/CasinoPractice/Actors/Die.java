package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Nick on 4/16/2015.
 */
public class Die extends Actor {
    private CasinoPracticeGame game;
    private Assets assets;
    private HashMap<Integer, Texture> faceTexture = new HashMap<Integer, Texture>();

    private Random rand = new Random();
    private int rolledNumber = 1;

    public Die(CasinoPracticeGame game) {
        this.game = game;
        assets = game.getAssets();
        this.setWidth(100);
        this.setHeight(100);
        setBounds(0, 0, getWidth(), getHeight());

        faceTexture.put(1, assets.getTexture(Assets.TEX_NAME.DIEFACE1));
        faceTexture.put(2, assets.getTexture(Assets.TEX_NAME.DIEFACE2));
        faceTexture.put(3, assets.getTexture(Assets.TEX_NAME.DIEFACE3));
        faceTexture.put(4, assets.getTexture(Assets.TEX_NAME.DIEFACE4));
        faceTexture.put(5, assets.getTexture(Assets.TEX_NAME.DIEFACE5));
        faceTexture.put(6, assets.getTexture(Assets.TEX_NAME.DIEFACE6));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Image card = new Image(faceTexture.get(rolledNumber));
        card.setPosition(this.getX(), this.getY());
        card.setOrigin(card.getWidth() / 2, card.getHeight() / 2); // origin sets the location to rotate around
        card.setRotation(this.getRotation());
        card.setScaleX(this.getScaleX());
        card.setScaleY(this.getScaleY());
        card.draw(batch, parentAlpha);
    }

    public void rollDie() {
        rolledNumber = rand.nextInt(6) + 1;
    }

    public int getRolledNumber() { return rolledNumber; }
}
