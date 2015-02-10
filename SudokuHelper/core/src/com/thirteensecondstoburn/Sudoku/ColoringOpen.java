package com.thirteensecondstoburn.Sudoku;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ColoringOpen extends Actor {
	private TextureRegion backgroundTextureRegion;
	private BitmapFont font;
	private Assets.DistanceFieldShader distanceFieldShader;
	ArrayList<ColoringListener> listeners = new ArrayList<ColoringListener>();

	public ColoringOpen(Assets assets) {
		backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.COLORING));
		this.font = assets.getFont();
		this.distanceFieldShader = assets.getDistanceFieldShader();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(backgroundTextureRegion, getX(), getY(), getOriginX(),
				getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
		drawFont(batch, font, "Coloring");
	}

	private void drawFont(Batch spriteBatch, BitmapFont font, String text) {
		font.setScale(6f);
		float scale = (getHeight()) / (font.getBounds(text).height);
		
		spriteBatch.setShader(distanceFieldShader);

		font.setScale(scale);
		font.setColor(Color.BLACK);

		TextBounds bounds = font.getBounds(text); 
		int x = (int) ((getWidth() - bounds.width) / 2);

		distanceFieldShader.setSmoothing( 1f / 8f / scale);
		font.draw(spriteBatch, text, getX() + x, getY() + getHeight()*.25f);
		spriteBatch.flush();
		spriteBatch.setShader(null);
	}
	
	public void open() {		
		OnChanged();
	}

	public void addListener(ColoringListener toAdd) {
		listeners.add(toAdd);
	}

	protected void OnChanged() {
		if (listeners.size() > 0) {
			for(ColoringListener ccl : listeners) {
				ccl.ColoringOpened();
			}
		}
	}
}
