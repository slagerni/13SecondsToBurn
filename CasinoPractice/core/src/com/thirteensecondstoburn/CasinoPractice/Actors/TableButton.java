package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Assets;

/**
 * Created by Nick on 1/17/2015.
 */
public class TableButton extends Actor {
    private Assets assets;
    private String text;
    private Color color;
    private boolean isUp = true;
    private Image backgroundDown;
    private Image backgroundUp;
    private float textScale = 2.0f;

    public static int BUTTON_WIDTH = 256;
    public static int BUTTON_HEIGHT = 256;

    public TableButton(Assets assets, String text, Color color) {
        this.assets = assets;
        this.text = text;
        this.color = color;
        setWidth(BUTTON_WIDTH);
        setHeight(BUTTON_HEIGHT);

        backgroundDown = new Image(assets.getTexture(Assets.TEX_NAME.BUTTON_BLANK_DOWN));
        backgroundUp = new Image(assets.getTexture(Assets.TEX_NAME.BUTTON_BLANK));

        addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isUp = true;
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isUp = false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Image background = backgroundDown;
        if(isUp) background = backgroundUp;

        background.setColor(color);
        background.setPosition(this.getX(), this.getY());
        background.draw(batch, parentAlpha);

        drawFont(batch, assets.getFont(), text);
    }

    public void setTextScale(float val) {
        textScale = val;
    }

    private void drawFont(Batch Batch, BitmapFont font, String text) {
        font.setScale(textScale);

        Batch.setShader(assets.getDistanceFieldShader());
        font.setColor(Color.BLACK);

        BitmapFont.TextBounds bounds = font.getBounds(text);
        int x = (int) ((getWidth() - bounds.width) / 2);
        int y = (int) ((getHeight() - bounds.height) / 2);

        assets.getDistanceFieldShader().setSmoothing( 1f / 8f / textScale);
        font.draw(Batch, text, getX() + x, getY() - y + getHeight() + getHeight()*.05f);
        Batch.flush();
        Batch.setShader(null);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isInside(float x, float y) {
        if(x > 0 && x < getWidth()
                && y > 0 && y < getHeight()) {
            return true;
        }
        return false;
    }
}
