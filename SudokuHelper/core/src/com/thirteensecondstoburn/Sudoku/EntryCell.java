package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Nick on 12/31/2014.
 */
public class EntryCell extends Actor {
    private int index;
    private Data data;
    private TextureRegion backgroundTextureRegion;
    private BitmapFont font;
    private Assets.DistanceFieldShader distanceFieldShader;

    public EntryCell(int index, Data data, Assets assets) {
        this.index = index;
        this.data = data;
        backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.CELL_BACKGROUND));
        this.font = assets.getFont();
        this.distanceFieldShader = assets.getDistanceFieldShader();
    }

    public int getIndex() { return index; }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.draw(backgroundTextureRegion, getX(), getY(), getOriginX(),
                getOriginY(), getWidth(), getHeight(), getScaleX(),
                getScaleY(), getRotation());

        if(data.getNumber(index) > 0) {
            drawBigFont(batch, font, "" + data.getNumber(index));
        }
    }

    private void drawBigFont(Batch spriteBatch, BitmapFont font, String text) {
        font.setScale(1.0f);
        float scale = (getHeight()) / (font.getBounds(text).height);

        spriteBatch.setShader(distanceFieldShader);

        font.setScale(scale);
        font.setColor(SudokuGame.settings.numberForgroundColor);

        BitmapFont.TextBounds bounds = font.getBounds(text);
        int x = (int) ((getWidth() - bounds.width) / 2);
        int y = (int) ((getHeight() - bounds.height) / 2);

        distanceFieldShader.setSmoothing( 1f / 8f / scale);
        font.draw(spriteBatch, text, getX() + x, getY() - y + getHeight() + getHeight()*.15f);
        spriteBatch.setShader(null);
    }
}
