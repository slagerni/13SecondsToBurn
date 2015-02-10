package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thirteensecondstoburn.CasinoPractice.Actors.Card;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Deck;

import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Nick on 1/14/2015.
 */
public class SplashScreen implements Screen{
    CasinoPracticeGame game;
    Stage stage = new Stage(new FitViewport(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT));
    Image splashImage;
    Image splashImageShadow;
    Image logo;
    Assets assets;
    Sprite background;
    Deck deck1;
    Deck deck2;
    Random rand = new Random();

    public SplashScreen(CasinoPracticeGame game) {
        this.game = game;
        assets = game.getAssets();
        deck1 = new Deck(assets, Card.Back.BACK1, 2);
        deck1.shuffle();
        deck2 = new Deck(assets, Card.Back.BACK2, 2);
        deck2.shuffle();
    }

    private void throwCards() {
        for(int i=0; i<deck1.getCards().size(); i++) {
            Card card = deck1.getCards().get(i);
            card.setVisible(true);
            card.setScale(3,3);
            card.setPosition(rand.nextInt((int) stage.getWidth() - 100) - 25, -Card.CARD_HEIGHT * 2);
            card.setFaceUp(rand.nextBoolean());
            card.addAction(sequence(
                            delay(i * .05f),
                            parallel(scaleTo(1, 1, .5f), moveTo(rand.nextInt((int) stage.getWidth() - 100), rand.nextInt((int) stage.getHeight() - 100) - 50, .5f), rotateTo(360f + rand.nextInt(360), .5f)))
            );
        }
        for(int i=0; i<deck2.getCards().size(); i++) {
            Card card = deck2.getCards().get(i);
            card.setVisible(true);
            card.setScale(3,3);
            card.setPosition(rand.nextInt((int) stage.getWidth() - 100) - 25, (int) stage.getHeight() + Card.CARD_HEIGHT * 2);
            card.setFaceUp(rand.nextBoolean());
            card.addAction(sequence( delay(.03f),
                            delay(i * .05f),
                            parallel(scaleTo(1, 1, .5f), moveTo(rand.nextInt((int) stage.getWidth() - 100), rand.nextInt((int) stage.getHeight() - 100) - 50, .5f), rotateTo(360f + rand.nextInt(360), .5f)))
            );
        }
    }

    @Override
    public void show() {
        for(int i=0; i<54; i++) {
            stage.addActor(deck1.getCards().get(i));
            deck1.getCards().get(i).setVisible(false);
            stage.addActor(deck2.getCards().get(i));
            deck2.getCards().get(i).setVisible(false);
        }
        splashImage = new Image(assets.getTexture(Assets.TEX_NAME.SPLASH_TITLE));
        splashImage.addAction(Actions.sequence(
                //Actions.alpha(0),Actions.fadeIn(0.5f),
                color(Color.DARK_GRAY), delay(.2f), color(Color.RED), delay(.05f),
                color(Color.DARK_GRAY), delay(.05f), color(Color.RED), delay(.17f),
                color(Color.DARK_GRAY), delay(.05f), color(Color.RED),
                parallel(Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                throwCards();
                            }
                        }),
                        Actions.color(Color.YELLOW, 4)
                ),
                Actions.delay(1.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.getMenuScreen());
                    }
                })));

        splashImageShadow = new Image(assets.getTexture(Assets.TEX_NAME.SPLASH_TITLE));
        splashImageShadow.setColor(Color.BLACK);
        splashImageShadow.setPosition(5, -5);

        logo = new Image(assets.getTexture(Assets.TEX_NAME.BURN_LOGO));
        logo.setPosition(825, 30);

        stage.addActor(splashImageShadow);
        stage.addActor(splashImage);
        stage.addActor(logo);

        Texture back = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setColor(new Color().valueOf("265614FF"));
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
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
