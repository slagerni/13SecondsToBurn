package com.thirteensecondstoburn.Sudoku;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

public class Assets {
	public enum TEX_NAME {
		CELL_BACKGROUND, FONT, POSSIBILITY_HIGHLIGHT, NOT_POSSIBLE, SELECTION, CYCLE, ZOOM_OUT, 
		UNDO, REDO, HELP, HINT, CHECKED, UNCHECKED, CLOSE, COLORING, SPLASH, HOME,
	};
	
	private HashMap<TEX_NAME, Texture> textures = new HashMap<TEX_NAME, Texture>();

	TextureAtlas atlas;
	
	private BitmapFont font;
	public static class DistanceFieldShader extends ShaderProgram {
		public DistanceFieldShader() {
			super(Gdx.files.internal("data/shaders/distancefield.vert"),
					Gdx.files.internal("data/shaders/distancefield.frag"));
			if (!isCompiled()) {
				throw new RuntimeException("Shader compilation failed:\n"+ getLog());
			}
		}

		/**
		 * @param smoothing
		 *            a value between 0 and 1
		 */
		public void setSmoothing(float smoothing) {
			float delta = 0.5f * MathUtils.clamp(smoothing, 0, 1);
			setUniformf("u_lower", 0.5f - delta);
			setUniformf("u_upper", 0.5f + delta);
		}
	}
	private DistanceFieldShader distanceFieldShader;

	Assets() {
		Texture texture = new Texture(Gdx.files.internal("data/cellBackground.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textures.put(TEX_NAME.CELL_BACKGROUND, texture);
		
		texture = new Texture(Gdx.files.internal("data/ArialDistance.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.FONT, texture);
		
		texture = new Texture(Gdx.files.internal("data/possibilityHighlight.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.POSSIBILITY_HIGHLIGHT, texture);
		
		texture = new Texture(Gdx.files.internal("data/notPossible.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.NOT_POSSIBLE, texture);
		
		texture = new Texture(Gdx.files.internal("data/selection.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.SELECTION, texture);
		
		texture = new Texture(Gdx.files.internal("data/cycle.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.CYCLE, texture);
		
		texture = new Texture(Gdx.files.internal("data/zoomOut.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.ZOOM_OUT, texture);
		
		texture = new Texture(Gdx.files.internal("data/undo.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.UNDO, texture);
		
		texture = new Texture(Gdx.files.internal("data/redo.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.REDO, texture);
		
		texture = new Texture(Gdx.files.internal("data/help.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.HELP, texture);
		
		texture = new Texture(Gdx.files.internal("data/hint.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.HINT, texture);
		
		texture = new Texture(Gdx.files.internal("data/checked.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.CHECKED, texture);
		
		texture = new Texture(Gdx.files.internal("data/unchecked.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.UNCHECKED, texture);
		
		texture = new Texture(Gdx.files.internal("data/close.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.CLOSE, texture);		
		
		texture = new Texture(Gdx.files.internal("data/coloring.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.COLORING, texture);
		
		texture = new Texture(Gdx.files.internal("data/splash.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.SPLASH, texture);
		
		texture = new Texture(Gdx.files.internal("data/mainMenu.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		textures.put(TEX_NAME.HOME, texture);
		
		font = new BitmapFont(Gdx.files.internal("data/ArialDistance.fnt"), new TextureRegion(textures.get(TEX_NAME.FONT)), false);
		ShaderProgram fontShader = new ShaderProgram(Gdx.files.internal("data/shaders/distancefield.vert"), Gdx.files.internal("data/shaders/distancefield.frag"));
		if (!fontShader.isCompiled()) {
		    Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
		}
		
		distanceFieldShader = new DistanceFieldShader();

		atlas = new TextureAtlas(Gdx.files.internal("data/title.atlas"));
	}
	
	public void dispose() {
		for (Texture t : textures.values()) {
			t.dispose();
		}
		font.dispose();
		distanceFieldShader.dispose();
		atlas.dispose();
	}
	
	public BitmapFont getFont() {
		return font;
	}
	
	public DistanceFieldShader getDistanceFieldShader() {
		return distanceFieldShader;
	}
	
	public Texture getTexture(TEX_NAME name) {
		return textures.get(name);
	}
	
	public TextureRegion getAtlasTexture(String name) {
		return atlas.findRegion(name);
	}
}
