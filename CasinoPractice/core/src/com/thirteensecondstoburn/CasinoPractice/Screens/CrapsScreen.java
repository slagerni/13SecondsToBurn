package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStackGroup;
import com.thirteensecondstoburn.CasinoPractice.Actors.Die;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

/**
 * Created by Nick on 4/15/2015.
 */
public class CrapsScreen extends TableScreen implements ActionCompletedListener {

    public CrapsScreen(CasinoPracticeGame game) {
        super(game);
    }

    int thePoint = -1;

    Image tableImage;

    TableButton rollButton;

    Die die1;
    Die die2;

    Image onChip;
    Image offChip;
    
    // woo! 50000000 chipstacks in craps!
    ChipStackGroup passLineStack;
    ChipStackGroup passLineOddsStack;
    ChipStackGroup dontPassLineStack;
    ChipStackGroup dontPassLineOddsStack;
    ChipStackGroup comeStack;
    ChipStackGroup dontComeStack;
    ChipStackGroup fieldStack;
    ChipStackGroup lay4Stack;
    ChipStackGroup lay5Stack;
    ChipStackGroup lay6Stack;
    ChipStackGroup lay8Stack;
    ChipStackGroup lay9Stack;
    ChipStackGroup lay10Stack;
    ChipStackGroup buy4Stack;
    ChipStackGroup place5Stack;
    ChipStackGroup place6Stack;
    ChipStackGroup place8Stack;
    ChipStackGroup place9Stack;
    ChipStackGroup buy10Stack;
    ChipStackGroup hard4Stack;
    ChipStackGroup hard10Stack;
    ChipStackGroup hard6Stack;
    ChipStackGroup hard8Stack;
    ChipStackGroup any7Stack;
    ChipStackGroup snakeEyesStack;
    ChipStackGroup boxCarsStack;
    ChipStackGroup threeStack;
    ChipStackGroup elevenStack;
    ChipStackGroup anyCrapsStack;
    ChipStackGroup come4Stack;
    ChipStackGroup come4OddsStack;
    ChipStackGroup come5Stack;
    ChipStackGroup come5OddsStack;
    ChipStackGroup come6Stack;
    ChipStackGroup come6OddsStack;
    ChipStackGroup come8Stack;
    ChipStackGroup come8OddsStack;
    ChipStackGroup come9Stack;
    ChipStackGroup come9OddsStack;
    ChipStackGroup come10Stack;
    ChipStackGroup come10OddsStack;
    ChipStackGroup dontCome4Stack;
    ChipStackGroup dontCome4OddsStack;
    ChipStackGroup dontCome5Stack;
    ChipStackGroup dontCome5OddsStack;
    ChipStackGroup dontCome6Stack;
    ChipStackGroup dontCome6OddsStack;
    ChipStackGroup dontCome8Stack;
    ChipStackGroup dontCome8OddsStack;
    ChipStackGroup dontCome9Stack;
    ChipStackGroup dontCome9OddsStack;
    ChipStackGroup dontCome10Stack;
    ChipStackGroup dontCome10OddsStack;




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
//            float startx=0, starty=0;
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                startx = x;
//                starty = y;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("Rectangle passLine1 = new Rectangle(" + (int)startx + ", " + (int)starty + ", " + (int)(x - startx) + ", " + (int)(y - starty) + ");");
                System.out.println((int)x + ", " + (int)y);

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

                Rectangle any7 = new Rectangle(288, 336, 426, 64);
                Rectangle snakeEyes = new Rectangle(288, 242, 211, 86);
                Rectangle boxCars = new Rectangle(503, 242, 210, 86);
                Rectangle three = new Rectangle(290, 128, 209, 106);
                Rectangle eleven = new Rectangle(506, 130, 208, 103);
                Rectangle anyCraps = new Rectangle(288, 48, 426, 72);
                Rectangle four = new Rectangle(286, 722, 162, 140);
                Rectangle five = new Rectangle(454, 724, 160, 140);
                Rectangle six = new Rectangle(624, 724, 158, 140);
                Rectangle eight = new Rectangle(792, 724, 162, 140);
                Rectangle nine = new Rectangle(962, 724, 162, 140);
                Rectangle ten = new Rectangle(1130, 724, 160, 140);

                if (passLine1.contains(x, y) || passLine2.contains(x, y)) {
                    System.out.println("PASS LINE");
                    if(thePoint == -1) { // no point? work the pass line stack
                        if(dontPassLineStack.getTotal() > 0) {
                            showHint("You can't bet on both the pass and don't pass lines. Please clear to switch.");
                            return;
                        }
                        int newTotal = passLineStack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = passLineStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        passLineStack.setTotal(newTotal);
                    } else { // point established? all you can do it mess w/ the odds
                        int newTotal = passLineOddsStack.getTotal() + leftSide.getBetAmount();
                        int odds = 3;
                        if(thePoint == 5 || thePoint == 9) odds = 4;
                        if(thePoint == 6 || thePoint == 8) odds = 5;
                        if(newTotal > passLineStack.getTotal() * odds) {
                            showHint("You can only take " + odds + "x your pass line bet when the point is " + thePoint + ". Setting to max odds");
                            newTotal = passLineStack.getTotal() * odds;
                        }
                        int oldTotal = passLineOddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        passLineOddsStack.setTotal(newTotal);
                    }
                } else if (dontPassLine1.contains(x, y) || dontPassLine2.contains(x, y)) {
                    System.out.println("DONT PASS LINE");
                    if(thePoint == -1) { // no point? work the dontPass line stack
                        if(passLineStack.getTotal() > 0) {
                            showHint("You can't bet on both the pass and don't pass lines. Please clear to switch.");
                            return;
                        }
                        int newTotal = dontPassLineStack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = dontPassLineStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        dontPassLineStack.setTotal(newTotal);
                    } else { // point established? all you can do it mess w/ the odds
                        int newTotal = dontPassLineOddsStack.getTotal() + leftSide.getBetAmount();
                        if(newTotal > dontPassLineStack.getTotal() * 6) {
                            showHint("You can only lay 6x your don't pass line bet when the point is " + thePoint + ". Setting to max odds");
                            newTotal = dontPassLineStack.getTotal() * 6;
                        }
                        int oldTotal = dontPassLineOddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        dontPassLineOddsStack.setTotal(newTotal);
                    }
                } else if (field.contains(x, y)) {
                    System.out.println("FIELD");
                    int newTotal = fieldStack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = fieldStack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    fieldStack.setTotal(newTotal);
                } else if (come.contains(x, y)) {
                    System.out.println("COME");
                    if(thePoint != -1) {
                        int newTotal = comeStack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = comeStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        comeStack.setTotal(newTotal);                        
                    } // no come bets if there isn't a point established
                } else if (dontCome.contains(x, y)) {
                    System.out.println("DONT COME");
                    if(thePoint != -1) {
                        int newTotal = dontComeStack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = dontComeStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        dontComeStack.setTotal(newTotal);
                    } // no dontCome bets if there isn't a point established
                } else if (lay4.contains(x, y)) {
                    System.out.println("LAY 4");
                    int newTotal = lay4Stack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = lay4Stack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    lay4Stack.setTotal(newTotal);
                } else if (lay5.contains(x, y)) {
                    System.out.println("LAY 5");
                    int newTotal = lay5Stack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = lay5Stack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    lay5Stack.setTotal(newTotal);
                } else if (lay6.contains(x, y)) {
                    System.out.println("LAY 6");
                    int newTotal = lay6Stack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = lay6Stack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    lay6Stack.setTotal(newTotal);
                } else if (lay8.contains(x, y)) {
                    System.out.println("LAY 8");
                    int newTotal = lay8Stack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = lay8Stack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    lay8Stack.setTotal(newTotal);
                } else if (lay9.contains(x, y)) {
                    System.out.println("LAY 9");
                    int newTotal = lay9Stack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = lay9Stack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    lay9Stack.setTotal(newTotal);
                } else if (lay10.contains(x, y)) {
                    System.out.println("Lay 10");
                    int newTotal = lay10Stack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = lay10Stack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    lay10Stack.setTotal(newTotal);
                } else if (buy4.contains(x, y)) {
                    System.out.println("BUY 4");
                    if(thePoint != -1) {
                        int newTotal = buy4Stack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = buy4Stack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        buy4Stack.setTotal(newTotal);
                    }
                } else if (place5.contains(x, y)) {
                    if(thePoint != -1) {
                        System.out.println("PLACE 5");
                        int newTotal = place5Stack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = place5Stack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        place5Stack.setTotal(newTotal);
                    }
                } else if (place6.contains(x, y)) {
                    System.out.println("PLACE 6");
                    if(thePoint != -1) {
                        int newTotal = place6Stack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = place6Stack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        place6Stack.setTotal(newTotal);
                    }
                } else if (place8.contains(x, y)) {
                    System.out.println("PLACE 8");
                    if(thePoint != -1) {
                        int newTotal = place8Stack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = place8Stack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        place8Stack.setTotal(newTotal);
                    }
                } else if (place9.contains(x, y)) {
                    System.out.println("PLACE 9");
                    if(thePoint != -1) {
                        int newTotal = place9Stack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = place9Stack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        place9Stack.setTotal(newTotal);
                    }
                } else if (buy10.contains(x, y)) {
                    System.out.println("BUY 10");
                    if(thePoint != -1) {
                        int newTotal = buy10Stack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = buy10Stack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        buy10Stack.setTotal(newTotal);
                    }
                } else if (hard6.contains(x, y)) {
                    System.out.println("HARD 6");
                    if(thePoint != -1) {
                        int newTotal = hard6Stack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = hard6Stack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        hard6Stack.setTotal(newTotal);
                    }
                } else if (hard8.contains(x, y)) {
                    System.out.println("HARD 8");
                    if(thePoint != -1) {
                        int newTotal = hard8Stack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = hard8Stack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        hard8Stack.setTotal(newTotal);
                    }
                } else if (hard4.contains(x, y)) {
                    System.out.println("HARD 4");
                    if(thePoint != -1) {
                        int newTotal = hard4Stack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = hard4Stack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        hard4Stack.setTotal(newTotal);
                    }
                } else if (hard10.contains(x, y)) {
                    System.out.println("HARD 10");
                    if(thePoint != -1) {
                        int newTotal = hard10Stack.getTotal() + leftSide.getBetAmount();
                        newTotal = checkTableMax(newTotal);
                        int oldTotal = hard10Stack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        hard10Stack.setTotal(newTotal);
                    }
                } else if (any7.contains(x, y)) {
                    System.out.println("ANY 7");
                    int newTotal = any7Stack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = any7Stack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    any7Stack.setTotal(newTotal);
                } else if (anyCraps.contains(x, y)) {
                    System.out.println("ANY CRAPS");
                    int newTotal = anyCrapsStack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = anyCrapsStack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    anyCrapsStack.setTotal(newTotal);
                } else if (snakeEyes.contains(x, y)) {
                    System.out.println("SNAKE EYES");
                    int newTotal = snakeEyesStack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = snakeEyesStack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    snakeEyesStack.setTotal(newTotal);
                } else if (boxCars.contains(x, y)) {
                    System.out.println("BOX CARS");
                    int newTotal = boxCarsStack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = boxCarsStack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    boxCarsStack.setTotal(newTotal);
                } else if (three.contains(x, y)) {
                    System.out.println("THREE");
                    int newTotal = threeStack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = threeStack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    threeStack.setTotal(newTotal);
                } else if (eleven.contains(x, y)) {
                    System.out.println("ELEVEN");
                    int newTotal = elevenStack.getTotal() + leftSide.getBetAmount();
                    newTotal = checkTableMax(newTotal);
                    int oldTotal = elevenStack.getTotal();
                    game.subtractFromBalance(newTotal - oldTotal);
                    elevenStack.setTotal(newTotal);
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

                calculateWager();
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

                    int total = die1.getRolledNumber() + die2.getRolledNumber();
                    boolean hardWay = die1.getRolledNumber() == die2.getRolledNumber();

                    System.out.println("Rolled: " + total + " hardway: " + hardWay);

                    if(total > 3 && total < 11 && total != 7) {
                        if(thePoint == -1) {
                            thePoint = total;
                            offChip.setVisible(false);
                            onChip.setVisible(true);
                            switch (total) {
                                case 4:
                                    onChip.setPosition(320, 760);
                                    break;
                                case 5:
                                    onChip.setPosition(490, 760);
                                    break;
                                case 6:
                                    onChip.setPosition(655, 760);
                                    break;
                                case 8:
                                    onChip.setPosition(824, 760);
                                    break;
                                case 9:
                                    onChip.setPosition(990, 760);
                                    break;
                                case 10:
                                    onChip.setPosition(1164, 760);
                                    break;
                            }
                        } else {
                            if(total == thePoint) { // come roll made
                                thePoint = -1;
                                offChip.setVisible(true);
                                onChip.setVisible(false);
                            }
                        }
                    } else if(total == 7) {
                        thePoint = -1;
                        offChip.setVisible(true);
                        onChip.setVisible(false);
                    }
                }
            }
        });

        die1 = new Die(game);
        die1.setPosition(stage.getWidth() /2 - 110, stage.getHeight() - 115);
        die1.setVisible(false);
        die2 = new Die(game);
        die2.setPosition(stage.getWidth() /2 + 10, stage.getHeight() - 115);
        die2.setVisible(false);

        onChip = new Image(assets.getTexture(Assets.TEX_NAME.CHIP_ON));
        onChip.setVisible(false);
        offChip = new Image(assets.getTexture(Assets.TEX_NAME.CHIP_OFF));
        offChip.layout();
        offChip.setPosition(270, stage.getHeight() - offChip.getImageHeight() - 20);

        passLineStack = new ChipStackGroup(game, assets,1028, 135, .75f);
        passLineOddsStack = new ChipStackGroup(game, assets, 1111, 135, .75f);

        dontPassLineStack = new ChipStackGroup(game, assets, 1028, 215, .75f);
        dontPassLineOddsStack = new ChipStackGroup(game, assets, 1111, 215, .75f);

        comeStack = new ChipStackGroup(game, assets, 1028, 536, .75f);
        dontComeStack = new ChipStackGroup(game, assets, 1340, 772, .75f);
        fieldStack = new ChipStackGroup(game, assets, 1062, 330, .75f);

        lay4Stack = new ChipStackGroup(game, assets, 312, 854, .75f);
        lay5Stack = new ChipStackGroup(game, assets, 478, 854, .75f);
        lay6Stack = new ChipStackGroup(game, assets, 648, 854, .75f);
        lay8Stack = new ChipStackGroup(game, assets, 815, 854, .75f);
        lay9Stack = new ChipStackGroup(game, assets, 986, 854, .75f);
        lay10Stack = new ChipStackGroup(game, assets, 1152, 854, .75f);

        buy4Stack = new ChipStackGroup(game, assets, 312, 655, .75f);
        place5Stack = new ChipStackGroup(game, assets, 478, 655, .75f);
        place6Stack = new ChipStackGroup(game, assets, 648, 655, .75f);
        place8Stack = new ChipStackGroup(game, assets, 815, 655, .75f);
        place9Stack = new ChipStackGroup(game, assets, 986, 655, .75f);
        buy10Stack = new ChipStackGroup(game, assets, 1152, 655, .75f);

        hard4Stack = new ChipStackGroup(game, assets, 336, 410, .75f);
        hard6Stack = new ChipStackGroup(game, assets, 336, 526, .75f);
        hard8Stack = new ChipStackGroup(game, assets, 553, 526, .75f);
        hard10Stack = new ChipStackGroup(game, assets, 553, 410, .75f);

        any7Stack = new ChipStackGroup(game, assets, 449, 324, .75f);
        snakeEyesStack = new ChipStackGroup(game, assets, 336, 226, .75f);
        boxCarsStack = new ChipStackGroup(game, assets, 552, 226, .75f);
        threeStack = new ChipStackGroup(game, assets, 336, 114, .75f);
        elevenStack = new ChipStackGroup(game, assets, 552, 114, .75f);
        anyCrapsStack = new ChipStackGroup(game, assets, 449, 28, .75f);

        come4Stack = new ChipStackGroup(game, assets, 272, 705, .75f);
        come4OddsStack = new ChipStackGroup(game, assets, 352, 705, .75f);
        come5Stack = new ChipStackGroup(game, assets, 440, 705, .75f);
        come5OddsStack = new ChipStackGroup(game, assets, 520, 705, .75f);
        come6Stack = new ChipStackGroup(game, assets, 607, 705, .75f);
        come6OddsStack = new ChipStackGroup(game, assets, 687, 705, .75f);
        come8Stack = new ChipStackGroup(game, assets, 775, 705, .75f);
        come8OddsStack = new ChipStackGroup(game, assets, 855, 705, .75f);
        come9Stack = new ChipStackGroup(game, assets, 945, 705, .75f);
        come9OddsStack = new ChipStackGroup(game, assets, 1025, 705, .75f);
        come10Stack = new ChipStackGroup(game, assets, 1113, 705, .75f);
        come10OddsStack = new ChipStackGroup(game, assets, 1193, 705, .75f);

        dontCome4Stack = new ChipStackGroup(game, assets, 272, 770, .75f);
        dontCome4OddsStack = new ChipStackGroup(game, assets, 352, 770, .75f);
        dontCome5Stack = new ChipStackGroup(game, assets, 440, 770, .75f);
        dontCome5OddsStack = new ChipStackGroup(game, assets, 520, 770, .75f);
        dontCome6Stack = new ChipStackGroup(game, assets, 607, 770, .75f);
        dontCome6OddsStack = new ChipStackGroup(game, assets, 687, 770, .75f);
        dontCome8Stack = new ChipStackGroup(game, assets, 775, 770, .75f);
        dontCome8OddsStack = new ChipStackGroup(game, assets, 855, 770, .75f);
        dontCome9Stack = new ChipStackGroup(game, assets, 945, 770, .75f);
        dontCome9OddsStack = new ChipStackGroup(game, assets, 1025, 770, .75f);
        dontCome10Stack = new ChipStackGroup(game, assets, 1113, 770, .75f);
        dontCome10OddsStack = new ChipStackGroup(game, assets, 1193, 770, .75f);
        // TEMP
        come4Stack.setTotal(20);
        come4OddsStack.setTotal(20);
        come5Stack.setTotal(20);
        come5OddsStack.setTotal(20);
        come6Stack.setTotal(20);
        come6OddsStack.setTotal(20);
        come8Stack.setTotal(20);
        come8OddsStack.setTotal(20);
        come9Stack.setTotal(20);
        come9OddsStack.setTotal(20);
        come10Stack.setTotal(20);
        come10OddsStack.setTotal(20);
        dontCome4Stack.setTotal(20);
        dontCome4OddsStack.setTotal(20);
        dontCome5Stack.setTotal(20);
        dontCome5OddsStack.setTotal(20);
        dontCome6Stack.setTotal(20);
        dontCome6OddsStack.setTotal(20);
        dontCome8Stack.setTotal(20);
        dontCome8OddsStack.setTotal(20);
        dontCome9Stack.setTotal(20);
        dontCome9OddsStack.setTotal(20);
        dontCome10Stack.setTotal(20);
        dontCome10OddsStack.setTotal(20);

        stage.addActor(tableImage);
        stage.addActor(rollButton);
        stage.addActor(die1);
        stage.addActor(die2);
        stage.addActor(onChip);
        stage.addActor(offChip);

        stage.addActor(passLineStack);
        stage.addActor(passLineOddsStack);
        stage.addActor(dontPassLineStack);
        stage.addActor(dontPassLineOddsStack);
        stage.addActor(comeStack);
        stage.addActor(dontComeStack);
        stage.addActor(fieldStack);
        stage.addActor(lay4Stack);
        stage.addActor(lay5Stack);
        stage.addActor(lay6Stack);
        stage.addActor(lay8Stack);
        stage.addActor(lay9Stack);
        stage.addActor(lay10Stack);
        stage.addActor(hard4Stack);
        stage.addActor(hard6Stack);
        stage.addActor(hard8Stack);
        stage.addActor(hard10Stack);
        stage.addActor(any7Stack);
        stage.addActor(snakeEyesStack);
        stage.addActor(boxCarsStack);
        stage.addActor(threeStack);
        stage.addActor(elevenStack);
        stage.addActor(anyCrapsStack);
        stage.addActor(dontCome4Stack);
        stage.addActor(dontCome4OddsStack);
        stage.addActor(dontCome5Stack);
        stage.addActor(dontCome5OddsStack);
        stage.addActor(dontCome6Stack);
        stage.addActor(dontCome6OddsStack);
        stage.addActor(dontCome8Stack);
        stage.addActor(dontCome8OddsStack);
        stage.addActor(dontCome9Stack);
        stage.addActor(dontCome9OddsStack);
        stage.addActor(dontCome10Stack);
        stage.addActor(dontCome10OddsStack);
        stage.addActor(come4Stack);
        stage.addActor(come4OddsStack);
        stage.addActor(come5Stack);
        stage.addActor(come5OddsStack);
        stage.addActor(come6Stack);
        stage.addActor(come6OddsStack);
        stage.addActor(come8Stack);
        stage.addActor(come8OddsStack);
        stage.addActor(come9Stack);
        stage.addActor(come9OddsStack);
        stage.addActor(come10Stack);
        stage.addActor(come10OddsStack);
        stage.addActor(buy4Stack);
        stage.addActor(place5Stack);
        stage.addActor(place6Stack);
        stage.addActor(place8Stack);
        stage.addActor(place9Stack);
        stage.addActor(buy10Stack);

    }

    private void calculateWager() {
        int total = 0;
        total += passLineStack.getTotal();
        total += passLineOddsStack.getTotal();
        total += dontPassLineStack.getTotal();
        total += dontPassLineOddsStack.getTotal();
        total += comeStack.getTotal();
        total += dontComeStack.getTotal();
        total += fieldStack.getTotal();
        total += lay4Stack.getTotal();
        total += lay5Stack.getTotal();
        total += lay6Stack.getTotal();
        total += lay8Stack.getTotal();
        total += lay9Stack.getTotal();
        total += lay10Stack.getTotal();
        total += hard4Stack.getTotal();
        total += hard6Stack.getTotal();
        total += hard8Stack.getTotal();
        total += hard10Stack.getTotal();
        total += any7Stack.getTotal();
        total += snakeEyesStack.getTotal();
        total += boxCarsStack.getTotal();
        total += threeStack.getTotal();
        total += elevenStack.getTotal();
        total += anyCrapsStack.getTotal();
        total += dontCome4Stack.getTotal();
        total += dontCome4OddsStack.getTotal();
        total += dontCome5Stack.getTotal();
        total += dontCome5OddsStack.getTotal();
        total += dontCome6Stack.getTotal();
        total += dontCome6OddsStack.getTotal();
        total += dontCome8Stack.getTotal();
        total += dontCome8OddsStack.getTotal();
        total += dontCome9Stack.getTotal();
        total += dontCome9OddsStack.getTotal();
        total += dontCome10Stack.getTotal();
        total += dontCome10OddsStack.getTotal();
        total += come4Stack.getTotal();
        total += come4OddsStack.getTotal();
        total += come5Stack.getTotal();
        total += come5OddsStack.getTotal();
        total += come6Stack.getTotal();
        total += come6OddsStack.getTotal();
        total += come8Stack.getTotal();
        total += come8OddsStack.getTotal();
        total += come9Stack.getTotal();
        total += come9OddsStack.getTotal();
        total += come10Stack.getTotal();
        total += come10OddsStack.getTotal();
        total += buy4Stack.getTotal();
        total += place5Stack.getTotal();
        total += place6Stack.getTotal();
        total += place8Stack.getTotal();
        total += place9Stack.getTotal();
        total += buy10Stack.getTotal();

        leftSide.setWagerText("" + total);
        leftSide.updateBalance();
    }

    private int checkTableMax(int newTotal) {
        if(newTotal > game.getTableMaximum()) {
            showHint("You're trying to bet more than the table maximum. Setting to the table max.");
            newTotal = game.getTableMaximum();
        }
        return newTotal;
    }
}
