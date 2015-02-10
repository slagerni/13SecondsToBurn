package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;

/**
 * Created by Nick on 1/18/2015.
 */
public class Text extends Actor {
    Assets assets;
    String text;
    float scale;

    public Text (Assets assets,String text, float scale) {
        this.assets = assets;
        this.text = text;
        this.scale = scale;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawFont(batch, assets.getFont());
    }

    private void drawFont(Batch Batch, BitmapFont font) {
        font.setScale(scale);

        Batch.setShader(assets.getDistanceFieldShader());
        font.setColor(this.getColor());

        assets.getDistanceFieldShader().setSmoothing(1f / 8f / scale);
        font.draw(Batch, text, getX(), getY());
        Batch.flush();
        Batch.setShader(null);
    }

    public void setText(String text) {
        this.text = text;
    }
}
