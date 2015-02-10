package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.thirteensecondstoburn.Sudoku.Assets.TEX_NAME;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class SplashScreen implements Screen {
	private SudokuGame game;
	private Assets assets;
	private Image splashImage;
	private Stage stage;
	private Sprite background;
	
	public SplashScreen(SudokuGame game) {
		this.game = game;
		assets = game.getAssets();
		//stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage();
	}
	
	@Override
	public void render(float delta) {
		stage.act(delta);
		
		Gdx.gl.glClearColor(173f/255f, 216f/255f, 230f/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        SpriteBatch batch = stage.getSpriteBatch();
        Batch batch = stage.getBatch();

		batch.getProjectionMatrix().setToOrtho2D(0, 0, stage.getWidth(), stage.getHeight());
		batch.begin();
		background.draw(batch);
		batch.end();
		
		batch.setProjectionMatrix(stage.getCamera().combined);

		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		background.setSize(width, height);
        stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		TextureRegion region = new TextureRegion(assets.getTexture(TEX_NAME.SPLASH), 270, 480);
		splashImage = new Image(new TextureRegionDrawable(region), Scaling.fit);
		splashImage.setFillParent(true);
		splashImage.addAction( sequence(delay( 1.75f ), fadeOut( 0.75f ),
	            new Action() {
	                @Override
	                public boolean act(float delta )
	                {
	                    // the last action will move to the next screen
	                    game.setScreen( game.getMainMenuScreen() );
	                    return true;
	                }
	            } ) );

        // and finally we add the actor to the stage
        stage.addActor( splashImage );
        
		Texture back = assets.getTexture(TEX_NAME.CELL_BACKGROUND);
		back.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
		background.setColor(SudokuGame.settings.backgroundColor);
		background.setSize(stage.getWidth(), stage.getHeight());

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
