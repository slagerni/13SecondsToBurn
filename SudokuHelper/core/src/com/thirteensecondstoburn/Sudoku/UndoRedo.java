package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class UndoRedo extends Actor {
	boolean isUndo;
	private TextureRegion backgroundTextureRegion;
	private Data data;
	private BitmapFont font;
	Assets.DistanceFieldShader distanceFieldShader;

	public UndoRedo (boolean isUndo, Data data, Assets assets) {
		this.isUndo = isUndo;
		this.data = data;
		this.font = assets.getFont();
		this.distanceFieldShader = assets.getDistanceFieldShader();
		if(isUndo) {
			backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.UNDO));
		} else {
			backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.REDO));
		}

	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(backgroundTextureRegion, getX(), getY(), getOriginX(),
				getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
		if(isUndo ) {
			drawFont(batch, font, "Undo");
		} else {
			drawFont(batch, font, "Redo");
		}
		
	}
	
	private void drawFont(Batch Batch, BitmapFont font, String text) {
		font.setScale(6f);
		float scale = (getHeight()) / (font.getBounds(text).height);
		
		Batch.setShader(distanceFieldShader);

		font.setScale(scale);
		font.setColor(Color.BLACK);

		TextBounds bounds = font.getBounds(text); 
		int x = (int) ((getWidth() - bounds.width) / 2);
		int y = (int) ((getHeight() - bounds.height + getHeight()*.05f) / 2);

		distanceFieldShader.setSmoothing( 1f / 8f / scale);
		font.draw(Batch, text, getX() + x, getY() - y + getHeight());
		Batch.flush();
		Batch.setShader(null);
	}
	
	public void undoRedo() {
        if(data.unsolvedCount() == 0) return;

		if(isUndo) {
			data.undo();			
		} else {
			data.redo();
		}
	}
	
	public boolean isUndo() {
		return isUndo;
	}
}
