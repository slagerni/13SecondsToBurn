package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Nick on 3/24/2015.
 */
public class NagScreen implements Screen {
    SudokuGame game;
    Assets assets;
    Stage stage;
    Skin skin;
    boolean fromHint;
    Sprite background;
    Table table;

    public NagScreen(SudokuGame game) {
        this.game = game;
        assets = game.getAssets();
        //stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        stage = new Stage(new ScreenViewport());
        stage = new Stage(new FitViewport(1080, 1920));
//        stage = new Stage();
        skin = new Skin(Gdx.files.internal("data/ui/myUI.json"));
    }

    public void setFromHint(boolean fromHint) {
        this.fromHint = fromHint;
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


        final TextButton btnPaidLink = new TextButton("Get the Full Version", skin);
        btnPaidLink.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.thirteensecondstoburn.SudokuHelper");
            }
        });

        final TextButton btnReturn = new TextButton("No Thanks, Maybe Later", skin);
        btnReturn.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction( sequence(
                        fadeOut( 0.5f ),
                        new Action() {
                            @Override
                            public boolean act(float delta )
                            {
                                // the last action will move to the next screen
                                if(fromHint) {
                                    game.setScreen(game.getSudokuScreen());
                                } else {
                                    game.setScreen(game.getMainMenuScreen());
                                }
                                return true;
                            }
                        } ) );
            }
        });

        Label topText = new Label("Thank you for using Sudoku Helper! You seem to be enjoying it. At this point you've played " + game.settings.gamesPlayed + " games.", skin);
        topText.setWrap(true);
        table.add(topText).width(stage.getWidth() - 10);
        table.row();
        table.add(" ");
        table.row();
        Label sorryText = new Label("Sorry for this screen but I felt this was better than interfering with your play by adding advertisements.", skin);
        sorryText.setWrap(true);
        table.add(sorryText).width(stage.getWidth() -10);
        if(fromHint) {
            table.row();
            table.add(" ");
            Label hintText = new Label("Being the free version I'm limiting the number of hints to 50. Sorry. You have " + (50 - game.settings.hintsUsed) + " left.", skin);
            hintText.setWrap(true);
            table.row();
            table.add(hintText).width(stage.getWidth() - 10);
        }
        table.row();
        table.add(" ");
        table.row();
        Label bottomText = new Label("Please consider buying the full version:", skin);
        bottomText.setWrap(true);
        table.add(bottomText).width(stage.getWidth() - 10);
        table.row();
        table.add(" - Unlimited Hints").left();
        table.row();
        table.add(" - Statistics").left();
        table.row();
        table.add(" - No more nagging").left();
        table.row();
        table.add("Thank you for your support!").width(stage.getWidth() - 10);
        table.row();
        table.add(" ");
        table.row();
        table.add(btnPaidLink);
        table.row();
        table.add(" ");
        table.row();
        table.add(btnReturn);

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(173f/255f, 216f/255f, 230f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();

        Batch batch = stage.getBatch();

        batch.begin();
        background.draw(batch);
        batch.end();

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
