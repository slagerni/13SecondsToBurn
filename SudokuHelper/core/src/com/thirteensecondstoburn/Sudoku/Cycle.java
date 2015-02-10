package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Cycle extends Actor {
	private int currentCycle = -1;
	private TextureRegion backgroundTextureRegion;
	private Data data;
	private BitmapFont font;
	Assets.DistanceFieldShader distanceFieldShader;

	public Cycle(Data data, Assets assets) {
		backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.CYCLE));
		this.data = data;
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
		drawFont(batch, font, "Cycle");
	}
	
	private void drawFont(Batch Batch, BitmapFont font, String text) {
		font.setScale(7f);
		float scale = (getHeight()) / (font.getBounds(text).height);
		
		Batch.setShader(distanceFieldShader);

		font.setScale(scale);
		font.setColor(Color.BLACK);

		TextBounds bounds = font.getBounds(text); 
		int x = (int) ((getWidth() - bounds.width) / 2);
		int y = (int) ((getHeight() - bounds.height - 4f) / 2);

		distanceFieldShader.setSmoothing( 1f / 8f / scale);
		font.draw(Batch, text, getX() + x, getY() - y + getHeight() + getHeight()*.05f);
		Batch.flush();
		Batch.setShader(null);
	}

	public void doCycle() {
		if(currentCycle > -1) {
			if(data.isHighlight(currentCycle)) {
				data.toggleHighlight(currentCycle);
			} 
		}
		currentCycle++;
		if(currentCycle < 9) {
			if(!data.isHighlight(currentCycle)) {
				data.toggleHighlight(currentCycle);
			}
		} else {
			currentCycle = -1;
		}		
	}
}
