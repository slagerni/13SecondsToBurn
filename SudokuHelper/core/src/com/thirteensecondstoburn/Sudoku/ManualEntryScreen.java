package com.thirteensecondstoburn.Sudoku;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.thirteensecondstoburn.Sudoku.Assets.TEX_NAME;

public class ManualEntryScreen implements Screen {
	private SudokuGame game;
	private Assets assets;
	private Stage stage;
	private Sprite background;
	private Skin skin;
	private Table table;
    private EntryCell[] cells = new EntryCell[81];
    private EntrySelector[] selectors = new EntrySelector[9];
    private int selectedCell = -1;

	public ManualEntryScreen(SudokuGame game) {
		this.game = game;
		assets = game.getAssets();
		//stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage();
		skin = new Skin(Gdx.files.internal("data/ui/myUI.json"));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(173f/255f, 216f/255f, 230f/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();

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
        stage.getViewport().update(width, height, true);
        for(int i=0; i<cells.length; i++) {
            resizeCell(i, width, height - width);
        }
        table.setBounds(0,0,width, height - width);

        for(int i=0; i<selectors.length; i++) {
            resizeSelector(i, (int)(width*.75), (int)(width*.25/2.0), (int)(height - width + width*.25/2.0));
        }
    }

	@Override
	public void show() {
        game.getData().clear();
		Gdx.input.setInputProcessor(stage);		
		stage.addAction(fadeIn(.25f));
		stage.clear();

		Texture back = assets.getTexture(TEX_NAME.CELL_BACKGROUND);
		back.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
		background.setColor(SudokuGame.settings.backgroundColor);
		background.setSize(stage.getWidth(), stage.getHeight());

        for(int i=80; i>=0; i--) {
            EntryCell cell = new EntryCell(80-i, game.getData(), assets);

            cell.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    EntryCell ec = (EntryCell)event.getListenerActor();
                    selectedCell = ec.getIndex();
                    for(int s = 0; s<selectors.length; s++) {
                        selectors[s].setCellIndex(selectedCell);
                        selectors[s].setVisible(true);
                    }
                }
            });

            //cell.addListener(new CellEventListener(boardStage));
            if(Data.getHouse(i) % 2 != 0) {
                cell.setColor(SudokuGame.settings.cellOddBackgroundColor);
            } else {
                cell.setColor(SudokuGame.settings.cellBackgroundColor);
            }
            cells[i] = cell;
            stage.addActor(cell);
        }

		table = new Table(skin).top();
		table.pad(0);
		
		final TextButton btnManualEntry = new TextButton("Play", skin);
		btnManualEntry.addListener(new ActorGestureListener() {
        	@Override
        	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        		stage.addAction( sequence( //fadeOut( 0.25f ),
        	            new Action() {
        	                @Override
        	                public boolean act(float delta )
        	                {
        	                    // the last action will move to the next screen
        	                	game.setScreen( game.getSudokuScreen());
        	                    return true;
        	                }
        	            } ) );

        	}
		});

		final TextButton btnClipboard = new TextButton("Clipboard", skin);
		btnClipboard.addListener(new ActorGestureListener() {
        	@Override
        	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.getData().clear();
        		Clipboard cb = Gdx.app.getClipboard();
        		String fromClip = cb.getContents();
                if(fromClip.length() < 81) {
                    fromClip = String.format("%-81s", fromClip).replace(' ', '.');
                }
                String flipped = "";
                for(int i=0; i<9; i++) {
                    flipped += new StringBuilder(fromClip.substring(i*9, i*9+9)).reverse().toString();
                }

                for(int i=0; i<fromClip.length(); i++) {
                    try {
                        int value = Integer.parseInt("" + flipped.charAt(i));
                        if(value > 0 && value <= 9) {
                            game.getData().setNumber(i, value);
                        }
                    } catch (Exception ex) {
                        continue;
                    }
                }
        	}
		});

		MainMenu mainMenu = new MainMenu(game, assets);
		mainMenu.addListener(new MainMenuEventListener());
		mainMenu.setBounds(0, 0, 200, 200);

		table.row();
		table.add("Manual Entry").colspan(2);
        table.row().expand();
        table.add(mainMenu);
		table.add(btnManualEntry);
		table.row();
		table.add(btnClipboard).colspan(2);
		stage.addActor(table);

        for(int i=0; i<9; i++) {
            final Data data = game.getData();
            EntrySelector sel = new EntrySelector(i, game.getData(), assets);
            sel.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    EntrySelector es = (EntrySelector)event.getListenerActor();
                    if(data.getNumber(selectedCell) == es.getIndex() + 1) {
                        data.setNumber(selectedCell, -1);
                    }
                    else {
                        data.setNumber(selectedCell, es.getIndex() + 1);
                    }
                    for(int s = 0; s<selectors.length; s++) {
                        selectors[s].setCellIndex(-1);
                        selectors[s].setVisible(false);
                    }
                }
            });
            sel.setColor(SudokuGame.settings.numberHighlightColors[i]);
            sel.setZIndex(100);
            sel.setVisible(false);
            selectors[i] = sel;
            stage.addActor(sel);
        }
	}

    private void resizeSelector(int index, int size, int startX, int startY) {
        int width = (size/3) * 3 - 3;
        int height = ((size)/3) * 3 - 3;
        int woff = (size - (width)) / 2 + startX;
        int hoff = ((size - (height)) / 2) + size/3 + startY;
        selectors[index].setBounds(((index % 3) * width/3) + woff, size/3 - ((index / 3) * height/3) + hoff, width/3, height/3);
    }
    private void resizeCell(int index, int size, int startingHeight) {
        int width = size - 9;
        int height = size - 9;
        int woff = (size - (width + 3)) / 2;
        int hoff = ((size - (height + 3)) / 2);
        if(index%9 >= 6) {
            woff += 6;
        }
        else if(index%9 >= 3){
            woff += 3;
        }
        if(index/9 >=6) {
            hoff += 6;
        } else if(index/9 >= 3) {
            hoff += 3;
        }
        cells[index].setBounds(((index % 9) * width/9) + woff, ((index / 9) * height/9) + hoff + startingHeight, width/9, height/9);
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
		// TODO Auto-generated method stub

	}

}
