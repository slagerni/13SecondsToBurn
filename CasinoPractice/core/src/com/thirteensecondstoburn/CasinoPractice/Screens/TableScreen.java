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
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thirteensecondstoburn.CasinoPractice.Actors.LeftSide;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Nick on 2/11/2015.
 */
abstract public class TableScreen implements Screen {
    public Color backgroundColor = new Color().valueOf("265614FF");
    public Color mainColor = Color.YELLOW;
    public Color hintColor = Color.GREEN;

    Stage stage = new Stage(new FitViewport(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT));
    CasinoPracticeGame game;
    Assets assets;
    Sprite background;

    LeftSide leftSide;
    Text hintText;

    public TableScreen(CasinoPracticeGame game) {
        this.game = game;
        this.assets = game.getAssets();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addAction(Actions.alpha(1));

        Texture back = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setColor(backgroundColor);

        leftSide = new LeftSide(game, assets);
        leftSide.setSize(256, stage.getHeight());
        leftSide.setColor(mainColor);

        hintText = new Text(assets, "", 1.5f, true);
        hintText.setVisible(false);

        stage.addActor(leftSide);

        // let the subclass setup what they need
        setup();
        stage.addActor(hintText);

    }

    public void showHint(String text) {
        hintText.setTextCentered(text);
        hintText.setZIndex(1000);
        int duration = 3;
        if(hintText.getWidth() > stage.getWidth()) {
            duration += text.length() / 10;
        }
        hintText.addAction(sequence(Actions.moveTo(stage.getWidth(), hintText.getY()), Actions.show(), Actions.moveTo(-hintText.getWidth(), hintText.getY(), duration), Actions.hide()));
        hintText.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                hintText.setVisible(false);
            }
        });
    }

    protected abstract void setup();

    protected void addToBalance(int amount) {
        game.addToBalance(amount);
        leftSide.setBalanceText("" + game.getBalance());
    }

    protected void subtractFromBalance(int amount) {
        addToBalance(-amount);
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
}
