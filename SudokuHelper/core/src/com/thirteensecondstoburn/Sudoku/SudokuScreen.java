package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.thirteensecondstoburn.Sudoku.Assets.TEX_NAME;

public class SudokuScreen implements Screen, ColoringListener {
	private SudokuGame game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Stage boardStage;
	private Stage hudStage;
	private Stage coloringStage;
	private Assets assets;
	private ZoomOut zoomOut;
	private Sprite background;
	private HumanSolver solver;
	private Array<ColoringCell> coloringCells = new Array<ColoringCell>();
		
	private Data data;

	public SudokuScreen(SudokuGame game) {
		this.game = game;
		this.data = game.getData();
	}

	@Override
	public void render(float delta) {
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		Gdx.gl.glClearColor(0, .2f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
		batch.begin();
		background.draw(batch);
		batch.end();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		coloringStage.draw();
		boardStage.draw();
		
		batch.end();
		
		batch.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
		Rectangle scissors = new Rectangle(0,0,w,(h-w)/2);
		batch.begin();
		ScissorStack.pushScissors(scissors);
		background.draw(batch);
		batch.flush();
		ScissorStack.popScissors();
		scissors = new Rectangle(0,h - ((h-w)/2),w,(h-w)/2);
		ScissorStack.pushScissors(scissors);
		background.draw(batch);
		batch.flush();
		ScissorStack.popScissors();
		batch.end();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		hudStage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		int fullSize = (height - width)/2;
        int topSize = (int)(fullSize * .75f);
        if(topSize *5 > width) topSize = width/5;

        int topSpace = width/5;

		background.setSize(width, height);
        boardStage.getViewport().update(width, height);
        boardStage.getCamera().position.set(width/2, width/2, 0);

		for(int i=0; i<boardStage.getActors().size; i++) {
            resizeCell(i, width);
		}

        coloringStage.getViewport().update(width, height, true);
        coloringStage.getCamera().position.set(width/2, width/2, 0);
		for(int i=0; i<coloringCells.size; i++) {
			resizeColoringCell(i, width - (int)(width*.1));
		}

        for(int i=0; i<coloringStage.getActors().size; i++) {
            Actor actor = coloringStage.getActors().get(i);
            if(actor instanceof ColorCycle ) {
                actor.setBounds((width * 2.0f/3.0f), width*.93f , width/3, (int)(height*.045));
            } else if(actor instanceof ColorSelect ) {
                actor.setBounds(width/3.0f, width*.93f , width/3, (int)(height*.045));
            } else if(actor instanceof ColoringClose) {
                actor.setBounds(0, width*.93f, width/3, (int)(height*.045));
            }
        }

        hudStage.getViewport().update(width, height);
		for(int i=0; i<hudStage.getActors().size; i++) {
			Actor actor = hudStage.getActors().get(i); 
			if(actor instanceof Highlighter) {
//                resizeHighlighter(i, fullSize, (int)(fullSize*.75f) + (int)(scaleY*10f));
                resizeHighlighter(i, fullSize, width/2 - fullSize/2);
			} else if(actor instanceof Cycle) {
				actor.setBounds(width - fullSize *.75f, fullSize/4, (int)(fullSize*.75f), (int)(fullSize*.75f));
            } else if (actor instanceof SelectedOnly) {
                actor.setBounds(width - fullSize *.8f, 0, fullSize*.75f, fullSize/4);
			} else if(actor instanceof ZoomOut) {
				actor.setBounds(0, fullSize, width, width);
            } else if (actor instanceof StatusText) {
                actor.setBounds(0, height - fullSize, width, fullSize*.25f);
            } else if (actor instanceof ColoringOpen) {
                actor.setBounds(0, fullSize/4, (int)(fullSize*.75f), (int)(fullSize*.75f));
            // TOP MENU ITEMS
			} else if(actor instanceof UndoRedo) {
				UndoRedo undo = (UndoRedo) actor;
				if(undo.isUndo) {
					actor.setBounds(0, height-topSize, topSize, topSize);
				} else {
					actor.setBounds(topSpace, height-topSize, topSize, topSize);
				}
			} else if (actor instanceof Help) {
				actor.setBounds(topSpace * 2,  height - topSize, topSize, topSize);
			} else if (actor instanceof Hint) {
				actor.setBounds(topSpace * 3,  height - topSize, topSize, topSize);
			} else if (actor instanceof MainMenu) {
				actor.setBounds(topSpace *4, height - topSize, topSize, topSize);
			}
		}
	}

	@Override
	public void show() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		solver = new HumanSolver(data);
		boardStage = new Stage();
		coloringStage = new Stage();
		hudStage = new Stage();
        hudStage.getViewport().setCamera(new OrthographicCamera(w, h));
		hudStage.getCamera().position.set(w/2f, h/2f, 0);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(hudStage);
		multiplexer.addProcessor(coloringStage);
		multiplexer.addProcessor(boardStage);
		Gdx.input.setInputProcessor(multiplexer);		
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		assets = game.getAssets();
		zoomOut = new ZoomOut(assets);
		zoomOut.setVisible(false);

		// draw the cells from the top down
		for(int i=80; i>=0; i--) {
			Cell cell = new Cell(i, data, assets, zoomOut);
			cell.addListener(new CellEventListener(boardStage));
			if(Data.getHouse(i) % 2 != 0) {
				cell.setColor(SudokuGame.settings.cellOddBackgroundColor);
			} else {
				cell.setColor(SudokuGame.settings.cellBackgroundColor);
			}
			boardStage.addActor(cell);
		}
		boardStage.addListener(new BoardListener(boardStage, data, zoomOut));
		
		// set up the coloring cells
		coloringCells.clear(); // make sure we aren't adding 5000 of these
		for(int i=80; i>=0; i--) {
			ColoringCell cell = new ColoringCell(i, data, assets);
			cell.addListener(new ColoringCellEventListener());
			//cell.addListener(new CellEventListener(boardStage));
			if(Data.getHouse(i) % 2 != 0) {
				cell.setColor(SudokuGame.settings.cellOddBackgroundColor);
			} else {
				cell.setColor(SudokuGame.settings.cellBackgroundColor);
			}
			coloringCells.add(cell);
			coloringStage.addActor(cell);
		}
		ColorCycle colorCycle = new ColorCycle(data, assets);
		colorCycle.addListener(new ColorCycleEventListener());
		ColorSelect colorSelect = new ColorSelect(assets);
		colorSelect.addListener(new ColorSelectEventListener());
		ColoringClose close = new ColoringClose(assets);
		close.addListener(new ColoringCloseEventListener());
		close.addListener(this);
		
		coloringStage.addActor(colorCycle);
		coloringStage.addActor(colorSelect);
		coloringStage.addActor(close);
		
		for(int i=0; i<9; i++) {
			Highlighter hi = new Highlighter(i, data, assets);
			hi.addListener(new HighlightEventListener());
			hi.setColor(SudokuGame.settings.numberHighlightColors[i]);
			hudStage.addActor(hi);
		}
		Cycle cycle = new Cycle(data, assets);
		cycle.addListener(new CycleEventListener());
		UndoRedo undo = new UndoRedo(true, data, assets);
		undo.addListener(new UndoRedoEventListener());
		UndoRedo redo = new UndoRedo(false, data, assets);
		redo.addListener(new UndoRedoEventListener());
		Help help = new Help(assets);
		help.addListener(new HelpEventListener());
		Hint hint = new Hint(solver, data, assets);
		hint.addListener(new HintEventListener());
		SelectedOnly selectedOnly = new SelectedOnly(data, assets);
		selectedOnly.addListener(new SelectedOnlyEventListener());
		StatusText statusText = new StatusText(data, assets);
		ColoringOpen open = new ColoringOpen(assets);
		open.addListener(new ColoringOpenEventListener());
		open.addListener(this);
		MainMenu mainMenu = new MainMenu(game, assets);
		mainMenu.addListener(new MainMenuEventListener());
		
		hudStage.addActor(cycle);
		hudStage.addActor(zoomOut);
		hudStage.addActor(undo);
		hudStage.addActor(redo);
		hudStage.addActor(help);
		hudStage.addActor(hint);
		hudStage.addActor(selectedOnly);
		hudStage.addActor(statusText);
		hudStage.addActor(open);
		hudStage.addActor(mainMenu);
		
		Texture back = assets.getTexture(TEX_NAME.CELL_BACKGROUND);
		back.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		background = new Sprite(back, (int)w, (int)h);
		background.setColor(SudokuGame.settings.backgroundColor);
		background.setSize(w, h);

		toggleColoringVisibility(false);
	}

	@Override
	public void hide() {
		boolean isSolved = true;
		for(int i=0; i<81; i++ ){
			if(game.getData().getItems()[i].number == 0) {
				isSolved = false;
				break;
			}
		}
		
		boolean canSolve = false;
		Data dataCopy = new Data();
		for(int i = 0; i < game.getData().getItems().length; i++) {
			dataCopy.getItems()[i] = new DataItem(game.getData().getItems()[i]);
		}
		HumanSolver solver = new HumanSolver(dataCopy);
		canSolve = solver.Solve();
		
		if(canSolve && !isSolved) {
			game.getData().save();
		} else {
			SudokuGame.settings.saveData("");
		}
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
		batch.dispose();
		boardStage.dispose();
		hudStage.dispose();
		coloringStage.dispose();
		assets.dispose();
	}

	private void resizeCell(int index, int size) {
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
		boardStage.getActors().get(index).setBounds(((index % 9) * width/9) + woff, ((index / 9) * height/9) + hoff, width/9, height/9);
	}

	private void resizeColoringCell(int index, int size) {
		int width = size - 9;
		int height = size - 9;
		int woff = (size - (width + 3)) / 2 + (int)(size*.05);
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
		coloringCells.get(index).setBounds(((index % 9) * width/9) + woff, ((index / 9) * height/9) + hoff, width/9, height/9);
	}

	private void resizeHighlighter(int index, int size, int start) {
		int width = (size/3) * 3 - 3;
		int height = ((size)/3) * 3 - 3;
		int woff = (size - (width)) / 2 + start;
		int hoff = ((size - (height)) / 2) + size/3;
		hudStage.getActors().get(index).setBounds(((index % 3) * width/3) + woff, size/3 - ((index / 3) * height/3) + hoff, width/3, height/3);
	}
	
	private void toggleColoringVisibility(boolean isColoring) {
		boolean isZoomed = false;
		for(Actor a : boardStage.getActors()) {
			if(a instanceof Cell) {
				if(((Cell) a).isZoomedTo()) {
					isZoomed = true;
				}
			}
			a.setVisible(!isColoring);
		}
	
		for(Actor a : hudStage.getActors()) {
			if(a instanceof ZoomOut) {
				a.setVisible(isZoomed && !isColoring);
			} else {
				a.setVisible(!isColoring);
			}
		}
	
		for(Actor a : coloringStage.getActors()) {
			a.setVisible(isColoring);
		}
	
	}

	@Override
	public void ColoringClosed() {		
		toggleColoringVisibility(false);
	}

	@Override
	public void ColoringOpened() {		
		toggleColoringVisibility(true);
		data.setColors(0, false);
		ColoringCell.setPossibility(0);
	}
}
