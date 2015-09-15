package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.AnimatedDice;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStackGroup;
import com.thirteensecondstoburn.CasinoPractice.Actors.Die;
import com.thirteensecondstoburn.CasinoPractice.Actors.MessagePopup;
import com.thirteensecondstoburn.CasinoPractice.Actors.MultiLineTableButton;
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
    int lastComeBet = game.getTableMinimum();

    Image tableImage;

    TableButton rollButton;
    TableButton backOddsButton;
    MultiLineTableButton comeButton;

    Die die1;
    Die die2;

    Image onChip;
    Image offChip;

    AnimatedDice rollingDice;
    Animation rollingDiceAnimation;
    
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
        if(caller == rollingDice) {
            rollingDice.setVisible(false);
            die1.rollDie();
            die1.setVisible(true);
            die2.rollDie();
            die2.setVisible(true);
            rollButton.setVisible(true);

            calculateRollResult();
            showActionButtons();
        }
    }

    @Override
    protected void setup() {
        thePoint = -1;

        Texture tableTex = assets.getTexture(Assets.TEX_NAME.CRAPS_TABLE);
        tableImage = new Image(tableTex);
        //tableImage.setColor(mainColor);
        tableImage.setPosition(280, 40);

        stage.addListener(new ActorGestureListener() {
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
                    if (thePoint == -1) { // no point? work the pass line stack
                        if (dontPassLineStack.getTotal() > 0) {
                            showHint("You can't bet on both the pass and don't pass lines.");
                            return;
                        }
                        placeBet(passLineStack, leftSide.getBetAmount());
                    } else { // point established? all you can do it mess w/ the odds
                        if(passLineStack.getTotal() > 0) {
                            int newTotal = passLineOddsStack.getTotal() + leftSide.getBetAmount();
                            int odds = 3;
                            if (thePoint == 5 || thePoint == 9) odds = 4;
                            if (thePoint == 6 || thePoint == 8) odds = 5;
                            if (newTotal > passLineStack.getTotal() * odds) {
                                showHint("You can only take " + odds + "x your pass line bet when the point is " + thePoint + ". Setting to max odds");
                                newTotal = passLineStack.getTotal() * odds;
                            }
                            int oldTotal = passLineOddsStack.getTotal();
                            game.subtractFromBalance(newTotal - oldTotal);
                            passLineOddsStack.setTotal(newTotal);
                        }
                    }
                } else if (dontPassLine1.contains(x, y) || dontPassLine2.contains(x, y)) {
                    if (thePoint == -1) { // no point? work the dontPass line stack
                        if (passLineStack.getTotal() > 0) {
                            showHint("You can't bet on both the pass and don't pass lines.");
                            return;
                        }
                        placeBet(dontPassLineStack, leftSide.getBetAmount());
                    } else { // point established? all you can do it mess w/ the odds
                        if(dontPassLineStack.getTotal() > 0) {
                            int newTotal = dontPassLineOddsStack.getTotal() + leftSide.getBetAmount();
                            if (newTotal > dontPassLineStack.getTotal() * 6) {
                                showHint("You can only lay 6x your don't pass line bet. Setting to max odds");
                                newTotal = dontPassLineStack.getTotal() * 6;
                            }
                            int oldTotal = dontPassLineOddsStack.getTotal();
                            game.subtractFromBalance(newTotal - oldTotal);
                            dontPassLineOddsStack.setTotal(newTotal);
                        }
                    }
                } else if (field.contains(x, y)) {
                    placeBet(fieldStack, leftSide.getBetAmount());
                } else if (come.contains(x, y)) {
                    if (thePoint != -1) {
                        lastComeBet = placeBet(comeStack, leftSide.getBetAmount());
                    } // no come bets if there isn't a point established
                } else if (dontCome.contains(x, y)) {
                    if (thePoint != -1) {
                        lastComeBet = placeBet(dontComeStack, leftSide.getBetAmount());
                    } // no dontCome bets if there isn't a point established
                } else if (lay4.contains(x, y)) {
                    placeBet(lay4Stack, leftSide.getBetAmount());
                } else if (lay5.contains(x, y)) {
                    placeBet(lay5Stack, leftSide.getBetAmount());
                } else if (lay6.contains(x, y)) {
                    placeBet(lay6Stack, leftSide.getBetAmount());
                } else if (lay8.contains(x, y)) {
                    placeBet(lay8Stack, leftSide.getBetAmount());
                } else if (lay9.contains(x, y)) {
                    placeBet(lay9Stack, leftSide.getBetAmount());
                } else if (lay10.contains(x, y)) {
                    placeBet(lay10Stack, leftSide.getBetAmount());
                } else if (buy4.contains(x, y)) {
                    if (thePoint != -1) {
                        placeBet(buy4Stack, leftSide.getBetAmount());
                    }
                } else if (place5.contains(x, y)) {
                    if (thePoint != -1) {
                        placeBet(place5Stack, leftSide.getBetAmount());
                    }
                } else if (place6.contains(x, y)) {
                    if (thePoint != -1) {
                        placeBet(place6Stack, leftSide.getBetAmount());
                    }
                } else if (place8.contains(x, y)) {
                    if (thePoint != -1) {
                        placeBet(place8Stack, leftSide.getBetAmount());
                    }
                } else if (place9.contains(x, y)) {
                    if (thePoint != -1) {
                        placeBet(place9Stack, leftSide.getBetAmount());
                    }
                } else if (buy10.contains(x, y)) {
                    if (thePoint != -1) {
                        placeBet(buy10Stack, leftSide.getBetAmount());
                    }
                } else if (hard6.contains(x, y)) {
                    if (thePoint != -1) {
                        placeBet(hard6Stack, leftSide.getBetAmount());
                    }
                } else if (hard8.contains(x, y)) {
                    if (thePoint != -1) {
                        placeBet(hard8Stack, leftSide.getBetAmount());
                    }
                } else if (hard4.contains(x, y)) {
                    if (thePoint != -1) {
                        placeBet(hard4Stack, leftSide.getBetAmount());
                    }
                } else if (hard10.contains(x, y)) {
                    if (thePoint != -1) {
                        placeBet(hard10Stack, leftSide.getBetAmount());
                    }
                } else if (any7.contains(x, y)) {
                    placeBet(any7Stack, leftSide.getBetAmount());
                } else if (anyCraps.contains(x, y)) {
                    placeBet(anyCrapsStack, leftSide.getBetAmount());
                } else if (snakeEyes.contains(x, y)) {
                    placeBet(snakeEyesStack, leftSide.getBetAmount());
                } else if (boxCars.contains(x, y)) {
                    placeBet(boxCarsStack, leftSide.getBetAmount());
                } else if (three.contains(x, y)) {
                    placeBet(threeStack, leftSide.getBetAmount());
                } else if (eleven.contains(x, y)) {
                    placeBet(elevenStack, leftSide.getBetAmount());
                } else if (four.contains(x, y)) {
                    if (come4Stack.getTotal() > 0) {
                        int newTotal = come4OddsStack.getTotal() + leftSide.getBetAmount();
                        int odds = 3;
                        if (newTotal > come4Stack.getTotal() * odds) {
                            showHint("You can only take " + odds + "x your pass line bet when the point is " + thePoint + ". Setting to max odds");
                            newTotal = come4Stack.getTotal() * odds;
                        }
                        int oldTotal = come4OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        come4OddsStack.setTotal(newTotal);
                    }
                    if (dontCome4Stack.getTotal() > 0) {
                        int newTotal = dontCome4OddsStack.getTotal() + leftSide.getBetAmount();
                        if (newTotal > dontCome4Stack.getTotal() * 6) {
                            showHint("You can only lay 6x your don't pass line bet. Setting to max odds");
                            newTotal = dontCome4Stack.getTotal() * 6;
                        }
                        int oldTotal = dontCome4OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        dontCome4OddsStack.setTotal(newTotal);
                    }
                } else if (five.contains(x, y)) {
                    if (come5Stack.getTotal() > 0) {
                        int newTotal = come5OddsStack.getTotal() + leftSide.getBetAmount();
                        int odds = 4;
                        if (newTotal > come5Stack.getTotal() * odds) {
                            showHint("You can only take " + odds + "x your pass line bet when the point is " + thePoint + ". Setting to max odds");
                            newTotal = come5Stack.getTotal() * odds;
                        }
                        int oldTotal = come5OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        come5OddsStack.setTotal(newTotal);
                    }
                    if (dontCome5Stack.getTotal() > 0) {
                        int newTotal = dontCome5OddsStack.getTotal() + leftSide.getBetAmount();
                        if (newTotal > dontCome5Stack.getTotal() * 6) {
                            showHint("You can only lay 6x your don't pass line bet. Setting to max odds");
                            newTotal = dontCome5Stack.getTotal() * 6;
                        }
                        int oldTotal = dontCome5OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        dontCome5OddsStack.setTotal(newTotal);
                    }
                } else if (six.contains(x, y)) {
                    if (come6Stack.getTotal() > 0) {
                        int newTotal = come6OddsStack.getTotal() + leftSide.getBetAmount();
                        int odds = 5;
                        if (newTotal > come6Stack.getTotal() * odds) {
                            showHint("You can only take " + odds + "x your pass line bet when the point is " + thePoint + ". Setting to max odds");
                            newTotal = come6Stack.getTotal() * odds;
                        }
                        int oldTotal = come6OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        come6OddsStack.setTotal(newTotal);
                    }
                    if (dontCome6Stack.getTotal() > 0) {
                        int newTotal = dontCome6OddsStack.getTotal() + leftSide.getBetAmount();
                        if (newTotal > dontCome6Stack.getTotal() * 6) {
                            showHint("You can only lay 6x your don't pass line bet. Setting to max odds");
                            newTotal = dontCome6Stack.getTotal() * 6;
                        }
                        int oldTotal = dontCome6OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        dontCome6OddsStack.setTotal(newTotal);
                    }
                } else if (eight.contains(x, y)) {
                    if (come8Stack.getTotal() > 0) {
                        int newTotal = come8OddsStack.getTotal() + leftSide.getBetAmount();
                        int odds = 5;
                        if (newTotal > come8Stack.getTotal() * odds) {
                            showHint("You can only take " + odds + "x your pass line bet when the point is " + thePoint + ". Setting to max odds");
                            newTotal = come8Stack.getTotal() * odds;
                        }
                        int oldTotal = come8OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        come8OddsStack.setTotal(newTotal);
                    }
                    if (dontCome8Stack.getTotal() > 0) {
                        int newTotal = dontCome8OddsStack.getTotal() + leftSide.getBetAmount();
                        if (newTotal > dontCome8Stack.getTotal() * 6) {
                            showHint("You can only lay 6x your don't pass line bet. Setting to max odds");
                            newTotal = dontCome8Stack.getTotal() * 6;
                        }
                        int oldTotal = dontCome8OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        dontCome8OddsStack.setTotal(newTotal);
                    }
                } else if (nine.contains(x, y)) {
                    if (come9Stack.getTotal() > 0) {
                        int newTotal = come9OddsStack.getTotal() + leftSide.getBetAmount();
                        int odds = 4;
                        if (newTotal > come9Stack.getTotal() * odds) {
                            showHint("You can only take " + odds + "x your pass line bet when the point is " + thePoint + ". Setting to max odds");
                            newTotal = come9Stack.getTotal() * odds;
                        }
                        int oldTotal = come9OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        come9OddsStack.setTotal(newTotal);
                    }
                    if (dontCome9Stack.getTotal() > 0) {
                        int newTotal = dontCome9OddsStack.getTotal() + leftSide.getBetAmount();
                        if (newTotal > dontCome9Stack.getTotal() * 6) {
                            showHint("You can only lay 6x your don't pass line bet. Setting to max odds");
                            newTotal = dontCome9Stack.getTotal() * 6;
                        }
                        int oldTotal = dontCome9OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        dontCome9OddsStack.setTotal(newTotal);
                    }
                } else if (ten.contains(x, y)) {
                    if (come10Stack.getTotal() > 0) {
                        int newTotal = come10OddsStack.getTotal() + leftSide.getBetAmount();
                        int odds = 3;
                        if (newTotal > come10Stack.getTotal() * odds) {
                            showHint("You can only take " + odds + "x your pass line bet when the point is " + thePoint + ". Setting to max odds");
                            newTotal = come10Stack.getTotal() * odds;
                        }
                        int oldTotal = come10OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        come10OddsStack.setTotal(newTotal);
                    }
                    if (dontCome10Stack.getTotal() > 0) {
                        int newTotal = dontCome10OddsStack.getTotal() + leftSide.getBetAmount();
                        if (newTotal > dontCome10Stack.getTotal() * 6) {
                            showHint("You can only lay 6x your don't pass line bet. Setting to max odds");
                            newTotal = dontCome10Stack.getTotal() * 6;
                        }
                        int oldTotal = dontCome10OddsStack.getTotal();
                        game.subtractFromBalance(newTotal - oldTotal);
                        dontCome10OddsStack.setTotal(newTotal);
                    }
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
                    die1.setVisible(false);
                    die2.setVisible(false);
                    rollButton.setVisible(false);
                    comeButton.setVisible(false);
                    backOddsButton.setVisible(false);
                    rollingDice.roll(leftSide.getWidth(),stage.getHeight() - rollingDice.getHeight(), stage.getWidth(), stage.getHeight() - rollingDice.getHeight());
                }
            }
        });


        TextureRegion[][] tmp = TextureRegion.split(assets.getTexture(Assets.TEX_NAME.DICE_FRAMES), 320, 160);
        TextureRegion[] diceFrames = new TextureRegion[14];
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                if(index > 13) break; // only have 14 frames, not 15
                diceFrames[index++] = tmp[i][j];
            }
        }
        rollingDiceAnimation = new Animation(0.05f, diceFrames);
        rollingDice = new AnimatedDice(rollingDiceAnimation);
        rollingDice.setVisible(false);
        rollingDice.addActionListener(this);

        backOddsButton = new TableButton(assets, "Odds", mainColor);
        backOddsButton.setPosition(stage.getWidth() - backOddsButton.getWidth(), backOddsButton.getWidth() + 10);
        backOddsButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (backOddsButton.isInside(x, y)) {
                    if (thePoint > -1) {
                        int odds = 3;
                        if (thePoint == 5 || thePoint == 9) odds = 4;
                        if (thePoint == 6 || thePoint == 8) odds = 5;
                        backOdds(passLineStack, passLineOddsStack, odds);

                        backOdds(dontPassLineStack, dontPassLineOddsStack, 6);
                    }

                    backOdds(come4Stack, come4OddsStack, 3);
                    backOdds(dontCome4Stack, dontCome4OddsStack, 6);
                    backOdds(come5Stack, come5OddsStack, 4);
                    backOdds(dontCome5Stack, dontCome5OddsStack, 6);
                    backOdds(come6Stack, come6OddsStack, 5);
                    backOdds(dontCome6Stack, dontCome6OddsStack, 6);
                    backOdds(come8Stack, come8OddsStack, 5);
                    backOdds(dontCome8Stack, dontCome8OddsStack, 6);
                    backOdds(come9Stack, come9OddsStack, 4);
                    backOdds(dontCome9Stack, dontCome9OddsStack, 6);
                    backOdds(come10Stack, come10OddsStack, 3);
                    backOdds(dontCome10Stack, dontCome10OddsStack, 6);

                    showActionButtons();
                }
            }
        });

        comeButton = new MultiLineTableButton(assets, "Come", mainColor);
        comeButton.setPosition(stage.getWidth() - comeButton.getWidth(), comeButton.getWidth() * 2 + 20);
        comeButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (comeButton.isInside(x, y)) {
                    if (thePoint > -1) {
                        if (passLineStack.getTotal() > 0 && comeStack.getTotal() == 0) {
                            game.subtractFromBalance(lastComeBet);
                            comeStack.setTotal(lastComeBet);
                        }
                        if (dontPassLineStack.getTotal() > 0 && dontComeStack.getTotal() == 0) {
                            game.subtractFromBalance(lastComeBet);
                            dontComeStack.setTotal(lastComeBet);
                        }
                    }

                    showActionButtons();
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

        stage.addActor(tableImage);
        stage.addActor(rollButton);
        stage.addActor(backOddsButton);
        stage.addActor(comeButton);
        stage.addActor(die1);
        stage.addActor(die2);
        stage.addActor(onChip);
        stage.addActor(offChip);
        stage.addActor(rollingDice);

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

        showActionButtons();
    }

    private void backOdds(ChipStackGroup checkStack, ChipStackGroup oddsStack, int odds) {
        if (checkStack.getTotal() > 0 && oddsStack.getTotal() == 0) {
            int oddsTotal = checkStack.getTotal() * odds;
            game.subtractFromBalance(oddsTotal);
            oddsStack.setTotal(oddsTotal);
            switch (odds) {
                case 3:
                    oddsStack.popStack(MessagePopup.Message.THREEX);
                    break;
                case 4:
                    oddsStack.popStack(MessagePopup.Message.FOURX);
                    break;
                case 5:
                    oddsStack.popStack(MessagePopup.Message.FIVEX);
                    break;
                case 6:
                    oddsStack.popStack(MessagePopup.Message.SIXX);
                    break;
            }
        }
    }

    private void showActionButtons() {
        comeButton.setVisible(thePoint > -1 && comeStack.getTotal() == 0 && dontComeStack.getTotal() == 0);
        comeButton.setColor((game.usePreActionHints() && thePoint > -1 && comeStack.getTotal() == 0 && dontComeStack.getTotal() == 0) ? hintColor : mainColor);
        comeButton.setText(dontPassLineStack.getTotal() > 0 ? "Don't|Come" : "Come");

        boolean canBack = (passLineStack.getTotal() > 0 && passLineOddsStack.getTotal() == 0)
                ||(dontPassLineStack.getTotal() > 0 && dontPassLineOddsStack.getTotal() == 0)
                ||(come4Stack.getTotal() > 0 && come4OddsStack.getTotal() == 0)
                || (dontCome4Stack.getTotal() > 0 && dontCome4OddsStack.getTotal() == 0)
                || (come5Stack.getTotal() > 0 && come5OddsStack.getTotal() == 0)
                || (dontCome5Stack.getTotal() > 0 && dontCome5OddsStack.getTotal() == 0)
                || (come6Stack.getTotal() > 0 && come6OddsStack.getTotal() == 0)
                || (dontCome6Stack.getTotal() > 0 && dontCome6OddsStack.getTotal() == 0)
                || (come8Stack.getTotal() > 0 && come8OddsStack.getTotal() == 0)
                || (dontCome8Stack.getTotal() > 0 && dontCome8OddsStack.getTotal() == 0)
                || (come9Stack.getTotal() > 0 && come9OddsStack.getTotal() == 0)
                || (dontCome9Stack.getTotal() > 0 && dontCome9OddsStack.getTotal() == 0)
                || (come10Stack.getTotal() > 0 && come10OddsStack.getTotal() == 0)
                || (dontCome10Stack.getTotal() > 0 && dontCome10OddsStack.getTotal() == 0);

        backOddsButton.setVisible(canBack);
        backOddsButton.setColor(game.usePreActionHints() && canBack ? hintColor : mainColor);
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

    private void calculateRollResult() {
        int total = die1.getRolledNumber() + die2.getRolledNumber();
        boolean hardWay = die1.getRolledNumber() == die2.getRolledNumber();
        int won = 0;

        // come on role
        if(thePoint == -1 && (total == 7 || total == 11)) {
            if(passLineStack.getTotal() > 0) {
                won += passLineStack.getTotal();
                game.addToBalance(passLineStack.getTotal() * 2); // return the original wager, then 1x bet
                passLineStack.popStack(true);
                passLineStack.setTotal(0);
            }

            if(dontPassLineStack.getTotal() > 0) {
                won -= dontPassLineStack.getTotal();
                dontPassLineStack.popStack(false);
                dontPassLineStack.setTotal(0);
            }
        } else if(thePoint == -1 && (total == 2 || total == 3 || total == 12)) {
            if(passLineStack.getTotal() > 0) {
                won -= passLineStack.getTotal();
                passLineStack.popStack(false);
                passLineStack.setTotal(0);
            }

            if(dontPassLineStack.getTotal() > 0) {
                won += dontPassLineStack.getTotal();
                if(total == 12) {
                    game.addToBalance(dontPassLineStack.getTotal()); // return the original wager
                } else {
                    game.addToBalance(dontPassLineStack.getTotal() * 2); // return the original wager, then 1x bet
                }
                dontPassLineStack.popStack(true);
                dontPassLineStack.setTotal(0);
            }
        }

        // pass line, don't pass line, pass line odds, don't pass line odds (non come on roll)
        if(thePoint != -1 && total == 7) {
            if(passLineStack.getTotal() > 0) {
                won -= passLineStack.getTotal();
                passLineStack.popStack(false);
                passLineStack.setTotal(0);
            }
            if(passLineOddsStack.getTotal() > 0) {
                won -= passLineOddsStack.getTotal();
                passLineOddsStack.popStack(false);
                passLineOddsStack.setTotal(0);
            }

            if(dontPassLineStack.getTotal() > 0) {
                won += dontPassLineStack.getTotal();
                game.addToBalance(dontPassLineStack.getTotal() * 2); // return the original wager, then 1x bet
                dontPassLineStack.popStack(true);
                dontPassLineStack.setTotal(0);
            }
            if(dontPassLineOddsStack.getTotal() > 0) {
                int w = 0;
                if(thePoint == 4 || thePoint == 10) {
                    w = dontPassLineOddsStack.getTotal() / 2;
                } else if(thePoint == 5 || thePoint== 9) {
                    w = (dontPassLineOddsStack.getTotal() * 2) / 3;
                } else if(thePoint == 6 || thePoint == 8) {
                    w = (dontPassLineOddsStack.getTotal() * 5) / 6;
                }
                won += w;
                game.addToBalance(w + dontPassLineOddsStack.getTotal());
                dontPassLineOddsStack.popStack(true);
                dontPassLineOddsStack.setTotal(0);
            }
        } else if(total == thePoint) {
            if(passLineStack.getTotal() > 0) {
                won += passLineStack.getTotal();
                game.addToBalance(passLineStack.getTotal() * 2); // return the original wager, then 1x bet
                passLineStack.popStack(true);
                passLineStack.setTotal(0);
            }
            if(passLineOddsStack.getTotal() > 0) {
                int w = 0;
                if (thePoint == 4 || thePoint == 10) {
                    w = passLineOddsStack.getTotal() * 2;
                } else if (thePoint == 5 || thePoint == 9) {
                    w = (passLineOddsStack.getTotal() * 3) / 2;
                } else if (thePoint == 6 || thePoint == 8) {
                    w = (passLineOddsStack.getTotal() * 6) / 5;
                }
                won += w;
                game.addToBalance(w + passLineOddsStack.getTotal());
                passLineOddsStack.popStack(true);
                passLineOddsStack.setTotal(0);
            }
            if(dontPassLineStack.getTotal() > 0) {
                won -= dontPassLineStack.getTotal();
                dontPassLineStack.popStack(false);
                dontPassLineStack.setTotal(0);
            }
            if(dontPassLineOddsStack.getTotal() > 0) {
                won -= dontPassLineOddsStack.getTotal();
                dontPassLineOddsStack.popStack(false);
                dontPassLineOddsStack.setTotal(0);
            }
        }

        // come/don't come numbers, place numbers, hardways
        if(total == 7) {
            // all the don't come bets win
            // all the come bets lose
            // all the place bets lose
            if(dontCome4Stack.getTotal() > 0) {
                won += dontCome4Stack.getTotal();
                game.addToBalance(dontCome4Stack.getTotal() * 2); // return the original wager, then 1x bet
                dontCome4Stack.popStack(true);
                dontCome4Stack.setTotal(0);
            }
            if(dontCome4OddsStack.getTotal() > 0) {
                int w = dontCome4OddsStack.getTotal() / 2;
                won += w;
                game.addToBalance(w + dontCome4OddsStack.getTotal());
                dontCome4OddsStack.popStack(true);
                dontCome4OddsStack.setTotal(0);
            }
            if(come4Stack.getTotal() > 0) {
                won -= come4Stack.getTotal();
                come4Stack.popStack(false);
                come4Stack.setTotal(0);
            }
            if(come4OddsStack.getTotal() > 0) {
                won -= come4OddsStack.getTotal();
                come4OddsStack.popStack(false);
                come4OddsStack.setTotal(0);
            }

            if(dontCome5Stack.getTotal() > 0) {
                won += dontCome5Stack.getTotal();
                game.addToBalance(dontCome5Stack.getTotal() * 2); // return the original wager, then 1x bet
                dontCome5Stack.popStack(true);
                dontCome5Stack.setTotal(0);
            }
            if(dontCome5OddsStack.getTotal() > 0) {
                int w = (dontCome5OddsStack.getTotal() * 2) / 3;
                won += w;
                game.addToBalance(w + dontCome5OddsStack.getTotal());
                dontCome5OddsStack.popStack(true);
                dontCome5OddsStack.setTotal(0);
            }
            if(come5Stack.getTotal() > 0) {
                won -= come5Stack.getTotal();
                come5Stack.popStack(false);
                come5Stack.setTotal(0);
            }
            if(come5OddsStack.getTotal() > 0) {
                won -= come5OddsStack.getTotal();
                come5OddsStack.popStack(false);
                come5OddsStack.setTotal(0);
            }

            if(dontCome6Stack.getTotal() > 0) {
                won += dontCome6Stack.getTotal();
                game.addToBalance(dontCome6Stack.getTotal() * 2); // return the original wager, then 1x bet
                dontCome6Stack.popStack(true);
                dontCome6Stack.setTotal(0);
            }
            if(dontCome6OddsStack.getTotal() > 0) {
                int w = (dontCome6OddsStack.getTotal() * 5) / 6;
                won += w;
                game.addToBalance(w + dontCome6OddsStack.getTotal());
                dontCome6OddsStack.popStack(true);
                dontCome6OddsStack.setTotal(0);
            }
            if(come6Stack.getTotal() > 0) {
                won -= come6Stack.getTotal();
                come6Stack.popStack(false);
                come6Stack.setTotal(0);
            }
            if(come6OddsStack.getTotal() > 0) {
                won -= come6OddsStack.getTotal();
                come6OddsStack.popStack(false);
                come6OddsStack.setTotal(0);
            }

            if(dontCome8Stack.getTotal() > 0) {
                won += dontCome8Stack.getTotal();
                game.addToBalance(dontCome8Stack.getTotal() * 2); // return the original wager, then 1x bet
                dontCome8Stack.popStack(true);
                dontCome8Stack.setTotal(0);
            }
            if(dontCome8OddsStack.getTotal() > 0) {
                int w = (dontCome8OddsStack.getTotal() * 5) / 6;
                won += w;
                game.addToBalance(w + dontCome8OddsStack.getTotal());
                dontCome8OddsStack.popStack(true);
                dontCome8OddsStack.setTotal(0);
            }
            if(come8Stack.getTotal() > 0) {
                won -= come8Stack.getTotal();
                come8Stack.popStack(false);
                come8Stack.setTotal(0);
            }
            if(come8OddsStack.getTotal() > 0) {
                won -= come8OddsStack.getTotal();
                come8OddsStack.popStack(false);
                come8OddsStack.setTotal(0);
            }

            if(dontCome9Stack.getTotal() > 0) {
                won += dontCome9Stack.getTotal();
                game.addToBalance(dontCome9Stack.getTotal() * 2); // return the original wager, then 1x bet
                dontCome9Stack.popStack(true);
                dontCome9Stack.setTotal(0);
            }
            if(dontCome9OddsStack.getTotal() > 0) {
                int w = (dontCome9OddsStack.getTotal() * 2) / 3;
                won += w;
                game.addToBalance(w + dontCome9OddsStack.getTotal());
                dontCome9OddsStack.popStack(true);
                dontCome9OddsStack.setTotal(0);
            }
            if(come9Stack.getTotal() > 0) {
                won -= come9Stack.getTotal();
                come9Stack.popStack(false);
                come9Stack.setTotal(0);
            }
            if(come9OddsStack.getTotal() > 0) {
                won -= come9OddsStack.getTotal();
                come9OddsStack.popStack(false);
                come9OddsStack.setTotal(0);
            }

            if(dontCome10Stack.getTotal() > 0) {
                won += dontCome10Stack.getTotal();
                game.addToBalance(dontCome10Stack.getTotal() * 2); // return the original wager, then 1x bet
                dontCome10Stack.popStack(true);
                dontCome10Stack.setTotal(0);
            }
            if(dontCome10OddsStack.getTotal() > 0) {
                int w = dontCome10OddsStack.getTotal() / 2;
                won += w;
                game.addToBalance(w + dontCome10OddsStack.getTotal());
                dontCome10OddsStack.popStack(true);
                dontCome10OddsStack.setTotal(0);
            }
            if(come10Stack.getTotal() > 0) {
                won -= come10Stack.getTotal();
                come10Stack.popStack(false);
                come10Stack.setTotal(0);
            }
            if(come10OddsStack.getTotal() > 0) {
                won -= come10OddsStack.getTotal();
                come10OddsStack.popStack(false);
                come10OddsStack.setTotal(0);
            }

            // place and buy lose
            if(buy4Stack.getTotal() > 0) {
                won -= buy4Stack.getTotal();
                buy4Stack.popStack(false);
                buy4Stack.setTotal(0);
            }
            if(place5Stack.getTotal() > 0) {
                won -= place5Stack.getTotal();
                place5Stack.popStack(false);
                place5Stack.setTotal(0);
            }
            if(place6Stack.getTotal() > 0) {
                won -= place6Stack.getTotal();
                place6Stack.popStack(false);
                place6Stack.setTotal(0);
            }
            if(place8Stack.getTotal() > 0) {
                won -= place8Stack.getTotal();
                place8Stack.popStack(false);
                place8Stack.setTotal(0);
            }
            if(place9Stack.getTotal() > 0) {
                won -= place9Stack.getTotal();
                place9Stack.popStack(false);
                place9Stack.setTotal(0);
            }
            if(buy10Stack.getTotal() > 0) {
                won -= buy10Stack.getTotal();
                buy10Stack.popStack(false);
                buy10Stack.setTotal(0);
            }
            // lay win
            if(lay4Stack.getTotal() > 0) {
                float w = lay4Stack.getTotal() / 2.0f;
                w = (int)(w * .95f);
                won += w;
                game.addToBalance(w + lay4Stack.getTotal());
                lay4Stack.popStack(true);
                lay4Stack.setTotal(0);
            }
            if(lay5Stack.getTotal() > 0) {
                int w = (lay5Stack.getTotal() * 2) / 3;
                w = (int)(w * .95f);
                won += w;
                game.addToBalance(w + lay5Stack.getTotal());
                lay5Stack.popStack(true);
                lay5Stack.setTotal(0);
            }
            if(lay6Stack.getTotal() > 0) {
                int w = (lay6Stack.getTotal() * 5) / 6;
                w = (int)(w * .95f);
                won += w;
                game.addToBalance(w + lay6Stack.getTotal());
                lay6Stack.popStack(true);
                lay6Stack.setTotal(0);
            }      
            if(lay8Stack.getTotal() > 0) {
                int w = (lay8Stack.getTotal() * 5) / 6;
                w = (int)(w * .95f);
                won += w;
                game.addToBalance(w + lay8Stack.getTotal());
                lay8Stack.popStack(true);
                lay8Stack.setTotal(0);
            }
            if(lay9Stack.getTotal() > 0) {
                int w = (lay9Stack.getTotal() * 2) / 3;
                w = (int)(w * .95f);
                won += w;
                game.addToBalance(w + lay9Stack.getTotal());
                lay9Stack.popStack(true);
                lay9Stack.setTotal(0);
            }
            if(lay10Stack.getTotal() > 0) {
                int w = lay10Stack.getTotal() / 2;
                w = (int)(w * .95f);
                won += w;
                game.addToBalance(w + lay10Stack.getTotal());
                lay10Stack.popStack(true);
                lay10Stack.setTotal(0);
            }

            if(hard4Stack.getTotal() > 0) {
                won -= hard4Stack.getTotal();
                hard4Stack.popStack(false);
                hard4Stack.setTotal(0);
            }
            if(hard6Stack.getTotal() > 0) {
                won -= hard6Stack.getTotal();
                hard6Stack.popStack(false);
                hard6Stack.setTotal(0);
            }
            if(hard8Stack.getTotal() > 0) {
                won -= hard8Stack.getTotal();
                hard8Stack.popStack(false);
                hard8Stack.setTotal(0);
            }
            if(hard10Stack.getTotal() > 0) {
                won -= hard10Stack.getTotal();
                hard10Stack.popStack(false);
                hard10Stack.setTotal(0);
            }
        } else {
            switch (total) {
                case 4:
                    if(come4Stack.getTotal() > 0) {
                        won += come4Stack.getTotal();
                        game.addToBalance(come4Stack.getTotal() * 2); // return the original wager, then 1x bet
                        come4Stack.popStack(true);
                        come4Stack.setTotal(0);
                    }
                    if(come4OddsStack.getTotal() > 0) {
                        int w = come4OddsStack.getTotal() * 2;
                        won += w;
                        game.addToBalance(w + come4OddsStack.getTotal());
                        come4OddsStack.popStack(true);
                        come4OddsStack.setTotal(0);
                    }
                    if(dontCome4Stack.getTotal() > 0) {
                        won -= dontCome4Stack.getTotal();
                        dontCome4Stack.popStack(false);
                        dontCome4Stack.setTotal(0);
                    }
                    if(dontCome4OddsStack.getTotal() > 0) {
                        won -= dontCome4OddsStack.getTotal();
                        dontCome4OddsStack.popStack(false);
                        dontCome4OddsStack.setTotal(0);
                    }
                    if(lay4Stack.getTotal() > 0) {
                        won -= lay4Stack.getTotal();
                        lay4Stack.popStack(false);
                        lay4Stack.setTotal(0);
                    }
                    if(buy4Stack.getTotal() > 0) {
                        int w = (int)((buy4Stack.getTotal() * 2) * .95);
                        won += w;
                        game.addToBalance(w + buy4Stack.getTotal()); // return the original wager, then 2x bet
                        buy4Stack.popStack(true);
                        buy4Stack.setTotal(0);
                    }

                    if(hard4Stack.getTotal() > 0) {
                        if(hardWay) {
                            won += hard4Stack.getTotal() * 7;
                            game.addToBalance(hard4Stack.getTotal() * 8); // return the original wager, then 9x bet
                            hard4Stack.popStack(true);
                            hard4Stack.setTotal(0);
                        } else {
                            won -= hard4Stack.getTotal();
                            hard4Stack.popStack(false);
                            hard4Stack.setTotal(0);
                        }
                    }
                    break;
                case 5:
                    if(come5Stack.getTotal() > 0) {
                        won += come5Stack.getTotal();
                        game.addToBalance(come5Stack.getTotal() * 2); // return the original wager, then 1x bet
                        come5Stack.popStack(true);
                        come5Stack.setTotal(0);
                    }
                    if(come5OddsStack.getTotal() > 0) {
                        int w = (come5OddsStack.getTotal() * 3) / 2;
                        won += w;
                        game.addToBalance(w + come5OddsStack.getTotal());
                        come5OddsStack.popStack(true);
                        come5OddsStack.setTotal(0);
                    }
                    if(dontCome5Stack.getTotal() > 0) {
                        won -= dontCome5Stack.getTotal();
                        dontCome5Stack.popStack(false);
                        dontCome5Stack.setTotal(0);
                    }
                    if(dontCome5OddsStack.getTotal() > 0) {
                        won -= dontCome5OddsStack.getTotal();
                        dontCome5OddsStack.popStack(false);
                        dontCome5OddsStack.setTotal(0);
                    }
                    if(lay5Stack.getTotal() > 0) {
                        won -= lay5Stack.getTotal();
                        lay5Stack.popStack(false);
                        lay5Stack.setTotal(0);
                    }
                    if(place5Stack.getTotal() > 0) {
                        int w = (place5Stack.getTotal() * 7) / 5;
                        won += w;
                        game.addToBalance(w + place5Stack.getTotal());
                        place5Stack.popStack(true);
                        place5Stack.setTotal(0);
                    }
                    break;
                case 6:
                    if(come6Stack.getTotal() > 0) {
                        won += come6Stack.getTotal();
                        game.addToBalance(come6Stack.getTotal() * 2); // return the original wager, then 1x bet
                        come6Stack.popStack(true);
                        come6Stack.setTotal(0);
                    }
                    if(come6OddsStack.getTotal() > 0) {
                        int w = (come6OddsStack.getTotal() * 6) / 5;
                        won += w;
                        game.addToBalance(w + come6OddsStack.getTotal());
                        come6OddsStack.popStack(true);
                        come6OddsStack.setTotal(0);
                    }
                    if(dontCome6Stack.getTotal() > 0) {
                        won -= dontCome6Stack.getTotal();
                        dontCome6Stack.popStack(false);
                        dontCome6Stack.setTotal(0);
                    }
                    if(dontCome6OddsStack.getTotal() > 0) {
                        won -= dontCome6OddsStack.getTotal();
                        dontCome6OddsStack.popStack(false);
                        dontCome6OddsStack.setTotal(0);
                    }
                    if(lay6Stack.getTotal() > 0) {
                        won -= lay6Stack.getTotal();
                        lay6Stack.popStack(false);
                        lay6Stack.setTotal(0);
                    }
                    if(place6Stack.getTotal() > 0) {
                        int w = (place6Stack.getTotal() * 7) / 6;
                        won += w;
                        game.addToBalance(w + place6Stack.getTotal());
                        place6Stack.popStack(true);
                        place6Stack.setTotal(0);
                    }

                    if(hard6Stack.getTotal() > 0) {
                        if(hardWay) {
                            won += hard6Stack.getTotal() * 9;
                            game.addToBalance(hard6Stack.getTotal() * 10); // return the original wager, then 9x bet
                            hard6Stack.popStack(true);
                            hard6Stack.setTotal(0);
                        } else {
                            won -= hard6Stack.getTotal();
                            hard6Stack.popStack(false);
                            hard6Stack.setTotal(0);
                        }
                    }

                    break;
                case 8:
                    if(come8Stack.getTotal() > 0) {
                        won += come8Stack.getTotal();
                        game.addToBalance(come8Stack.getTotal() * 2); // return the original wager, then 1x bet
                        come8Stack.popStack(true);
                        come8Stack.setTotal(0);
                    }
                    if(come8OddsStack.getTotal() > 0) {
                        int w = (come8OddsStack.getTotal() * 6) / 5;
                        won += w;
                        game.addToBalance(w + come8OddsStack.getTotal());
                        come8OddsStack.popStack(true);
                        come8OddsStack.setTotal(0);
                    }
                    if(dontCome8Stack.getTotal() > 0) {
                        won -= dontCome8Stack.getTotal();
                        dontCome8Stack.popStack(false);
                        dontCome8Stack.setTotal(0);
                    }
                    if(dontCome8OddsStack.getTotal() > 0) {
                        won -= dontCome8OddsStack.getTotal();
                        dontCome8OddsStack.popStack(false);
                        dontCome8OddsStack.setTotal(0);
                    }
                    if(lay8Stack.getTotal() > 0) {
                        won -= lay8Stack.getTotal();
                        lay8Stack.popStack(false);
                        lay8Stack.setTotal(0);
                    }
                    if(place8Stack.getTotal() > 0) {
                        int w = (place8Stack.getTotal() * 7) / 6;
                        won += w;
                        game.addToBalance(w + place8Stack.getTotal());
                        place8Stack.popStack(true);
                        place8Stack.setTotal(0);
                    }
                    if(hard8Stack.getTotal() > 0) {
                        if(hardWay) {
                            won += hard8Stack.getTotal() * 9;
                            game.addToBalance(hard8Stack.getTotal() * 10); // return the original wager, then 9x bet
                            hard8Stack.popStack(true);
                            hard8Stack.setTotal(0);
                        } else {
                            won -= hard8Stack.getTotal();
                            hard8Stack.popStack(false);
                            hard8Stack.setTotal(0);
                        }
                    }
                    break;
                case 9:
                    if(come9Stack.getTotal() > 0) {
                        won += come9Stack.getTotal();
                        game.addToBalance(come9Stack.getTotal() * 2); // return the original wager, then 1x bet
                        come9Stack.popStack(true);
                        come9Stack.setTotal(0);
                    }
                    if(come9OddsStack.getTotal() > 0) {
                        int w = (come9OddsStack.getTotal() * 3) / 2;
                        won += w;
                        game.addToBalance(w + come9OddsStack.getTotal());
                        come9OddsStack.popStack(true);
                        come9OddsStack.setTotal(0);
                    }
                    if(dontCome9Stack.getTotal() > 0) {
                        won -= dontCome9Stack.getTotal();
                        dontCome9Stack.popStack(false);
                        dontCome9Stack.setTotal(0);
                    }
                    if(dontCome9OddsStack.getTotal() > 0) {
                        won -= dontCome9OddsStack.getTotal();
                        dontCome9OddsStack.popStack(false);
                        dontCome9OddsStack.setTotal(0);
                    }
                    if(lay9Stack.getTotal() > 0) {
                        won -= lay9Stack.getTotal();
                        lay9Stack.popStack(false);
                        lay9Stack.setTotal(0);
                    }
                    if(place9Stack.getTotal() > 0) {
                        int w = (place9Stack.getTotal() * 7) / 5;
                        won += w;
                        game.addToBalance(w + place9Stack.getTotal());
                        place9Stack.popStack(true);
                        place9Stack.setTotal(0);
                    }
                    break;
                case 10:
                    if(come10Stack.getTotal() > 0) {
                        won += come10Stack.getTotal();
                        game.addToBalance(come10Stack.getTotal() * 2); // return the original wager, then 1x bet
                        come10Stack.popStack(true);
                        come10Stack.setTotal(0);
                    }
                    if(come10OddsStack.getTotal() > 0) {
                        int w = come10OddsStack.getTotal() * 2;
                        won += w;
                        game.addToBalance(w + come10OddsStack.getTotal());
                        come10OddsStack.popStack(true);
                        come10OddsStack.setTotal(0);
                    }
                    if(dontCome10Stack.getTotal() > 0) {
                        won -= dontCome10Stack.getTotal();
                        dontCome10Stack.popStack(false);
                        dontCome10Stack.setTotal(0);
                    }
                    if(dontCome10OddsStack.getTotal() > 0) {
                        won -= dontCome10OddsStack.getTotal();
                        dontCome10OddsStack.popStack(false);
                        dontCome10OddsStack.setTotal(0);
                    }
                    if(lay10Stack.getTotal() > 0) {
                        won -= lay10Stack.getTotal();
                        lay10Stack.popStack(false);
                        lay10Stack.setTotal(0);
                    }
                    if(buy10Stack.getTotal() > 0) {
                        int w = (int)((buy10Stack.getTotal() * 2) * .95);
                        won += w;
                        game.addToBalance(w + buy10Stack.getTotal()); // return the original wager, then 2x bet
                        buy10Stack.popStack(true);
                        buy10Stack.setTotal(0);
                    }
                    
                    if(hard10Stack.getTotal() > 0) {
                        if(hardWay) {
                            won += hard10Stack.getTotal() * 7;
                            game.addToBalance(hard10Stack.getTotal() * 8); // return the original wager, then 9x bet
                            hard10Stack.popStack(true);
                            hard10Stack.setTotal(0);
                        } else {
                            won -= hard10Stack.getTotal();
                            hard10Stack.popStack(false);
                            hard10Stack.setTotal(0);
                        }
                    }

                    break;
            }
        }

        // come/don't come
        if((total == 7 || total == 11)) {
            if(comeStack.getTotal() > 0) {
                won += comeStack.getTotal();
                game.addToBalance(comeStack.getTotal() * 2); // return the original wager, then 1x bet
                comeStack.popStack(true);
                comeStack.setTotal(0);
            }

            if(dontComeStack.getTotal() > 0) {
                won -= dontComeStack.getTotal();
                dontComeStack.popStack(false);
                dontComeStack.setTotal(0);
            }
        } else if((total == 2 || total == 3 || total == 12)) {
            if(comeStack.getTotal() > 0) {
                won -= comeStack.getTotal();
                comeStack.popStack(false);
                comeStack.setTotal(0);
            }

            if(dontComeStack.getTotal() > 0) {
                won += dontComeStack.getTotal();
                if(total == 12) {
                    game.addToBalance(dontComeStack.getTotal()); // return the original wager
                } else {
                    game.addToBalance(dontComeStack.getTotal() * 2); // return the original wager, then 1x bet
                }
                dontComeStack.popStack(true);
                dontComeStack.setTotal(0);
            }
        } else {
            // move to a number
            if (comeStack.getTotal() > 0) {
                switch (total) {
                    case 4:
                        come4Stack.setTotal(comeStack.getTotal());
                        comeStack.setTotal(0);
                        break;
                    case 5:
                        come5Stack.setTotal(comeStack.getTotal());
                        comeStack.setTotal(0);
                        break;
                    case 6:
                        come6Stack.setTotal(comeStack.getTotal());
                        comeStack.setTotal(0);
                        break;
                    case 8:
                        come8Stack.setTotal(comeStack.getTotal());
                        comeStack.setTotal(0);
                        break;
                    case 9:
                        come9Stack.setTotal(comeStack.getTotal());
                        comeStack.setTotal(0);
                        break;
                    case 10:
                        come10Stack.setTotal(comeStack.getTotal());
                        comeStack.setTotal(0);
                        break;
                }
            }
            if (dontComeStack.getTotal() > 0) {
                switch (total) {
                    case 4:
                        dontCome4Stack.setTotal(dontComeStack.getTotal());
                        dontComeStack.setTotal(0);
                        break;
                    case 5:
                        dontCome5Stack.setTotal(dontComeStack.getTotal());
                        dontComeStack.setTotal(0);
                        break;
                    case 6:
                        dontCome6Stack.setTotal(dontComeStack.getTotal());
                        dontComeStack.setTotal(0);
                        break;
                    case 8:
                        dontCome8Stack.setTotal(dontComeStack.getTotal());
                        dontComeStack.setTotal(0);
                        break;
                    case 9:
                        dontCome9Stack.setTotal(dontComeStack.getTotal());
                        dontComeStack.setTotal(0);
                        break;
                    case 10:
                        dontCome10Stack.setTotal(dontComeStack.getTotal());
                        dontComeStack.setTotal(0);
                        break;
                }
            }
        }

        // one roll bets
        if(any7Stack.getTotal() > 0) {
            if(total == 7) {
                won += any7Stack.getTotal() * 4;
                game.addToBalance(any7Stack.getTotal() * 5);
                any7Stack.popStack(true);
                any7Stack.setTotal(0);
            } else {
                won -= any7Stack.getTotal();
                any7Stack.popStack(false);
                any7Stack.setTotal(0);
            }
        }

        if(anyCrapsStack.getTotal() > 0) {
            if(total == 2 || total == 3 || total == 12) {
                won += anyCrapsStack.getTotal() * 7;
                game.addToBalance(anyCrapsStack.getTotal() * 8);
                anyCrapsStack.popStack(true);
                anyCrapsStack.setTotal(0);
            } else {
                won -= anyCrapsStack.getTotal();
                anyCrapsStack.popStack(false);
                anyCrapsStack.setTotal(0);
            }
        }

        if(snakeEyesStack.getTotal() > 0) {
            if(total == 2) {
                won += snakeEyesStack.getTotal() * 30;
                game.addToBalance(snakeEyesStack.getTotal() * 31);
                snakeEyesStack.popStack(true);
                snakeEyesStack.setTotal(0);
            } else {
                won -= snakeEyesStack.getTotal();
                snakeEyesStack.popStack(false);
                snakeEyesStack.setTotal(0);
            }
        }

        if(boxCarsStack.getTotal() > 0) {
            if(total == 12) {
                won += boxCarsStack.getTotal() * 30;
                game.addToBalance(boxCarsStack.getTotal() * 31);
                boxCarsStack.popStack(true);
                boxCarsStack.setTotal(0);
            } else {
                won -= boxCarsStack.getTotal();
                boxCarsStack.popStack(false);
                boxCarsStack.setTotal(0);
            }
        }

        if(threeStack.getTotal() > 0) {
            if(total == 3) {
                won += threeStack.getTotal() * 15;
                game.addToBalance(threeStack.getTotal() * 16);
                threeStack.popStack(true);
                threeStack.setTotal(0);
            } else {
                won -= threeStack.getTotal();
                threeStack.popStack(false);
                threeStack.setTotal(0);
            }
        }

        if(elevenStack.getTotal() > 0) {
            if(total == 11) {
                won += elevenStack.getTotal() * 15;
                game.addToBalance(elevenStack.getTotal() * 16);
                elevenStack.popStack(true);
                elevenStack.setTotal(0);
            } else {
                won -= elevenStack.getTotal();
                elevenStack.popStack(false);
                elevenStack.setTotal(0);
            }
        }

        if(fieldStack.getTotal() > 0) {
            if(total == 2) {
                won += fieldStack.getTotal() * 2;
                game.addToBalance(fieldStack.getTotal() * 3);
                fieldStack.popStack(true);
                fieldStack.setTotal(0);
            } else if(total == 12) {
                won += fieldStack.getTotal() * 3;
                game.addToBalance(fieldStack.getTotal() * 4);
                fieldStack.popStack(true);
                fieldStack.setTotal(0);
            } else if(total == 3 || total == 4 || total == 9 || total == 10 || total == 11) {
                won += fieldStack.getTotal();
                game.addToBalance(fieldStack.getTotal() * 2);
                fieldStack.popStack(true);
                fieldStack.setTotal(0);
            } else {
                won -= fieldStack.getTotal();
                fieldStack.popStack(false);
                fieldStack.setTotal(0);
            }
        }

        if(won > 0)
            leftSide.setWonColor(Color.GREEN);
        else if(won < 0)
            leftSide.setWonColor(Color.RED);
        else
            leftSide.setWonColor(Color.WHITE);
        leftSide.setWonText("" + (won));

        // now that we've payed up what we need to, set the new point if needed
        setThePoint();
        // recalculate since we just won/lost on a whole lot of stacks potentially
        calculateWager();
    }

    private void setThePoint() {
        int total = die1.getRolledNumber() + die2.getRolledNumber();
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
            } else if(total == thePoint) { // come roll made
                thePoint = -1;
                offChip.setVisible(true);
                onChip.setVisible(false);

            }
        } else if(total == 7) {
            thePoint = -1;
            offChip.setVisible(true);
            onChip.setVisible(false);
        }
    }

    private int placeBet(ChipStackGroup stack, int amount) {
        int newTotal = stack.getTotal() + amount;
        newTotal = checkTableMax(newTotal);
        int oldTotal = stack.getTotal();
        game.subtractFromBalance(newTotal - oldTotal);
        stack.setTotal(newTotal);
        return newTotal;
    }
}
