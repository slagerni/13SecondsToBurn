package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.thirteensecondstoburn.CasinoPractice.Assets;

/**
 * Created by Nick on 8/28/2015.
 */
public class MultiLineTableButton extends TableButton {
    String regex = "\\|";

    public MultiLineTableButton(Assets assets, String text, Color color) {
        super(assets, text, color);
    }

    public MultiLineTableButton(Assets assets, String text, Color color, String regex) {
        super(assets, text, color);
        this.regex = regex;
    }

    @Override
    protected void drawFont(Batch batch, BitmapFont font, String text) {
        String[] lines = text.split(regex);
        if(lines.length == 1) {
            super.drawFont(batch, font, text);
            return;
        }

        for(int line=0; line < lines.length; ++line) {

            font.setScale(textScale);

            batch.setShader(assets.getDistanceFieldShader());
            font.setColor(Color.BLACK);

            BitmapFont.TextBounds bounds = font.getBounds(lines[line]);
            int x = (int) ((getWidth() - bounds.width) / 2);
            int y = (int) ((getHeight() / (lines.length + 1) * (line)+1) + bounds.height/2);

            assets.getDistanceFieldShader().setSmoothing(1f / 8f / textScale);
            font.draw(batch, lines[line], getX() + x, getY() - y + getHeight());
            batch.flush();
            batch.setShader(null);
        }
    }
}
