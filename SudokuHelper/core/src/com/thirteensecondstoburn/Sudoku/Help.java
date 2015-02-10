package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Help extends Actor {
	private TextureRegion backgroundTextureRegion;
	private BitmapFont font;
	Assets.DistanceFieldShader distanceFieldShader;

	public Help (Assets assets) {
		this.font = assets.getFont();
		this.distanceFieldShader = assets.getDistanceFieldShader();
		backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.HELP));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(backgroundTextureRegion, getX(), getY(), getOriginX(),
				getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
		drawFont(batch, font, "Help");		
	}
	
	private void drawFont(Batch Batch, BitmapFont font, String text) {
		font.setScale(6f);
		float scale = (getHeight()) / (font.getBounds(text).height);
		
		Batch.setShader(distanceFieldShader);

		font.setScale(scale);
		font.setColor(Color.BLACK);

		TextBounds bounds = font.getBounds(text); 
		int x = (int) ((getWidth() - bounds.width) / 2);

		distanceFieldShader.setSmoothing( 1f / 8f / scale);
		font.draw(Batch, text, getX() + x, getY() + getHeight()*.25f);
		Batch.flush();
		Batch.setShader(null);
	}
}
