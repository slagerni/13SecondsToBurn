package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

/**
 * Created by Nick on 3/24/2015.
 */
public class StatisticsScreen implements Screen {
    SudokuGame game;
    Assets assets;
    Stage stage;
    Skin skin;
    boolean fromHint;
    Sprite background;
    Table table;

    public StatisticsScreen(SudokuGame game) {
        this.game = game;
        assets = game.getAssets();
        stage = new Stage(new FitViewport(1080, 1920));
//        stage = new Stage();
        skin = new Skin(Gdx.files.internal("data/ui/myUI.json"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Texture back = assets.getTexture(Assets.TEX_NAME.CELL_BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setColor(SudokuGame.settings.backgroundColor);
        background.setSize(stage.getWidth(), stage.getHeight());

        table = new Table(skin);
        table.center();
        table.setFillParent(true);
        //table.debug();

        stage.clear();
        stage.addAction(fadeIn(.25f));

        MainMenu mainMenu = new MainMenu(game, assets);
        mainMenu.addListener(new MainMenuEventListener());
        mainMenu.setBounds(0, 0, 200, 200);

        table.add("Statistics").colspan(2).left();
        table.row();
        table.add("Games Played").colspan(2).padBottom(20).padTop(20).left();
        table.row();
        table.add("  All").left(); table.add("" + game.settings.gamesPlayed);
        table.row();
        table.add("  Beginner").left(); table.add("" + game.settings.beginnerPlayed);
        table.row();
        table.add("  Easy").left(); table.add("" + game.settings.easyPlayed);
        table.row();
        table.add("  Medium").left(); table.add("" + game.settings.mediumPlayed);
        table.row();
        table.add("  Hard").left(); table.add("" + game.settings.hardPlayed);
        table.row();
        table.add("Solved").colspan(2).padBottom(20).padTop(20).left();
        table.row();
        table.add("  Beginner").left(); table.add("" + game.settings.beginnerSolved);
        table.row();
        table.add("  Easy").left(); table.add("" + game.settings.easySolved);
        table.row();
        table.add("  Medium").left(); table.add("" + game.settings.mediumSolved);
        table.row();
        table.add("  Hard").left(); table.add("" + game.settings.hardSolved);
        table.row();
        table.add("Hints Used").left().padTop(20).padBottom(20); table.add("" + game.settings.hintsUsed).padTop(20).padBottom(20);
        table.row();
        table.add("Hardest Solved").left().padTop(20).padBottom(20).padRight(20); table.add("" + game.settings.hardestRatingSolved).padTop(20).padBottom(20);
        table.row();
        table.add(mainMenu);
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(173f/255f, 216f/255f, 230f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();

        Batch batch = stage.getBatch();

        //batch.getProjectionMatrix().setToOrtho2D(0, 0, stage.getWidth(), stage.getHeight());
        batch.begin();
        background.draw(batch);
        batch.end();

        //batch.setProjectionMatrix(stage.getCamera().combined);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
