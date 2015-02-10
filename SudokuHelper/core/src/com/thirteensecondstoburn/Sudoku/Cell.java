package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Cell extends Actor {
	private Data data;
	private int index;
	private TextureRegion backgroundTextureRegion;
	private TextureRegion possibleHighlightRegion;
	private TextureRegion select;
	private TextureRegion remove;
	private BitmapFont font;
	private ZoomOut zoomOut;
	private Assets.DistanceFieldShader distanceFieldShader;
	
	public Cell(int index, Data data, Assets assets, ZoomOut zoomOut) {
		this.index = index;
		this.data = data;
		backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.CELL_BACKGROUND));
		possibleHighlightRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.CELL_BACKGROUND));
		select = new TextureRegion(assets.getTexture(Assets.TEX_NAME.SELECTION));
		remove = new TextureRegion(assets.getTexture(Assets.TEX_NAME.NOT_POSSIBLE));
		this.font = assets.getFont();
		this.distanceFieldShader = assets.getDistanceFieldShader();
		this.zoomOut = zoomOut;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		if(!data.isSelectedPossibilitiesOnly() && data.numberOfPossibilities(index) == 1) {
			batch.setColor(SudokuGame.settings.singlePossibilityColor);			
		} else { 
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		}
		batch.draw(backgroundTextureRegion, getX(), getY(), getOriginX(),
				getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
		if(data.getNumber(index) > 0) {
			drawBigFont(batch, font, "" + data.getNumber(index));
		} else {
			for (int i=0; i<9; ++i) {
				float x = getX() + (i%3)*(int)getWidth()/3;
				float y = getY() + getHeight()*2/3 - (i/3)*(int)getHeight()/3;
				if(data.getPossible(index)[i]) {
					if(data.isHighlight(i) && !data.isSelectedPossibilitiesOnly()){
						batch.setColor(SudokuGame.settings.numberHighlightColors[i]);
						batch.draw(possibleHighlightRegion, x + 2, y + 2, getWidth()/3 - 2, getHeight()/3 - 2);
					}
					
					if((!data.isSelectedPossibilitiesOnly() || data.getMarked(index, i) != Data.MarkType.NONE || (data.isSelectedPossibilitiesOnly() && data.isHighlight(i))) && data.getPossible(index)[i]) {
						drawSmallFont(batch, font, "" + (i + 1), (i%3)*(int)getWidth()/3, (i/3)*(int)getHeight()/3 + 2);
					}
					if(data.getMarked(index, i) == Data.MarkType.SELECT) {
						batch.setColor(SudokuGame.settings.numberHighlightColors[i]);
						batch.draw(select, x, y, getWidth()/3, getHeight()/3);
					} else if(data.getMarked(index, i) == Data.MarkType.REMOVE) {
						batch.setColor(SudokuGame.settings.numberHighlightColors[i]);
						batch.draw(remove, x, y, getWidth()/3, getHeight()/3);
					}
				}
			}
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
		//spriteBatch.flush();
		spriteBatch.setShader(null);
	}

	private void drawSmallFont(Batch spriteBatch, BitmapFont font, String text, int x, int y) {
		font.setScale(1.0f);
		
		TextBounds bounds = font.getBounds(text);

		float scale = (getHeight()) / (bounds.height);
		scale = scale / 3f;
		
		spriteBatch.setShader(distanceFieldShader);

		font.setScale(scale);
		bounds = font.getBounds(text);
		font.setColor(SudokuGame.settings.numberForgroundColor);

		//System.out.println(bounds.width + ":" + bounds.height + " - " + getWidth() + ":" + getHeight());		
		
		distanceFieldShader.setSmoothing( 1f / 8f / scale);
		font.draw(spriteBatch, text, getX() + x + (getWidth()/3 - bounds.width)/2, getY() - y + getHeight() + getHeight()*.10f);
		//spriteBatch.flush();
		spriteBatch.setShader(null);
	}
	
	public void setZoom() {
		data.setZoom(index);
		zoomOut.setVisible(isZoomedTo());
	}
	
	public int getZoomedTo() {
		return data.getZoom();
	}
	
	public boolean isZoomedTo() {
		return data.getZoom() == index;
	}
	
	public int getNumber() {
		return data.getNumber(index);
	}
	
	public void setNumber(int number) {
		data.setNumber(index, number);
	}
	
	public void togglePossible(int number) {
		data.togglePossible(index, number);
	}
	
	public boolean isPossible(int number) {
		return data.getPossible(index)[number];
	}
	
	public int getOnlyPossibility() {
		return data.getOnlyPossibility(index);
	}
}
