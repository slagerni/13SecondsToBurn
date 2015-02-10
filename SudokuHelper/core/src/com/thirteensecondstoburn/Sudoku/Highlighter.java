package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Highlighter extends Actor {
	private Data data;
	private int index;
	private TextureRegion backgroundTextureRegion;
	private BitmapFont font;

	Assets.DistanceFieldShader distanceFieldShader;

	public Highlighter(int index, Data data, Assets assets) {
		this.index = index;
		this.data = data;
		backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.CELL_BACKGROUND));
		this.font = assets.getFont();
		this.distanceFieldShader = assets.getDistanceFieldShader();
	}

	public int getIndex() {
		return index;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		if(data.isHighlight(index) ) {
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		} else {
			batch.setColor(1f, 1f, 1f, 1f);
		}

		batch.draw(backgroundTextureRegion, getX(), getY(), getOriginX(),
				getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
		drawBigFont(batch, font, "" + (index + 1), (int) (getX() + 4), (int) (getY()));
	}

	private void drawBigFont(Batch spriteBatch, BitmapFont font, String text, int x, int y) {

		font.setScale(1.0f);
		float scale = (getHeight()) / (font.getBounds(text).height);

		spriteBatch.setShader(distanceFieldShader);

		font.setScale(scale);
		font.setColor(Color.BLACK);

		TextBounds bounds = font.getBounds(text);
		x = (int) ((getWidth() - bounds.width) / 2f);
		y = (int) ((getHeight() - bounds.height) / 2f);

		distanceFieldShader.setSmoothing( 1f / 8f / scale);
        font.draw(spriteBatch, text, getX() + x, getY() + getHeight() - y + getHeight()/6);
		spriteBatch.flush();
		spriteBatch.setShader(null);
	}
	
	public void toggle() {		
		data.toggleHighlight(index);
	}
}
