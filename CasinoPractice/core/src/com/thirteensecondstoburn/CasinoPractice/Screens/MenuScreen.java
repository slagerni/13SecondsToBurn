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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
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

    Stage stage = new Stage(new FitViewport(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT));
    Table gamesTable;
    Table windowTable;
    ScrollPane scrollPane;

    Button settingsButton;

    Button tcpButton;
    Button cfpButton;
    Button cStudButton;
    Button blackjackButton;
    Button crapsButton;

    Image tcpTitle;
    Image cfpTitle;
    Image cStudTitle;
    Image blackjackTitle;
    Image crapsTitle;

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

        Color menuButtonColor = skin.getColor("menuButtonColor");

        Texture back = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setColor(backgroundColor);

        settingsButton = new Button(skin);
        settingsButton.setColor(menuButtonColor);
        Image settingsImage = new Image(assets.getTexture(Assets.TEX_NAME.SETTINGS));
        settingsImage.setColor(Color.GRAY);
        settingsButton.add(settingsImage);
        settingsButton.pad(10);
        settingsButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.getSettingsScreen());
                    }
                })));
            }
        });

        windowTable = new Table();
        windowTable.add(settingsButton).right();
        windowTable.row();

        gamesTable = new Table(skin);
        //table.debug();
        gamesTable.setWidth(stage.getWidth());
        gamesTable.setHeight(stage.getHeight());
        gamesTable.setFillParent(true);

        tcpTitle = new Image(assets.getTexture(Assets.TEX_NAME.THREE_CARD_POKER_TITLE));
        tcpTitle.setColor(Color.GREEN);
        cfpTitle = new Image(assets.getTexture(Assets.TEX_NAME.CRAZY_FOUR_POKER_TITLE));
        cfpTitle.setColor(Color.GREEN);
        cStudTitle = new Image(assets.getTexture(Assets.TEX_NAME.CARIBBEAN_STUD_POKER_TITLE));
        cStudTitle.setColor(Color.GREEN);
        blackjackTitle = new Image(assets.getTexture(Assets.TEX_NAME.BLACKJACK_TITLE));
        blackjackTitle.setColor(Color.WHITE);
        crapsTitle = new Image(assets.getTexture(Assets.TEX_NAME.CRAPS_TITLE));
        crapsTitle.setColor(Color.WHITE);

        tcpButton = new Button(skin);
        tcpButton.setColor(menuButtonColor);
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
        cfpButton.setColor(menuButtonColor);
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

        cStudButton = new Button(skin);
        cStudButton.setColor(menuButtonColor);
        cStudButton.add(cStudTitle).center().expand();
        cStudButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.getCaribbeanStudPokerScreen());
                    }
                })));
            }
        });

        blackjackButton = new Button(skin);
        blackjackButton.setColor(menuButtonColor);
        blackjackButton.add(blackjackTitle).center().expand();
        blackjackButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.getBlackJackScreen());
                    }
                })));
            }
        });

        crapsButton = new Button(skin);
        crapsButton.setColor(menuButtonColor);
        crapsButton.add(crapsTitle).center().expand();
        crapsButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.getCrapsScreen());
                    }
                })));
            }
        });

        gamesTable.add(tcpButton).width(300).height(300).expand().pad(10);
        gamesTable.add(cfpButton).width(300).height(300).expand().pad(10);
        gamesTable.add(cStudButton).width(300).height(300).expand().pad(10);
        gamesTable.add(blackjackButton).width(300).height(300).expand().pad(10);
        gamesTable.add(crapsButton).width(300).height(300).expand().pad(10);
//        scrollPane = new ScrollPane(gamesTable, skin);
//        scrollPane.setTouchable(Touchable.enabled);
//        scrollPane.setWidth(stage.getWidth());
//        scrollPane.setHeight(stage.getHeight());

        windowTable.add(gamesTable).expand().fill();
        windowTable.setFillParent(true);
        stage.addActor(windowTable);
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
