package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thirteensecondstoburn.CasinoPractice.Actors.MenuButton;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Statistics.CasinoPracticeStatistics;
import com.thirteensecondstoburn.CasinoPractice.Statistics.StatisticType;
import com.thirteensecondstoburn.CasinoPractice.TableGame;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Nick on 11/16/2016.
 */
public class StatisticsScreen implements Screen {
    Assets assets;
    CasinoPracticeGame game;
    Skin skin;
    Sprite background;
    ScrollPane scrollPane;
    Table outerTable;
    Table statsTable;
    Label currentBalance;
    Color menuButtonColor;


    Stage stage = new Stage(new FitViewport(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT));

    public StatisticsScreen(CasinoPracticeGame game) {
        this.game = game;
        assets = game.getAssets();
        skin = assets.getSkin();
        menuButtonColor = skin.getColor("menuButtonColor");
    }

    @Override
    public void show() {
        stage.addAction(Actions.fadeIn(0.5f));
        InputMultiplexer multiplexer = new InputMultiplexer(stage, game.getBackButtonProcessor(stage));
        Gdx.input.setInputProcessor(multiplexer);

        Texture back = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setColor(assets.getBackgroundColor());

        MenuButton menuButton = new MenuButton(assets);
        menuButton.setColor(menuButtonColor);
        menuButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        MenuScreen screen = game.getMenuScreen();
                        if (screen.getStage() != null) {
                            screen.getStage().addAction(Actions.fadeIn(0.5f));
                        }
                        stage.clear();
                        game.setScreen(screen);
                    }
                })));
            }
        });

        currentBalance = new Label("", skin);
        updateBalanceLabel();

        Drawable bgDrawable = new SpriteDrawable(background);

        outerTable = new Table(skin);
        outerTable.setBackground(bgDrawable);
        outerTable.setFillParent(true);
        outerTable.add(menuButton).left().pad(20);
        outerTable.add(new Label("Statistics", skin, "large-font")).center();
        outerTable.add(currentBalance).right().pad(20);
        outerTable.row();

        statsTable = new Table(skin);

        loadStatsTable();

        scrollPane = new ScrollPane(statsTable, skin);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = bgDrawable;
        scrollPane.setStyle(scrollPaneStyle);
        scrollPane.setTouchable(Touchable.enabled);
        scrollPane.setWidth(stage.getWidth());
        scrollPane.setHeight(stage.getHeight());
        outerTable.add(scrollPane).colspan(3).expand().fill();

        stage.addActor(outerTable);
    }

    private void loadStatsTable() {
        statsTable.clearChildren();

        final CasinoPracticeStatistics stats = game.getStatistics();
        int i = 0;
        for (final TableGame tableGame : stats.getGameStatistics().keySet()) {
            if(i % 2 == 0 ) {
                statsTable.row();
            }
            Table gameTable = new Table(skin);

            gameTable.add(new Label(tableGame.getDisplayName(), skin, "large-font")).pad(5).left();

            TextButton clearGameButton = new TextButton("Clear", skin);
            clearGameButton.setColor(menuButtonColor);
            clearGameButton.pad(10);
            clearGameButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    stats.getGameStatistics().remove(tableGame);
                    loadStatsTable();
                }
            });

            gameTable.add(clearGameButton).pad(5).right();

            gameTable.row();

            final Map<StatisticType, Double> individualStats = stats.getGameStatistics().get(tableGame);
            final TreeSet<StatisticType> sortedKeys = new TreeSet<>(individualStats.keySet());
            for (final StatisticType type : sortedKeys) {
                gameTable.add(new Label(type.getDisplay(), skin)).pad(5).left();
                double value = individualStats.get(type);
                if(value == (int) value) {
                    gameTable.add(new Label(String.format("%.0f", value), skin)).pad(5).left();
                } else {
                    gameTable.add(new Label(String.format("%.2f", value), skin)).pad(5).left();
                }
                gameTable.row();
            }

            statsTable.add(gameTable).pad(50).top();
            i++;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(43f / 255f, 96f / 255f, 22f / 255f, 1);
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
}
