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
    Text sessionBalance;
    BetButton betButton;
    MenuButton menuButton;
    HelpButton helpButton;

    public LeftSide(final CasinoPracticeGame game, Assets assets) {
        this.game = game;
        this.assets = assets;

        backgroundImage = new Image(assets.getTexture(Assets.TEX_NAME.LEFT_SIDE));

        betButton = new BetButton(assets);
        betButton.setPosition(0, 0);
        betButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (betButton.isInside(x, y)) {
                    int amount = betButton.increaseAmount();
                    if (amount > game.getTableMaximum()) {
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

        helpButton = new HelpButton(assets);


        balance = new Text(assets, "", 1.0f);
        wager = new Text(assets, "0", 1.5f);
        won = new Text(assets, "0", 1.5f);
        sessionBalance = new Text(assets, "0", 1.0f);

        updateBalance();

        addActor(backgroundImage);
        addActor(betButton);
        addActor(balance);
        addActor(wager);
        addActor(won);
        addActor(sessionBalance);
        addActor(menuButton);
        addActor(helpButton);

    }

    @Override
    public void setStage(Stage stage) {
        if(stage != null) {
            super.setStage(stage);
            balance.setPosition(30, stage.getHeight() - 100);
            wager.setPosition(30, stage.getHeight() - 215);
            won.setPosition(30, stage.getHeight() - 325);
            sessionBalance.setPosition(30, stage.getHeight() - 430);
            backgroundImage.setColor(this.getColor());
            menuButton.setPosition(30, stage.getHeight() - 600);
            menuButton.setColor(this.getColor());
            helpButton.setPosition(68, stage.getHeight() - 750);
            helpButton.setColor(this.getColor());
        }
    }

    public int getBetAmount() {
        return betButton.getAmount();
    }

    public void setWagerText(String text) {
        wager.setText(text);
    }

    public void updateBalance() {
        if(game.getBalance() == (int)game.getBalance()) {
            balance.setText(String.format("%.0f", game.getBalance()));
        } else {
            balance.setText(String.format("%.2f", game.getBalance()));
        }
        if(game.getBalance() < 500000000) {
            balance.setScale(1.0f);
        } else {
            balance.setScale(.75f);
        }
        
        // update the session winning as well
        if(game.getSessionBalance() > 0) {
            sessionBalance.setColor(Color.GREEN);
        } else if (game.getSessionBalance() == 0) {
            sessionBalance.setColor(Color.WHITE);
        } else {
            sessionBalance.setColor(Color.RED);
        }

        if(game.getSessionBalance() == (int)game.getSessionBalance()) {
            sessionBalance.setText(String.format("%.0f", game.getSessionBalance()));
        } else {
            sessionBalance.setText(String.format("%.2f", game.getSessionBalance()));
        }
        if(game.getSessionBalance() < 500000000) {
            sessionBalance.setScale(1.0f);
        } else {
            sessionBalance.setScale(.75f);
        }
        
    }

    public void setWonText(String text) {
        won.setText(text);
    }

    public void setWonColor(Color color) {
        won.setColor(color);
    }
}
