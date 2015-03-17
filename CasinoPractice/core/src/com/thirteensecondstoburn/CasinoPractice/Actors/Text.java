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
    BitmapFont font;
    BitmapFont.TextBounds bounds;

    Texture backgroundTex;

    public Text (Assets assets,String text, float scale) {
        this(assets, text, scale, false);
    }
    public Text (Assets assets,String text, float scale, boolean showBackground) {
        this.assets = assets;
        this.text = text;
        this.scale = scale;

        this.font = assets.getFont();
        font.setScale(scale);
        bounds = font.getBounds(text);

        this.setWidth(bounds.width);
        this.setHeight(bounds.height);

        this.showBackground = showBackground;
        backgroundTex = assets.getTexture(Assets.TEX_NAME.BLACK_50_ALPHA);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawFont(batch, parentAlpha);
    }

    private void drawFont(Batch batch, float parentAlpha) {

        batch.setShader(assets.getDistanceFieldShader());
        font.setColor(this.getColor());

        if(showBackground) {
            batch.setColor(Color.BLACK);
            batch.draw(backgroundTex, getX(), getY() - 10, bounds.width + 10, bounds.height + 10);
        }

        assets.getDistanceFieldShader().setSmoothing(1f / 8f / scale);
        font.draw(batch, text, getX() + 5, getY() + bounds.height);
        batch.flush();
        batch.setShader(null);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextCentered(String text) {
        setText(text);
        BitmapFont.TextBounds bounds = font.getBounds(text);
        setSize(bounds.width * scale + 10, bounds.height * scale + 10);
        setPosition((getStage().getWidth() - bounds.width + 10)/2f, (getStage().getHeight() + bounds.height + 10)/2f );
    }
}
