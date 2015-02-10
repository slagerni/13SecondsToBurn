package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

/**
 * Created by Nick on 2/3/2015.
 */
public class MenuScreen implements Screen {
    Assets assets;
    CasinoPracticeGame game;
    Skin skin;

    Stage stage = new Stage(new FitViewport(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT));;
    Table table;
    ScrollPane scrollPane;
    Button tcpButton;
    Button cfpButton;

    Image tcpTitle;
    Image cfpTitle;

    Sprite background;
    public Color backgroundColor = new Color().valueOf("265614FF");

    public MenuScreen(CasinoPracticeGame game) {
        this.game = game;
        assets = game.getAssets();
        skin = assets.getSkin();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Texture back = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setColor(backgroundColor);

        table = new Table(skin);
        //table.debug();
        table.setBackground(new SpriteDrawable(background));
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());

        tcpTitle = new Image(assets.getTexture(Assets.TEX_NAME.THREE_CARD_POKER_TITLE));
        tcpTitle.setColor(Color.GREEN);
        cfpTitle = new Image(assets.getTexture(Assets.TEX_NAME.CRAZY_FOUR_POKER_TITLE));
        cfpTitle.setColor(Color.GREEN);

        tcpButton = new Button(skin);
        tcpButton.add(tcpTitle).center().expand();
        tcpButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.getThreeCardPokerScreen());
                    }
                })));
            }
        });
        cfpButton = new Button(skin);
        cfpButton.add(cfpTitle).center().expand();
        cfpButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.getCrazyFourPokerScreen());
                    }
                })));
            }
        });

        table.add(tcpButton).width(300).height(300).expand().pad(10);
        table.add(cfpButton).width(300).height(300).expand().pad(10);
        scrollPane = new ScrollPane(table, skin);
        scrollPane.setTouchable(Touchable.enabled);
        scrollPane.setWidth(stage.getWidth());
        scrollPane.setHeight(stage.getHeight());
        stage.addActor(scrollPane);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(43f/255f,96f/255f,22f/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Batch batch = stage.getBatch();
        batch.begin();
        background.draw(batch);
        batch.end();

        stage.act(delta);
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

    }

    public Stage getStage() {
        return stage;
    }
}
