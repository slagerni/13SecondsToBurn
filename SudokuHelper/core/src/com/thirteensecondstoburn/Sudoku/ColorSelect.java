package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ColorSelect extends Actor {
	private TextureRegion backgroundTextureRegion;

	public static final Color[] AColors = new Color[] {Color.RED, 
			 Color.GREEN, 
			 Color.BLUE, 
			 Color.YELLOW,
			 Color.valueOf("8A2BE2FF"),
			 Color.valueOf("B8860BFF"), 
			 Color.valueOf("A52A2AFF")};
	public static final Color[] BColors = new Color[] {Color.valueOf("FFC0CBFF"), 
			 Color.valueOf("90EE90FF"), 
			 Color.CYAN, 
			 Color.valueOf("F0E68CFF"), 
			 Color.valueOf("DDA0DDFF"),
			 Color.valueOf("FF8C00FF"), 
			 Color.valueOf("DEB887FF")};
	
	private static int index = 0;
	
	public ColorSelect(Assets assets) {
		backgroundTextureRegion = new TextureRegion(assets.getTexture(Assets.TEX_NAME.CELL_BACKGROUND));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(AColors[index]);
		batch.draw(backgroundTextureRegion, getX() + 4, getY(), getOriginX(),
				getOriginY(), getWidth()/2 - 4, getHeight(), getScaleX(),
				getScaleY(), getRotation());
		batch.setColor(BColors[index]);
		batch.draw(backgroundTextureRegion, getX() + 4 + getWidth()/2, getY(), getOriginX(),
				getOriginY(), getWidth()/2 - 4, getHeight(), getScaleX(),
				getScaleY(), getRotation());
	}

	public void cycle() {
		index++;
		if(index >= AColors.length) {
			index = 0;
		}
	}

	public static int getAColor() {
		return index + 1;
	}
	
	public static int getBColor() {
		return -(index + 1);
	}
}
