package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Screens.MenuScreen;

/**
 * Created by Nick on 2/3/2015.
 */
public class LeftSide extends Group {
    CasinoPracticeGame game;
    Assets assets;

    Image backgroundImage;
    Text wager;
    Text balance;
    Text won;
    BetButton betButton;
    MenuButton menuButton;

    public LeftSide(final CasinoPracticeGame game, Assets assets) {
        this.game = game;
        this.assets = assets;

        backgroundImage = new Image(assets.getTexture(Assets.TEX_NAME.LEFT_SIDE));

        betButton = new BetButton(assets);
        betButton.setPosition(0,0);
        betButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (betButton.isInside(x, y)) {
                    int amount = betButton.increaseAmount();
                    if(amount > ChipStack.TABLE_MAX) {
                        betButton.resetToMin();
                    }
                }
            }
        });

        menuButton = new MenuButton(assets);
        menuButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getStage().addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        MenuScreen screen = game.getMenuScreen();
                        if(screen.getStage() != null) {
                            screen.getStage().addAction(Actions.fadeIn(0.5f));
                        }
                        getStage().clear();
                        game.setScreen(screen);
                    }
                })));
            }
        });

        balance = new Text(assets, "" + game.getBalance(), 1.0f);
        wager = new Text(assets, "0", 1.5f);
        won = new Text(assets, "0", 1.5f);

        addActor(backgroundImage);
        addActor(betButton);
        addActor(balance);
        addActor(wager);
        addActor(won);
        addActor(menuButton);

    }

    @Override
    public void setStage(Stage stage) {
        if(stage != null) {
            super.setStage(stage);
            balance.setPosition(30, stage.getHeight() - 100);
            wager.setPosition(30, stage.getHeight() - 215);
            won.setPosition(30, stage.getHeight() - 325);
            backgroundImage.setColor(this.getColor());
            menuButton.setPosition(30, stage.getHeight() - 500);
            menuButton.setColor(this.getColor());
        }
    }

    public int getBetAmount() {
        return betButton.getAmount();
    }

    public void setWagerText(String text) {
        wager.setText(text);
    }

    public void setBalanceText(String text) {
        balance.setText(text);
    }

    public void setWonText(String text) {
        won.setText(text);
    }

    public void setWonColor(Color color) {
        won.setColor(color);
    }
}
