package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.Card;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStack;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Actors.WinLosePopup;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Deck;
import com.thirteensecondstoburn.CasinoPractice.Actors.Hand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nick on 1/30/2015.
 */
public class CrazyFourPokerScreen extends TableScreen implements ActionCompletedListener {
    public static final float HAND_X_START = (CasinoPracticeGame.SCREEN_WIDTH - Card.CARD_WIDTH * 3) / 2;

    Deck deck;

    boolean isFirstDeck = true;
    boolean canBet = true;

    ChipStack anteStack;
    ChipStack superBonusStack;
    ChipStack queensUpStack;
    ChipStack playStack;
    Image anteCircle;
    Image superBonusCircle;
    Image queensUpCircle;
    Image playCircle;

    TableButton dealButton;
    TableButton oneTimesButton;
    TableButton twoTimesButton;
    TableButton threeTimesButton;
    TableButton foldButton;
    TableButton clearButton;

    Hand dealerHand;
    Hand playerHand;

    Image paytable;
    Image title;

    Text dealerHandText;
    Text playerHandText;
    Text qualifyText;

    WinLosePopup queensUpPopup;
    WinLosePopup antePopup;
    WinLosePopup superBonusPopup;
    WinLosePopup playPopup;

    int lastAnteBet = ChipStack.TABLE_MIN;
    int lastQueensUpBet = 0;
    BestHand bestHandPlayer = null;

    public CrazyFourPokerScreen(CasinoPracticeGame game) {
        super(game);
    }

    @Override
    public void setup() {
        paytable = new Image(assets.getTexture(Assets.TEX_NAME.CRAZY_FOUR_POKER_PAYTABLE));
        paytable.setScale(.75f, .75f);
        paytable.setColor(1, 1, 1, .75f);
        paytable.setPosition(stage.getWidth() - 350 * paytable.getScaleX(), stage.getHeight() - 500 * paytable.getScaleY());

        title = new Image(assets.getTexture(Assets.TEX_NAME.CRAZY_FOUR_POKER_TITLE));
        title.setColor(mainColor);
        title.setPosition(315, stage.getHeight() - 200);


        queensUpCircle = new Image(assets.getTexture(Assets.TEX_NAME.QUEENS_UP_CIRCLE));
        queensUpCircle.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + queensUpCircle.getWidth()) / 2, stage.getHeight()/2);
        anteCircle = new Image(assets.getTexture(Assets.TEX_NAME.ANTE_CIRCLE));
        anteCircle.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + anteCircle.getWidth() * 2) / 2 - 20, queensUpCircle.getY() - 200);
        superBonusCircle = new Image(assets.getTexture(Assets.TEX_NAME.SUPER_BONUS_CIRCLE));
        superBonusCircle.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + superBonusCircle.getWidth() * 2) / 2 + superBonusCircle.getWidth() + 20, queensUpCircle.getY() - 200);
        playCircle = new Image(assets.getTexture(Assets.TEX_NAME.PLAY_CIRCLE));
        playCircle.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + playCircle.getWidth()) / 2, anteCircle.getY() - 200);

        queensUpStack = new ChipStack(assets, 0);
        queensUpStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + queensUpCircle.getWidth()) / 2, stage.getHeight() / 2 + 10);
        queensUpStack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (queensUpStack.isInside(x, y) && canBet) {
                    clearButton.setVisible(true);
                    int betAmount = leftSide.getBetAmount();
                    if (anteStack.getTotal() + betAmount > game.getBalance()) {
                        playerHandText.setText("Insufficient funds to make that bet");
                        dealerHandText.setText("");
                        return;
                    }
                    if (queensUpStack.getTotal() + betAmount > ChipStack.TABLE_MAX) {
                        playerHandText.setText("Table maximum of " + ChipStack.TABLE_MAX);
                        dealerHandText.setText("");
                        return;
                    }
                    queensUpStack.increaseTotal(betAmount);
                    lastQueensUpBet = queensUpStack.getTotal();
                    subtractFromBalance(betAmount);
                    calculateWager();
                    dealButton.setVisible(true);
                    if(game.useHintText()) {
                        showHint("Queens Up bet is a sucker bet!");
                    }
                }
            }
        });

        anteStack = new ChipStack(assets, 0);
        anteStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + anteCircle.getWidth() * 2) / 2 - 20, queensUpStack.getY() - 200);
        anteStack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (anteStack.isInside(x, y) && canBet) {
                    clearButton.setVisible(true);
                    int betAmount = leftSide.getBetAmount();
                    if(anteStack.getTotal() + leftSide.getBetAmount() * 2 > game.getBalance() - betAmount) {
                        playerHandText.setText("Insufficient funds to make that bet");
                        dealerHandText.setText("");
                        return;
                    }
                    if(anteStack.getTotal() + betAmount > ChipStack.TABLE_MAX) {
                        playerHandText.setText("Table maximum of " + ChipStack.TABLE_MAX);
                        dealerHandText.setText("");
                        return;
                    }
                    anteStack.increaseTotal(betAmount);
                    superBonusStack.increaseTotal(betAmount);
                    lastAnteBet = anteStack.getTotal();
                    subtractFromBalance(betAmount * 2);
                    calculateWager();
                    dealButton.setVisible(true);
                }
            }
        });

        superBonusStack = new ChipStack(assets, 0);
        superBonusStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + superBonusCircle.getWidth() * 2) / 2 + superBonusCircle.getWidth() + 20, queensUpStack.getY() - 200); // TO DO FIX THIS
        // can't click on the super bonus stack. It's always = ante

        playStack = new ChipStack(assets, 0);
        playStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + playCircle.getWidth()) / 2, anteStack.getY() - 200);
        // doesn't make sense to have this clickable here

        dealButton = new TableButton(assets, "Deal", mainColor);
        dealButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, 0);
        dealButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (dealButton.isInside(x, y)) {
                    dealHand();
                }
            }
        });
        dealButton.setVisible(true);

        oneTimesButton = new TableButton(assets, "Bet 1x", mainColor);
        oneTimesButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, 0);
        oneTimesButton.setName("1xButton");
        oneTimesButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (oneTimesButton.isInside(x, y) && !canBet) {
                    playHand(1, oneTimesButton);
                    oneTimesButton.setVisible(false);
                }
            }
        });
        oneTimesButton.setVisible(false);

        twoTimesButton = new TableButton(assets, "Bet 2x", mainColor);
        twoTimesButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH * 2 - 10, TableButton.BUTTON_HEIGHT + 10);
        twoTimesButton.setName("2xButton");
        twoTimesButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (twoTimesButton.isInside(x, y) && !canBet) {
                    if(anteStack.getTotal() * 2 > game.getBalance()) {
                        playerHandText.setText("Insufficient funds to make that bet");
                        return;
                    }
                    playHand(2, twoTimesButton);
                    twoTimesButton.setVisible(false);
                }
            }
        });
        twoTimesButton.setVisible(false);

        threeTimesButton = new TableButton(assets, "Bet 3x", mainColor);
        threeTimesButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH * 2 - 10, 0);
        threeTimesButton.setName("3xButton");
        threeTimesButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (threeTimesButton.isInside(x, y) && !canBet) {
                    if(anteStack.getTotal() * 2 > game.getBalance()) {
                        playerHandText.setText("Insufficient funds to make that bet");
                        return;
                    }
                    playHand(3, threeTimesButton);
                    threeTimesButton.setVisible(false);
                }
            }
        });
        threeTimesButton.setVisible(false);

        clearButton = new TableButton(assets, "Clear", mainColor);
        clearButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, TableButton.BUTTON_HEIGHT + 10);
        clearButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (clearButton.isInside(x, y)) {
                    clearHand();
                }
            }
        });
        clearButton.setVisible(false);

        foldButton = new TableButton(assets, "Fold", mainColor);
        foldButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, TableButton.BUTTON_HEIGHT + 10);
        foldButton.setName("foldButton");
        foldButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (foldButton.isInside(x, y)) {
                    foldHand();
                }
            }
        });
        foldButton.setVisible(false);

        dealerHandText = new Text(assets, "", 1.5f);
        dealerHandText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 90);

        playerHandText = new Text(assets, "", 1.5f);
        playerHandText.setPosition(HAND_X_START, Card.CARD_HEIGHT + 60);

        qualifyText = new Text(assets, "DEALER QUALIFIES WITH KING HIGH", 1);
        qualifyText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - 40);
        qualifyText.setColor(.5f, .5f, .5f, .5f);

        queensUpPopup = new WinLosePopup(assets);
        antePopup = new WinLosePopup(assets);
        superBonusPopup = new WinLosePopup(assets);
        playPopup = new WinLosePopup(assets);

        stage.addActor(paytable);
        stage.addActor(title);

        stage.addActor(anteCircle);
        stage.addActor(superBonusCircle);
        stage.addActor(queensUpCircle);
        stage.addActor(playCircle);
        stage.addActor(anteStack);
        stage.addActor(superBonusStack);
        stage.addActor(queensUpStack);
        stage.addActor(playStack);

        stage.addActor(dealButton);
        stage.addActor(clearButton);
        stage.addActor(oneTimesButton);
        stage.addActor(twoTimesButton);
        stage.addActor(threeTimesButton);
        stage.addActor(foldButton);

        stage.addActor(dealerHandText);
        stage.addActor(playerHandText);
        stage.addActor(qualifyText);

        stage.addActor(queensUpPopup);
        stage.addActor(antePopup);
        stage.addActor(superBonusPopup);
        stage.addActor(playPopup);
    }

    private void dealHand() {
        if(playerHand != null) playerHand.remove();
        if(dealerHand != null) dealerHand.remove();
        playStack.setTotal(0);
        if(anteStack.getTotal() == 0) {
            if(lastAnteBet * 3 + lastQueensUpBet > game.getBalance()) {
                playerHandText.setText("You can't repeat your last bet");
                dealerHandText.setText("");
                return;
            }
            anteStack.setTotal(lastAnteBet);
            superBonusStack.setTotal(lastAnteBet);
            queensUpStack.setTotal(lastQueensUpBet);
            leftSide.setWagerText("" + (lastAnteBet * 2 + lastQueensUpBet));
            subtractFromBalance(lastAnteBet * 2 + lastQueensUpBet);
        }

        Card.Back back;
        if(isFirstDeck) { back = Card.Back.BACK1;} else {back = Card.Back.BACK2;}
        isFirstDeck = !isFirstDeck;
        deck = new Deck(assets, back);
        deck.shuffle();

        dealerHand = new Hand(deck.getCards().subList(0, 5), 125);
        // TEST
//        ArrayList<Card> dealerTestCards = new ArrayList<Card>();
//        dealerTestCards.add(new Card(Card.FaceValue.ACE, Card.Suit.HEART, Card.Back.BACK1, true, assets));
//        dealerTestCards.add(new Card(Card.FaceValue.KING, Card.Suit.SPADE, Card.Back.BACK1, true, assets));
//        dealerTestCards.add(new Card(Card.FaceValue.FIVE, Card.Suit.DIAMOND, Card.Back.BACK1, true, assets));
//        dealerTestCards.add(new Card(Card.FaceValue.FOUR, Card.Suit.CLUB, Card.Back.BACK1, true, assets));
//        dealerTestCards.add(new Card(Card.FaceValue.THREE, Card.Suit.DIAMOND, Card.Back.BACK1, true, assets));
//        dealerHand = new Hand(dealerTestCards, 125);

        dealerHand.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 50);
        dealerHand.dealAction(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        dealerHand.addActionListener(this);

        // TEST
//        ArrayList<Card> playerTestCards = new ArrayList<Card>();
//        playerTestCards.add(new Card(Card.FaceValue.JACK, Card.Suit.HEART, Card.Back.BACK1, true, assets));
//        playerTestCards.add(new Card(Card.FaceValue.TEN, Card.Suit.SPADE, Card.Back.BACK1, true, assets));
//        playerTestCards.add(new Card(Card.FaceValue.EIGHT, Card.Suit.DIAMOND, Card.Back.BACK1, true, assets));
//        playerTestCards.add(new Card(Card.FaceValue.FIVE, Card.Suit.CLUB, Card.Back.BACK1, true, assets));
//        playerTestCards.add(new Card(Card.FaceValue.THREE, Card.Suit.SPADE, Card.Back.BACK1, true, assets));
//        bestHandPlayer = new BestHand(playerTestCards);

        bestHandPlayer = new BestHand(deck.getCards().subList(5, 10));
        playerHand = new Hand(bestHandPlayer.sortedCards, 125);
        playerHand.setPosition(HAND_X_START, 50);
        playerHand.setVisible(false);

        playerHand.showHand();
        playerHand.addActionListener(this);

        stage.addActor(dealerHand);
        stage.addActor(playerHand);

        canBet = false;
        hideButtons(); // cause we're dealing and we'll turn them on when it's done
        leftSide.setWonText("");
        dealerHandText.setText("");
        playerHandText.setText("");
    }

    private void playHand(int bet, TableButton buttonPressed) {
        playStack.setTotal(anteStack.getTotal() * bet);
        subtractFromBalance(anteStack.getTotal() * bet);
        calculateWager();
        showDealerHand();
        calculateWinner(false);
        canBet = true;
        toggleButtons(false);
        if(game.useHintText()) {
            TableButton correctButton = getCorrectButtonForHint();
            if(correctButton != buttonPressed) {
                System.out.println("Correct: " + correctButton + " Pressed: " + buttonPressed);
                if(correctButton == threeTimesButton) {
                    showHint("Always bet 3x when possible");
                } else if(correctButton == oneTimesButton) {
                    showHint("Always bet with K, Q, 8, 4 or better");
                } else {
                    showHint("Always fold with worse than K, Q, 8, 4");
                }
            }
        }
    }

    private void foldHand() {
        calculateWinner(true);
        canBet = true;
        toggleButtons(false);
        showDealerHand();
        anteStack.clearTotal();
        queensUpStack.clearTotal();
        if(game.useHintText()) {
            TableButton correctButton = getCorrectButtonForHint();
            if(correctButton != foldButton) {
                if(threeTimesButton.isVisible()) {
                    showHint("Always bet 3x when possible");
                } else {
                    showHint("Always bet with K, Q, 8, 4 or better");
                }
            }
        }
    }

    private void clearHand() {
        addToBalance(anteStack.getTotal() + superBonusStack.getTotal() + queensUpStack.getTotal());
        lastAnteBet = ChipStack.TABLE_MIN;
        lastQueensUpBet = 0;
        anteStack.setTotal(0);
        superBonusStack.setTotal(0);
        queensUpStack.setTotal(0);
        playStack.setTotal(0);
        leftSide.setWonText("");
        dealerHandText.setText("");
        playerHandText.setText("");
        leftSide.setWagerText("");
        canBet = true;
        toggleButtons(false);
    }

    private void calculateWager() {
        leftSide.setWagerText("" + (anteStack.getTotal() + superBonusStack.getTotal() + queensUpStack.getTotal() + playStack.getTotal()));
    }

    private void toggleButtons(boolean isPlaying) {
        dealButton.setVisible(!isPlaying);
        clearButton.setVisible(!isPlaying);
        oneTimesButton.setVisible(isPlaying);
        boolean qualifies = (bestHandPlayer != null &&
                ((bestHandPlayer.bestFourHand.getHandType().ordinal() > 1) ||
                        (bestHandPlayer.bestFourHand.getHandType().ordinal() == 1 && bestHandPlayer.bestFourHand.getStraightValues().get(0) == 14))
        );
        twoTimesButton.setVisible(isPlaying && qualifies);
        threeTimesButton.setVisible(isPlaying && qualifies);

        foldButton.setVisible(isPlaying);
    }

    private void hideButtons() {
        dealButton.setVisible(false);
        clearButton.setVisible(false);
        oneTimesButton.setVisible(false);
        twoTimesButton.setVisible(false);
        threeTimesButton.setVisible(false);
        foldButton.setVisible(false);
    }

    private void showDealerHand() {
        dealerHand.showHand();
    }

    @Override
    public void actionCompleted(Actor caller) {
        if(caller == dealerHand) {
            playerHand.setVisible(true);
            playerHand.dealAction(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        } else if(caller == playerHand) {

            toggleButtons(true);
            if(game.usePreBetHints()) {
                setPreBetHint();
            }
        }
    }

    enum HandType {HIGH_CARD, PAIR, TWO_PAIR, STRAIGHT, FLUSH, THREE_OF_A_KIND, STRAIGHT_FLUSH, FOUR_OF_A_KIND};
    private void calculateWinner(boolean playerFolded) {
        BestHand bestHandDealer = new BestHand(dealerHand.getCards());
        dealerHand.setCards(bestHandDealer.sortedCards);

        // ok, now that we know what they both have, who won and how much?
        BestFourHand playerFour = bestHandPlayer.bestFourHand;
        BestFourHand dealerFour = bestHandDealer.bestFourHand;
        int playerWon = playerFour.getHandType().ordinal() - dealerFour.getHandType().ordinal();
        if(playerFolded) playerWon = -1;

        if(playerWon == 0) {
            // run through the top values
            for(int i=0; i<playerFour.getStraightValues().size(); i++) {
                if(playerFour.getStraightValues().get(i) > dealerFour.getStraightValues().get(i)) {
                    playerWon = 1;
                    break;
                } else if(playerFour.getStraightValues().get(i) < dealerFour.getStraightValues().get(i)) {
                    playerWon = -1;
                    break;
                }
            }
        }

        boolean dealerQualifies = dealerFour.getHandType().ordinal() > 0 || dealerFour.getStraightValues().get(0) >= 13;
        int total;
        if(playerWon > 0 || (!dealerQualifies && !playerFolded)) {
            // the player really won
            if(dealerQualifies) {
                //If the player has the higher poker hand then the Ante and Play will both pay even money.
                total = anteStack.getTotal() * 2 + playStack.getTotal() * 2;
                antePopup.pop(true, anteCircle.getX() + 37, anteCircle.getY() + 38);
                playPopup.pop(true, playCircle.getX() + 37, playCircle.getY() + 38);
            } else {
                // If the dealer does not qualify then the Ante bet will push and the Play bet will win.
                total = anteStack.getTotal() + playStack.getTotal() * 2;
                playPopup.pop(true, playCircle.getX() + 37, playCircle.getY() + 38);
            }
        } else if(playerWon < 0) {
            // the player lost
            total = 0; // we've already pulled this from the balance. no need to re-deduct
            antePopup.pop(false, anteCircle.getX() + 37, anteCircle.getY() + 38);
            if(playStack.getTotal() > 0) {
                playPopup.pop(false, playCircle.getX() + 37, playCircle.getY() + 38);
            }
        } else {
            // tie.
            total = anteStack.getTotal() + playStack.getTotal();
        }

        // super bonus
        if(!playerFolded) {
            if(playerFour.getHandType().ordinal() >= HandType.STRAIGHT.ordinal()) {
                // doesn't matter if the player won or the dealer qualified
                superBonusPopup.pop(true, superBonusCircle.getX() + 37, superBonusCircle.getY() + 38);
                total += superBonusStack.getTotal(); // get your initial bet back too
                switch (playerFour.getHandType()) {
                    case FOUR_OF_A_KIND:
                        if(playerFour.getStraightValues().get(0) == 14)
                            total += superBonusStack.getTotal() * 200;
                        else
                            total += superBonusStack.getTotal() * 30;
                        break;
                    case STRAIGHT_FLUSH:
                        total += superBonusStack.getTotal() * 15;
                        break;
                    case THREE_OF_A_KIND:
                        total += superBonusStack.getTotal() * 2;
                        break;
                    case FLUSH:
                        total += (int)(superBonusStack.getTotal() * 1.5);
                        break;
                    case STRAIGHT:
                        total += superBonusStack.getTotal();
                        break;
                }
            } else if(playerWon >= 0) {
                total += superBonusStack.getTotal();
            } else {
                superBonusPopup.pop(false, superBonusCircle.getX() + 37, superBonusCircle.getY() + 38);
            }
        } else {
            superBonusPopup.pop(false, superBonusCircle.getX() + 37, superBonusCircle.getY() + 38);
        }

        // Queens Up
        if(queensUpStack.getTotal() > 0 && !playerFolded
                && (playerFour.getHandType().ordinal() > HandType.PAIR.ordinal()
                    || (playerFour.getHandType().ordinal() == HandType.PAIR.ordinal() && playerFour.getStraightValues().get(0) >= 12) )
                ) {
            total += queensUpStack.getTotal(); // return the initial bet
            queensUpPopup.pop(true, queensUpCircle.getX() + 37, queensUpCircle.getY() + 38);
            switch(playerFour.getHandType()) {
                case FOUR_OF_A_KIND:
                    total += queensUpStack.getTotal() * 50;
                case STRAIGHT_FLUSH:
                    total += queensUpStack.getTotal() * 40;
                    break;
                case THREE_OF_A_KIND:
                    total += queensUpStack.getTotal() * 7;
                    break;
                case FLUSH:
                    total += queensUpStack.getTotal() * 4;
                    break;
                case STRAIGHT:
                    total += queensUpStack.getTotal() * 3;
                    break;
                case TWO_PAIR:
                    total += queensUpStack.getTotal() * 2;
                    break;
                case PAIR:
                    total += queensUpStack.getTotal();
                    break;
            }
        } else if(queensUpStack.getTotal() > 0) {
            queensUpPopup.pop(false, queensUpCircle.getX() + 37, queensUpCircle.getY() + 38);
        }
        addToBalance(total);

        int initialBet = anteStack.getTotal() + superBonusStack.getTotal() + playStack.getTotal() + queensUpStack.getTotal();

        if(total - initialBet > 0)
            leftSide.setWonColor(Color.GREEN);
        else if(total - initialBet < 0)
            leftSide.setWonColor(Color.RED);
        else
            leftSide.setWonColor(Color.WHITE);

        leftSide.setWonText("" + (total - initialBet));

        setHandText(playerFour, dealerFour, dealerQualifies, playerFolded);

        anteStack.clearTotal();
        queensUpStack.clearTotal();
        playStack.clearTotal();
        superBonusStack.clearTotal();
    }

    private void setHandText(BestFourHand playerFour, BestFourHand dealerFour, boolean dealerQualified, boolean playerFolded) {
        StringBuilder pb = new StringBuilder();
        StringBuilder db = new StringBuilder();

        if(playerFolded) {
            pb.append("Folded: ");
        } else {
            if(!dealerQualified) {
                db.append("Dealer doesn't qualify: ");
            }
        }

        pb.append(setHandValueText(playerFour));
        db.append(setHandValueText(dealerFour));

        dealerHandText.setText(db.toString());
        playerHandText.setText(pb.toString());
    }

    private String setHandValueText(BestFourHand hand) {
        Card.FaceValue firstFaceValue = hand.getSortedCards().get(0).getFaceValue();
        switch(hand.getHandType()) {
            case FOUR_OF_A_KIND:
                return "Four of a kind of " + firstFaceValue.getSingleText();
            case STRAIGHT_FLUSH:
                return "Straight Flush to the " + firstFaceValue.getSingleText();
            case THREE_OF_A_KIND:
                return "Three of a kind of " + firstFaceValue.getMultiText();
            case FLUSH:
                return firstFaceValue.getSingleText() + " high flush";
            case STRAIGHT:
                return "Straight to the " + firstFaceValue.getSingleText();
            case TWO_PAIR:
                return "Two pair " + firstFaceValue.getMultiText() + " and " + hand.getSortedCards().get(2).getFaceValue().getMultiText();
            case PAIR:
                return "Pair of " + firstFaceValue.getMultiText();
            default:
                return firstFaceValue.getSingleText() + " high";
        }
    }

    private void setPreBetHint() {
        foldButton.setColor(mainColor);
        oneTimesButton.setColor(mainColor);
        threeTimesButton.setColor(mainColor); // even though that should never happen I guess

        TableButton button = getCorrectButtonForHint();
        button.setColor(hintColor);
    }

    private TableButton getCorrectButtonForHint() {
        boolean qualifies = (bestHandPlayer != null &&
                ((bestHandPlayer.bestFourHand.getHandType().ordinal() > 1) ||
                        (bestHandPlayer.bestFourHand.getHandType().ordinal() == 1 && bestHandPlayer.bestFourHand.getStraightValues().get(0) == 14))
        );

        if (qualifies) return threeTimesButton;

        BestFourHand fourHand = bestHandPlayer.bestFourHand;

        if(fourHand.getHandType().ordinal() > HandType.HIGH_CARD.ordinal()) return oneTimesButton;

        // better than K,Q,8,4?
        ArrayList<Integer> compVals = new ArrayList<Integer>();
        compVals.add(13);
        compVals.add(12);
        compVals.add(8);
        compVals.add(4);
        ArrayList<Integer> vals = fourHand.getStraightValues();
        for(int i = 0; i<vals.size(); i++) {
            if (vals.get(i) > compVals.get(i)) {
                return oneTimesButton;
            } else if (vals.get(i) < compVals.get(i)) {
                return foldButton;
            }
        }
        // everything's equal to the min. bet it.
        return oneTimesButton;
    }

    class BestHand {
        BestFourHand bestFourHand;
        List<Card> originalCards;
        List<Card> sortedCards;

        public BestHand(List<Card> oc) {
            this.originalCards = new ArrayList<Card>(oc.size());
            for(Card c : oc) this.originalCards.add(c);
            calculateBestHand();
        }

        private void calculateBestHand() {
            Card otherCard = null;
            for(int i=0; i<5; i++) {
                ArrayList<Card>fourList = new ArrayList<Card>();
                for(Card c : originalCards) {
                    fourList.add(new Card(c));
                }
                Card oddCard = fourList.remove(i);
                BestFourHand checkHand = new BestFourHand(fourList);

                if(bestFourHand == null || (checkHand.compareTo(bestFourHand) > 0)) {
                    bestFourHand = checkHand;
                    otherCard = oddCard;
                }
            }
            sortedCards = new ArrayList<Card>();
            for(Card c : bestFourHand.getSortedCards()) {
                sortedCards.add(new Card(c));
            }
            sortedCards.add(otherCard);
        }
    }

    class BestFourHand implements Comparable<BestFourHand>{
        private HandType handType;
        private ArrayList<Integer> straightValues = new ArrayList<Integer>();
        private ArrayList<Card.FaceValue> faceValues = new ArrayList<Card.FaceValue>();
        private List<Card> sortedCards;

        public BestFourHand(List<Card> fourCards) {
            sortedCards = new ArrayList<Card>();
            for(Card c : fourCards) {
                sortedCards.add(new Card(c));
            }
            Collections.sort(sortedCards);
            calculateHand(sortedCards);
        }

        void calculateHand(List<Card> cards) {
            boolean twoPair = isTwoPair(cards);
            boolean straight = isStraight(cards);
            boolean flush = isFlush(cards);
            boolean threeOfAKind = isThreeOfAKind(cards);
            boolean fourOfAKind = isFourOfAKind(cards);
            boolean pair = isPair(cards);

            if(fourOfAKind) {
                handType = HandType.FOUR_OF_A_KIND;
                faceValues.add(sortedCards.get(0).getFaceValue()); // all the same, only need 1
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
            }
            if(straight && flush) {
                handType = HandType.STRAIGHT_FLUSH;
                // gotta move the ace if it's supposed to be low
                if(sortedCards.get(0).getFaceValue().getStraightValue() == 14 && sortedCards.get(1).getFaceValue().getStraightValue() == 4) {
                    Card move = sortedCards.remove(0);
                    sortedCards.add(move);
                }
                faceValues.add(sortedCards.get(0).getFaceValue()); // only the top matters
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
            } else if(threeOfAKind) {
                handType = HandType.THREE_OF_A_KIND;
                if(sortedCards.get(0) != sortedCards.get(1)) {
                    // resort since the single is higher value than then
                    Card single = sortedCards.remove(0);
                    sortedCards.add(single);
                }
                // first is the 3 of a kind, other card is a single
                faceValues.add(sortedCards.get(0).getFaceValue());
                faceValues.add(sortedCards.get(3).getFaceValue());
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(3).getFaceValue().getStraightValue());
            } else if(flush) {
                handType = HandType.FLUSH;
                // all the cards matter
                for(Card c : sortedCards) {
                    faceValues.add(c.getFaceValue());
                    straightValues.add(c.getFaceValue().getStraightValue());
                }
            } else if(straight) {
                handType = HandType.STRAIGHT;
                // gotta move the ace if it's supposed to be low
                if(sortedCards.get(0).getFaceValue().getStraightValue() == 14 && sortedCards.get(1).getFaceValue().getStraightValue() == 4) {
                    Card move = sortedCards.remove(0);
                    sortedCards.add(move);
                }
                faceValues.add(sortedCards.get(0).getFaceValue()); // only the top matters
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
            } else if(twoPair) {
                handType = HandType.TWO_PAIR;
                // sorted, so high pair is first
                faceValues.add(sortedCards.get(0).getFaceValue());
                faceValues.add(sortedCards.get(3).getFaceValue());
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(3).getFaceValue().getStraightValue());

            } else if(pair) {
                handType = HandType.PAIR;
                // sort the pair to the front
                if(sortedCards.get(1).compareTo(sortedCards.get(2)) == 0) {
                    Card c = sortedCards.remove(0);
                    sortedCards.add(2, c); // we know it's higher than the other one
                } else if(sortedCards.get(2).compareTo(sortedCards.get(3)) == 0) {
                    // pair at the end. shuffle them to the front
                    Card c = sortedCards.remove(0);
                    sortedCards.add(c);
                    c = sortedCards.remove(0);
                    sortedCards.add(c);
                }
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(2).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(3).getFaceValue().getStraightValue());
            } else {
                handType = HandType.HIGH_CARD;
                // all the cards matter
                for(Card c : sortedCards) {
                    faceValues.add(c.getFaceValue());
                    straightValues.add(c.getFaceValue().getStraightValue());
                }
            }
        }

        boolean isFourOfAKind(List<Card>cards) {
            int value = cards.get(0).getFaceValue().getStraightValue();
            for(Card c : cards) {
                if(c.getFaceValue().getStraightValue() != value) return false;
            }
            return true;
        }

        boolean isThreeOfAKind(List<Card>cards) {
            // sorted, so either the first 3 are the same or the last 3
            int value = cards.get(0).getFaceValue().getStraightValue();
            if((cards.get(0).getFaceValue().getStraightValue() == cards.get(1).getFaceValue().getStraightValue()
                && cards.get(0).getFaceValue().getStraightValue() == cards.get(2).getFaceValue().getStraightValue())
                    ||
                    (cards.get(1).getFaceValue().getStraightValue() == cards.get(2).getFaceValue().getStraightValue()
                            && cards.get(1).getFaceValue().getStraightValue() == cards.get(3).getFaceValue().getStraightValue())) {
                return true;
            }
            return false;
        }

        boolean isStraight(List<Card> cards) {
            String straightString = "14,13,12,11,10,9,8,7,6,5,4,3,2,";
            String aceLowString = "14,4,3,2,";
            String cardValues = "";
            for(Card c : cards) {
                cardValues += c.getFaceValue().getStraightValue() + ",";
            }
            return straightString.contains(cardValues) || aceLowString.contains(cardValues);
        }

        boolean isFlush(List<Card> cards) {
            Card.Suit suit = cards.get(0).getSuit();
            for(Card c : cards) {
                if(c.getSuit() != suit) return false;
            }
            return true;
        }

        boolean isTwoPair(List<Card> cards) {
            // sorted, so the first 2 and last 2 should match
            if(cards.get(0).getFaceValue().getStraightValue() == cards.get(1).getFaceValue().getStraightValue()
                    && cards.get(2).getFaceValue().getStraightValue() == cards.get(3).getFaceValue().getStraightValue()) {
                return true;
            }
            return false;
        }

        boolean isPair(List<Card> cards) {
            // sorted, so just check cards next to each other
            if(cards.get(0).getFaceValue().getStraightValue() == cards.get(1).getFaceValue().getStraightValue()
                || cards.get(1).getFaceValue().getStraightValue() == cards.get(2).getFaceValue().getStraightValue()
                || cards.get(2).getFaceValue().getStraightValue() == cards.get(3).getFaceValue().getStraightValue()
                    ) {
                return true;
            }
            return false;
        }

        @Override
        public int compareTo(BestFourHand other) {
            int returnValue = 0;
            if(handType.ordinal() != other.handType.ordinal()) {
                returnValue =  handType.ordinal() - other.handType.ordinal();
            }

            // if they're the same type, just go to the straight value
            for(int i = 0; i<straightValues.size(); i++) {
                if(returnValue == 0) {
                    returnValue = straightValues.get(i) - other.straightValues.get(i);
                }
            }

            return returnValue;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for(Card c : sortedCards) {
                sb.append(c.getFaceValue().getStraightValue() + ", ");
            }
            sb.append(handType);
            return sb.toString();
        }

        public HandType getHandType() {
            return handType;
        }

        public ArrayList<Integer> getStraightValues() {
            return straightValues;
        }

        public ArrayList<Card.FaceValue> getFaceValues() {
            return faceValues;
        }

        public List<Card> getSortedCards() {
            return sortedCards;
        }
    }
}
