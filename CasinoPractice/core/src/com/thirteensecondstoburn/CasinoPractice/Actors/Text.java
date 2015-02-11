package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    boolean showBackground = false;

    Texture backgroundTex;

    public Text (Assets assets,String text, float scale) {
        this(assets, text, scale, false);
    }
    public Text (Assets assets,String text, float scale, boolean showBackground) {
        this.assets = assets;
        this.text = text;
        this.scale = scale;

        this.showBackground = showBackground;
        backgroundTex = assets.getTexture(Assets.TEX_NAME.BLACK_50_ALPHA);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawFont(batch, assets.getFont());
    }

    private void drawFont(Batch batch, BitmapFont font) {
        font.setScale(scale);

        batch.setShader(assets.getDistanceFieldShader());
        font.setColor(this.getColor());

        if(showBackground) {
            BitmapFont.TextBounds bounds = font.getBounds(text);
            batch.setColor(Color.BLACK);
            batch.draw(backgroundTex, getX(), getY() - 10 - bounds.height, bounds.width + 10, bounds.height + 10);
        }

        assets.getDistanceFieldShader().setSmoothing(1f / 8f / scale);
        font.draw(batch, text, getX() + 5, getY());
        batch.flush();
        batch.setShader(null);
    }

    public void setText(String text) {
        this.text = text;
    }
}
