package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ColoringCell extends Actor {
	private int index;
	private Data data;
	private static int possibility;
	public enum ColorState {A, B, NONE};
	private ColorState state = ColorState.NONE;
	private TextureRegion backgroundTextureRegion;
	private BitmapFont font;
	private Assets.DistanceFieldShader distanceFieldShader;

	public ColoringCell(int index, Data data, Assets assets) {
		this.index = index;
		this.data = data;
		backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.CELL_BACKGROUND));
		this.font = assets.getFont();
		this.distanceFieldShader = assets.getDistanceFieldShader();
	}
	
	public static int getPossibility() {
		return possibility; 
	}
	public static void setPossibility(int value) {
		if(value > 8) value = 0;
		possibility = value; 
	}

	public void setColorState(ColorState value) { 
		state = value;
	}
	
	public ColorState getColorState() {
		return state;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		if(data.getColors()[index] == 0 || data.getNumber(index) > 0) {
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		} else {
			if(data.getColors()[index] > 0) {
				batch.setColor(ColorSelect.AColors[data.getColors()[index] - 1]);
			} else {
				batch.setColor(ColorSelect.BColors[Math.abs(data.getColors()[index]) - 1]);
			}
		}

		batch.draw(backgroundTextureRegion, getX(), getY(), getOriginX(),
				getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
		
		if(data.getPossible(index)[possibility] && !(data.getNumber(index) > 0)) {
			drawBigFont(batch, font, "" + (possibility + 1));
		}
	}

	private void drawBigFont(Batch spriteBatch, BitmapFont font, String text) {
		font.setScale(1.0f);
		float scale = (getHeight()) / (font.getBounds(text).height);
		
		spriteBatch.setShader(distanceFieldShader);

		font.setScale(scale);
		font.setColor(SudokuGame.settings.numberForgroundColor);

		TextBounds bounds = font.getBounds(text); 
		int x = (int) ((getWidth() - bounds.width) / 2);
		int y = (int) ((getHeight() - bounds.height) / 2);

		distanceFieldShader.setSmoothing( 1f / 8f / scale);
		font.draw(spriteBatch, text, getX() + x, getY() - y + getHeight() + getHeight()*.15f);
		spriteBatch.setShader(null);
	}

	public void cycle() {
		if(!data.getPossible(index)[possibility]) {
			data.togglePossible(index, possibility);
			data.getColors()[index] = 0;
		} else if(data.getColors()[index] == 0) {
			data.getColors()[index] = ColorSelect.getAColor();
		} else if(data.getColors()[index] > 0) {
			data.getColors()[index] = ColorSelect.getBColor();
		} else if(data.getColors()[index] < 0) {
			data.togglePossible(index, possibility);		
			data.getColors()[index] = 0;
		}
	
	}
}
