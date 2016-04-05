package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thirteensecondstoburn.CasinoPractice.Actors.MenuButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.MessagePopup;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.BillingException;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IInternalApplicationBilling;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IPurchaseListener;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.ProductDetails;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.PurchaseDetails;

import java.util.List;

/**
 * Created by Nick on 4/1/2016.
 */
public class StoreScreen implements Screen, IPurchaseListener {
    Assets assets;
    CasinoPracticeGame game;
    Skin skin;
    Sprite background;
    Text loadingText;
    Table outerTable;
    Table buyingTable;
    IPurchaseListener purchaseListener;
    Window errorWindow;
    Label errorMessage;
    Label currentBalance;
    MessagePopup boughtPopup;

    Stage stage = new Stage(new FitViewport(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT));

    public StoreScreen(CasinoPracticeGame game) {
        this.game = game;
        assets = game.getAssets();
        skin = assets.getSkin();
        purchaseListener = this;
    }

    public void doneLoadingBillingItems(List<ProductDetails> productDetailsList, String errorMessage) {
        if(errorMessage != null) {
            loadingText.setScale(1);
            loadingText.setText(errorMessage);
        } else {
            buyingTable.clearChildren();
            buyingTable.defaults().pad(5);

            Color buttonColor = skin.getColor("menuButtonColor");

            Label chipsTitle = new Label("Run out of chips?\nCant wait until tomorrow?\nBuy more now!", skin);

            Button buy5000ChipsButton = new Button(skin);
            buy5000ChipsButton.setColor(buttonColor);
            buy5000ChipsButton.add(new Image(assets.getTexture(Assets.TEX_NAME.BUY_5000_CHIPS)));
            buy5000ChipsButton.pad(10);
            buy5000ChipsButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    CasinoPracticeGame.billing.beginPurchase(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_5000.toString(), purchaseListener);
                }
            });
            Label buy5000ChipsPrice = new Label(getPriceForSku(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_5000, productDetailsList), skin);

            Button buy25000ChipsButton = new Button(skin);
            buy25000ChipsButton.setColor(buttonColor);
            buy25000ChipsButton.add(new Image(assets.getTexture(Assets.TEX_NAME.BUY_25000_CHIPS)));
            buy25000ChipsButton.pad(10);
            buy25000ChipsButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    CasinoPracticeGame.billing.beginPurchase(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_25000.toString(), purchaseListener);
                }
            });
            Label buy25000ChipsPrice = new Label(getPriceForSku(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_25000, productDetailsList), skin);

            Button buy100000ChipsButton = new Button(skin);
            buy100000ChipsButton.setColor(buttonColor);
            buy100000ChipsButton.add(new Image(assets.getTexture(Assets.TEX_NAME.BUY_100000_CHIPS)));
            buy100000ChipsButton.pad(10);
            buy100000ChipsButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    CasinoPracticeGame.billing.beginPurchase(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_100000.toString(), purchaseListener);
                }
            });
            Label buy100000ChipsPrice = new Label(getPriceForSku(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_100000, productDetailsList), skin);

            Button buy500000ChipsButton = new Button(skin);
            buy500000ChipsButton.setColor(buttonColor);
            buy500000ChipsButton.add(new Image(assets.getTexture(Assets.TEX_NAME.BUY_500000_CHIPS)));
            buy500000ChipsButton.pad(10);
            buy500000ChipsButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    CasinoPracticeGame.billing.beginPurchase(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_500000.toString(), purchaseListener);
                }
            });
            Label buy500000ChipsPrice = new Label(getPriceForSku(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_500000, productDetailsList), skin);

            Button buy5000000ChipsButton = new Button(skin);
            buy5000000ChipsButton.setColor(buttonColor);
            buy5000000ChipsButton.add(new Image(assets.getTexture(Assets.TEX_NAME.BUY_5000000_CHIPS)));
            buy5000000ChipsButton.pad(10);
            buy5000000ChipsButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    CasinoPracticeGame.billing.beginPurchase(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_5000000.toString(), purchaseListener);
                }
            });
            Label buy5000000ChipsPrice = new Label(getPriceForSku(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_5000000, productDetailsList), skin);

            Label disclaimer = new Label("* Chips are used for in game use only and hold no actual monetary value once purchased", skin);

            buyingTable.add(chipsTitle).colspan(2).left();
            buyingTable.row();
            buyingTable.add(buy5000ChipsButton);
            buyingTable.add(buy5000ChipsPrice).left().expand();
            buyingTable.row();
            buyingTable.add(buy25000ChipsButton);
            buyingTable.add(buy25000ChipsPrice).left();
            buyingTable.row();
            buyingTable.add(buy100000ChipsButton);
            buyingTable.add(buy100000ChipsPrice).left();
            buyingTable.row();
            buyingTable.add(buy500000ChipsButton);
            buyingTable.add(buy500000ChipsPrice).left();
            buyingTable.row();
            buyingTable.add(buy5000000ChipsButton);
            buyingTable.add(buy5000000ChipsPrice).left();
            buyingTable.row();
            buyingTable.add(disclaimer).colspan(2);
        }
    }

    private String getPriceForSku(IInternalApplicationBilling.BillingProduct product, List<ProductDetails> productDetailsList) {
        for (ProductDetails detail : productDetailsList) {
            if(detail.getSku().equals(product.toString())) {
                return detail.getPrice();
            }
        }
        return "ERROR: NOT FOUND";
    }

    @Override
    public void show() {
        stage.addAction(Actions.fadeIn(0.5f));
        Gdx.input.setInputProcessor(stage);
        Color menuButtonColor = skin.getColor("menuButtonColor");

        errorWindow = new Window("", skin);
        errorWindow.setVisible(false);
        errorWindow.setWidth(stage.getWidth() - 100);
        errorWindow.setHeight(stage.getHeight() - 80);
        errorWindow.setPosition(50, 40);
        errorWindow.setModal(true);

        Texture eback = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        eback.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Sprite errorBackground = new Sprite(eback, (int)errorWindow.getWidth(), (int)errorWindow.getHeight());
        errorBackground.setSize(errorWindow.getWidth(), errorWindow.getHeight());
        errorBackground.setColor(Color.GRAY);

        errorWindow.setBackground(new SpriteDrawable(errorBackground));

        errorMessage = new Label("", skin);
        errorMessage.setWrap(true);

        Button errorCancelButton = new Button(skin);
        errorCancelButton.setColor(menuButtonColor);
        Label closeLabel = new Label("Close", skin);
        closeLabel.setColor(Color.BLACK);
        errorCancelButton.add(closeLabel);
        errorCancelButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                errorWindow.setVisible(false);
            }
        });

        errorWindow.add(new Label("Purchase Error", skin));
        errorWindow.row();
        errorWindow.add(errorMessage).center().fill().expand().padLeft(30).padRight(30);
        errorWindow.row();
        errorWindow.add(errorCancelButton).center();


        Texture back = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setColor(assets.getBackgroundColor());

        outerTable = new Table(skin);

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

        outerTable.add(menuButton).left().pad(20);
        outerTable.add(currentBalance).right().pad(20);
        outerTable.row();

        buyingTable = new Table(skin);

//        outerTable.debug();
//        buyingTable.debug();

        loadingText = new Text(assets, "Loading store items", 4.0f);
        buyingTable.add(loadingText).width(1400).height(100).fill().expand();
        outerTable.add(buyingTable).colspan(2).expand();
        outerTable.setFillParent(true);

        boughtPopup = new MessagePopup(assets);

        stage.addActor(outerTable);
        stage.addActor(boughtPopup);
        stage.addActor(errorWindow);
        LoadBillingItemsAsync();
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

    @Override
    public void onPurchased(PurchaseDetails details) {
        if(!details.isSuccess()) {
            errorWindow.setVisible(true);
            errorMessage.setText(details.getMessage());
            return;
        }
        boughtPopup.pop(MessagePopup.Message.WIN, stage.getWidth()/2, stage.getHeight()/2, 10);
        System.out.println("Notified of a purchase attempt: " + details.getSku());
    }

    @Override
    public void onConsumed(PurchaseDetails details) {
        if(!details.isSuccess() || !details.isConsumed()) {
            errorWindow.setVisible(true);
            errorMessage.setText(details.getMessage());
            return;
        }

        // ok we have a consumed purchase. Give the user more chips
        if(details.getSku().equals(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_5000.toString())) {
            game.addToBalance(5000);
        } else if(details.getSku().equals(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_25000.toString())) {
            game.addToBalance(25000);
        } else if(details.getSku().equals(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_100000.toString())) {
            game.addToBalance(100000);
        } else if(details.getSku().equals(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_500000.toString())) {
            game.addToBalance(500000);
        } else if(details.getSku().equals(IInternalApplicationBilling.BillingProduct.SKU_CHIPS_5000000.toString())) {
            game.addToBalance(5000000);
        } else {
            System.out.println("Unknown consumable: " + details.getSku());
        }
        boughtPopup.pop(MessagePopup.Message.WIN, stage.getWidth()/2, stage.getHeight()/2, 10);
        updateBalanceLabel();
    }
}
