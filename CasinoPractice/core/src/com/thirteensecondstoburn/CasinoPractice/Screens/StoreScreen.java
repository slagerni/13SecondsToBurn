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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thirteensecondstoburn.CasinoPractice.Actors.MenuButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.BillingException;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.ProductDetails;

import java.util.List;

/**
 * Created by Nick on 4/1/2016.
 */
public class StoreScreen implements Screen {
    Assets assets;
    CasinoPracticeGame game;
    Skin skin;
    Sprite background;
    Text loadingText;
    Table innerTable;

    Stage stage = new Stage(new FitViewport(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT));

    public StoreScreen(CasinoPracticeGame game) {
        this.game = game;
        assets = game.getAssets();
        skin = assets.getSkin();
    }

    public void doneLoadingBillingItems(List<ProductDetails> productDetailsList, String errorMessage) {
        if(errorMessage != null) {
            loadingText.setScale(1);
            loadingText.setText(errorMessage);
        } else {
            // TODO remove the loading text and actually add some clickable buttons
            loadingText.setText("" + productDetailsList.size());
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Color menuButtonColor = skin.getColor("menuButtonColor");

        Texture back = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setColor(assets.getBackgroundColor());

        Table outerTable = new Table(skin);

        MenuButton menuButton = new MenuButton(assets);
        menuButton.setColor(Color.YELLOW);
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
        outerTable.add(menuButton).left().pad(20);
        outerTable.row();

        innerTable = new Table(skin);

        outerTable.debug();
        innerTable.debug();

        loadingText = new Text(assets, "Loading store items", 4.0f);
        innerTable.add(loadingText).width(1400).height(100).fill().expand();
        outerTable.add(innerTable).expand();
        outerTable.setFillParent(true);

        stage.addActor(outerTable);

        LoadBillingItemsAsync();
    }

    public void LoadBillingItemsAsync() {
        (new Thread(new Runnable() {
            public void run() {
                List<ProductDetails> productDetailsList = null;
                String errorMessage = null;

                try {
                    productDetailsList = CasinoPracticeGame.billing.listProductDetails();
                } catch (BillingException e) {
                    errorMessage = e.getMessage();
                }

                doneLoadingBillingItems(productDetailsList, errorMessage);
            }
        })).start();
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
