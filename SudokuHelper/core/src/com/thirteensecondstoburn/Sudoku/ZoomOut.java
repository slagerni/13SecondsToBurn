package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ZoomOut extends Actor {
	private TextureRegion backgroundTextureRegion;

	public ZoomOut(Assets assets) {
		backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.ZOOM_OUT));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(backgroundTextureRegion, getX(), getY(), getOriginX(),
				getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
	}
}
