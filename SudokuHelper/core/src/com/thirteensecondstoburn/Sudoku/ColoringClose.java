package com.thirteensecondstoburn.Sudoku;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ColoringClose extends Actor {
	private TextureRegion backgroundTextureRegion;
	private BitmapFont font;
	private Assets.DistanceFieldShader distanceFieldShader;

	ArrayList<ColoringListener> listeners = new ArrayList<ColoringListener>();
	
	public ColoringClose(Assets assets) {
		backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.CLOSE));
		this.font = assets.getFont();
		this.distanceFieldShader = assets.getDistanceFieldShader();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(getColor());
        float height = getHeight();
		font.setScale(height / 50f);
		batch.draw(backgroundTextureRegion, getX() + 4, getY(), getOriginX(),
				getOriginY(), height, height, getScaleX(),
				getScaleY(), getRotation());
		drawFont(batch, font, "Close", 4 + (int)height, 0);
	}

	private void drawFont(Batch spriteBatch, BitmapFont font, String text, int x, int y) {

//        float scale = (getHeight()) / (font.getBounds(text).height);
        float scale = getHeight() / 50f;

		spriteBatch.setShader(distanceFieldShader);

		font.setScale(scale);
		font.setColor(Color.BLACK);

		distanceFieldShader.setSmoothing( 1f / 8f / scale);
		font.draw(spriteBatch, text, getX() + x, getY() - y + getHeight() - getHeight()*.1f);
		spriteBatch.flush();
		spriteBatch.setShader(null);
	}
	
	public void close() {		
		OnChanged();
	}

	public void addListener(ColoringListener toAdd) {
		listeners.add(toAdd);
	}

	protected void OnChanged() {
		if (listeners.size() > 0) {
			for(ColoringListener ccl : listeners) {
				ccl.ColoringClosed();
			}
		}
	}
}
