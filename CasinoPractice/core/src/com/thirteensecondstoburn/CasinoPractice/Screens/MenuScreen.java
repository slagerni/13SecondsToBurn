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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thirteensecondstoburn.CasinoPractice.Actors.MessagePopup;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

import java.time.ZonedDateTime;

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
    Button storeButton;

    Button tcpButton;
    Button cfpButton;
    Button cStudButton;
    Button blackjackButton;
    Button crapsButton;
    Button rouletteButton;

    Label currentBalance;

    Image tcpTitle;
    Image cfpTitle;
    Image cStudTitle;
    Image blackjackTitle;
    Image crapsTitle;
    Image rouletteTitle;

    MessagePopup dailyChipsPopup;
    Window dailyChipsWindow;
    Label dailyChipsMessage;

    Window newInstallWindow;

    Sprite background;

    public MenuScreen(CasinoPracticeGame game) {
        this.game = game;
        assets = game.getAssets();
        skin = assets.getSkin();
    }

    @Override
    public void show() {
        if(!CasinoPracticeGame.googleServices.isConnected()) {
            while(CasinoPracticeGame.googleServices.isConnecting()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            CasinoPracticeGame.googleServices.signIn();
        }

        Gdx.input.setInputProcessor(stage);

        Color menuButtonColor = skin.getColor("menuButtonColor");

        Texture back = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setColor(assets.getBackgroundColor());

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

        storeButton = new Button(skin);
        storeButton.setColor(menuButtonColor);
        Image storeImage = new Image(assets.getTexture(Assets.TEX_NAME.STORE_BUTTON));
        storeImage.setColor(Color.GRAY);
        storeButton.add(storeImage);
        storeButton.pad(10);
        storeButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.getStoreScreen());
                    }
                })));
            }
        });

        currentBalance = new Label("", skin);
        updateBalanceLabel();

        windowTable = new Table();
        windowTable.setWidth(stage.getWidth());
        windowTable.setHeight(120);
        windowTable.add(storeButton).left().pad(10);
        windowTable.add(currentBalance).center();
        windowTable.add(settingsButton).right().pad(10);
        windowTable.row();

        gamesTable = new Table(skin);
        //table.debug();
        gamesTable.setWidth(stage.getWidth());
        gamesTable.setHeight(stage.getHeight() - 120);
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
        rouletteTitle = new Image(assets.getTexture(Assets.TEX_NAME.ROULETTE_TITLE));
        rouletteTitle.setColor(Color.WHITE);

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

        rouletteButton = new Button(skin);
        rouletteButton.setColor(menuButtonColor);
        rouletteButton.add(rouletteTitle).center().expand();
        rouletteButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.getRouletteScreen());
                    }
                })));
            }
        });

        gamesTable.defaults().minSize(300,300).prefSize(320,320).maxSize(320,320).pad(15);
        gamesTable.add(tcpButton);
        gamesTable.add(cfpButton);
        gamesTable.add(cStudButton);
        gamesTable.add(blackjackButton);
        gamesTable.row();
        gamesTable.add(crapsButton);
        gamesTable.add(rouletteButton);
//        scrollPane = new ScrollPane(gamesTable, skin);
//        scrollPane.setTouchable(Touchable.enabled);
//        scrollPane.setWidth(stage.getWidth());
//        scrollPane.setHeight(stage.getHeight());

        windowTable.add(gamesTable).colspan(3).fill().expand();
        windowTable.setFillParent(true);
        stage.addActor(windowTable);

        dailyChipsWindow = new Window("", skin);
        dailyChipsWindow.setVisible(false);
        dailyChipsWindow.setWidth(600);
        dailyChipsWindow.setHeight(500);
        dailyChipsWindow.setPosition(stage.getWidth() / 2 - 300, stage.getHeight() / 2 - 250);
        dailyChipsWindow.setModal(true);

        Texture popupBack = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        popupBack.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Sprite popupBackground = new Sprite(popupBack, (int)dailyChipsWindow.getWidth(), (int)dailyChipsWindow.getHeight());
        popupBackground.setSize(dailyChipsWindow.getWidth(), dailyChipsWindow.getHeight());
        popupBackground.setColor(Color.GRAY);

        dailyChipsWindow.setBackground(new SpriteDrawable(popupBackground));

        dailyChipsMessage = new Label("", skin);
        dailyChipsMessage.setWrap(true);

        Button dailyChipsCancelButton = new Button(skin);
        dailyChipsCancelButton.setColor(menuButtonColor);
        Label closeLabel = new Label("Get some chips", skin);
        closeLabel.setColor(Color.BLACK);
        dailyChipsCancelButton.add(closeLabel);
        dailyChipsCancelButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dailyChipsWindow.setVisible(false);
                game.addToBalance(1000);
                updateBalanceLabel();
                game.setLastDailyChips(ZonedDateTime.now());
                dailyChipsPopup.pop(MessagePopup.Message.WIN, stage.getWidth() / 2, stage.getHeight() * .75f, 10);
            }
        });

        dailyChipsWindow.add(new Label("Daily Chips!", skin, "large-font"));
        dailyChipsWindow.row();
        dailyChipsWindow.add(dailyChipsMessage).center().fill().expand().padLeft(30).padRight(30);
        dailyChipsWindow.row();
        dailyChipsWindow.add(dailyChipsCancelButton).center().pad(30);

        dailyChipsPopup = new MessagePopup(assets);

        newInstallWindow = new Window("", skin);
        newInstallWindow.setVisible(false);
        newInstallWindow.setWidth(600);
        newInstallWindow.setHeight(500);
        newInstallWindow.setPosition(stage.getWidth() / 2 - 300, stage.getHeight() / 2 - 250);
        newInstallWindow.setModal(true);
        newInstallWindow.setBackground(new SpriteDrawable(popupBackground));

        Button newInstallCancelButton = new Button(skin);
        newInstallCancelButton.setColor(menuButtonColor);
        Label closeNewInstallLabel = new Label("Got it. Let's play!", skin);
        closeNewInstallLabel.setColor(Color.BLACK);
        newInstallCancelButton.add(closeNewInstallLabel);
        newInstallCancelButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                newInstallWindow.setVisible(false);
                game.setIsNewInstall(false);
                checkDailyChips();
            }
        });

        Label hyperlink = new Label("casino.13stb.com", skin);
        hyperlink.setColor(Color.BLUE);
        hyperlink.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("http://casino.13secondstoburn.com");
            }
        });

        newInstallWindow.add(new Label("Welcome to Casino Practice", skin, "large-font")).center().pad(30);
        newInstallWindow.row();
        Label welcomeText = new Label("Test your skills on many games that you'll find in casinos around the world!\n\n If you need help at any point, please go to:", skin);
        welcomeText.setWrap(true);
        newInstallWindow.add(welcomeText).center().expand().fill();
        newInstallWindow.row();
        newInstallWindow.add(hyperlink).center().pad(10);
        newInstallWindow.row();
        newInstallWindow.add(newInstallCancelButton).center().pad(10).expand();

        stage.addActor(newInstallWindow);
        stage.addActor(dailyChipsWindow);
        stage.addActor(dailyChipsPopup);

        if(game.isNewInstall()) {
            newInstallWindow.setVisible(true);
        } else {
            checkDailyChips();
        }

//        windowTable.debug();
//        gamesTable.debug();
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

    private void updateBalanceLabel() {
        String balance;
        if(game.getBalance() == (int)game.getBalance()) {
            balance = String.format("%.0f", game.getBalance());
        } else {
            balance = String.format("%.2f", game.getBalance());
        }

        currentBalance.setText("Current Chips: " + balance);
    }

    private void checkDailyChips() {
        ZonedDateTime now = ZonedDateTime.now();
        long lastDay = game.getLastDailyChips().getYear() * 1000 + game.getLastDailyChips().getDayOfYear();
        long nowDay = now.getYear() * 1000 + now.getDayOfYear();

        if(nowDay > lastDay) {
            dailyChipsMessage.setText("Time for some more chips!\n\nCome back every day to receive 1000 more chips!");
            dailyChipsWindow.setVisible(true);
        }
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
