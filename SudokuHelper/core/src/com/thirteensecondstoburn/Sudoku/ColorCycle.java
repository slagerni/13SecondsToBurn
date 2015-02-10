package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ColorCycle extends Actor {
	private TextureRegion backgroundTextureRegion;
	private BitmapFont font;
	private Assets.DistanceFieldShader distanceFieldShader;
	private float fontScale = 1.5f;
	private Data data;
	
	public ColorCycle(Data data, Assets assets) {
		backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.CELL_BACKGROUND));
		this.font = assets.getFont();
		this.distanceFieldShader = assets.getDistanceFieldShader();
		this.data = data;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
        String number = "" + (ColoringCell.getPossibility() + 1);
        float height = getHeight();
        fontScale = height / 50f;
		font.setScale(fontScale);
		float selectWidth = font.getBounds("Select").width;
        int startX = (int)(getWidth() - selectWidth - height - 15f);
        batch.draw(backgroundTextureRegion, getX() + selectWidth + 5 + startX, getY(), getOriginX(),
                getOriginY(), height, height, getScaleX(),
                getScaleY(), getRotation());
		drawFont(batch, font, "Select", startX, 0);

        float numWidth = font.getBounds(number).width;
		drawFont(batch, font, number, (int)(startX + selectWidth + 5 + (height - numWidth)/2), 0);
	}

	private void drawFont(Batch spriteBatch, BitmapFont font, String text, int x, int y) {
		spriteBatch.setShader(distanceFieldShader);

		//font.setScale(fontScale);
		font.setColor(Color.BLACK);

		distanceFieldShader.setSmoothing( 1f / 8f / fontScale);
		font.draw(spriteBatch, text, getX() + x, getY() - y + getHeight() - getHeight()*.1f);
		spriteBatch.flush();
		spriteBatch.setShader(null);
	}
	
	
	public void cycle() {		
		ColoringCell.setPossibility(ColoringCell.getPossibility() + 1);
		data.setColors(ColoringCell.getPossibility(), false);
	}
}
