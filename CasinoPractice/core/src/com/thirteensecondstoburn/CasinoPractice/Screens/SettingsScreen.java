package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thirteensecondstoburn.CasinoPractice.Actors.MenuButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

/**
 * Created by Nick on 3/17/2015.
 */
public class SettingsScreen  implements Screen {
    Assets assets;
    CasinoPracticeGame game;
    Skin skin;

    Stage stage = new Stage(new FitViewport(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT));;
    ScrollPane scrollPane;

    Sprite background;
    public Color backgroundColor = new Color().valueOf("265614FF");

    public SettingsScreen(CasinoPracticeGame game) {
        this.game = game;
        assets = game.getAssets();
        skin = assets.getSkin();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addAction(Actions.fadeIn(0.5f));


        Texture back = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setColor(backgroundColor);

        Table table = new Table(skin);
        //table.debug();
        table.add(new Label("General Settings", skin, "large-font")).pad(20).colspan(2);
        table.row();

        final CheckBox chkShowHints = new CheckBox("Show Hints After a Play", skin);
        chkShowHints.setChecked(game.isShowHints());
        chkShowHints.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setShowHints(chkShowHints.isChecked());
                game.saveSettings();
            }
        });
        chkShowHints.getCells().get(0).padRight(10);
        table.add(chkShowHints).left().pad(10);

        final CheckBox chkActionHints = new CheckBox("Show Visual Hints Ahead of Time", skin);
        chkActionHints.setChecked(game.isActionHints());
        chkActionHints.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setActionHints(chkActionHints.isChecked());
                game.saveSettings();
            }
        });
        chkActionHints.getCells().get(0).padRight(10);
        table.add(chkActionHints).left().pad(10);

        table.row();
        table.add(new Label("Blackjack Settings", skin, "large-font")).pad(20).colspan(2);
        table.row();

        final CheckBox chkHitSoft17 = new CheckBox("Dealer Hits a Soft 17", skin);
        chkHitSoft17.setChecked(game.getBlackjackHitSoft17());
        chkHitSoft17.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setBlackjackHitSoft17(chkHitSoft17.isChecked());
                game.saveSettings();
            }
        });
        chkHitSoft17.getCells().get(0).padRight(10);
        table.add(chkHitSoft17).left().pad(10);

        final CheckBox chkSimpleBjStrat = new CheckBox("Use Simple Strategy Instead of Basic for Hints", skin);
        chkSimpleBjStrat.setChecked(game.isSimpleBlackjackHints());
        chkSimpleBjStrat.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setSimpleBlackjackHints(chkSimpleBjStrat.isChecked());
                game.saveSettings();
            }
        });
        chkSimpleBjStrat.getCells().get(0).padRight(10);
        table.add(chkSimpleBjStrat).left().pad(10);

        table.row();

        Table penTable = new Table(skin);
        penTable.add("Penetration").padRight(10);
        final SelectBox<Penetration> sbPenetration = new SelectBox<>(skin);
        sbPenetration.setItems(Penetration.toList());
        sbPenetration.setSelected(Penetration.getByValue(game.getBlackjackPenetration()));
        sbPenetration.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Penetration pen = sbPenetration.getSelection().first();
                game.setBlackjackPenetration(pen.getValue());
                game.saveSettings();
            }
        });
        penTable.add(sbPenetration).left();
        table.add(penTable).left().pad(10);

        Table decksTable = new Table(skin);
        decksTable.add("Number of Decks").padRight(10);
        final SelectBox<Integer> sbDecks = new SelectBox<>(skin);
        sbDecks.setItems(new Integer[] {1,2,4,6,8});
        sbDecks.setSelected(game.getBlackjackDecks());
        sbDecks.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Integer value = sbDecks.getSelection().first();
                game.setBlackjackDecks(value);
                game.saveSettings();
            }
        });
        decksTable.add(sbDecks).left();
        table.add(decksTable).left().pad(10);

        Table outerTable = new Table(skin);
        outerTable.setBackground(new SpriteDrawable(background));
        outerTable.setWidth(stage.getWidth());
        outerTable.setHeight(stage.getHeight());

        MenuButton menuButton = new MenuButton(assets);
        menuButton.setColor(Color.YELLOW);
        menuButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        MenuScreen screen = game.getMenuScreen();
                        if(screen.getStage() != null) {
                            screen.getStage().addAction(Actions.fadeIn(0.5f));
                        }
                        stage.clear();
                        game.setScreen(screen);
                    }
                })));
            }
        });
        outerTable.add(menuButton).left().pad(20);
        outerTable.row();
        outerTable.add(table);

        stage.addActor(outerTable);
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

    public enum Penetration {
        P35 ("35%", .35),
        P40 ("40%", .40),
        P45 ("45%", .45),
        P50 ("50%", .50),
        P55 ("55%", .55),
        P60 ("60%", .60),
        P65 ("65%", .65),
        P70 ("70%", .70),
        P75 ("75%", .75);

        private final String display;
        private final double value;
        Penetration(String display, double value) {
            this.display = display;
            this.value = value;
        }

        public double getValue() {return value;}
        @Override
        public String toString() { return display; }

        public static Penetration[] toList() {
            Penetration[] all = new Penetration[9];
            all[0] = P35;
            all[1] = P40;
            all[2] = P45;
            all[3] = P50;
            all[4] = P55;
            all[5] = P60;
            all[6] = P65;
            all[7] = P70;
            all[8] = P75;

            return all;
        }

        public static Penetration getByValue(double val) {
            for(Penetration p : Penetration.toList()) {
                if(p.value == val) { return p; }
            }

            // just in case
            return P65;
        }
    }
}
