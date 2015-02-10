package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.thirteensecondstoburn.Sudoku.Assets.TEX_NAME;

public class StatusText extends Actor {
	Texture back;
	Assets.DistanceFieldShader distanceFieldShader;
	Data data;
	BitmapFont font;
	
	StatusText(Data data, Assets assets) {		
		back = assets.getTexture(TEX_NAME.CELL_BACKGROUND);
		back.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		this.data = data;
		this.font = assets.getFont();
		this.distanceFieldShader = assets.getDistanceFieldShader();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color);

		Sprite background = new Sprite(back, (int)getWidth(), (int)getHeight());
		background.setColor(SudokuGame.settings.cellBackgroundColor);
		background.setPosition(getX(), getY() + getHeight()*.05f);

		background.draw(batch);
		drawFont(batch, font, data.getStatusText());
	}
	
	private void drawFont(Batch Batch, BitmapFont font, String text) {
		font.setScale(1.5f);
		float scale = (getHeight()) / (font.getBounds(text).height);
		
		Batch.setShader(distanceFieldShader);

		font.setScale(scale);
		font.setColor(SudokuGame.settings.numberForgroundColor);

		TextBounds bounds = font.getBounds(text); 
		int x = 4;// (int) ((getWidth() - bounds.width) / 2);
		int y = (int) ((getHeight() - bounds.height) / 2);

		distanceFieldShader.setSmoothing( 1f / 8f / scale);
		font.draw(Batch, text, getX() + x, getY() - y + getHeight() + getHeight()*.15f);
		//Batch.flush();
		Batch.setShader(null);
	}
}
