package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStack;
import com.thirteensecondstoburn.CasinoPractice.Actors.Die;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.WinLosePopup;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

/**
 * Created by Nick on 4/15/2015.
 */
public class CrapsScreen extends TableScreen implements ActionCompletedListener {

    public CrapsScreen(CasinoPracticeGame game) {
        super(game);
    }

    Image tableImage;

    TableButton rollButton;

    Die die1;
    Die die2;
    
    // woo! 50000000 chipstacks in craps!
    ChipStack passLineStack;
    WinLosePopup passLinePopup;
    ChipStack passLineOddsStack;
    WinLosePopup passLineOddsPopup;
    ChipStack dontPassLineStack;
    WinLosePopup dontPassLinePopup;
    ChipStack dontPassLineOddsStack;
    WinLosePopup dontPassLineOddsPopup;

    @Override
    public void actionCompleted(Actor caller) {

    }

    @Override
    protected void setup() {
        Texture tableTex = assets.getTexture(Assets.TEX_NAME.CRAPS_TABLE);
        tableImage = new Image(tableTex);
        //tableImage.setColor(mainColor);
        tableImage.setPosition(280, 40);

        stage.addListener(new ActorGestureListener() {
            float startX = 0; float startY = 0;
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startX = x;
                startY = y;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Rectangle passLine1 = new Rectangle(732, 150, 892, 74);
                Rectangle passLine2 = new Rectangle(1556, 150, 68, 778);
                Rectangle dontPassLine1 = new Rectangle(732, 230, 818, 68);
                Rectangle dontPassLine2 = new Rectangle(1468, 230, 82, 698);
                Rectangle field = new Rectangle(732, 304, 728, 206);
                Rectangle come = new Rectangle(732, 520, 728, 144);
                Rectangle dontCome = new Rectangle(1296, 670, 164, 258);
                Rectangle lay4 = new Rectangle(288, 872, 162, 56);
                Rectangle lay5 = new Rectangle(454, 872, 162, 56);
                Rectangle lay6 = new Rectangle(622, 872, 162, 56);
                Rectangle lay8 = new Rectangle(790, 872, 162, 56);
                Rectangle lay9 = new Rectangle(960, 872, 162, 56);
                Rectangle lay10 = new Rectangle(1128, 872, 162, 56);
                Rectangle buy4 = new Rectangle(286, 672, 160, 42);
                Rectangle place5 = new Rectangle(456, 672, 160, 42);
                Rectangle place6 = new Rectangle(622, 672, 160, 42);
                Rectangle place8 = new Rectangle(790, 672, 166, 42);
                Rectangle place9 = new Rectangle(960, 670, 160, 42);
                Rectangle buy10 = new Rectangle(1128, 672, 160, 42);
                Rectangle hard6 = new Rectangle(290, 538, 207, 86);
                Rectangle hard8 = new Rectangle(506, 540, 208, 84);
                Rectangle hard4 = new Rectangle(290, 426, 207, 104);
                Rectangle hard10 = new Rectangle(506, 426, 206, 104);
                Rectangle any7 = new Rectangle(290, 352, 421, 40);
                Rectangle snakeEyes = new Rectangle(290, 292, 207, 54);
                Rectangle boxCars = new Rectangle(503, 290, 208, 56);
                Rectangle three = new Rectangle(290, 220, 207, 62);
                Rectangle eleven = new Rectangle(503, 220, 210, 60);
                Rectangle anyCraps = new Rectangle(290, 158, 421, 53);
                Rectangle four = new Rectangle(286, 722, 162, 140);
                Rectangle five = new Rectangle(454, 724, 160, 140);
                Rectangle six = new Rectangle(624, 724, 158, 140);
                Rectangle eight = new Rectangle(792, 724, 162, 140);
                Rectangle nine = new Rectangle(962, 724, 162, 140);
                Rectangle ten = new Rectangle(1130, 724, 160, 140);

                if (passLine1.contains(x, y) || passLine2.contains(x, y)) {
                    System.out.println("PASS LINE");
                } else if (dontPassLine1.contains(x, y) || dontPassLine2.contains(x, y)) {
                    System.out.println("DONT PASS LINE");
                } else if (field.contains(x, y)) {
                    System.out.println("FIELD");
                } else if (come.contains(x, y)) {
                    System.out.println("COME");
                } else if (dontCome.contains(x, y)) {
                    System.out.println("DONT COME");
                } else if (lay4.contains(x, y)) {
                    System.out.println("LAY 4");
                } else if (lay5.contains(x, y)) {
                    System.out.println("LAY 5");
                } else if (lay6.contains(x, y)) {
                    System.out.println("LAY 6");
                } else if (lay8.contains(x, y)) {
                    System.out.println("LAY 8");
                } else if (lay9.contains(x, y)) {
                    System.out.println("LAY 9");
                } else if (lay10.contains(x, y)) {
                    System.out.println("Lay 10");
                } else if (buy4.contains(x, y)) {
                    System.out.println("BUY 4");
                } else if (place5.contains(x, y)) {
                    System.out.println("PLACE 5");
                } else if (place6.contains(x, y)) {
                    System.out.println("PLACE 6");
                } else if (place8.contains(x, y)) {
                    System.out.println("PLACE 8");
                } else if (place9.contains(x, y)) {
                    System.out.println("PLACE 9");
                } else if (buy10.contains(x, y)) {
                    System.out.println("BUY 10");
                } else if (hard6.contains(x, y)) {
                    System.out.println("HARD 6");
                } else if (hard8.contains(x, y)) {
                    System.out.println("HARD 8");
                } else if (hard4.contains(x, y)) {
                    System.out.println("HARD 4");
                } else if (hard10.contains(x, y)) {
                    System.out.println("HARD 10");
                } else if (any7.contains(x, y)) {
                    System.out.println("ANY 7");
                } else if (anyCraps.contains(x, y)) {
                    System.out.println("ANY CRAPS");
                } else if (snakeEyes.contains(x, y)) {
                    System.out.println("SNAKE EYES");
                } else if (boxCars.contains(x, y)) {
                    System.out.println("BOX CARS");
                } else if (three.contains(x, y)) {
                    System.out.println("THREE");
                } else if (eleven.contains(x, y)) {
                    System.out.println("ELEVEN");
                } else if (four.contains(x, y)) {
                    System.out.println("FOUR");
                } else if (five.contains(x, y)) {
                    System.out.println("FIVE");
                } else if (six.contains(x, y)) {
                    System.out.println("SIX");
                } else if (eight.contains(x, y)) {
                    System.out.println("EIGHT");
                } else if (nine.contains(x, y)) {
                    System.out.println("NINE");
                } else if (ten.contains(x, y)) {
                    System.out.println("TEN");
                }
            }

        });

        rollButton = new TableButton(assets, "Roll", mainColor);
        rollButton.setPosition(stage.getWidth() - rollButton.getWidth(), 0);
        rollButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (rollButton.isInside(x, y)) {
                    die1.rollDie();
                    die1.setVisible(true);
                    die2.rollDie();
                    die2.setVisible(true);
                }
            }
        });

        die1 = new Die(game);
        die1.setPosition(stage.getWidth() /2 - 110, stage.getHeight() - 115);
        die1.setVisible(false);
        die2 = new Die(game);
        die2.setPosition(stage.getWidth() /2 + 10, stage.getHeight() - 115);
        die2.setVisible(false);

        stage.addActor(tableImage);
        stage.addActor(rollButton);
        stage.addActor(die1);
        stage.addActor(die2);

    }
}
