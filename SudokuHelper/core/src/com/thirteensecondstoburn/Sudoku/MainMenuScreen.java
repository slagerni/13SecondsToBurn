package com.thirteensecondstoburn.Sudoku;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.thirteensecondstoburn.Sudoku.Assets.TEX_NAME;
import com.thirteensecondstoburn.Sudoku.Generate.Difficulty;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public class MainMenuScreen implements Screen {
	private SudokuGame game;
	private Assets assets;
	private Stage stage;
	private Sprite background;
	private Skin skin;
	private Table table;
	private Generate generator = new Generate();
	private Difficulty selectedDifficulty = Difficulty.Any;
    private Image title;
    private Image by;
    private boolean isNagging;

    public MainMenuScreen(SudokuGame game) {
		this.game = game;
		assets = game.getAssets();
//        stage = new Stage(new FitViewport(1080, 1920));
        stage = new Stage();

        skin = new Skin(Gdx.files.internal("data/ui/myUI.json"));
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(173f/255f, 216f/255f, 230f/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();

        //SpriteBatch batch = stage.getSpriteBatch();
        Batch batch = stage.getBatch();

		batch.getProjectionMatrix().setToOrtho2D(0, 0, stage.getWidth(), stage.getHeight());
		batch.begin();
		background.draw(batch);
		batch.end();
		
		batch.setProjectionMatrix(stage.getCamera().combined);
		
		stage.draw();
//		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        float scale = width/title.getWidth() * .75f;
        title.setScale(scale);
        title.setPosition((width - title.getWidth() * scale) / 2f, height - title.getHeight() * scale * 1.1f);
        table.setPosition(width/2, title.getY() - table.getMinHeight()/2);
        by.setX((width - by.getWidth()) / 2f);
	}

	@Override
	public void show() {
		if(SudokuGame.settings.defaultQuickPlay.equalsIgnoreCase("Beginner")) {
			selectedDifficulty = (Difficulty.Beginner);
		} else if(SudokuGame.settings.defaultQuickPlay.equalsIgnoreCase("Easy")) {
			selectedDifficulty = (Difficulty.Easy);
		} else if(SudokuGame.settings.defaultQuickPlay.equalsIgnoreCase("Medium")) {
			selectedDifficulty = (Difficulty.Medium);
		} else if(SudokuGame.settings.defaultQuickPlay.equalsIgnoreCase("Hard")) {
			selectedDifficulty = (Difficulty.Hard);
		} else {
			selectedDifficulty = (Difficulty.Any);
		}
		
		Gdx.input.setInputProcessor(stage);		

		Texture back = assets.getTexture(TEX_NAME.CELL_BACKGROUND);
		back.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
		background.setColor(SudokuGame.settings.backgroundColor);
		background.setSize(stage.getWidth(), stage.getHeight());

		table = new Table(skin);
		table.center();
		//table.debug();

		stage.clear();
		stage.addAction(fadeIn(.25f));

		final Label lblGenerating = new Label("Generating...", skin);
		lblGenerating.setVisible(false);
		final SelectBox<Difficulty> ddlQuickType = new SelectBox<>(skin);
        ddlQuickType.setItems(new Difficulty[] {Difficulty.Any, Difficulty.Beginner, Difficulty.Easy, Difficulty.Medium, Difficulty.Hard});
		ddlQuickType.setSelected(Difficulty.getByName(SudokuGame.settings.defaultQuickPlay));
		ddlQuickType.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
                selectedDifficulty = ddlQuickType.getSelection().first();
				SudokuGame.settings.defaultQuickPlay = selectedDifficulty.toString();
				SudokuGame.settings.save();
			}
		});		

		final TextButton btnQuickPlay = new TextButton("Play", skin);
		btnQuickPlay.addListener(new ActorGestureListener() {
        	@Override
        	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // boo nagging code
                if(SudokuGame.IS_FREE_VERSION && !isNagging && game.settings.gamesPlayed % 5 == 0) {
                    isNagging = true;
                    game.setScreen(game.getNagScreen(false));
                    return;
                }

                isNagging = false;

                stage.addAction( sequence(
                    new Action() {
                        @Override
                        public boolean act(float delta )
                        {
                            ddlQuickType.setVisible(false);
                            btnQuickPlay.setVisible(false);
                            lblGenerating.setVisible(true);
                            table.invalidate();
                            return true;
                        }
                    },
                    new Action() {
                        @Override
                        public boolean act(float delta )
                        {
                            Data data = game.getData();
                            int rating = generator.getPuzzle(data, selectedDifficulty);
                            data.setRating(rating);
                            String diff = "Any";
                            SudokuGame.settings.gamesPlayed++;
                            if(rating >= 30) {
                                diff = "Hard";
                                SudokuGame.settings.hardPlayed++;
                            }
                            else if (rating >= 10 && rating < 30) {
                                diff = "Medium";
                                SudokuGame.settings.mediumPlayed++;
                            }
                            else if (rating > 0 && rating < 10) {
                                diff = "Easy";
                                SudokuGame.settings.easyPlayed++;
                            }
                            else if (rating == 0) {
                                diff = "Beginner";
                                SudokuGame.settings.beginnerPlayed++;
                            }
                            data.setStatusText("Rating: " + rating + " (" + diff + ")");
                            return true;
                        }
                    },
                    fadeOut( 0.5f ),
                    new Action() {
                        @Override
                        public boolean act(float delta )
                        {
                            // the last action will move to the next screen
                            game.setScreen( game.getSudokuScreen() );
                            return true;
                        }
                    } ) );
          	}
		});		
		
		final TextButton btnManualEntry = new TextButton("Manual Entry", skin);
		btnManualEntry.addListener(new ActorGestureListener() {
        	@Override
        	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        		stage.addAction( sequence( fadeOut( 0.25f ),
        	            new Action() {
        	                @Override
        	                public boolean act(float delta )
        	                {
        	                    // the last action will move to the next screen
        	                	game.setScreen( game.getManualEntryScreen() );
        	                    return true;
        	                }
        	            } ) );

        	}
		});

		final TextButton btnAppHelp = new TextButton("App Help", skin);
		btnAppHelp.addListener(new ActorGestureListener() {
        	@Override
        	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               	Gdx.net.openURI("http://sudoku.13secondstoburn.com");
        	}
		});
		
		final TextButton btnTechniqueHelp = new TextButton("Technique Help", skin);
		btnTechniqueHelp.addListener(new ActorGestureListener() {
        	@Override
        	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               	Gdx.net.openURI("http://sudoku.13secondstoburn.com/SudokuHelper/Home/Techniques");
        	}
		});
		
		final TextButton btnResume = new TextButton("Resume Last Game", skin);
		btnResume.addListener(new ActorGestureListener() {
        	@Override
        	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        		stage.addAction( sequence(
        				fadeOut( 0.5f ),
        	            new Action() {
        	                @Override
        	                public boolean act(float delta )
        	                {
        	                    // the last action will move to the next screen
        	                	game.setScreen( game.getSudokuScreen() );
        	                    return true;
        	                }
        	            } ) );
        	}
		});
		btnResume.setVisible(game.getData().load());

		table.row();
		table.add("Quick Play").colspan(2);
		table.row();
		table.add(ddlQuickType);
		table.add(btnQuickPlay).padLeft(2);
		table.row().colspan(2);
		table.add(lblGenerating);
		table.row();
		table.add(btnManualEntry).colspan(2);
		table.row();
		table.add(" ").colspan(2);
		table.row();
		table.add(btnAppHelp).colspan(2);
		table.row();
		table.add(" ").colspan(2);
		table.row();
		table.add(btnTechniqueHelp).colspan(2);
		table.row();
		table.add(" ").colspan(2);
        if(btnResume.isVisible()) {
            table.row();
            table.add(btnResume).colspan(2);
        }
        if(!SudokuGame.IS_FREE_VERSION) {
            final TextButton btnStats = new TextButton("Statistics", skin);
            btnStats.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    stage.addAction( sequence(
                            fadeOut( 0.5f ),
                            new Action() {
                                @Override
                                public boolean act(float delta )
                                {
                                    // the last action will move to the next screen
                                    game.setScreen( game.getStatisticsScreen() );
                                    return true;
                                }
                            } ) );
                }
            });
            table.row();
            table.add(" ").colspan(2);
            table.row();
            table.add(btnStats).colspan(2);
        }

		by = new Image(assets.getAtlasTexture("13stb"));
        title = new Image(assets.getAtlasTexture("title"));
        stage.addActor(title);
		stage.addActor(table);
        stage.addActor(by);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
