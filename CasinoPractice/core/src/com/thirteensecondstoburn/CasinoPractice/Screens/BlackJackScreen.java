package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.BlackjackHand;
import com.thirteensecondstoburn.CasinoPractice.Actors.Card;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStack;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Deck;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Nick on 2/20/2015.
 */
public class BlackJackScreen extends TableScreen implements ActionCompletedListener {

    public BlackJackScreen(CasinoPracticeGame game) {
        super(game);
    }
    public static final float HAND_X_START = (CasinoPracticeGame.SCREEN_WIDTH - Card.CARD_WIDTH * 3) / 2;

    Deck deck;
    boolean canBet = true;
    Image title;
    int lastBet = ChipStack.TABLE_MIN;

    BlackjackHand dealerHand;

    int cardIndex = 0;
    int currentHandIndex = 0;
    BlackjackHand currentHand;

    Text blackjackPayText;
    Text dealerHandText;

    TableButton dealButton;
    TableButton hitButton;
    TableButton standButton;
    TableButton surrenderButton;
    TableButton doubleButton;
    TableButton splitButton;
    TableButton clearButton;

    BlackjackHand mainHand;

    List<BlackjackHand> hands;


    @Override
    protected void setup() {
        Texture titleTex = assets.getTexture(Assets.TEX_NAME.BLACKJACK_TITLE);
        title = new Image(titleTex);
        title.setColor(mainColor);
        title.setPosition(315, stage.getHeight() - titleTex.getHeight());


        dealerHandText = new Text(assets, "", 1.5f);
        dealerHandText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 90);

        blackjackPayText = new Text(assets, "BLACKJACK PAYS 3 TO 2 - DEALER HITS ON SOFT 17", 1);
        blackjackPayText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - 40);
        blackjackPayText.setColor(.5f, .5f, .5f, .5f);

        mainHand = new BlackjackHand(game, assets);
        mainHand.addActionListener(this);

        hands = new ArrayList<BlackjackHand>();
        hands.add(mainHand);
        setHandPositions();

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
        clearButton.setVisible(true);

        hitButton = new TableButton(assets, "Hit", mainColor);
        hitButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, 0);
        hitButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (hitButton.isInside(x, y)) {
                    hitHand();
                }
            }
        });
        hitButton.setVisible(false);

        standButton = new TableButton(assets, "Stand", mainColor);
        standButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, TableButton.BUTTON_HEIGHT + 10);
        standButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (standButton.isInside(x, y)) {
                    stand();
                }
            }
        });
        standButton.setVisible(false);

        stage.addActor(title);

        stage.addActor(dealerHandText);
        stage.addActor(blackjackPayText);

        stage.addActor(dealButton);
        stage.addActor(clearButton);
        stage.addActor(hitButton);
        stage.addActor(standButton);

        for(BlackjackHand hand : hands) {
            stage.addActor(hand);
        }
    }

    private void dealHand() {
        if(dealerHand != null) dealerHand.remove();

        if(mainHand.getBetTotal() == 0) {
            if(lastBet > game.getBalance()) {
                mainHand.setHandText("You can't repeat your last bet");
                dealerHandText.setText("");
                return;
            }
            mainHand.setBetTotal(lastBet);
            leftSide.setWagerText("" + (lastBet));
            subtractFromBalance(lastBet);
        }

        Card.Back back;
        back = Card.Back.BACK1;
        deck = new Deck(assets, back);
        deck.shuffle();

        cardIndex = 0;
        dealerHand = new BlackjackHand(game, assets);
        dealerHand.addCard(nextCard(), false);
        dealerHand.addCard(nextCard());
        dealerHand.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 80);
        dealerHand.deal(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        dealerHand.addActionListener(this);
        dealerHand.hideBets();

        mainHand.addCard(nextCard());
        mainHand.addCard(nextCard());
        currentHand = mainHand;
        currentHandIndex = 0;

        stage.addActor(dealerHand);
        canBet = false;
        hideButtons(); // cause we're dealing and we'll turn them on when it's done
        leftSide.setWonText("");
        dealerHandText.setText("");
    }

    private void hideButtons() {
        dealButton.setVisible(false);
        clearButton.setVisible(false);
        hitButton.setVisible(false);
        standButton.setVisible(false);
        //doubleButton.setVisible(false);
        //splitButton.setVisible(false);
        //surrenderButton.setVisible(false);
    }

    private void clearHand() {
        for(BlackjackHand hand : hands) {
            addToBalance(hand.getBetTotal());
            hand.setHandText("");
        }
        lastBet = ChipStack.TABLE_MIN;
        leftSide.setWonText("");
        dealerHandText.setText("");
        leftSide.setWagerText("");
        canBet = true;
        toggleButtons(false);
        mainHand.clear();
        currentHand = mainHand;
        currentHandIndex = 0;

        for(int i = 1; i < hands.size(); i++) { hands.remove(i); } // clear out any split hands from previous rounds
    }

    private void toggleButtons(boolean isPlaying) {
        dealButton.setVisible(!isPlaying);
        clearButton.setVisible(!isPlaying);
        hitButton.setVisible(isPlaying);
        standButton.setVisible(isPlaying);
    }

    private void setHandPositions() {
        float totalSpace = stage.getWidth() - leftSide.getWidth();
        int totalHands = hands.size();
        // fuck it. for now put it somewhere in the middle til it's time to split
        hands.get(0).setPosition(totalSpace/2 - 150, 50);
    }

    private void stand() {
        BlackjackHand nextHand = getNextHand();
        if(nextHand == null) {
            // all betting is done, let the dealer go
            playDealer();
            return;
        } else {
            // move to the next split hand
            currentHand = nextHand;
        }
    }

    private void hitHand() {
        currentHand.addCard(nextCard());
        int hard = currentHand.getHard();
        int soft = currentHand.getSoft();
        if(soft > 21 ) {
            BlackjackHand nextHand = getNextHand();
            if(nextHand == null) {
                // all betting is done, let the dealer go
                playDealer();
                return;
            } else {
                // move to the next split hand
                currentHand = nextHand;
            }
        }

    }

    private BlackjackHand getNextHand() {
        if(currentHandIndex >= hands.size() - 1)
            return null;

        return hands.get(++currentHandIndex);
    }

    private Card nextCard() {
        return deck.getCards().get(cardIndex++);
    }

    private void playDealer() {
        hideButtons();
        dealerHand.showCards();
        if(dealerHand.getHard() <= 16 || (dealerHand.getHard() > 21 && dealerHand.getSoft() <= 17)) {
            hitDealer();
        }

        calculateWinners(false);
    }

    private void hitDealer() {
        dealerHand.addCard(nextCard());
        dealerHand.addAction(
                sequence(
                        Actions.delay(1000),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                dealerHand.addCard(nextCard());
                            }
                        }),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                playDealer();
                            }
                        })
                        ));
    }

    private void calculateWinners(boolean dealerBlackjack) {
        int total = 0;
        int initialBet = 0;

        if(dealerBlackjack) {
            // special case, if the dealer has blackjack the only thing the player could have done is tie
            if(mainHand.isBlackjack()) {
                total = mainHand.getBetTotal();
            } else {
                mainHand.popStack(false);
            }
            initialBet += mainHand.getBetTotal();
        } else {
            for(BlackjackHand hand : hands) {
                initialBet += hand.getBetTotal();
                if(hand == mainHand && hand.isBlackjack()) {
                    // pay out 3-2 for the blackjack
                    total = hand.getBetTotal() + hand.getBetTotal()*3/2;
                    hand.popStack(true);
                } else if(hand.bestHandTotal() > dealerHand.bestHandTotal()) {
                    // play won even money.
                    total = hand.getBetTotal() * 2;
                    hand.popStack(true);
                } else if(hand.bestHandTotal() == dealerHand.bestHandTotal()) {
                    // push
                    total = hand.getBetTotal();
                } else {
                    hand.popStack(false);
                }
            }
        }

        addToBalance(total);

        if(total - initialBet > 0)
            leftSide.setWonColor(Color.GREEN);
        else if(total - initialBet < 0)
            leftSide.setWonColor(Color.RED);
        else
            leftSide.setWonColor(Color.WHITE);

        leftSide.setWonText("" + (total - initialBet));

        canBet = true;
        toggleButtons(false);
        mainHand.clear();
        currentHand = mainHand;
        currentHandIndex = 0;

        for(int i = 1; i < hands.size(); i++) { hands.remove(i); } // clear out any split hands from previous rounds
    }

    @Override
    public void actionCompleted(Actor caller) {
        if(caller == dealerHand) {
            mainHand.setVisible(true);
            mainHand.deal(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        } else if(caller.getName() == "InnerHand") {
            toggleButtons(true);
            if(game.usePreBetHints()) {
                // TODO HINTS
                //setPreBetHint();
            }
        }
    }
}
