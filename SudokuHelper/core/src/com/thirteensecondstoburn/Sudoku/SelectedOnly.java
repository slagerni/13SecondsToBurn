package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SelectedOnly extends Actor {
	private Data data;
	private TextureRegion checkedTextureRegion;
	private TextureRegion uncheckedTextureRegion;
	private BitmapFont font;

	Assets.DistanceFieldShader distanceFieldShader;
	
	public SelectedOnly(Data data, Assets assets) {
		this.data = data;
		checkedTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.CHECKED));
		uncheckedTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.UNCHECKED));
		this.font = assets.getFont();
		this.distanceFieldShader = assets.getDistanceFieldShader();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float checkWidth = getHeight() - getHeight()*.05f;
		float checkHeight = getHeight() - getHeight()*.05f;
		if(data.isSelectedPossibilitiesOnly()) {
			batch.draw(checkedTextureRegion, getX(), getY(), getOriginX(),
				getOriginY(), checkWidth, checkHeight, getScaleX(),
				getScaleY(), getRotation());
		} else {
			batch.draw(uncheckedTextureRegion, getX(), getY(), getOriginX(),
					getOriginY(), checkWidth, checkHeight, getScaleX(),
					getScaleY(), getRotation());
		}
		drawFont(batch, font, "Selected Only", (int) (checkWidth + 1), 0);
	}

	private void drawFont(Batch Batch, BitmapFont font, String text, int x, int y) {

		font.setScale(3.5f);
		float scale = (getHeight()) / (font.getBounds(text).height);
		
		Batch.setShader(distanceFieldShader);

		font.setScale(scale);
		font.setColor(Color.BLACK);

		distanceFieldShader.setSmoothing( 1f / 8f / scale);
		font.draw(Batch, text, getX() + x, getY() - y + getHeight() - getHeight()*.35f);
		Batch.flush();
		Batch.setShader(null);
	}
	
	public void toggle() {		
		data.setSelectedPossibilitiesOnly(!data.isSelectedPossibilitiesOnly());
	}
}
