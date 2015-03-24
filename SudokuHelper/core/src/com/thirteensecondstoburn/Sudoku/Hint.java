package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Hint extends Actor {
	private TextureRegion backgroundTextureRegion;
	private BitmapFont font;
	Assets.DistanceFieldShader distanceFieldShader;
	private HumanSolver solver;
	private Data data;
    private SudokuGame game;

	public Hint (HumanSolver solver, SudokuGame game) {
        this.game = game;
		this.font = game.getAssets().getFont();
		this.distanceFieldShader = game.getAssets().getDistanceFieldShader();
		backgroundTextureRegion = new TextureRegion(game.getAssets().getTexture(Assets.TEX_NAME.HINT));
		this.solver = solver;
		this.data = game.getData();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(backgroundTextureRegion, getX(), getY(), getOriginX(),
				getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
		drawFont(batch, font, "Hint");		
	}
	
	private void drawFont(Batch Batch, BitmapFont font, String text) {
		font.setScale(6f);
		float scale = (getHeight()) / (font.getBounds(text).height);
		
		Batch.setShader(distanceFieldShader);

		font.setScale(scale);
		font.setColor(Color.BLACK);

		TextBounds bounds = font.getBounds(text); 
		int x = (int) ((getWidth() - bounds.width) / 2);
//		int y = (int) ((getHeight() - bounds.height - 4f) / 2);

		distanceFieldShader.setSmoothing( 1f / 8f / scale);
		font.draw(Batch, text, getX() + x, getY() + getHeight()*.25f);
		Batch.flush();
		Batch.setShader(null);
	}
	
	public void showHint() {
		data.setStatusText(solver.NextMove(false));
	}

    public SudokuGame getGame() {return game;}
}
